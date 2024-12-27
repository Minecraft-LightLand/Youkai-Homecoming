package dev.xkmc.youkaishomecoming.content.spell.custom.data;

import dev.xkmc.l2serial.util.Wrappers;
import dev.xkmc.youkaishomecoming.content.spell.item.ItemSpell;
import net.minecraft.world.item.Item;

public interface ISpellFormData<T extends Record & ISpellFormData<T>> {

	int getDuration();

	int cost();

	ItemSpell createInstance();

	default T cast() {
		return Wrappers.cast(this);
	}

	Item getAmmoCost();

}
