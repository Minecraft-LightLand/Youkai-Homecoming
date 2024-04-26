package dev.xkmc.youkaishomecoming.content.spell.spellcard;

import dev.xkmc.l2serial.serialization.SerialClass;

import javax.annotation.OverridingMethodsMustInvokeSuper;

@SerialClass
public class Ticker<T extends SpellCard> {

	protected int tick;

	@OverridingMethodsMustInvokeSuper
	public boolean tick(CardHolder holder, T card) {
		tick++;
		return true;
	}

}
