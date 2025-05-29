package dev.xkmc.youkaishomecoming.content.item.danmaku;

import dev.xkmc.l2library.util.raytrace.IGlowingTarget;
import dev.xkmc.l2library.util.raytrace.RayTraceUtil;
import dev.xkmc.youkaishomecoming.content.capability.GrazeHelper;
import dev.xkmc.youkaishomecoming.content.spell.item.ItemSpell;
import dev.xkmc.youkaishomecoming.content.spell.item.SpellContainer;
import dev.xkmc.youkaishomecoming.init.data.YHLangData;
import dev.xkmc.youkaishomecoming.init.data.YHModConfig;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.FakePlayer;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class SpellItem extends ProjectileWeaponItem implements IGlowingTarget, ISpellItem {

	public static final List<SpellItem> LIST = new ArrayList<>();

	private final Supplier<ItemSpell> spell;
	private final boolean requireTarget;
	private final Supplier<Item> pred;

	public SpellItem(Properties prop, Supplier<ItemSpell> spell, boolean requireTarget, Supplier<Item> pred) {
		super(prop);
		this.spell = spell;
		this.requireTarget = requireTarget;
		this.pred = pred;
		synchronized (LIST) {
			LIST.add(this);
		}
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		if (GrazeHelper.forbidDanmaku(player))
			return InteractionResultHolder.fail(stack);
		boolean consume = !player.getAbilities().instabuild && !(player instanceof FakePlayer);
		if (!castSpell(stack, player, consume, true)) {
			return InteractionResultHolder.fail(stack);
		}
		return InteractionResultHolder.consume(stack);
	}

	public boolean castSpell(ItemStack stack, Player player, boolean consume, boolean cooldown) {
		ItemStack ammo = !consume ? ItemStack.EMPTY : player.getProjectile(stack);
		if (consume && ammo.isEmpty())
			return false;
		LivingEntity target = RayTraceUtil.serverGetTarget(player);
		if (target != null) GrazeHelper.addSession(player, target);
		if (target == null && requireTarget) {
			target = GrazeHelper.getTarget(player);
			if (target == null) return false;
		}
		if (player instanceof ServerPlayer sp) {
			if (consume)
				ammo.shrink(1);
			SpellContainer.castSpell(sp, spell, target);
			if (cooldown) {
				int cd = YHModConfig.COMMON.playerSpellCooldown.get();
				sp.getCooldowns().addCooldown(this, cd);
			}
		}
		return true;
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag) {
		list.add(YHLangData.SPELL_COST.get(1, pred.get().getName(pred.get().getDefaultInstance())));
		if (requireTarget) {
			list.add(YHLangData.SPELL_TARGET.get());
		}
	}

	@Override
	public void inventoryTick(ItemStack stack, Level level, Entity user, int slot, boolean sel) {
		if (user instanceof Player player && level.isClientSide && sel) {
			RayTraceUtil.clientUpdateTarget(player, 64);
		}
	}

	@Override
	public Predicate<ItemStack> getAllSupportedProjectiles() {
		return e -> e.is(pred.get());
	}

	@Override
	public int getDefaultProjectileRange() {
		return 40;
	}

	@Override
	public int getDistance(ItemStack itemStack) {
		return 64;
	}

}
