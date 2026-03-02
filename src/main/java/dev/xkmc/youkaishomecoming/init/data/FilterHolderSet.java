package dev.xkmc.youkaishomecoming.init.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.resources.HolderSetCodec;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.neoforged.neoforge.registries.holdersets.CompositeHolderSet;
import net.neoforged.neoforge.registries.holdersets.HolderSetType;
import net.neoforged.neoforge.registries.holdersets.ICustomHolderSet;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class FilterHolderSet<T> extends CompositeHolderSet<T> {

	private static final DeferredRegister<HolderSetType> HOLDERSET =
			DeferredRegister.create(NeoForgeRegistries.Keys.HOLDER_SET_TYPES, YoukaisHomecoming.MODID);

	public static final Holder<HolderSetType> FILTER =
			HOLDERSET.register("filter", () -> FilterHolderSet::codec);

	public static void register() {
		HOLDERSET.register(YoukaisHomecoming.REGISTRATE.getModEventBus());
	}

	public static <T> MapCodec<? extends ICustomHolderSet<T>> codec(ResourceKey<? extends Registry<T>> registryKey, Codec<Holder<T>> holderCodec, boolean forceList) {
		return HolderSetCodec.create(registryKey, holderCodec, forceList)
				.listOf()
				.xmap(FilterHolderSet::new, CompositeHolderSet::homogenize)
				.fieldOf("values");
	}

	public FilterHolderSet(List<HolderSet<T>> components) {
		super(components);
	}

	@Override
	public HolderSetType type() {
		return FILTER.value();
	}

	@Override
	protected Set<Holder<T>> createSet() {
		List<HolderSet<T>> components = this.getComponents();
		if (components.isEmpty()) {
			return Set.of();
		}
		if (components.size() == 1) {
			return components.get(0).stream().collect(Collectors.toSet());
		}
		List<HolderSet<T>> remain = components.subList(1, components.size());
		return components.get(0).stream()
				.filter(e -> remain.stream().noneMatch(s -> s.contains(e)))
				.collect(Collectors.toSet());
	}

	@Override
	public String toString() {
		return "FilterSet[" + this.getComponents() + "]";
	}

}
