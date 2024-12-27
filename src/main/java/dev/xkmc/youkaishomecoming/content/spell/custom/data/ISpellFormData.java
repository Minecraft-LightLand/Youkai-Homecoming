package dev.xkmc.youkaishomecoming.content.spell.custom.data;

import dev.xkmc.l2serial.util.Wrappers;
import dev.xkmc.youkaishomecoming.content.spell.item.ItemSpell;

public interface ISpellFormData<T extends Record & ISpellFormData<T>> {

	int getDuration();

	int cost();

	ItemSpell createInstance();

	default T cast() {
		return Wrappers.cast(this);
	}

}
