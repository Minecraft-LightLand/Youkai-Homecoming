package dev.xkmc.youkaishomecoming.content.spell.custom.forms;

import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.youkaishomecoming.content.spell.item.ItemSpell;

@SerialClass
public abstract class ISpellForm<D> extends ItemSpell {

	public abstract void init(D data);

}
