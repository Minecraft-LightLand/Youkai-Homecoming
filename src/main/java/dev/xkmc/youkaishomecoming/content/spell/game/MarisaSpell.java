package dev.xkmc.youkaishomecoming.content.spell.game;

import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.youkaishomecoming.content.entity.danmaku.DanmakuHelper;
import dev.xkmc.youkaishomecoming.content.spell.mover.AttachedMover;
import dev.xkmc.youkaishomecoming.content.spell.mover.RectMover;
import dev.xkmc.youkaishomecoming.content.spell.spellcard.ActualSpellCard;
import dev.xkmc.youkaishomecoming.content.spell.spellcard.CardHolder;
import dev.xkmc.youkaishomecoming.content.spell.spellcard.Ticker;
import dev.xkmc.youkaishomecoming.init.registrate.YHDanmaku;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.Mob;

@SerialClass
public class MarisaSpell extends ActualSpellCard {

	@SerialClass.SerialField
	private int cooldown;
	// 布尔变量：标记当前是否正在使用 EarthLight
	private boolean isEarthLightActive = false;
	private int debug = 0;
	@Override
	public void tick(CardHolder holder) {
		super.tick(holder); // 调用父类 tick 方法
		var target = holder.target();
		var vec = holder.targetVelocity();
		if (target == null) {
			cooldown = 0;
			return;
		}
		// 检查是否进入第三阶段
		if (holder.self() instanceof Mob mob) {

			boolean phase3 = mob.getHealth() / mob.getMaxHealth() <= 0.1;
			if (phase3 && !(debug ==1)) {
				 debug = 1;
				cooldown = 50; // 设置短暂冷却
				return;
			}
		}

		if (cooldown > 0) {
			cooldown--;
			return;
		}
		if (vec == null) return;

		var cen = holder.center();
		double dist = cen.distanceTo(target);
		boolean far = dist > 32;
		boolean close = dist < 12;
		boolean fast = vec.horizontalDistance() > 0.7;
		boolean r0 = holder.random().nextDouble() < 0.3;
		boolean r1 = holder.random().nextDouble() < 0.3;
		boolean r2 = holder.random().nextDouble() < 0.4;

		if (holder.self() instanceof Mob mob) {
			boolean phase1 = mob.getHealth() / mob.getMaxHealth() > 0.5;
			boolean phase2 = mob.getHealth() / mob.getMaxHealth() <= 0.5 && mob.getHealth() / mob.getMaxHealth() > 0.1;
			boolean phase3 = mob.getHealth() / mob.getMaxHealth() <= 0.1;
			// 新增条件：当玩家距离Boss超过56格时触发DashStarSpell
			if (dist > 56&&!phase3) {
				addTicker(new DashStarSpell(target.subtract(cen).normalize()));
					if (phase1) {
						cooldown = 140;
						return; // 避免同时触发其他符卡
					} else {
						cooldown = 200;
					}
				}
			if (phase1) {
				if(!r0 && (close || r1 || far && !fast)) {
					addTicker(new MasterSpark(target.subtract(cen).normalize()));
					cooldown = 100;
					isEarthLightActive = false;}
				else if (far && fast && !isEarthLightActive || r0 && !isEarthLightActive) {
					addTicker(new EarthLight());
					isEarthLightActive = true; // 标记 EarthLight 使用
					cooldown = 20;}
				else {
					addTicker(new BlackHole());
					cooldown = 150;
					isEarthLightActive = false;
				}}
			else if(phase2 && r2){
				addTicker(new EarthLight());
				cooldown = 20;
				addTicker(new MasterSpark(target.subtract(cen).normalize()));
				cooldown = 100;
			}else if(phase2)
			{addTicker(new DashStarSpell(target.subtract(cen).normalize()));
				addTicker(new BlackHole());
				cooldown = 150;
			}else if(phase3){
				addTicker(new BlackHole());
				addTicker(new MasterSpark(target.subtract(cen).normalize()));
				cooldown = 200;
			}
		}
	}
	@SerialClass
	public static class DashStarSpell extends Ticker<MarisaSpell> {

		@SerialClass.SerialField
		private Vec3 target; // 冲刺目标方向
		private int dashCount = 0; // 冲刺次数计数器
		private int maxDashCount; // 最大冲刺次数（1~3次）
		private Vec3 dashDirection; // 当前冲刺方向

		public DashStarSpell() {
		}

		public DashStarSpell(Vec3 vec) {
			this.target = vec;
			this.maxDashCount = 1 + (int) (Math.random() * 2); // 随机1~3次
		}

		@Override
		public boolean tick(CardHolder holder, MarisaSpell card) {
			if (target == null) return true;

			var cen = holder.center();
			var tar = holder.target();

			if (tar == null) return true;

			// 如果当前冲刺次数未达到最大值，则继续冲刺
			if (dashCount < maxDashCount) {
				// 冲刺逻辑
				if (tick < 40) {
					// 在冲刺开始时记录玩家方向并加上随机偏移量
					if (tick == 0) {
						Vec3 playerDirection = tar.subtract(cen).normalize();
						double offsetX = (holder.random().nextDouble() - 0.5) * 0.1; // 随机偏移量
						double offsetY = (holder.random().nextDouble() - 0.5) * 0.1;
						this.dashDirection = playerDirection.add(offsetX, offsetY, 0).normalize();
					}

					// Boss向目标方向冲刺
					holder.self().setDeltaMovement(dashDirection.scale(2.5)); // 设置Boss的移动速度
				} else if (tick == 40) {
					// 冲刺结束后停止移动
					holder.self().setDeltaMovement(Vec3.ZERO);
					dashCount++; // 增加冲刺次数
					tick = -1; // 重置tick，准备下一次冲刺
				}
			}

			// 在冲刺轨迹上释放星星弹幕
			if (tick < 40 && tick % 1 == 0) { // 每 tick释放一次弹幕
				var rand = holder.random();
				var o = DanmakuHelper.getOrientation(target);

				// 随机生成星星弹幕
				for (int i = 0; i < 5; i++) {
					double x = rand.nextDouble() * 60 - 30; // 更大的随机范围
					double y = rand.nextDouble() * 60 - 30;
					var vec = o.rotateDegrees(x, y);
					var v = rand.nextDouble() * 0.5 + 0.5; // 弹幕速度较慢

					// 随机颜色
					DyeColor color = DyeColor.values()[rand.nextInt(DyeColor.values().length)];

					var e = holder.prepareDanmaku(80, vec.scale(v),
							YHDanmaku.Bullet.STAR, color);
					e.setPos(cen.add(dashDirection.scale(i * 1.0 )));
					holder.shoot(e);
				}
			}

			super.tick(holder, card);
			return dashCount >= maxDashCount ; // 结束条件：冲刺次数达到最大值
		}
	}

	@SerialClass
	public static class BlackHole extends Ticker<MarisaSpell> {

		private static final DyeColor[] COLOR = {
				DyeColor.RED, DyeColor.YELLOW, DyeColor.GREEN, DyeColor.CYAN, DyeColor.BLUE
		};
		private static final double W0 = 7, V0 = 0.2, W1 = -4, ACC = 0.05, VEC = 0.8;

		@Override
		public boolean tick(CardHolder holder, MarisaSpell card) {
			boolean isPhase3 = holder.self().getHealth() <= holder.self().getMaxHealth() * 0.05;
			double speedMultiplier = isPhase3 ? 0.5 : 1.0; // 阶段3速度减半

			// 获取目标方向
			var tar = holder.forward();

			// 获取中心位置
			Vec3 cen = holder.center().add(tar.scale(-24));

			// 获取一个基准方向（X 轴方向），用于计算弹幕的旋转
			var o = DanmakuHelper.getOrientation(tar).asNormal();

			// 根据 tick 值动态调整弹幕速度和向量
			double speedScale = tick < 40 ? 0.1 : 0.8; // 前 40 tick 速度为 0.5 倍，后 60 tick 恢复原速度
			double vectorScale = tick < 20 ? 0.5 : 0.9; // 前 40 tick 向量为 0.5 倍，后 60 tick 恢复原向量

			// 计算弹幕的加速度（朝向目标方向）
			Vec3 acc = tar.scale(ACC * vectorScale * speedMultiplier); // 应用速度乘数

			// 外层循环：生成 5 组弹幕
			for (int i = 0; i < 5; i++) {
				// 中层循环：每组弹幕生成 3 个子组
				for (int t = 0; t < 2; t++) {
					// 计算当前弹幕的位置
					// 使用旋转角度和扩展速度计算弹幕的位置
					Vec3 pos = cen.add(o.rotateDegrees(tick * W0 + 72 * i + 24 * t).scale(tick * V0));

					// 内层循环：每个子组生成 2 个弹幕
					for (int j = 0; j < 2; j++) {
						// 计算弹幕的速度
						double v = 0.35 + t * 0.2 + Math.sin(tick * 0.1 + i) * 0.15;

						// 计算弹幕的方向
						// 使用旋转角度和速度缩放因子计算弹幕的方向
						Vec3 dir = o.rotateDegrees(tick * W1 + 72 * j + 24 * t).scale(v * VEC * speedScale);

						// 创建弹幕实例
						var e = holder.prepareDanmaku(80, dir, YHDanmaku.Bullet.SPARK, COLOR[i]);

						// 设置弹幕的位置
						e.setPos(pos);

						// 设置弹幕的移动器（位置、方向、加速度）
						e.mover = new RectMover(pos, dir, acc);

						// 发射弹幕
						holder.shoot(e);
					}
				}
			}

			// 调用父类的 tick 方法
			super.tick(holder, card);

			// 判断是否结束：如果 tick 大于 100，返回 true 表示结束
			return tick > 120;
		}
	}

	@SerialClass
	public static class EarthLight extends Ticker<MarisaSpell> {

		@Override
		public boolean tick(CardHolder holder, MarisaSpell card) {
			var tar = holder.target();
			if (tar == null) return true;
			var rand = holder.random();
			for (int i = 0; i < 2; i++) {
				double x = rand.nextGaussian() * 10;
				double z = rand.nextGaussian() * 10;
				var pos = tar.add(x, 0, z);
				BlockPos air = BlockPos.containing(pos);
				BlockPos ground = holder.self().level().getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, air);
				if (ground.getY() < air.getY() - 20) {
					pos = pos.add(0, -20, 0);
				} else if (ground.getY() < air.getY()) {
					pos = new Vec3(pos.x, ground.getY() - 1, pos.z);
				}
				Vec3 dir = new Vec3(rand.nextGaussian(), 5, rand.nextGaussian()).normalize();
				var color = rand.nextBoolean() ? DyeColor.RED : DyeColor.BLUE;
				var e = holder.prepareLaser(60, pos, dir, 80,
						YHDanmaku.Laser.LASER, color);
				holder.shoot(e);
			}
			super.tick(holder, card);
			return tick > 100;
		}
	}

	@SerialClass
	public static class MasterSpark extends Ticker<MarisaSpell> {

		@SerialClass.SerialField
		private Vec3 target;

		public MasterSpark() {

		}

		public MasterSpark(Vec3 vec) {
			this.target = vec;
		}

		@Override
		public boolean tick(CardHolder holder, MarisaSpell card) {
			if (target == null) return true;
			var cen = holder.center();
			boolean isPhase3 = holder.self().getHealth() <= holder.self().getMaxHealth() * 0.1;
			double speedMultiplier = isPhase3 ? 0.5 : 1.0; // 阶段3速度减半

			// 在tick == 0时发射激光
			if (tick == 0) {
				var e = holder.prepareLaser(1, cen, target, 80,
						YHDanmaku.Laser.LASER, DyeColor.YELLOW);
				e.setupTime(20, 1, 1, 1);
				e.mover = new AttachedMover();
				holder.shoot(e);
			}

			// 在tick > 20时发射弹幕
			boolean isLowHealth = false;
			if (tick > 20) {
				var tar = holder.target();
				if (tar != null) {
					// 调整target方向
					double maxMove = 0.02;
					var db = tar.subtract(cen).normalize();
					double dist = target.distanceTo(db);
					double perc = dist < maxMove ? 1 : maxMove / dist;
					target = target.lerp(db, perc);
				}

				var rand = holder.random();
				var o = DanmakuHelper.getOrientation(target);

				// 判断Boss是否半血且不低于10%
				boolean isHalfHealth = holder.self().getHealth() <= holder.self().getMaxHealth() / 2;
				isLowHealth = holder.self().getHealth() <= holder.self().getMaxHealth() / 10;

				// 发射星星弹幕
				for (int i = 0; i < 20; i++) {
					var pos = cen.add(target.scale(i * 1.4 + 2));
					var repos = cen.add(target.scale(-i * 1.4 - 2));
					double x = rand.nextDouble() * 30 - 15;
					double y = rand.nextDouble() * 30 - 15;
					var vec = o.rotateDegrees(x, y);
					var vec2 = o.rotateDegrees(2 * x, 2 * y);
					var v = (rand.nextDouble() + 2) * speedMultiplier; // 应用速度乘数
					// 如果低血量，释放究极火花
					if (!isLowHealth) {
						// 发射正向弹幕
						var e = holder.prepareDanmaku(40, vec.scale(v),
								YHDanmaku.Bullet.STAR, DyeColor.WHITE);
						e.setPos(pos);
						holder.shoot(e);
					} else {// 发射正向弹幕
						var e = holder.prepareDanmaku(40, vec2.scale(0.5 * v),
								YHDanmaku.Bullet.STAR, DyeColor.WHITE);
						e.setPos(pos);
						holder.shoot(e);
					}
					// 如果半血，发射反向弹幕
					if (isHalfHealth && !isLowHealth) {
						var reverseVec = vec.scale(-1); // 反向
						var reverseE = holder.prepareDanmaku(40, reverseVec.scale(v),
								YHDanmaku.Bullet.STAR, DyeColor.WHITE);
						reverseE.setPos(repos);
						holder.shoot(reverseE);
					}
				}

				// 发射火花弹幕
				for (int i = 0; i < 10; i++) {
					double x = rand.nextDouble() * 120 - 60;
					double y = rand.nextDouble() * 120 - 60;
					var vec = o.rotateDegrees(x, y);
					var v = (rand.nextDouble() * 0.3 + 0.6) * speedMultiplier; // 应用速度乘数
					// 如果低血量，释放究极火花
					if (!isLowHealth) {
						// 发射正向弹幕
						var e = holder.prepareDanmaku(40, vec.scale(v),
								YHDanmaku.Bullet.SPARK, DyeColor.YELLOW);
						e.setPos(cen);
						holder.shoot(e);
					}
					// 如果半血，发射反向弹幕
					 if (isHalfHealth && !isLowHealth) {
						var reverseVec = vec.scale(-1); // 反向
						var reverseE = holder.prepareDanmaku(40, reverseVec.scale(v),
								YHDanmaku.Bullet.SPARK, DyeColor.YELLOW);
						reverseE.setPos(cen);
						holder.shoot(reverseE);
					}
				}
			}
			if (!isLowHealth) {
				super.tick(holder, card);
				return tick > 100;
			}else {super.tick(holder, card);
				return tick > 150;}
		}

	}


}
