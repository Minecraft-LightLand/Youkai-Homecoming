package dev.xkmc.youkaishomecoming.content.spell.spellcard;

import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.l2serial.util.Wrappers;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.util.ArrayList;

@SerialClass
public class ActualSpellCard extends SpellCard {

	@SerialClass.SerialField
	public int tick, hit;

	@SerialClass.SerialField
	private final ArrayList<Ticker<?>> tickers = new ArrayList<>();

	@OverridingMethodsMustInvokeSuper
	public void tick(CardHolder holder) {
		tick++;
		tickers.removeIf(t -> t.tick(holder, Wrappers.cast(this)));
	}

	public void reset() {
		tick = 0;
		hit = 0;
		tickers.clear();
	}

	public void hurt(CardHolder holder, DamageSource source, float amount) {
		if (source.getEntity() instanceof LivingEntity && amount > 1) {
			hit++;
		}
	}

	protected <T extends ActualSpellCard> void addTicker(Ticker<T> tick) {
		tickers.add(tick);
	}

}
