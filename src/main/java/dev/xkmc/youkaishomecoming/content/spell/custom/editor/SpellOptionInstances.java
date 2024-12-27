package dev.xkmc.youkaishomecoming.content.spell.custom.editor;

import com.mojang.serialization.Codec;
import dev.xkmc.l2serial.util.Wrappers;
import dev.xkmc.youkaishomecoming.content.spell.custom.annotation.*;
import dev.xkmc.youkaishomecoming.content.spell.custom.data.ISpellFormData;
import dev.xkmc.youkaishomecoming.content.spell.custom.screen.ClientCustomSpellHandler;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import it.unimi.dsi.fastutil.doubles.Double2DoubleFunction;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.Options;
import net.minecraft.client.gui.components.OptionsList;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class SpellOptionInstances<T extends Record & ISpellFormData<T>> {

	private SimpleValue<Integer> createLinear(String name, int def, int low, int high) {
		var key = YoukaisHomecoming.MODID + ".custom_spell." + name;
		return new SimpleValue<>(new OptionInstance<>(key,
				v -> Tooltip.create(Component.translatable(key + ".desc")),
				Options::genericValueLabel, new OptionInstance.IntRange(low, high), def, v -> update()), def);
	}

	private DoubleValue createDouble(String name, OptionInstance.CaptionBasedToString<Double> caption, double def, Double2DoubleFunction toUnit, Double2DoubleFunction fromUnit) {
		var key = YoukaisHomecoming.MODID + ".custom_spell." + name;
		return new DoubleValue(new OptionInstance<>(key,
				v -> Tooltip.create(Component.translatable(key + ".desc")),
				(e, v) -> caption.toString(e, fromUnit.apply(v)),
				OptionInstance.UnitDouble.INSTANCE, toUnit.apply(def), v -> update()), toUnit, fromUnit, def);
	}

	private <E extends Enum<E>> SimpleValue<E> createEnum(String name, E[] values, E def) {
		var key = YoukaisHomecoming.MODID + ".custom_spell." + name;
		return new SimpleValue<>(new OptionInstance<>(key + ".title",
				OptionInstance.noTooltip(),
				(e, v) -> Component.translatable(key + "." + v.name().toLowerCase(Locale.ROOT)),
				new OptionInstance.Enum<>(Arrays.asList(values), Codec.INT.xmap(e -> values[e], E::ordinal)),
				def, (p_268018_) -> update()), def);
	}

	private DoubleValue createExp(String name, double def, double a, double b, int decimal) {
		double factor = Math.pow(10, decimal);
		return createDouble(name, (e, v) -> Component.translatable("options.generic_value", e, v), def,
				v -> Math.log(v / b + 1) / Math.log(a + 1),
				u -> Math.round((Math.pow(a + 1, u) - 1) * b * factor) / factor
		);
	}

	private DoubleValue createPercent(String name, double def) {
		return createDouble(name, (e, v) -> Component.translatable("options.percent_value",
				e, Math.round(v * 100)), def, v -> v, u -> Math.round(u * 100) * 0.01);
	}

	private OptionHolder<?> create(ArgField entry, Object obj) {
		String name = entry.field().getName();
		if (entry.range() instanceof UnitArgEntry)
			return createPercent(name, ((Number) obj).doubleValue());
		if (entry.range() instanceof IntArgEntry arg)
			return createLinear(name, ((Number) obj).intValue(), arg.low(), arg.high());
		if (entry.range() instanceof ExpArgEntry arg)
			return createExp(name, ((Number) obj).doubleValue(), arg.base(), arg.factor(), arg.decimal());
		if (entry.range() instanceof EnumArgEntry arg)
			return createEnum(name, Wrappers.cast(arg.vals()), Wrappers.cast(obj));
		throw new IllegalArgumentException("Argument Type " + entry.getClass() + " is invalid");
	}

	private <E> OptionGroup<E> create(List<OptionHolder<?>> list, ArgGroup entry, E obj) throws Exception {
		List<OptionPair> pairs = new ArrayList<>();
		for (var e : entry.list()) {
			var sub = e.field().getAccessor().invoke(obj);
			if (e.range() instanceof ArgGroup group) {
				pairs.add(new OptionPair(e, create(list, group, sub)));
			} else {
				var opt = create(e, sub);
				list.add(opt);
				pairs.add(new OptionPair(e, opt));
			}
		}
		return new OptionGroup<>(entry, pairs);
	}

	@Nullable
	public static <T extends Record & ISpellFormData<T>> SpellOptionInstances<T> create(T obj) {
		try {
			var ans = new SpellOptionInstances<T>();
			ans.list = new ArrayList<>();
			var group = ArgGroup.of(obj.getClass());
			ans.builder = ans.create(ans.list, group, obj);
			return ans;
		} catch (Exception ignored) {
		}
		return null;
	}

	private OptionGroup<T> builder;
	private List<OptionHolder<?>> list;
	private Runnable listener;

	private SpellOptionInstances() {
	}

	public void add(OptionsList widget, Runnable listener) {
		this.listener = listener;
		int n = list.size();
		for (int i = 0; i < n; i += 2) {
			if (i == n - 1) {
				widget.addBig(list.get(i).option());
			} else {
				widget.addSmall(list.get(i).option(), list.get(i + 1).option());
			}
		}
	}

	private void update() {
		if (listener != null) listener.run();
	}

	public void reset() {
		for (var e : list) {
			e.reset();
		}
	}

	@Nullable
	public T build() {
		try {
			return builder.get();
		} catch (Exception ignored) {
			return null;
		}
	}

	public void save() {
		var val = build();
		if (val == null) return;
		ClientCustomSpellHandler.sendToPlayer(val);
	}

}
