package dev.xkmc.youkaishomecoming.init.registrate;

import com.tterrag.registrate.builders.Builder;
import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import dev.xkmc.l2core.init.reg.registrate.L2Registrate;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredHolder;

public class YHRegistrate extends L2Registrate {

	public YHRegistrate(String modid) {
		super(modid);
	}

	protected <R, T extends R> RegistryEntry<R, T> accept(
			String name,
			ResourceKey<? extends Registry<R>> type,
			Builder<R, T, ?, ?> builder,
			NonNullSupplier<? extends T> creator,
			NonNullFunction<DeferredHolder<R, T>, ? extends RegistryEntry<R, T>> entryFactory) {
		var reg = BuiltInRegistries.REGISTRY.get(type.location());
		if (reg != null) {
			reg.addAlias(ResourceLocation.fromNamespaceAndPath("youkaishomecoming", name), YoukaisHomecoming.loc(name));
		}
		return super.accept(name, type, builder, creator, entryFactory);
	}
}
