package dev.xkmc.youkaishomecoming.init.data;

import dev.xkmc.l2damagetracker.init.data.DamageTypeAndTagsGen;
import dev.xkmc.l2damagetracker.init.data.L2DamageTypes;
import dev.xkmc.youkaishomecoming.content.entity.danmaku.IYHDanmaku;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class YHDamageTypes extends DamageTypeAndTagsGen {

	public static final ResourceKey<DamageType> KOISHI = createDamage("koishi_attack");
	public static final ResourceKey<DamageType> RUMIA = createDamage("rumia_attack");
	public static final ResourceKey<DamageType> DANMAKU = createDamage("danmaku");
	public static final ResourceKey<DamageType> ABYSSAL = createDamage("abyssal_danmaku");

	public static final TagKey<DamageType> DANMAKU_TYPE = TagKey.create(Registries.DAMAGE_TYPE, YoukaisHomecoming.loc("danmaku"));

	public YHDamageTypes(PackOutput output, CompletableFuture<HolderLookup.Provider> pvd, ExistingFileHelper helper) {
		super(output, pvd, helper, YoukaisHomecoming.MODID);
		new DamageTypeHolder(KOISHI, new DamageType("koishi_attack", 0.1f))
				.add(DamageTypeTags.BYPASSES_ENCHANTMENTS, DamageTypeTags.BYPASSES_RESISTANCE, DamageTypeTags.BYPASSES_EFFECTS);
		new DamageTypeHolder(RUMIA, new DamageType("rumia_attack", 0.1f))
				.add(DamageTypeTags.BYPASSES_ENCHANTMENTS, DamageTypeTags.BYPASSES_RESISTANCE, DamageTypeTags.BYPASSES_EFFECTS);
		new DamageTypeHolder(DANMAKU, new DamageType("danmaku", 0.1f))
				.add(L2DamageTypes.MAGIC, DamageTypeTags.BYPASSES_ARMOR, DamageTypeTags.BYPASSES_COOLDOWN, DANMAKU_TYPE);
		new DamageTypeHolder(ABYSSAL, new DamageType("abyssal_danmaku", 0.1f))
				.add(L2DamageTypes.MAGIC, DamageTypeTags.BYPASSES_ARMOR, DamageTypeTags.BYPASSES_COOLDOWN, DANMAKU_TYPE)
				.add(L2DamageTypes.BYPASS_MAGIC);
	}

	private static ResourceKey<DamageType> createDamage(String id) {
		return ResourceKey.create(Registries.DAMAGE_TYPE, YoukaisHomecoming.loc(id));
	}

	public static DamageSource koishi(LivingEntity target, Vec3 source) {
		return new DamageSource(target.level().registryAccess()
				.registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(KOISHI),
				source);
	}

	public static DamageSource rumia(LivingEntity self) {
		return new DamageSource(self.level().registryAccess()
				.registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(RUMIA), self);
	}

	public static DamageSource danmaku(IYHDanmaku self) {
		return new DamageSource(self.self().level().registryAccess()
				.registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DANMAKU), self.self(),
				self.self().getOwner());
	}

	public static DamageSource abyssal(IYHDanmaku self) {
		return new DamageSource(self.self().level().registryAccess()
				.registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(ABYSSAL), self.self(),
				self.self().getOwner());
	}


}
