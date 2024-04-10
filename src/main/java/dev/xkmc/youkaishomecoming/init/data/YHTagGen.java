package dev.xkmc.youkaishomecoming.init.data;

import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.providers.RegistrateItemTagsProvider;
import com.tterrag.registrate.providers.RegistrateTagsProvider;
import dev.xkmc.youkaishomecoming.compat.sereneseasons.SeasonCompat;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import dev.xkmc.youkaishomecoming.init.food.YHCrops;
import dev.xkmc.youkaishomecoming.init.food.YHTea;
import dev.xkmc.youkaishomecoming.init.registrate.YHItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;
import sereneseasons.core.SereneSeasons;

public class YHTagGen {


	public static final ProviderType<RegistrateTagsProvider.IntrinsicImpl<MobEffect>> EFF_TAGS =
			ProviderType.register("tags/mob_effect",
					type -> (p, e) -> new RegistrateTagsProvider.IntrinsicImpl<>(p, type, "mob_effects",
							e.getGenerator().getPackOutput(), Registries.MOB_EFFECT, e.getLookupProvider(),
							ench -> ResourceKey.create(ForgeRegistries.MOB_EFFECTS.getRegistryKey(),
									ForgeRegistries.MOB_EFFECTS.getKey(ench)),
							e.getExistingFileHelper()));

	public static final TagKey<Item> RAW_EEL = item("raw_eel");
	public static final TagKey<Item> RAW_FLESH = item("raw_flesh");
	public static final TagKey<Item> DANGO = item("dango");
	public static final TagKey<Item> FLESH_FOOD = item("flesh_food");
	public static final TagKey<Item> APPARENT_FLESH_FOOD = item("apparent_flesh_food");
	public static final TagKey<Block> FARMLAND_REDBEAN = block("farmland_redbean");
	public static final TagKey<Block> FARMLAND_COFFEA = block("farmland_coffea");
	public static final TagKey<EntityType<?>> FLESH_SOURCE = entity("flesh_source");


	public static final TagKey<Item> MATCHA = ItemTags.create(new ResourceLocation("forge", "matcha"));

	public static final TagKey<Item> TEA_GREEN = ItemTags.create(new ResourceLocation("forge", "tea_leaves/green"));
	public static final TagKey<Item> TEA_BLACK = ItemTags.create(new ResourceLocation("forge", "tea_leaves/black"));
	public static final TagKey<Item> TEA_WHITE = ItemTags.create(new ResourceLocation("forge", "tea_leaves/white"));
	public static final TagKey<Item> TEA_OOLONG = ItemTags.create(new ResourceLocation("forge", "tea_leaves/oolong"));
	public static final TagKey<Item> TEA = ItemTags.create(new ResourceLocation("forge", "tea_leaves"));

	public static final TagKey<Item> TOUHOU_HAT = item("touhou_hat");
	public static final TagKey<Item> CIRCLE_DANMAKU = item("circle_danmaku");

	public static void onEffectTagGen(RegistrateTagsProvider.IntrinsicImpl<MobEffect> pvd) {
	}

	public static void onEntityTagGen(RegistrateTagsProvider.IntrinsicImpl<EntityType<?>> pvd) {
		pvd.addTag(FLESH_SOURCE).add(EntityType.EVOKER, EntityType.PILLAGER, EntityType.VINDICATOR, EntityType.ILLUSIONER, EntityType.WITCH,
				EntityType.VILLAGER, EntityType.WANDERING_TRADER, EntityType.PLAYER);
	}

	public static void onBlockTagGen(RegistrateTagsProvider.IntrinsicImpl<Block> pvd) {
		pvd.addTag(FARMLAND_REDBEAN).add(Blocks.CLAY, Blocks.MUD, Blocks.COARSE_DIRT);
		pvd.addTag(FARMLAND_COFFEA).add(Blocks.PODZOL, Blocks.MUD, Blocks.SOUL_SOIL);

		if (ModList.get().isLoaded(SereneSeasons.MOD_ID)) {
			SeasonCompat.genBlock(pvd);
		}
	}

	@SuppressWarnings("unchecked")
	public static void onItemTagGen(RegistrateItemTagsProvider pvd) {
		pvd.addTag(MATCHA).add(YHItems.MATCHA.get()).addOptional(new ResourceLocation("delightful", "matcha"));

		pvd.addTag(TEA_GREEN).add(YHTea.GREEN.leaves.get());
		pvd.addTag(TEA_BLACK).add(YHTea.BLACK.leaves.get());
		pvd.addTag(TEA_WHITE).add(YHTea.WHITE.leaves.get());
		pvd.addTag(TEA_OOLONG).add(YHTea.OOLONG.leaves.get());
		pvd.addTag(TEA).add(YHCrops.TEA.getFruits())
				.addTags(TEA_GREEN, TEA_BLACK, TEA_WHITE, TEA_OOLONG);
		if (ModList.get().isLoaded(SereneSeasons.MOD_ID)) {
			SeasonCompat.genItem(pvd);
		}

	}

	public static TagKey<Item> item(String id) {
		return ItemTags.create(YoukaisHomecoming.loc(id));
	}

	public static TagKey<Block> block(String id) {
		return BlockTags.create(YoukaisHomecoming.loc(id));
	}

	public static TagKey<EntityType<?>> entity(String id) {
		return TagKey.create(Registries.ENTITY_TYPE, YoukaisHomecoming.loc(id));
	}

}
