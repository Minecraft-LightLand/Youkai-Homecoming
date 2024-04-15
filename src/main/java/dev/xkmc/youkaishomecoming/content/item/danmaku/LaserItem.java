package dev.xkmc.youkaishomecoming.content.item.danmaku;

import dev.xkmc.danmaku.render.DoubleLayerLaserType;
import dev.xkmc.youkaishomecoming.content.entity.danmaku.ItemLaserEntity;
import dev.xkmc.youkaishomecoming.content.item.curio.TouhouHatItem;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import dev.xkmc.youkaishomecoming.init.data.YHLangData;
import dev.xkmc.youkaishomecoming.init.data.YHTagGen;
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
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class LaserItem extends Item {

	public final DyeColor color;
	public final float size;

	public LaserItem(Properties pProperties, DyeColor color, float size) {
		super(pProperties);
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
			ItemLaserEntity danmaku = new ItemLaserEntity(YHEntities.ITEM_LASER.get(), player, level);
			danmaku.setItem(stack);
			danmaku.setup(4, 100, 10, false, player.getYRot(), player.getXRot());
			level.addFreshEntity(danmaku);
		}
		player.awardStat(Stats.ITEM_USED.get(this));
		if (head.is(YHTagGen.TOUHOU_HAT) && head.getItem() instanceof TouhouHatItem item && item.support(color)) {
			player.getCooldowns().addCooldown(this, 40);
		} else {
			player.getCooldowns().addCooldown(this, 80);
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

	private DoubleLayerLaserType render;

	public DoubleLayerLaserType getTypeForRender() {
		if (render == null) {
			render = new DoubleLayerLaserType(
					YoukaisHomecoming.loc("textures/entities/laser_inner.png"),
					YoukaisHomecoming.loc("textures/entities/laser_outer.png"),
					0xff000000 | color.getFireworkColor());
		}
		return render;
	}

}
