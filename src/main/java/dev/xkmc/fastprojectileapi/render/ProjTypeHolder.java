package dev.xkmc.fastprojectileapi.render;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.xkmc.fastprojectileapi.entity.SimplifiedProjectile;
import dev.xkmc.l2serial.util.Wrappers;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class ProjTypeHolder<T extends RenderableProjectileType<T, I>, I> implements Consumer<I> {

	protected static final List<ProjTypeHolder<?, ?>> HOLDERS = new ArrayList<>();
	protected static final Map<RenderableProjectileType<?, ?>, ProjTypeHolder<?, ?>> MAP = new LinkedHashMap<>();

	public static <T extends RenderableProjectileType<T, I>, I> ProjTypeHolder<T, I> wrap(T type) {
		var ans = MAP.get(type);
		if (ans == null) {
			ans = new ProjTypeHolder<>(type, HOLDERS.size());
			HOLDERS.add(ans);
			MAP.put(type, ans);
		}
		return Wrappers.cast(ans);
	}

	protected final RenderableProjectileType<T, I> type;
	protected final int index;

	private ProjTypeHolder(RenderableProjectileType<T, I> type, int index) {
		this.type = type;
		this.index = index;
	}

	public void create(ProjectileRenderer r, SimplifiedProjectile e, PoseStack pose, float pTick) {
		type.create(this, r, e, pose, pTick);
	}

	public void accept(I ins) {
		ProjectileRenderHelper.add(this, ins);
	}

}
