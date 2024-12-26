package dev.xkmc.youkaishomecoming.content.spell.custom.editor;

import it.unimi.dsi.fastutil.doubles.Double2DoubleFunction;
import net.minecraft.client.OptionInstance;

public record DoubleValue(
		OptionInstance<Double> option,
		Double2DoubleFunction toUnit,
		Double2DoubleFunction fromUnit,
		double def
) implements OptionHolder<Double> {

	public Double get() {
		return fromUnit.apply(option.get());
	}

	@Override
	public void reset() {
		option.set(toUnit().applyAsDouble(def));
	}

}
