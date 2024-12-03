package dev.xkmc.youkaishomecoming.compat.touhoulittlemaid;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import dev.xkmc.youkaishomecoming.init.registrate.YHEntities;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fml.ModList;

public class TouhouConditionalSpawns {

	public static void triggerKoishi(LivingEntity le, Vec3 pos) {
		if (le.level().isClientSide()) return;
		if (!ModList.get().isLoaded(TouhouLittleMaid.MOD_ID)) return;
		var e = YHEntities.KOISHI.create(le.level());
		if (e == null) return;
		e.setPos(pos);
		e.setTarget(le);
		e.initSpellCard();
		le.level().addFreshEntity(e);
	}

	public static void triggetYukari(LivingEntity le, Vec3 pos) {
		if (le.level().isClientSide()) return;
		if (!ModList.get().isLoaded(TouhouLittleMaid.MOD_ID)) return;
		var e = YHEntities.YUKARI.create(le.level());
		if (e == null) return;
		e.setPos(pos);
		e.setTarget(le);
		e.initSpellCard();
		le.level().addFreshEntity(e);

	}
}
