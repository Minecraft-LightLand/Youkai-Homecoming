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

    @Override
    public void tick(CardHolder holder) {
        super.tick(holder);
        if (tick % 10 == 0) {
            //每10tick发射一次自机弹幕
            if (holder.self() instanceof Mob mob && mob.getTarget() instanceof Frog) {
                holder.shoot(holder.prepareDanmaku(50, holder.forward(), YHDanmaku.Bullet.BALL, DyeColor.RED));
                //设置弹幕的属性——50tick的生存时间，前进方向为holder的前进方向，弹幕类型为CIRCLE，颜色为RED
                return;
            }
            var le = holder.target();
            // 获取目标实体
            if (le == null) return;
            var dir = holder.forward();
            // 获取holder的前进方向
            var ans = new Sugiruyoru.tardanmu();
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
                holder.shoot(holder.prepareDanmaku(50, holder.forward(), YHDanmaku.Bullet.MENTOS, DyeColor.LIGHT_BLUE));
                return;
            }
            var le = holder.target();
            // 获取目标实体
            if (le == null) return;
            var dir = holder.forward();
            // 获取holder的前进方向
            var ans = new Sugiruyoru.floatdanmu();
            // 创建一个StateChange对象
            ans.pos = holder.center();
            ans.init = dir;
            ans.normal = dir;

            addTicker(ans);
            // 添加一个StateChange对象到当前的tickers中
        }
    }

    public static class tardanmu extends Ticker<Sugiruyoru> {

        @SerialClass.SerialField
        private Vec3 pos, init, normal;

        public boolean tick(CardHolder holder, Sugiruyoru card) {
            step(holder);
            // 调用step方法
            super.tick(holder, card);
            // 调用父类的tick方法
            return false;
        }
        private void step(CardHolder holder) {
            var le = holder.target();
            // 获取目标实体
            if (le == null) return;
            // 获取随机数
            if (init == null) {
                pos = holder.center();
                var dir = le.subtract(holder.center()).normalize();
                init = DanmakuHelper.getOrientation(dir).rotateDegrees(0, 0);
                normal = dir.cross(init);
            }
            if (tick < 0) return;
            if (tick == 0) {
                var o0 = DanmakuHelper.getOrientation(init, normal);
                // 获取init和normal的方向
                double acc = 16 * 2d / 50 / 50;
                var front = o0.rotateDegrees(360.0 );
                var vec = front.scale(acc * 50);
                var e = holder.prepareDanmaku(50, vec, YHDanmaku.Bullet.CIRCLE, DyeColor.RED);
                // 设置弹幕的属性
                e.mover = new RectMover(pos, vec, front.scale(-acc));
                // 设置弹幕的移动器——RectMover（矩形移动器）
                holder.shoot(e);
                // 发射弹幕

            }
        }
    }

    public static class floatdanmu extends Ticker<Sugiruyoru> {
        @SerialClass.SerialField
        private Vec3 pos, init, normal;

        public boolean tick(CardHolder holder, Sugiruyoru card) {
            step(holder);
            // 调用step方法
            super.tick(holder, card);
            // 调用父类的tick方法
            return false;
        }
        private void step(CardHolder holder) {
            var le = holder.target();
            // 获取目标实体
            if (le == null) return;
            // 获取随机数
            if (init == null) {
                pos = holder.center();
                // 设置位置为holder的中心
                var dir = le.subtract(holder.center()).normalize();
                // 获取目标实体到holder中心的向量，并归一化
                init = DanmakuHelper.getOrientation(dir).rotateDegrees(0, 0);
                // 获取dir的方向，然后旋转90度
                normal = dir.cross(init);
                // 获取dir和init的叉乘
            }
            if (tick < 0) return;
            if (tick % 2 == 0 && tick < 40) {
                var dir = le.subtract(holder.center()).normalize();
                var o0 = DanmakuHelper.getOrientation(dir);
                // 获取init和normal的方向
                for (int i = 0; i < 2; i++) {
                    var f0 = o0.rotateDegrees(180.0 * i + 90.0);
                    // 获取o0旋转360/n*i度的方向
                    var p0 = pos.add(f0.scale(8));
                    // 获取f0放大r0倍的向量
                    var f1 = le.subtract(p0).normalize();
                    // 获取目标实体到p0的向量，并归一化
                    var o1 = DanmakuHelper.getOrientation(f1);
                    // 获取f1的方向
                    var r2 = tick % 40;

                    double acc = 10 * 2d / 50 / 50;

                    var front = o1.rotateDegrees(360.0 * r2 / 40 + 90.0);

                    var vec = front.scale(acc * 50);

                    var e = holder.prepareDanmaku(40, vec, YHDanmaku.Bullet.MENTOS, DyeColor.LIGHT_BLUE);
                    // 设置弹幕的属性
                    e.setPos(p0);
                    // 设置弹幕的位置
                    e.mover = new RectMover(p0, vec, Vec3.ZERO);
                    // 设置弹幕的移动器
                    holder.shoot(e);

                }
            }

        }
    }




}
