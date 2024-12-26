package dev.xkmc.youkaishomecoming.content.spell.custom.editor;

import net.minecraft.client.OptionInstance;

public record SimpleValue<T>(
		OptionInstance<T> option,
		T def
) implements OptionHolder<T> {

	public T get() {
		return option.get();
	}

	@Override
	public void reset() {
		option.set(def);
	}

}
