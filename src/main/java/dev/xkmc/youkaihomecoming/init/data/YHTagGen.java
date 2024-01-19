package dev.xkmc.youkaihomecoming.init.data;

import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.providers.RegistrateItemTagsProvider;
import com.tterrag.registrate.providers.RegistrateTagsProvider;
import dev.xkmc.youkaihomecoming.init.YoukaiHomecoming;
import dev.xkmc.youkaihomecoming.init.registrate.YHEffects;
import net.mehvahdjukaar.jeed.Jeed;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.registries.ForgeRegistries;
import vectorwing.farmersdelight.common.registry.ModBlocks;

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
	public static final TagKey<Block> FARMLAND_REDBEAN = block("farmland_redbean");

	public static final TagKey<MobEffect> HIDDEN = TagKey.create(ForgeRegistries.MOB_EFFECTS.getRegistryKey(),
			new ResourceLocation(Jeed.MOD_ID, "hidden"));

	public static void onEffectTagGen(RegistrateTagsProvider.IntrinsicImpl<MobEffect> pvd) {
		pvd.addTag(HIDDEN).add(YHEffects.YOUKAIFYING.get());
	}

	public static void onBlockTagGen(RegistrateTagsProvider.IntrinsicImpl<Block> pvd) {
		pvd.addTag(FARMLAND_REDBEAN).add(Blocks.CLAY, Blocks.MUD, Blocks.COARSE_DIRT, ModBlocks.RICH_SOIL.get());
	}

	public static void onItemTagGen(RegistrateItemTagsProvider pvd) {
	}

	public static TagKey<Item> item(String id) {
		return ItemTags.create(new ResourceLocation(YoukaiHomecoming.MODID, id));
	}

	public static TagKey<Block> block(String id) {
		return BlockTags.create(new ResourceLocation(YoukaiHomecoming.MODID, id));
	}

}
