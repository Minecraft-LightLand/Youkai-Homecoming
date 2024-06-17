package dev.xkmc.youkaishomecoming.content.spell.game.sanae;

import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.youkaishomecoming.content.entity.danmaku.DanmakuHelper;
import dev.xkmc.youkaishomecoming.content.spell.mover.RectMover;
import dev.xkmc.youkaishomecoming.content.spell.spellcard.ActualSpellCard;
import dev.xkmc.youkaishomecoming.content.spell.spellcard.CardHolder;
import dev.xkmc.youkaishomecoming.content.spell.spellcard.Ticker;
import dev.xkmc.youkaishomecoming.init.registrate.YHDanmaku;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.frog.Frog;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.phys.Vec3;

public class Sugiruyoru extends ActualSpellCard {

    private int s = 0;

    @Override
    public void tick(CardHolder holder) {
        super.tick(holder);

        if (tick % 20 == 0) {
            var le = holder.target();
            var pos = holder.center();
            // 当目标实体和holder的中心在y轴上的距离小于10时
            if (le == null || le.distanceTo(pos) < 25) {
                s = 0;
                // 设置s为0
            }else {
                s = 1;
                // 设置s为1
            }
        }

        switch (s) {
            case 0:
                //奇迹「客星辉煌之夜」
                if (tick % 10 == 0) {
                    //每10tick发射一次自机弹幕
                    if (holder.self() instanceof Mob mob && mob.getTarget() instanceof Frog) {
                        holder.shoot(holder.prepareDanmaku(80, holder.forward(), YHDanmaku.Bullet.BALL, DyeColor.RED));
                        //设置弹幕的属性——50tick的生存时间，前进方向为holder的前进方向，弹幕类型为CIRCLE，颜色为RED
                        return;
                    }
                    var le = holder.target();
                    // 获取目标实体
                    if (le == null) return;
                    var dir = holder.forward();
                    // 获取holder的前进方向
                    var ans = new tardanmu1();
                    // 创建一个StateChange对象
                    ans.pos = holder.center();
                    ans.init = dir;
                    ans.normal = dir;
                    addTicker(ans);
                    // 添加一个StateChange对象到当前的tickers中
                }
                if (tick % 40 == 0) {
                    //每40tick发射一轮浮动弹幕
                    if (holder.self() instanceof Mob mob && mob.getTarget() instanceof Frog) {
                        holder.shoot(holder.prepareDanmaku(60, holder.forward(), YHDanmaku.Bullet.MENTOS, DyeColor.LIGHT_BLUE));
                        return;
                    }
                    var le = holder.target();
                    // 获取目标实体
                    if (le == null) return;
                    var dir = holder.forward();
                    // 获取holder的前进方向
                    var ans = new floatdanmu1();
                    // 创建一个StateChange对象
                    ans.pos = holder.center();
                    ans.init = dir;
                    addTicker(ans);
                    // 添加一个StateChange对象到当前的tickers中
                }
                break;
            case 1:
                //神德「五谷丰穰米之浴」
                if (tick % 20 == 0) {
                    //每10tick发射一次自机弹幕
                    if (holder.self() instanceof Mob mob && mob.getTarget() instanceof Frog) {
                        holder.shoot(holder.prepareDanmaku(80, holder.forward(), YHDanmaku.Bullet.BALL, DyeColor.RED));
                        //设置弹幕的属性——50tick的生存时间，前进方向为holder的前进方向，弹幕类型为CIRCLE，颜色为RED
                        return;
                    }
                    var le = holder.target();
                    if (le == null) return;
                    var dir = holder.forward();
                    var ans = new tardanmu2();
                    ans.pos = holder.center();
                    ans.init = dir;
                    ans.normal = dir;
                    addTicker(ans);
                }
                if (tick % 2 == 0) {
                    //每40tick发射一轮浮动弹幕
                    if (holder.self() instanceof Mob mob && mob.getTarget() instanceof Frog) {
                        holder.shoot(holder.prepareDanmaku(60, holder.forward(), YHDanmaku.Bullet.MENTOS, DyeColor.LIGHT_BLUE));
                        return;
                    }
                    var le = holder.target();
                    if (le == null) return;
                    var dir = holder.forward();
                    var ans = new floatdanmu2();
                    ans.pos = holder.center();
                    ans.init = dir;
                    addTicker(ans);
                }
        }
    }

    //奇迹「客星辉煌之夜」
    public static class tardanmu1 extends Ticker<Sugiruyoru> {

        @SerialClass.SerialField
        private Vec3 pos, init, normal;

        public boolean tick(CardHolder holder, Sugiruyoru card) {
            step(holder);
            super.tick(holder, card);
            return false;
        }

        private void step(CardHolder holder) {
            var le = holder.target();
            if (le == null) return;
            if (init == null) {
                pos = holder.center();
                var dir = le.subtract(holder.center()).normalize();
                init = dir;
                normal = dir;
            }
            if (tick < 0) return;
            if (tick == 0) {
                var o0 = DanmakuHelper.getOrientation(init, normal);
                double acc = 16 * 2d / 80 / 80;
                var front = o0.rotateDegrees(360.0);
                var vec = front.scale(acc * 80 * 1.5);
                var e = holder.prepareDanmaku(80, vec, YHDanmaku.Bullet.CIRCLE, DyeColor.RED);
                e.mover = new RectMover(pos, vec, front.scale(-acc));
                holder.shoot(e);
            }
        }
    }

    //奇迹「客星辉煌之夜」
    public static class floatdanmu1 extends Ticker<Sugiruyoru> {
        @SerialClass.SerialField
        private Vec3 pos, init;

        public boolean tick(CardHolder holder, Sugiruyoru card) {
            step(holder);
            super.tick(holder, card);
            return false;
        }

        private void step(CardHolder holder) {
            var le = holder.target();
            if (le == null) return;
            if (init == null) {
                pos = holder.center();
                init = le.subtract(holder.center()).normalize();
            }
            if (tick < 0) return;
            if (tick % 2 == 0 && tick < 40) {
                var dir = le.subtract(holder.center()).normalize();
                var o0 = DanmakuHelper.getOrientation(dir);
                for (int i = 0; i < 2; i++) {
                    var f0 = o0.rotateDegrees(180.0 * i + 90.0);
                    var p0 = pos.add(f0.scale(8));
                    var f1 = le.subtract(p0).normalize();
                    var o1 = DanmakuHelper.getOrientation(f1);
                    var r2 = tick % 40;
                    double acc = 10 * 2d / 60 / 60;
                    var front = o1.rotateDegrees(360.0 * r2 / 40 + 90.0);
                    var vec = front.scale(acc * 60 * 1.5);
                    var e = holder.prepareDanmaku(60, vec, YHDanmaku.Bullet.MENTOS, DyeColor.LIGHT_BLUE);
                    e.setPos(p0);
                    e.mover = new RectMover(p0, vec, Vec3.ZERO);
                    holder.shoot(e);
                }
            }
        }
    }

    //神德「五谷丰穰米之浴」
    public static class tardanmu2 extends Ticker<Sugiruyoru> {

        @SerialClass.SerialField
        private Vec3 pos, init, normal;
        @SerialClass.SerialField
        private DanmakuHelper.Orientation o0;

        public boolean tick(CardHolder holder, Sugiruyoru card) {
            step(holder);
            super.tick(holder, card);
            return false;
        }

        private void step(CardHolder holder) {
            var le = holder.target();
            if (le == null) return;
            if (init == null) {
                pos = holder.center();
                var dir = le.subtract(holder.center()).normalize();
                init = dir;
                normal = dir;
            }
            if (tick < 0) return;
            if (tick == 0) {
                var dir = le.subtract(holder.center()).normalize();
                o0 = DanmakuHelper.getOrientation(dir);
            }
            if (tick % 2 == 0 && tick < 10) {
                for (int i = 0; i < 5; i++) {
                    var f0 = o0.rotateDegrees(90, 72 * i);
                    var p0 = pos.add(f0.scale(12));
                    var f1 = le.subtract(p0).normalize();
                    var o1 = DanmakuHelper.getOrientation(f1);
                    double acc = 36 * 2d / 80 / 80;
                    var vec = f1.scale(acc * 80 * 1.5);
                    var e = holder.prepareDanmaku(80, vec, YHDanmaku.Bullet.CIRCLE, DyeColor.RED);
                    e.setPos(p0);
                    e.mover = new RectMover(p0, vec, Vec3.ZERO);
                    holder.shoot(e);
                    for (int j = 0; j < 5; j++) {
                        var front = o1.asNormal().rotateDegrees(72*j,60);
                        vec = front.scale(acc * 80 );
                        e = holder.prepareDanmaku(80, vec, YHDanmaku.Bullet.CIRCLE, DyeColor.RED);
                        e.mover = new RectMover(p0, vec, Vec3.ZERO);
                        holder.shoot(e);
                    }
                }
            }
        }
    }

    //神德「五谷丰穰米之浴」
    public static class floatdanmu2 extends Ticker<Sugiruyoru> {
        @SerialClass.SerialField
        private Vec3 pos, init;

        public boolean tick(CardHolder holder, Sugiruyoru card) {
            step(holder);
            super.tick(holder, card);
            return false;
        }

        private void step(CardHolder holder) {
            var le = holder.target();
            if (le == null) return;
            var vc = holder.targetVelocity();
            if (vc == null) return;
            le =le.add(vc.scale(20));
            if (init == null) {
                pos = holder.center();
                init = le.subtract(holder.center()).normalize();
            }
            if (tick < 0) return;
            if (tick == 0) {
                var dir = le.subtract(holder.center()).normalize();
                var o0 = DanmakuHelper.getOrientation(dir);
                var r = holder.random();
                for (int i = 0; i < 5; i++) {
                    var f0 = o0.rotateDegrees(90, 72 * i);
                    var p0 = pos.add(f0.scale(20));
                    var f1 = le.subtract(p0).normalize();
                    var o1 = DanmakuHelper.getOrientation(f1);
                    double acc = 24 * 2d / 40 / 40;
                    var front = o1.rotateDegrees(r.nextDouble() * 90.0 - 45.0, r.nextDouble() * 90.0 - 45.0);
                    var vec = front.scale(acc * 40);
                    var e = holder.prepareDanmaku(60, vec, YHDanmaku.Bullet.MENTOS, switchcolor(i));
                    e.setPos(p0);
                    e.mover = new RectMover(p0, vec, Vec3.ZERO);
                    holder.shoot(e);

                }
            }

        }

        private DyeColor switchcolor(int i){
            switch (i){
                case 0:
                    return DyeColor.LIGHT_BLUE;
                case 1:
                    return DyeColor.GREEN;
                case 2:
                    return DyeColor.WHITE;
                case 3:
                    return DyeColor.YELLOW;
                case 4:
                    return DyeColor.RED;
                default:
                    return DyeColor.LIGHT_BLUE;
            }
        }
    }

}
