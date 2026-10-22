package dev.xkmc.youkaishomecoming.util;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.MatrixUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HalfTransparentBlock;
import net.minecraft.world.level.block.StainedGlassPaneBlock;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * Renders one item stack several times, each copy with its own transform.
 * <p>
 * {@link ItemRenderer#renderStatic} resolves the model, its render passes, its render types and
 * its quads again for every single call, which is a lot of repeated work when the same stack is
 * drawn a dozen times. This class does that resolution once and then only replays the cached
 * quads for every copy, so the cost of a stack no longer grows with the number of copies.
 */
public class BatchedItemRenderer {

	/**
	 * The seed vanilla uses when it collects the quads of a model, see
	 * {@link ItemRenderer#renderModelLists}.
	 */
	private static final long QUAD_SEED = 42L;

	private final ItemRenderer itemRenderer;
	private final ItemStack stack;
	private final ItemDisplayContext context;
	@Nullable
	private final Level level;
	private final int seed;

	/** trident and spyglass are drawn with a dedicated model, they need the vanilla path */
	private final boolean useVanillaRenderer;
	private final BakedModel model;
	private final boolean fabulous;
	private final boolean animated;
	private final boolean foil;

	/** the model the cached passes belong to, {@link BakedModel#applyTransform} may return another one */
	@Nullable
	private BakedModel passModel;
	private List<Pass> passes = List.of();

	private BatchedItemRenderer(ItemRenderer itemRenderer, ItemStack stack, @Nullable Level level, ItemDisplayContext context, int seed) {
		this.itemRenderer = itemRenderer;
		this.stack = stack;
		this.level = level;
		this.context = context;
		this.seed = seed;
		this.model = itemRenderer.getModel(stack, level, null, seed);
		this.useVanillaRenderer = stack.is(Items.TRIDENT) || stack.is(Items.SPYGLASS);
		this.fabulous = isFabulous(stack, context);
		this.animated = stack.is(ItemTags.COMPASSES) || stack.is(Items.CLOCK);
		this.foil = stack.hasFoil();
		if (!useVanillaRenderer && !model.isCustomRenderer()) {
			this.passModel = model;
			this.passes = buildPasses(model);
		}
	}

	/**
	 * Prepares a batch for a stack.
	 * <p>
	 * When one of the {@code prepared} batches holds the same item, its render data is reused, so
	 * the model, the render passes, the render types, the quads and the tint of that item are only
	 * resolved once instead of once per slot.
	 */
	public static BatchedItemRenderer create(ItemRenderer itemRenderer, ItemStack stack, @Nullable Level level,
			ItemDisplayContext context, int seed, List<BatchedItemRenderer> prepared) {
		BatchedItemRenderer batch = new BatchedItemRenderer(itemRenderer, stack, level, context, seed);
		for (BatchedItemRenderer other : prepared) {
			if (batch.canShareWith(other)) {
				batch.passModel = other.passModel;
				batch.passes = other.passes;
				break;
			}
		}
		return batch;
	}

	/**
	 * Two batches can share their render data when they resolve to the same model and hold the same
	 * item, the quads and the tint of an item do not depend on the slot the item is drawn in.
	 */
	private boolean canShareWith(BatchedItemRenderer other) {
		return passModel != null && other.passModel != null
				&& model == other.model
				&& fabulous == other.fabulous
				&& ItemStack.isSameItemSameComponents(stack, other.stack);
	}

	/**
	 * Draws one copy of the stack, the pose is expected to already hold the transform of the copy.
	 */
	public void render(PoseStack pose, MultiBufferSource buffer, int light, int overlay) {
		if (useVanillaRenderer) {
			itemRenderer.renderStatic(stack, context, light, overlay, pose, buffer, level, seed);
			return;
		}
		pose.pushPose();
		BakedModel current = model.applyTransform(context, pose, false);
		pose.translate(-0.5F, -0.5F, -0.5F);
		if (current.isCustomRenderer()) {
			IClientItemExtensions.of(stack).getCustomRenderer().renderByItem(stack, context, pose, buffer, light, overlay);
		} else {
			List<Pass> currentPasses = passes;
			if (current != passModel) {
				passModel = current;
				currentPasses = buildPasses(current);
				passes = currentPasses;
			}
			for (Pass pass : currentPasses) {
				for (RenderType type : pass.types) {
					putQuads(pose, foilBuffer(buffer, type, pose), pass, light, overlay);
				}
			}
		}
		pose.popPose();
	}

	private List<Pass> buildPasses(BakedModel model) {
		List<BakedModel> modelPasses = model.getRenderPasses(stack, fabulous);
		List<Pass> result = new ArrayList<>(modelPasses.size());
		RandomSource random = RandomSource.create();
		for (BakedModel modelPass : modelPasses) {
			List<BakedQuad> quads = new ArrayList<>();
			for (Direction direction : Direction.values()) {
				random.setSeed(QUAD_SEED);
				quads.addAll(modelPass.getQuads(null, direction, random));
			}
			random.setSeed(QUAD_SEED);
			quads.addAll(modelPass.getQuads(null, null, random));
			result.add(new Pass(modelPass.getRenderTypes(stack, fabulous), quads, tintColors(quads)));
		}
		return result;
	}

	/**
	 * The tint of a quad only depends on the stack, so it is resolved once instead of once per copy.
	 */
	private float[] tintColors(List<BakedQuad> quads) {
		ItemColors colors = Minecraft.getInstance().getItemColors();
		float[] result = new float[quads.size() * 3];
		for (int i = 0; i < quads.size(); i++) {
			BakedQuad quad = quads.get(i);
			int color = quad.isTinted() ? colors.getColor(stack, quad.getTintIndex()) : -1;
			result[i * 3] = (color >> 16 & 255) / 255.0F;
			result[i * 3 + 1] = (color >> 8 & 255) / 255.0F;
			result[i * 3 + 2] = (color & 255) / 255.0F;
		}
		return result;
	}

	private void putQuads(PoseStack pose, VertexConsumer consumer, Pass pass, int light, int overlay) {
		PoseStack.Pose current = pose.last();
		List<BakedQuad> quads = pass.quads;
		float[] colors = pass.colors;
		for (int i = 0; i < quads.size(); i++) {
			consumer.putBulkData(current, quads.get(i), colors[i * 3], colors[i * 3 + 1], colors[i * 3 + 2], 1.0F, light, overlay, true);
		}
	}

	private VertexConsumer foilBuffer(MultiBufferSource buffer, RenderType type, PoseStack pose) {
		if (animated && foil) {
			pose.pushPose();
			PoseStack.Pose current = pose.last();
			if (context == ItemDisplayContext.GUI) {
				MatrixUtil.mulComponentWise(current.pose(), 0.5F);
			} else if (context.firstPerson()) {
				MatrixUtil.mulComponentWise(current.pose(), 0.75F);
			}
			VertexConsumer consumer = ItemRenderer.getCompassFoilBuffer(buffer, type, current);
			pose.popPose();
			return consumer;
		}
		return fabulous ?
				ItemRenderer.getFoilBufferDirect(buffer, type, true, foil) :
				ItemRenderer.getFoilBuffer(buffer, type, true, foil);
	}

	private static boolean isFabulous(ItemStack stack, ItemDisplayContext context) {
		if (context != ItemDisplayContext.GUI && !context.firstPerson() && stack.getItem() instanceof BlockItem blockItem) {
			Block block = blockItem.getBlock();
			return !(block instanceof HalfTransparentBlock) && !(block instanceof StainedGlassPaneBlock);
		}
		return true;
	}

	private static class Pass {

		private final List<RenderType> types;
		private final List<BakedQuad> quads;
		private final float[] colors;

		private Pass(List<RenderType> types, List<BakedQuad> quads, float[] colors) {
			this.types = types;
			this.quads = quads;
			this.colors = colors;
		}

	}

}
