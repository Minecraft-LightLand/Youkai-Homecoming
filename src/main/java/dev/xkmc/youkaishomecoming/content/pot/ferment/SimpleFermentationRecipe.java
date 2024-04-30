package dev.xkmc.youkaishomecoming.content.pot.ferment;

import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

@SerialClass
public class SimpleFermentationRecipe extends FermentationRecipe<SimpleFermentationRecipe> {

	public SimpleFermentationRecipe(ResourceLocation id) {
		super(id, YHBlocks.FERMENT_RS.get());
	}

	@Override
	public boolean matches(FermentationDummyContainer fermentationDummyContainer, Level level) {
		return false; //TODO
	}

	@Override
	public ItemStack assemble(FermentationDummyContainer fermentationDummyContainer, RegistryAccess registryAccess) {
		return ItemStack.EMPTY; //TODO
	}

}
