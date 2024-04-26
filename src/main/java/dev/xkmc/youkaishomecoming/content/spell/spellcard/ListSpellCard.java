package dev.xkmc.youkaishomecoming.content.spell.spellcard;

import dev.xkmc.danmaku.entity.DanmakuMovement;
import dev.xkmc.l2serial.serialization.SerialClass;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;

@SerialClass
public class ListSpellCard extends SpellCard {

	@SerialClass.SerialField
	public final ArrayList<SpellCard> list = new ArrayList<>();

	@SerialClass.SerialField
	public int index, health = 20;

	public SpellCard card() {
		return list.get(index);
	}

	@Override
	public void tick(CardHolder holder) {
		super.tick(holder);
		if (card().hit >= health) {
			index++;
			if (index == list.size()) {
				index = 0;
			}
			card().reset();
		}
		card().tick(holder);
	}

	@Override
	public DanmakuMovement move(int code, int tickCount, Vec3 vec) {
		return card().move(code, tickCount, vec);
	}

	public void hurt(DamageSource source, float amount) {
		super.hurt(source, amount);
		card().hurt(source, amount);
	}

	@Override
	public void reset() {
		super.reset();
		for (var e : list) {
			e.reset();
		}
		index = 0;
	}

}
