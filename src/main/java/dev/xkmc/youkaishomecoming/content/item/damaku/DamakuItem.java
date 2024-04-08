package dev.xkmc.youkaishomecoming.content.item.damaku;

import dev.xkmc.l2library.util.raytrace.RayTraceUtil;
import dev.xkmc.youkaishomecoming.content.entity.damaku.ItemDamakuEntity;
import dev.xkmc.youkaishomecoming.content.item.curio.TouhouHatItem;
import dev.xkmc.youkaishomecoming.init.data.YHTagGen;
import dev.xkmc.youkaishomecoming.init.registrate.YHEffects;
import dev.xkmc.youkaishomecoming.init.registrate.YHEntities;
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
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class DamakuItem extends Item {

	private final DyeColor color;

	public DamakuItem(Properties pProperties, DyeColor color) {
		super(pProperties);
		this.color = color;
	}

	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		ItemStack head = player.getItemBySlot(EquipmentSlot.HEAD);
		boolean support = head.is(YHTagGen.TOUHOU_HAT) && head.getItem() instanceof TouhouHatItem item && item.support(color);
		if (!support && !player.getAbilities().instabuild &&
				!player.hasEffect(YHEffects.YOUKAIFIED.get()) &&
				!player.hasEffect(YHEffects.YOUKAIFYING.get())) {
			return InteractionResultHolder.fail(stack);
		}
		level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.SNOWBALL_THROW, SoundSource.PLAYERS,
				0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
		if (!level.isClientSide) {
			ItemDamakuEntity damaku = new ItemDamakuEntity(YHEntities.ITEM_DAMAKU.get(), player, level);
			damaku.setItem(stack);
			damaku.setup(4, 40, RayTraceUtil.getRayTerm(Vec3.ZERO, player.getXRot(), player.getYRot(), 2));
			level.addFreshEntity(damaku);
		}
		player.awardStat(Stats.ITEM_USED.get(this));
		if (support) {
			player.getCooldowns().addCooldown(this, 10);
		} else {
			player.getCooldowns().addCooldown(this, 20);
			if (!player.getAbilities().instabuild) {
				stack.shrink(1);
			}
		}
		return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
	}

	public int getDamakuColor(ItemStack stack, int i) {
		return i == 0 ? color.getFireworkColor() : 0xffffffff;
	}
}
