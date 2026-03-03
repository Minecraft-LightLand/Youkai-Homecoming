package dev.xkmc.youkaishomecoming.init.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import dev.xkmc.l2core.init.reg.simple.SR;
import dev.xkmc.l2core.init.reg.simple.Val;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.HolderSetCodec;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.neoforged.neoforge.registries.holdersets.CompositeHolderSet;
import net.neoforged.neoforge.registries.holdersets.HolderSetType;
import net.neoforged.neoforge.registries.holdersets.ICustomHolderSet;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class FilterHolderSet<T> extends CompositeHolderSet<T> {

	public record HolderCodec() implements HolderSetType {

		@Override
		public <T> MapCodec<? extends ICustomHolderSet<T>> makeCodec(ResourceKey<? extends Registry<T>> registryKey, Codec<Holder<T>> holderCodec, boolean forceList) {
			return HolderSetCodec.create(registryKey, holderCodec, forceList)
					.listOf()
					.xmap(FilterHolderSet::new, CompositeHolderSet::homogenize)
					.fieldOf("values");
		}

		@Override
		public <T> StreamCodec<RegistryFriendlyByteBuf, ? extends ICustomHolderSet<T>> makeStreamCodec(ResourceKey<? extends Registry<T>> registryKey) {
			return ByteBufCodecs.<RegistryFriendlyByteBuf, HolderSet<T>>list().apply(ByteBufCodecs.holderSet(registryKey))
					.map(FilterHolderSet::new, CompositeHolderSet::getComponents);
		}
	}

	private static final SR<HolderSetType> HOLDERSET = SR.of(YoukaisHomecoming.REG, NeoForgeRegistries.Keys.HOLDER_SET_TYPES);
	public static final Val<HolderSetType> FILTER = HOLDERSET.reg("filter", HolderCodec::new);

	public static void register() {
	}

	public FilterHolderSet(List<HolderSet<T>> components) {
		super(components);
	}

	@Override
	public HolderSetType type() {
		return FILTER.get();
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
