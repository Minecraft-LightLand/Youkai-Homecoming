


{

food(pvd, YHFood.FLESH, YHFood.COOKED_FLESH);

unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, YHFood.FLESH_ROLL.item, 2)::unlockedBy, YHFood.FLESH.item.get())
		.pattern("FF").pattern("KR")
					.define('F', YHTagGen.RAW_FLESH)
					.define('K', Items.DRIED_KELP)
					.define('R', ModItems.COOKED_RICE.get())
		.save(pvd);

unlock(pvd, ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, YHFood.HIGI_CHOCOLATE.item, 3)::unlockedBy, Items.COCOA_BEANS)
		.requires(YHItems.MATCHA).requires(Items.TWISTING_VINES).requires(Items.PINK_PETALS)
					.requires(Items.HONEY_BOTTLE).requires(Items.BLAZE_POWDER).requires(Items.BLUE_ORCHID)
					.requires(Items.COCOA_BEANS, 3)
					.save(pvd);

unlock(pvd, ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, YHFood.HIGI_DOUGHNUT.item, 1)::unlockedBy, YHFood.HIGI_CHOCOLATE.item.get())
		.requires(YHFood.DOUGHNUT.item).requires(YHFood.HIGI_CHOCOLATE.item)
					.save(pvd);
			CookingPotRecipeBuilder.cookingPotRecipe(YHFood.KOISHI_MOUSSE.item.get(), 1, 200, 0.1f)
		.addIngredient(Items.CORNFLOWER)
					.addIngredient(Items.ALLIUM)
					.addIngredient(CommonTags.FOODS_DOUGH)
					.addIngredient(Items.HONEY_BOTTLE)
					.addIngredient(YHItems.CREAM.get())
		.build(pvd, YHFood.KOISHI_MOUSSE.item.getRegisteredName());


unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.MISC, YHItems.STRAW_HAT)::unlockedBy, ModItems.CANVAS.get())
		.pattern(" A ").pattern("ASA")
					.define('A', ModItems.CANVAS.get())
		.define('S', Items.STRING)
					.save(pvd);

cake(pvd, YHItems.RED_VELVET);

unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, YHFood.FLESH_CHOCOLATE_MOUSSE.item, 4)::unlockedBy, YHFood.FLESH.item.get())
		.pattern(" B ").pattern("FDF").pattern("ECE")
					.define('B', CommonTags.FOODS_MILK)
					.define('C', YHItems.BLOOD_BOTTLE.item)
					.define('D', YHFood.FLESH.item)
					.define('E', Items.WHEAT)
					.define('F', Items.COCOA_BEANS)
					.save(pvd);

unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, YHFood.SCARLET_DEVIL_CAKE.item, 4)::unlockedBy, YHFood.FLESH.item.get())
		.pattern("FBF").pattern("ADA").pattern("ECE")
					.define('A', Items.HONEY_BOTTLE)
					.define('B', CommonTags.FOODS_MILK)
					.define('C', YHItems.BLOOD_BOTTLE.item)
					.define('D', YHFood.FLESH.item)
					.define('E', Items.WHEAT)
					.define('F', Items.PINK_PETALS)
					.save(pvd);

			CookingPotRecipeBuilder.cookingPotRecipe(YHFood.FLESH_DUMPLINGS.item.get(), 2, 200, 0.1f)
		.addIngredient(CommonTags.FOODS_DOUGH)
					.addIngredient(YHTagGen.RAW_FLESH)
					.addIngredient(Tags.Items.FOODS_VEGETABLE)
					.build(pvd, YHFood.FLESH_DUMPLINGS.item.getRegisteredName());

		CookingPotRecipeBuilder.cookingPotRecipe(YHFood.CANNED_FLESH.item.get(), 2, 200, 0.1f, YHItems.CAN)
		.addIngredient(YHTagGen.RAW_FLESH)
					.addIngredient(CommonTags.FOODS_ONION)
					.addIngredient(YHItems.SOY_SAUCE_BOTTLE.item)
					.build(pvd, YHFood.CANNED_FLESH.item.getRegisteredName());

		CookingPotRecipeBuilder.cookingPotRecipe(YHFood.FAIRY_CANDY.item.get(), 3, 200, 0.1f)
		.addIngredient(YHItems.FAIRY_ICE_CRYSTAL)
					.addIngredient(Items.SUGAR)
					.addIngredient(Items.HONEY_BOTTLE)
					.build(pvd, YHFood.FAIRY_CANDY.item.getRegisteredName());

unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, YHItems.RAW_FLESH_FEAST, 1)::unlockedBy, YHFood.FLESH.item.get())
		.pattern("FFF").pattern("1F2").pattern("3S4")
					.define('F', YHTagGen.RAW_FLESH)
					.define('S', Items.SKELETON_SKULL)
					.define('1', Tags.Items.CROPS_CARROT)
					.define('2', Items.BROWN_MUSHROOM)
					.define('3', CommonTags.FOODS_ONION)
					.define('4', CommonTags.FOODS_CABBAGE)
					.save(pvd);

unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, YHItems.RED_VELVET.block, 1)::unlockedBy, YHFood.FLESH.item.get())
		.pattern("ABA").pattern("CDC").pattern("EEE")
					.define('A', Items.SUGAR)
					.define('B', Items.MILK_BUCKET)
					.define('C', YHItems.BLOOD_BOTTLE.item)
					.define('D', YHFood.FLESH.item)
					.define('E', Items.WHEAT)
					.save(pvd);


			CookingPotRecipeBuilder.cookingPotRecipe(YHFood.FLESH_STEW.item.get(), 1, 200, 0.1f, Items.BOWL)
		.addIngredient(YHTagGen.RAW_FLESH)
					.addIngredient(YHTagGen.RAW_FLESH)
					.addIngredient(YHTagGen.RAW_EEL)
					.addIngredient(Tags.Items.FOODS_VEGETABLE)
					.addIngredient(Tags.Items.FOODS_VEGETABLE)
					.addIngredient(YHItems.SOY_SAUCE_BOTTLE.item)
					.build(pvd, YHFood.FLESH_STEW.item.getRegisteredName());

		CookingPotRecipeBuilder.cookingPotRecipe(YHItems.FLESH_FEAST.get(), 1, 200, 0.1f, Items.BOWL)
		.addIngredient(YHItems.RAW_FLESH_FEAST)
					.addIngredient(YHTagGen.RAW_FLESH)
					.addIngredient(YHItems.BLOOD_BOTTLE.item)
					.addIngredient(YHItems.BLOOD_BOTTLE.item)
					.addIngredient(YHItems.SOY_SAUCE_BOTTLE.item)
					.build(pvd, YHItems.FLESH_FEAST.getRegisteredName());

		CookingPotRecipeBuilder.cookingPotRecipe(YHDish.BLOOD_CURD.block.get(), 1, 200, 0.1f, YHItems.SAUCER.get())
		.addIngredient(YHItems.BLOOD_BOTTLE.item)
					.addIngredient(YHItems.BLOOD_BOTTLE.item)
					.addIngredient(Tags.Items.FOODS_VEGETABLE)
					.build(pvd, YHDish.BLOOD_CURD.block.getRegisteredName());

		CookingPotRecipeBuilder.cookingPotRecipe(YHFood.SCARLET_TEA.item.get(), 1, 200, 0.1f, Items.GLASS_BOTTLE)
		.setRecipeBookTab(CookingPotRecipeBookTab.DRINKS)
					.unlockedByAnyIngredient(YHItems.BLOOD_BOTTLE.item)
					.addIngredient(YHTagGen.TEA_BLACK)
					.addIngredient(YHItems.BLOOD_BOTTLE.item)
					.build(tea, YHFood.SCARLET_TEA.item.getRegisteredName());

unlock(pvd, new SimpleFermentationBuilder(YHItems.BLOOD_BOTTLE.fluid.get(), YHSake.SCARLET_MIST.fluid.get(), 3600)::unlockedBy, ModItems.RICE.get())
		.addInput(Items.ROSE_BUSH).addInput(Items.ROSE_BUSH)
					.addInput(Items.POPPY)
					.addInput(YHDanmaku.Bullet.CIRCLE.get(DyeColor.RED))
		.addInput(YHDanmaku.Bullet.CIRCLE.get(DyeColor.RED))
		.save(pvd, YHSake.SCARLET_MIST.item.getRegisteredName());

unlock(pvd, new SimpleFermentationBuilder(Fluids.WATER, YHSake.WIND_PRIESTESSES.fluid.get(), 3600)::unlockedBy, ModItems.RICE.get())
		.addInput(CommonTags.CROPS_RICE).addInput(CommonTags.CROPS_RICE).addInput(CommonTags.CROPS_RICE)
					.addInput(YHDanmaku.Bullet.CIRCLE.get(DyeColor.LIME))
		.addInput(Items.DANDELION).addInput(YHTagGen.TEA_GREEN).addInput(YHItems.MATCHA)
					.save(pvd, YHSake.WIND_PRIESTESSES.item.getRegisteredName());
		}

		if (ModList.get().isLoaded(Create.ID)) {
		CreateRecipeGen.onRecipeGen(pvd);
		}