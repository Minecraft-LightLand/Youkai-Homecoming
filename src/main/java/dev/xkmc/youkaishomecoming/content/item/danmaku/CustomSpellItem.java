package dev.xkmc.youkaishomecoming.content.item.danmaku;

import dev.xkmc.l2library.util.raytrace.IGlowingTarget;
import dev.xkmc.l2library.util.raytrace.RayTraceUtil;
import dev.xkmc.l2serial.serialization.codec.TagCodec;
import dev.xkmc.youkaishomecoming.content.capability.GrazeHelper;
import dev.xkmc.youkaishomecoming.content.spell.custom.data.ISpellFormData;
import dev.xkmc.youkaishomecoming.content.spell.custom.screen.ClientCustomSpellHandler;
import dev.xkmc.youkaishomecoming.content.spell.item.SpellContainer;
import dev.xkmc.youkaishomecoming.init.data.YHLangData;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.FakePlayer;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CustomSpellItem extends Item implements IGlowingTarget, ISpellItem {

	private ISpellFormData<?> getData(ItemStack stack) {
		var tag = stack.getTag();
		if (tag != null && tag.contains("SpellData")) {
			var obj = TagCodec.valueFromTag(tag.getCompound("SpellData"), Record.class);
			if (obj instanceof ISpellFormData<?> dat)
				return dat;
		}
		return def;
	}

	private final ISpellFormData<?> def;
	private final boolean requireTarget;

	public CustomSpellItem(Properties properties, boolean requireTarget, ISpellFormData<?> def) {
		super(properties);
		this.requireTarget = requireTarget;
		this.def = def;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		if (hand != InteractionHand.MAIN_HAND) return InteractionResultHolder.fail(stack);
		ISpellFormData<?> data = getData(stack);
		if (player.isShiftKeyDown()) {
			if (level.isClientSide()) {
				ClientCustomSpellHandler.open(data);
			}
		} else {
			boolean consume = !player.getAbilities().instabuild && !(player instanceof FakePlayer);
			if (!castSpellImpl(data, player, consume, true)) {
				return InteractionResultHolder.fail(stack);
			}
		}
		return InteractionResultHolder.success(stack);
	}

	public boolean castSpell(ItemStack stack, Player player, boolean consume, boolean cooldown) {
		return castSpellImpl(getData(stack), player, consume, cooldown);
	}

	private boolean castSpellImpl(ISpellFormData<?> data, Player player, boolean consume, boolean cooldown) {
		if (GrazeHelper.forbidDanmaku(player))
			return false;
		LivingEntity target = RayTraceUtil.serverGetTarget(player);
		if (target != null) GrazeHelper.addSession(player, target);
		if (requireTarget && target == null) {
			target = GrazeHelper.getTarget(player);
			if (target == null) return false;
		}
		if (consume) {
			Item ammo = data.getAmmoCost();
			int toCost = data.cost();
			if (!consumeAmmo(ammo, toCost, player, false))
				return false;
			if (player instanceof ServerPlayer)
				consumeAmmo(ammo, toCost, player, true);
		}
		if (player instanceof ServerPlayer sp) {
			SpellContainer.castSpell(sp, data::createInstance, target);
			if (cooldown) {
				player.getCooldowns().addCooldown(this, data.getDuration());
			}
		}
		return true;
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag) {
		ISpellFormData<?> data = getData(stack);
		var item = data.getAmmoCost();
		list.add(YHLangData.SPELL_COST.get(data.cost(), item.getName(item.getDefaultInstance())));
		if (requireTarget) {
			list.add(YHLangData.SPELL_TARGET.get());
		}
	}

	private static boolean consumeAmmo(Item ammo, int toCost, Player player, boolean execute) {
		var inv = player.getInventory();
		for (int i = 0; i < inv.getContainerSize(); i++) {
			if (toCost <= 0) break;
			ItemStack item = inv.getItem(i);
			if (item.is(ammo)) {
				int consume = Math.min(toCost, item.getCount());
				if (execute) item.shrink(consume);
				toCost -= consume;
			}
		}
		return toCost == 0;
	}

	@Override
	public void inventoryTick(ItemStack stack, Level level, Entity user, int slot, boolean sel) {
		if (user instanceof Player player && level.isClientSide && sel) {
			RayTraceUtil.clientUpdateTarget(player, 64);
		}
	}

	@Override
	public int getDistance(ItemStack stack) {
		return 64;
	}

}
