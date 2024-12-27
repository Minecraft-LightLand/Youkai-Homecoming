package dev.xkmc.youkaishomecoming.content.item.danmaku;

import dev.xkmc.l2library.util.raytrace.RayTraceUtil;
import dev.xkmc.l2serial.serialization.codec.TagCodec;
import dev.xkmc.youkaishomecoming.content.spell.custom.data.ISpellFormData;
import dev.xkmc.youkaishomecoming.content.spell.custom.data.RingSpellFormData;
import dev.xkmc.youkaishomecoming.content.spell.custom.screen.ClientCustomSpellHandler;
import dev.xkmc.youkaishomecoming.content.spell.item.SpellContainer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class CustomSpellItem extends Item {

	public CustomSpellItem(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		if (hand != InteractionHand.MAIN_HAND) return InteractionResultHolder.fail(stack);
		ISpellFormData<?> data = null;
		var tag = stack.getTag();
		if (tag != null && tag.contains("SpellData")) {
			var obj = TagCodec.valueFromTag(tag.getCompound("SpellData"), Record.class);
			if (obj instanceof ISpellFormData<?> dat)
				data = dat;
		}
		if (data == null) data = RingSpellFormData.FLOWER;
		if (player.isShiftKeyDown()) {
			if (level.isClientSide()) {
				ClientCustomSpellHandler.open(data);
			}
		} else {
			if (player instanceof ServerPlayer sp) {
				LivingEntity target = RayTraceUtil.serverGetTarget(player);
				SpellContainer.castSpell(sp, data::createInstance, target);
			}
		}
		return InteractionResultHolder.success(stack);
	}
}
