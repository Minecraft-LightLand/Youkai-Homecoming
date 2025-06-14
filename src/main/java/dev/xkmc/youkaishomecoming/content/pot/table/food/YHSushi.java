package dev.xkmc.youkaishomecoming.content.pot.table.food;

import com.tterrag.registrate.util.entry.ItemEntry;
import dev.xkmc.youkaishomecoming.compat.diet.DietTagGen;
import dev.xkmc.youkaishomecoming.init.food.EffectEntry;
import dev.xkmc.youkaishomecoming.init.food.FoodType;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.Nullable;
import vectorwing.farmersdelight.common.registry.ModEffects;

import java.util.List;
import java.util.Locale;

public enum YHSushi implements ItemLike {

	TOBIKO_GUNKAN(FoodType.MEAT_SLICE, 8, 0.8f, List.of(
			new EffectEntry(ModEffects.NOURISHMENT, 2400, 0, 1),
			new EffectEntry(ModEffects.COMFORT, 2400, 0, 1)),
			DietTagGen.GRAINS.tag, DietTagGen.PROTEINS.tag),//TODO recipe change
	SEAGRASS_GUNKAN(FoodType.FAST, 6, 0.6f, List.of(),
			DietTagGen.GRAINS.tag, DietTagGen.VEGETABLES.tag),
	//shirako
	EGG_NIGIRI(FoodType.MEAT_SLICE, 7, 0.6f, List.of(),
			DietTagGen.GRAINS.tag, DietTagGen.PROTEINS.tag),
	LORELEI_NIGIRI(FoodType.MEAT_SLICE, 7, 0.8f, List.of(
			new EffectEntry(ModEffects.NOURISHMENT, 1200, 0, 1)),
			DietTagGen.GRAINS.tag, DietTagGen.PROTEINS.tag),
	//TODO tuna nigiri
	;

	public final ItemEntry<Item> item;
	private final FoodType type;

	@SafeVarargs
	YHSushi(FoodType type, int nutrition, float sat, @Nullable String raw, List<EffectEntry> effs, TagKey<Item>... tags) {
		this.type = type;
		String name = name().toLowerCase(Locale.ROOT);
		String id = "food/sushi/";
		item = type.build(id, name, nutrition, sat, tags, effs);
	}

	@SafeVarargs
	YHSushi(FoodType type, int nutrition, float sat, List<EffectEntry> effs, TagKey<Item>... tags) {
		this(type, nutrition, sat, null, effs, tags);
	}

	@Override
	public Item asItem() {
		return item.asItem();
	}

	private boolean isFlesh() {
		return type.isFlesh();
	}

	private boolean isUnappealing() {
		return false;
	}

	public boolean isReimuFood() {
		return !isFlesh() && !isUnappealing();
	}

	public static void register() {

	}

}
