package dev.xkmc.youkaishomecoming.content.item.danmaku;

import dev.xkmc.fastprojectileapi.render.core.ProjTypeHolder;
import dev.xkmc.fastprojectileapi.render.type.DoubleLayerLaserType;
import dev.xkmc.fastprojectileapi.render.type.PencilLayerLaserType;
import dev.xkmc.fastprojectileapi.render.type.RenderableProjectileType;
import dev.xkmc.youkaishomecoming.content.capability.GrazeHelper;
import dev.xkmc.youkaishomecoming.content.entity.danmaku.ItemLaserEntity;
import dev.xkmc.youkaishomecoming.content.item.curio.hat.TouhouHatItem;
import dev.xkmc.youkaishomecoming.content.spell.item.SpellContainer;
import dev.xkmc.youkaishomecoming.events.EffectEventHandlers;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import dev.xkmc.youkaishomecoming.init.data.YHLangData;
import dev.xkmc.youkaishomecoming.init.data.YHModConfig;
import dev.xkmc.youkaishomecoming.init.data.YHTagGen;
import dev.xkmc.youkaishomecoming.init.registrate.YHDanmaku;
import dev.xkmc.youkaishomecoming.init.registrate.YHEffects;
import dev.xkmc.youkaishomecoming.init.registrate.YHEntities;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
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
	public final YHDanmaku.Laser type;
	public final float size;

	public LaserItem(Properties pProperties, YHDanmaku.Laser type, DyeColor color, float size) {
		super(pProperties);
		this.color = color;
		this.type = type;
		this.size = size;
	}

	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		ItemStack head = player.getItemBySlot(EquipmentSlot.HEAD);
		if (!head.is(YHTagGen.TOUHOU_HAT) && !player.getAbilities().instabuild &&
				!EffectEventHandlers.isCharacter(player)) {
			return InteractionResultHolder.fail(stack);
		}
		if (GrazeHelper.forbidDanmaku(player))
			return InteractionResultHolder.fail(stack);
		level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.SNOWBALL_THROW, SoundSource.PLAYERS,
				0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
		int cooldown = type.setupLength() ? YHModConfig.COMMON.playerDanmakuCooldown.get() : YHModConfig.COMMON.playerLaserCooldown.get();
		if (!level.isClientSide) {
			ItemLaserEntity danmaku = new ItemLaserEntity(YHEntities.ITEM_LASER.get(), player, level);
			danmaku.setItem(stack);
			int dur = YHModConfig.COMMON.playerLaserDuration.get();
			if (type.setupLength()) {
				int delay = 4;
				float v = 2f;
				float lenAll = v * delay;
				float vl = type.visualLength();
				float len = lenAll / vl;
				float v0 = (vl - 1) / 2 * v;
				danmaku.setup(type.damage(), dur, len, false, player.getYRot(), player.getXRot());
				danmaku.setupLength = true;
				danmaku.setupTime(1, delay, dur, 1);
				danmaku.setDelayedMover(v0, v, 1, delay);
			} else {
				danmaku.setup(type.damage(), dur, 40, false, player.getYRot(), player.getXRot());
			}
			level.addFreshEntity(danmaku);
			if (player instanceof ServerPlayer sp)
				SpellContainer.track(sp, danmaku);
		}
		player.awardStat(Stats.ITEM_USED.get(this));
		if (head.is(YHTagGen.TOUHOU_HAT) && head.getItem() instanceof TouhouHatItem item && item.support(color)) {
			player.getCooldowns().addCooldown(this, cooldown / 2);
		} else {
			player.getCooldowns().addCooldown(this, cooldown);
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
		list.add(YHLangData.DANMAKU_DAMAGE.get(type.damage()));
	}

	private ProjTypeHolder<? extends RenderableProjectileType<?, ?>, ?> render;

	public ProjTypeHolder<? extends RenderableProjectileType<?, ?>, ?> getTypeForRender() {
		if (render == null) {
			render = switch (type) {
				case LASER -> ProjTypeHolder.wrap(new DoubleLayerLaserType(
						YoukaisHomecoming.loc("textures/entities/laser_inner.png"),
						YoukaisHomecoming.loc("textures/entities/laser_outer.png"),
						0xff000000 | color.getFireworkColor()));
				case PENCIL -> ProjTypeHolder.wrap(new PencilLayerLaserType(
						YoukaisHomecoming.loc("textures/entities/laser_inner.png"),
						YoukaisHomecoming.loc("textures/entities/laser_outer.png"),
						0xff000000 | color.getFireworkColor()));
			};
		}
		return render;
	}

}
