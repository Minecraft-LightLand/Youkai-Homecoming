package dev.xkmc.youkaishomecoming.content.spell.custom.editor;

import net.minecraft.client.OptionInstance;

public interface OptionHolder<T> extends ValueProvider<T> {

	OptionInstance<T> option();

	T get();

	void reset();

}
