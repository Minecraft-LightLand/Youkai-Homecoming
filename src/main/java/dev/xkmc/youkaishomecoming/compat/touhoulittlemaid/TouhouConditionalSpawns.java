package dev.xkmc.youkaishomecoming.compat.touhoulittlemaid;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import dev.xkmc.youkaishomecoming.content.entity.fairy.FairyEntity;
import dev.xkmc.youkaishomecoming.init.data.YHModConfig;
import dev.xkmc.youkaishomecoming.init.registrate.YHEntities;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fml.ModList;

import java.util.ArrayList;
import java.util.List;

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

	public static void triggetFairyReinforcement(FairyEntity self, LivingEntity le, Vec3 pos) {
		if (le.level().isClientSide()) return;
		if (!ModList.get().isLoaded(TouhouLittleMaid.MOD_ID)) return;
		if (le.getRandom().nextDouble() > YHModConfig.COMMON.fairySummonReinforcement.get())
			return;
		List<EntityType<? extends FairyEntity>> list = new ArrayList<>(List.of(
				YHEntities.CIRNO.get(), YHEntities.SUNNY.get(), YHEntities.STAR.get(), YHEntities.LUNA.get()
		));
		list.remove(self.getType());
		var e = list.get(self.random().nextInt(list.size())).create(le.level());
		if (e == null) return;
		e.setPos(pos);
		e.setTarget(le);
		e.initSpellCard();
		le.level().addFreshEntity(e);
	}

}
