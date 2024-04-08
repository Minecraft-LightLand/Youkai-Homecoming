package dev.xkmc.youkaishomecoming.init.registrate;

import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import dev.xkmc.youkaishomecoming.content.item.damaku.DamakuItem;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import dev.xkmc.youkaishomecoming.init.data.YHTagGen;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Rarity;

import java.util.LinkedHashMap;
import java.util.Map;

public class YHDamaku {

	public static final RegistryEntry<CreativeModeTab> TAB = YoukaisHomecoming.REGISTRATE
			.buildModCreativeTab("damaku", "Youkai's Damaku",
					e -> e.icon(YHDamaku.SIMPLE.get(DyeColor.RED)::asStack));

	public static final Map<DyeColor, ItemEntry<DamakuItem>> SIMPLE = new LinkedHashMap<>();

	static {
		for (var e : DyeColor.values()) {
			var ent = YoukaisHomecoming.REGISTRATE
					.item("simple_" + e.getName() + "_damaku", p -> new DamakuItem(p.rarity(Rarity.RARE), e))
					.model((ctx, pvd) -> pvd.generated(ctx,
							pvd.modLoc("item/damaku/damaku"),
							pvd.modLoc("item/damaku/damaku_overlay")))
					.color(() -> () -> (stack, i) -> ((DamakuItem) stack.getItem()).getDamakuColor(stack, i))
					.tag(YHTagGen.SIMPLE_DAMAKU)
					.register();
			SIMPLE.put(e, ent);
		}
	}

	public static void register() {
	}

}
