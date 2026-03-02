package dev.xkmc.youkaishomecoming.content.pot.cooking.large;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateItemModelProvider;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.client.model.generators.loaders.SeparateTransformsModelBuilder;

public class BigSpoonItem extends Item {

	public BigSpoonItem(Properties prop) {
		super(prop.attributes(ItemAttributeModifiers.builder()
				.add(Attributes.BLOCK_INTERACTION_RANGE,
						new AttributeModifier(YoukaisHomecoming.loc("big_spoon"), 2,
								AttributeModifier.Operation.ADD_VALUE),
						EquipmentSlotGroup.HAND)
				.build()));
	}

	@Override
	public InteractionResult useOn(UseOnContext ctx) {
		var level = ctx.getLevel();
		var pos = ctx.getClickedPos();
		var state = level.getBlockState(pos);
		if (state.is(Blocks.CAULDRON)) {
			if (!level.isClientSide()) {
				level.setBlockAndUpdate(pos, YHBlocks.STOCKPOT.getDefaultState());
				ctx.getItemInHand().shrink(1);
			}
			return InteractionResult.SUCCESS;
		}
		if (state.is(Blocks.WATER_CAULDRON)) {
			if (!level.isClientSide()) {
				level.setBlockAndUpdate(pos, YHBlocks.LARGE_POT.getDefaultState());
				ctx.getItemInHand().shrink(1);
			}
			return InteractionResult.SUCCESS;
		}
		return super.useOn(ctx);
	}

	public static void buildModel(DataGenContext<Item, BigSpoonItem> ctx, RegistrateItemModelProvider pvd) {
		var itemModel = new ItemModelBuilder(null, pvd.existingFileHelper)
				.parent(new ModelFile.UncheckedModelFile(pvd.mcLoc("item/generated")))
				.texture("layer0", pvd.modLoc("item/" + ctx.getName()));
		var blockModel = new ItemModelBuilder(null, pvd.existingFileHelper)
				.parent(new ModelFile.UncheckedModelFile(pvd.modLoc("custom/big_spoon")))
				.texture("parts", pvd.modLoc("block/bowl/stock/stockpot_parts"))
				.texture("particle", pvd.modLoc("block/bowl/stock/stockpot_parts"))
				.renderType("cutout");
		pvd.getBuilder("item/" + ctx.getName())
				.guiLight(BlockModel.GuiLight.FRONT)
				.customLoader(SeparateTransformsModelBuilder::begin)
				.base(blockModel)
				.perspective(ItemDisplayContext.GUI, itemModel);
	}

}
