package dev.xkmc.youkaishomecoming.content.item.danmaku;

import dev.xkmc.danmaku.render.DoubleLayerDanmakuType;
import dev.xkmc.danmaku.render.RenderableDanmakuType;
import dev.xkmc.l2library.util.raytrace.RayTraceUtil;
import dev.xkmc.youkaishomecoming.content.entity.danmaku.ItemDanmakuEntity;
import dev.xkmc.youkaishomecoming.content.item.curio.TouhouHatItem;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import dev.xkmc.youkaishomecoming.init.data.YHLangData;
import dev.xkmc.youkaishomecoming.init.data.YHTagGen;
import dev.xkmc.youkaishomecoming.init.registrate.YHDanmaku;
import dev.xkmc.youkaishomecoming.init.registrate.YHEffects;
import dev.xkmc.youkaishomecoming.init.registrate.YHEntities;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DanmakuItem extends Item {

	public final YHDanmaku.Type type;
	public final DyeColor color;
	public final float size;

	public DanmakuItem(Properties pProperties, YHDanmaku.Type type, DyeColor color, float size) {
		super(pProperties);
		this.type = type;
		this.color = color;
		this.size = size;
	}

	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		ItemStack head = player.getItemBySlot(EquipmentSlot.HEAD);
		if (!head.is(YHTagGen.TOUHOU_HAT) && !player.getAbilities().instabuild &&
				!player.hasEffect(YHEffects.YOUKAIFIED.get()) &&
				!player.hasEffect(YHEffects.YOUKAIFYING.get())) {
			return InteractionResultHolder.fail(stack);
		}
		level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.SNOWBALL_THROW, SoundSource.PLAYERS,
				0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
		if (!level.isClientSide) {
			ItemDanmakuEntity danmaku = new ItemDanmakuEntity(YHEntities.ITEM_DANMAKU.get(), player, level);
			danmaku.setItem(stack);
			danmaku.setup(4, 40, false, type.size > 1,
					RayTraceUtil.getRayTerm(Vec3.ZERO, player.getXRot(), player.getYRot(), 2));
			level.addFreshEntity(danmaku);
		}
		player.awardStat(Stats.ITEM_USED.get(this));
		if (head.is(YHTagGen.TOUHOU_HAT) && head.getItem() instanceof TouhouHatItem item && item.support(color)) {
			player.getCooldowns().addCooldown(this, 10);
		} else {
			player.getCooldowns().addCooldown(this, 20);
			if (!player.getAbilities().instabuild) {
				stack.shrink(1);
			}
		}
		return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
	}

	public int getDanmakuColor(ItemStack stack, int i) {
		return i == 0 ? color.getFireworkColor() : 0xffffffff;
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag) {
		var fying = Component.translatable(YHEffects.YOUKAIFYING.get().getDescriptionId());
		var fied = Component.translatable(YHEffects.YOUKAIFIED.get().getDescriptionId());
		list.add(YHLangData.USAGE_DANMAKU.get(fying, fied));
	}

	private DoubleLayerDanmakuType render;

	public RenderableDanmakuType getTypeForRender() {
		if (render == null) {
			render = new DoubleLayerDanmakuType(
					YoukaisHomecoming.loc("textures/item/danmaku/" + type.name + ".png"),
					YoukaisHomecoming.loc("textures/item/danmaku/" + type.name + "_overlay.png"),
					0xff000000 | color.getFireworkColor());
		}
		return render;
	}

}
