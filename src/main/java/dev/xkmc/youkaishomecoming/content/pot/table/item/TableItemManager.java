package dev.xkmc.youkaishomecoming.content.pot.table.item;

import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import dev.xkmc.youkaishomecoming.content.pot.table.food.FoodModelHelper;
import dev.xkmc.youkaishomecoming.content.pot.table.model.*;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import dev.xkmc.youkaishomecoming.init.data.YHTagGen;
import dev.xkmc.youkaishomecoming.init.food.YHFood;
import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;
import dev.xkmc.youkaishomecoming.init.registrate.YHItems;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import vectorwing.farmersdelight.common.registry.ModItems;
import vectorwing.farmersdelight.common.tag.ForgeTags;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TableItemManager extends BaseTableItem {

	public static final ModelHolderManager MANAGER = ModelHolderManager.createModelBuilderManager(YoukaisHomecoming.REGISTRATE);

	private static FixedModelHolder fixed(String id) {
		return new FixedModelHolder(MANAGER, YoukaisHomecoming.loc("fixed/" + id));
	}

	private static VariantModelHolder variant(String id) {
		return new VariantModelHolder(MANAGER, YoukaisHomecoming.loc(id));
	}

	private static AdditionalModelHolder top(String id) {
		return new AdditionalModelHolder(MANAGER, YoukaisHomecoming.loc(id));
	}

	public static final TableItemManager TABLE = new TableItemManager();

	public static final IngredientTableItem RICE = TABLE.with(fixed("rice").putDefault("rice"), ModItems.COOKED_RICE::get);
	public static final IngredientTableItem SUSHI = RICE.addNext(variant("sushi").putDefault("rice"));
	public static final IngredientTableItem GUNKAN = SUSHI.with(variant("gunkan").putDefault("rice", "kelp"), Items.DRIED_KELP);
	public static final IngredientTableItem RICE_2 = RICE.with(fixed("rice_2").putDefault("rice"), ModItems.COOKED_RICE::get);
	public static final IngredientTableItem CAL = RICE_2.with(variant("open_california").putDefault("rice", "kelp"), Items.DRIED_KELP);

	public static final IngredientTableItem KELP = TABLE.with(fixed("kelp").putDefault("kelp"), Items.DRIED_KELP);
	public static final IngredientTableItem HOSOMAKI = KELP.with(variant("open_hosomaki").putDefault("kelp", "rice"), ModItems.COOKED_RICE::get);
	public static final IngredientTableItem FUTOMAKI = HOSOMAKI.with(variant("open_futomaki").putDefault("kelp", "rice"), ModItems.COOKED_RICE::get);

	public static final VariantTableItemBase BASE_SUSHI = SUSHI.asBase(YoukaisHomecoming.loc("sushi"));
	public static final VariantTableItemBase BASE_GUNKAN = GUNKAN.asBase(YoukaisHomecoming.loc("gunkan"));
	public static final VariantTableItemBase BASE_HOSOMAKI = HOSOMAKI.asBase(YoukaisHomecoming.loc("hosomaki"));
	public static final VariantTableItemBase BASE_FUTOMAKI = FUTOMAKI.asBase(YoukaisHomecoming.loc("futomaki"));
	public static final VariantTableItemBase BASE_CAL = CAL.asBase(YoukaisHomecoming.loc("california"));

	public static final VariantModelPart SUSHI_TOP = BASE_SUSHI.addPart("top", 1);
	public static final VariantModelPart SUSHI_KELP = BASE_SUSHI.addPart("kelp", 1);
	public static final VariantModelPart SUSHI_CURD = BASE_SUSHI.addPart("curd", 1);
	public static final VariantModelPart SUSHI_SAUCE = BASE_SUSHI.addPart("sauce", 1);
	public static final VariantModelPart GUNKAN_TOP = BASE_GUNKAN.addPart("top", 1);
	public static final VariantModelPart HOSOMAKI_SAUCE = BASE_HOSOMAKI.addPart("sauce", 1);
	public static final VariantModelPart HOSOMAKI_INGREDIENT = BASE_HOSOMAKI.addPart("ingredient", 1);
	public static final VariantModelPart FUTOMAKI_SAUCE = BASE_FUTOMAKI.addPart("sauce", 1);
	public static final VariantModelPart FUTOMAKI_INGREDIENT = BASE_FUTOMAKI.addPart("ingredient", 5);
	public static final VariantModelPart CAL_SAUCE = BASE_CAL.addPart("sauce", 1);
	public static final VariantModelPart CAL_INGREDIENT = BASE_CAL.addPart("ingredient", 5);

	public static final FoodTableItemBase COMPLETE_HOSOMAKI = BASE_HOSOMAKI.addNextStep(null);
	public static final FoodTableItemBase COMPLETE_FUTOMAKI = BASE_FUTOMAKI.addNextStep(null);
	public static final FoodTableItemBase COMPLETE_CALI = BASE_CAL.addNextStep(top("california"));

	public static final VariantModelPart CAL_TOP = COMPLETE_CALI.addPart("top", 3);
	public static final VariantModelPart CAL_COVER = COMPLETE_CALI.addPart("cover", 1);
	public static final VariantModelPart CAL_TOP_SAUCE = COMPLETE_CALI.addPart("sauce", 1);

	static {
		SUSHI_TOP.addMapping("salmon", ForgeTags.RAW_FISHES_SALMON).seareable();
		SUSHI_TOP.addMapping("cod", ForgeTags.RAW_FISHES_COD);
		SUSHI_TOP.addMapping("tamagoyaki", YHTagGen.TAMAGOYAKI);
		SUSHI_TOP.addMapping("lamprey", YHTagGen.COOKED_EEL);
		SUSHI_TOP.addMapping("tuna", YHTagGen.RAW_TUNA);
		SUSHI_TOP.addMapping("otoro", YHFood.OTORO.item);
		SUSHI_KELP.addMapping("kelp", Items.DRIED_KELP);
		SUSHI_SAUCE.addMapping("sugar", Items.SUGAR).seareable();
		SUSHI_SAUCE.addMapping("mayonnaise", YHItems.MAYONNAISE.item);

		GUNKAN_TOP.addMapping("roe", YHFood.ROE.item);
		GUNKAN_TOP.addMapping("seagrass", Items.SEAGRASS);
		GUNKAN_TOP.addMapping("nattou", YHFood.NATTOU.item);
		//shirako
		CAL_TOP.addMapping("salmon", ForgeTags.RAW_FISHES_SALMON).seareable();
		CAL_TOP.addMapping("cod", ForgeTags.RAW_FISHES_COD);
		CAL_TOP.addMapping("tuna", YHTagGen.RAW_TUNA);
		CAL_TOP.addMapping("otoro", YHFood.OTORO.item);
		CAL_COVER.addMapping("roe", YHFood.ROE.item);

		VariantModelPart[] rolls = {HOSOMAKI_INGREDIENT, FUTOMAKI_INGREDIENT, CAL_INGREDIENT};
		VariantModelPart[] sauces = {HOSOMAKI_SAUCE, FUTOMAKI_SAUCE, CAL_SAUCE, CAL_TOP_SAUCE};

		addBulk("soy_sauce", "sauce/soy_sauce", YHItems.SOY_SAUCE_BOTTLE.item, sauces);
		addBulk("mayonnaise", "sauce/mayonnaise", YHItems.MAYONNAISE.item, sauces);

		addBulk("salmon", "ingredient/salmon", ForgeTags.RAW_FISHES_SALMON, rolls);
		addBulk("tuna", "ingredient/tuna", YHTagGen.RAW_TUNA, rolls);
		addBulk("carrot", "ingredient/carrot", Items.CARROT, rolls);
		addBulk("beetroot", "ingredient/beetroot", Items.BEETROOT, rolls);
		addBulk("tamagoyaki", "ingredient/tamagoyaki", YHFood.TAMAGOYAKI_SLICE.item, rolls);
		addBulk("imitation_crab", "ingredient/imitation_crab", YHFood.IMITATION_CRAB.item, rolls);
		addBulk("cucumber", "ingredient/cucumber", YHTagGen.CUCUMBER_SLICE, rolls);
		// kappa
	}

	private static void addBulk(String id, String path, ItemLike item, VariantModelPart... parts) {
		for (var part : parts) {
			part.addMapping(id, item).tex(YoukaisHomecoming.loc("block/table/" + path));
		}
	}

	private static void addBulk(String id, String path, TagKey<Item> item, VariantModelPart... parts) {
		for (var part : parts) {
			part.addMapping(id, item).tex(YoukaisHomecoming.loc("block/table/" + path));
		}
	}

	@Override
	public Optional<TableItem> find(Level level, ItemStack stack) {
		var ans = super.find(level, stack);
		if (ans.isPresent()) return ans;
		var preset = FoodModelHelper.find(stack);
		if (preset != null) return Optional.of(new FoodTableItem(preset.base(), stack));
		var rec = level.getRecipeManager().getAllRecipesFor(YHBlocks.CUISINE_RT.get());
		for (var e : rec) {
			if (ItemStack.isSameItemSameTags(e.getResult(), stack)) {
				return e.recreate();
			}
		}
		return Optional.empty();
	}

	public Either<TableItem, Pair<TableItem, List<ItemStack>>> find(Level level, List<ItemStack> list) {
		TableItem ans = TABLE;
		List<ItemStack> success = new ArrayList<>();
		for (var e : list) {
			var opt = ans.find(level, e);
			if (opt.isEmpty()) return Either.right(Pair.of(ans, success));
			success.add(e);
			ans = opt.get();
		}
		return Either.left(ans);
	}

	public static void init() {

	}

}
