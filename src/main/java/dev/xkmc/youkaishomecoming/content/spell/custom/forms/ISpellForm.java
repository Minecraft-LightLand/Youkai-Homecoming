package dev.xkmc.youkaishomecoming.content.spell.custom.forms;

import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.youkaishomecoming.content.spell.spellcard.Ticker;

@SerialClass
public abstract class ISpellForm<D> extends Ticker<CustomItemSpell> {

	public abstract void init(D data);

}
