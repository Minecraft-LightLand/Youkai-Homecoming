package dev.xkmc.youkaishomecoming.content.client;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import dev.xkmc.youkaishomecoming.init.registrate.YHEffects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class DrunkEffectRenderer {
    private static final ResourceLocation NAUSEA_TEXTURE =
            new ResourceLocation("minecraft","textures/misc/nausea.png");
    private static float drunkEffectStrength = 0f;
    private static float aphrodisiacEffectStrength = 0f;
    private static float hypnosisEffectStrength = 0f;
    private static long lastGameTime = 0;
    private static boolean isFirstRender = true;

    // 醉酒滤镜触发的最低等级要求 (3级，实际amplifier为2)
    private static final int MIN_DRUNK_LEVEL_FOR_OVERLAY = 2;
    // 效果淡出时间(秒)
    private static final float FADE_OUT_DURATION = 2.0f;
    // 效果淡入时间(秒)
    private static final float FADE_IN_DURATION = 1.5f;

    @SubscribeEvent
    public static void onRenderOverlay(RenderGuiOverlayEvent.Post event) {
        if (event.getOverlay().id().getPath().equals("crosshair") ||
                event.getOverlay().id().getPath().equals("hotbar")) {

            Minecraft mc = Minecraft.getInstance();
            Player player = mc.player;
            if (player == null || mc.level == null) return;

            long currentTime = mc.level.getGameTime();
            float deltaTime = calculateDeltaTime(currentTime);
            lastGameTime = currentTime;

            // 初始化时跳过第一帧
            if (isFirstRender) {
                isFirstRender = false;
                return;
            }

            // 处理所有效果
            processEffects(player, deltaTime);

            // 按优先级渲染效果
            renderEffects(event);
        }
    }

    private static float calculateDeltaTime(long currentTime) {
        if (lastGameTime == 0) return 0.05f; // 初始值
        long deltaTicks = currentTime - lastGameTime;
        // 限制最大deltaTime避免突然变化
        return Mth.clamp(deltaTicks / 20f, 0f, 0.1f);
    }

    private static void processEffects(Player player, float deltaTime) {
        // 处理醉酒效果
        processDrunkEffect(player, deltaTime);

        // 处理催情效果
        processAphrodisiacEffect(player, deltaTime);

        // 处理催眠效果
        processHypnosisEffect(player, deltaTime);
    }

    private static void processDrunkEffect(Player player, float deltaTime) {
        MobEffectInstance effect = player.getEffect(YHEffects.DRUNK.get());
        float targetStrength = 0f;

        if (effect != null && effect.getAmplifier() >= MIN_DRUNK_LEVEL_FOR_OVERLAY) {
            // 根据剩余时间计算强度
            float durationFactor = Mth.clamp(effect.getDuration() / (20f * 30f), 0f, 1f);
            targetStrength = Mth.clamp((effect.getAmplifier() - MIN_DRUNK_LEVEL_FOR_OVERLAY + 1) * 0.3f, 0f, 1f);
            targetStrength *= durationFactor;
        }

        // 平滑过渡，使用不同的淡入淡出速度
        drunkEffectStrength = calculateNewStrength(drunkEffectStrength, targetStrength, deltaTime);
    }

    private static void processAphrodisiacEffect(Player player, float deltaTime) {
        MobEffectInstance effect = player.getEffect(YHEffects.APHRODISIAC.get());
        float targetStrength = 0f;

        if (effect != null) {
            float durationFactor = Mth.clamp(effect.getDuration() / (20f * 30f), 0f, 1f);
            targetStrength = Mth.clamp(0.5f + effect.getAmplifier() * 0.2f, 0f, 1f);
            targetStrength *= durationFactor;
        }

        aphrodisiacEffectStrength = calculateNewStrength(aphrodisiacEffectStrength, targetStrength, deltaTime);
    }

    private static void processHypnosisEffect(Player player, float deltaTime) {
        MobEffectInstance effect = player.getEffect(YHEffects.HYPNOSIS.get());
        float targetStrength = 0f;

        if (effect != null) {
            float durationFactor = Mth.clamp(effect.getDuration() / (20f * 30f), 0f, 1f);
            targetStrength = Mth.clamp(0.4f + effect.getAmplifier() * 0.15f, 0f, 0.8f);
            targetStrength *= durationFactor;
        }

        hypnosisEffectStrength = calculateNewStrength(hypnosisEffectStrength, targetStrength, deltaTime);
    }

    private static float calculateNewStrength(float current, float target, float deltaTime) {
        if (target > current) {
            // 淡入更快
            return Mth.lerp(Mth.clamp(deltaTime / FADE_IN_DURATION, 0f, 1f), current, target);
        } else if (target < current) {
            // 淡出更慢
            return Mth.lerp(Mth.clamp(deltaTime / FADE_OUT_DURATION, 0f, 1f), current, target);
        }
        return current;
    }

    private static void renderEffects(RenderGuiOverlayEvent.Post event) {
        // 按优先级渲染效果（多个效果同时存在时只渲染最强的一个）
        if (drunkEffectStrength > 0.01f) {
            renderEffect(event, drunkEffectStrength, 0.7f, 1.0f, 0.7f); // 绿色
        } else if (aphrodisiacEffectStrength > 0.01f) {
            renderEffect(event, aphrodisiacEffectStrength, 1.0f, 0.5f, 0.8f); // 粉红色
        } else if (hypnosisEffectStrength > 0.01f) {
            renderEffect(event, hypnosisEffectStrength, 0.7f, 0.7f, 1.0f); // 灰蓝色
        }
    }

    private static void renderEffect(RenderGuiOverlayEvent.Post event, float strength, float red, float green, float blue) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) return;

        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(
                GlStateManager.SourceFactor.SRC_ALPHA,
                GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
                GlStateManager.SourceFactor.ONE,
                GlStateManager.DestFactor.ZERO
        );

        // 设置颜色滤镜和透明度
        float alpha = Mth.clamp(strength * 0.5f, 0f, 0.6f);
        RenderSystem.setShaderColor(red, green, blue, alpha);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, NAUSEA_TEXTURE);

        Tesselator tessellator = Tesselator.getInstance();
        BufferBuilder buffer = tessellator.getBuilder();

        // 更平滑的动画效果
        float time = (mc.level.getGameTime() + event.getPartialTick()) / 20f;
        float distortionX = (float)(Mth.sin(time * 1.5f) * 0.03f * strength);
        float distortionY = (float)(Mth.cos(time * 1.3f) * 0.02f * strength);

        int width = event.getWindow().getGuiScaledWidth();
        int height = event.getWindow().getGuiScaledHeight();

        buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);

        // 应用扭曲效果
        buffer.vertex(0, height, -90)
                .uv(0 - distortionX, 1 + distortionY).endVertex();
        buffer.vertex(width, height, -90)
                .uv(1 + distortionX, 1 + distortionY).endVertex();
        buffer.vertex(width, 0, -90)
                .uv(1 + distortionX, 0 - distortionY).endVertex();
        buffer.vertex(0, 0, -90)
                .uv(0 - distortionX, 0 - distortionY).endVertex();

        tessellator.end();

        // 重置渲染状态
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.disableBlend();
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
    }

    // 获取当前效果强度，供其他类使用
    public static float getDrunkLevel() {
        return drunkEffectStrength;
    }

    public static float getAphrodisiacLevel() {
        return aphrodisiacEffectStrength;
    }

    public static float getHypnosisLevel() {
        return hypnosisEffectStrength;
    }
}
