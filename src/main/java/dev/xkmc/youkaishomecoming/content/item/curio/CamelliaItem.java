package dev.xkmc.youkaishomecoming.content.item.curio;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateItemModelProvider;
import dev.xkmc.youkaishomecoming.init.data.YHLangData;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.loaders.SeparateTransformsModelBuilder;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CamelliaItem extends Item {

	public CamelliaItem(Properties properties) {
		super(properties);
	}

	@Override
	public @Nullable EquipmentSlot getEquipmentSlot(ItemStack stack) {
		return EquipmentSlot.HEAD;
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag) {
		list.add(YHLangData.CAMELLIA.get());
	}

	public static void buildModel(DataGenContext<Item, CamelliaItem> ctx, RegistrateItemModelProvider pvd) {
		var itemModel = new ItemModelBuilder(null, pvd.existingFileHelper)
				.parent(new ModelFile.UncheckedModelFile(pvd.mcLoc("item/generated")))
				.texture("layer0", pvd.modLoc("item/crops/" + ctx.getName()));
		var blockModel = new ItemModelBuilder(null, pvd.existingFileHelper)
				.parent(new ModelFile.UncheckedModelFile(pvd.mcLoc("block/air")));
		pvd.getBuilder("item/" + ctx.getName())
				.guiLight(BlockModel.GuiLight.FRONT)
				.customLoader(SeparateTransformsModelBuilder::begin)
				.base(itemModel)
				.perspective(ItemDisplayContext.HEAD, blockModel);
	}

}
