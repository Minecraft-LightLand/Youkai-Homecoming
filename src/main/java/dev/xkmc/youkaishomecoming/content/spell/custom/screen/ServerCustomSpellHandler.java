package dev.xkmc.youkaishomecoming.content.spell.custom.screen;

import dev.xkmc.l2serial.serialization.codec.TagCodec;
import dev.xkmc.youkaishomecoming.content.spell.custom.annotation.ArgGroup;
import dev.xkmc.youkaishomecoming.init.data.YHTagGen;
import dev.xkmc.youkaishomecoming.init.registrate.YHItems;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class ServerCustomSpellHandler {

	public static void handle(@Nullable ServerPlayer sender, Object data) {
		if (sender == null) return;
		ItemStack stack = sender.getMainHandItem();
		if (!stack.is(YHTagGen.CUSTOM_SPELL)) return;
		var tag = TagCodec.valueToTag(Record.class, data, e -> true);
		if (tag == null) return;
		var group = ArgGroup.of(data.getClass());
		if (!group.verifySafe(data)) return;
		stack.getOrCreateTag().put("SpellData", tag);
	}

}
