package dev.xkmc.youkaishomecoming.compat.touhoulittlemaid;

import com.github.tartaricacid.touhoulittlemaid.init.InitItems;
import com.tterrag.registrate.providers.loot.RegistrateEntityLootTables;
import com.tterrag.registrate.util.entry.EntityEntry;
import dev.xkmc.youkaishomecoming.compat.touhoulittlemaid.fairy.SmallFairy;
import dev.xkmc.youkaishomecoming.compat.touhoulittlemaid.fairy.SmallFairyRenderer;
import dev.xkmc.youkaishomecoming.content.entity.fairy.FairyEntity;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import dev.xkmc.youkaishomecoming.init.registrate.YHDanmaku;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;

public class TLMRegistries {

	public static final EntityEntry<SmallFairy> SMALL_FAIRY = YoukaisHomecoming.REGISTRATE
			.entity("small_fairy", SmallFairy::new, MobCategory.MONSTER)
			.properties(e -> e.sized(0.4F, 1.8f).clientTrackingRange(10))
			.attributes(FairyEntity::createAttributes)
			.renderer(() -> SmallFairyRenderer::new)
			.spawnEgg(0x7f4f5f, 0xffffff).tab(YHDanmaku.TAB.getKey()).build()
			.loot(TLMRegistries::fairyLoot).register();

	private static void fairyLoot(RegistrateEntityLootTables pvd, EntityType<SmallFairy> et) {
		pvd.add(et, LootTable.lootTable().withPool(LootPool.lootPool()
				.add(LootItem.lootTableItem(InitItems.POWER_POINT.get()))));
	}

	public static void init() {

	}

}
