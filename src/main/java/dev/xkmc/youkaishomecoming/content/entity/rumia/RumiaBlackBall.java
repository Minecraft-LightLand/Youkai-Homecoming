package dev.xkmc.youkaishomecoming.content.entity.rumia;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.SlimeModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RumiaBlackBall<T extends LivingEntity> extends RenderLayer<T, SlimeModel<T>> {
   private final EntityModel<T> model;

   public RumiaBlackBall(RenderLayerParent<T, SlimeModel<T>> pRenderer, EntityModelSet pModelSet) {
      super(pRenderer);
      this.model = new SlimeModel<>(pModelSet.bakeLayer(ModelLayers.SLIME_OUTER));
   }

   public void render(PoseStack pose, MultiBufferSource buffer, int light, T e, float swing, float swingAmp, float pTick, float age, float yaw, float pitch) {
      Minecraft minecraft = Minecraft.getInstance();
      boolean flag = minecraft.shouldEntityAppearGlowing(e) && e.isInvisible();
      if (!e.isInvisible() || flag) {
         VertexConsumer vertexconsumer;
         if (flag) {
            vertexconsumer = buffer.getBuffer(RenderType.outline(this.getTextureLocation(e)));
         } else {
            vertexconsumer = buffer.getBuffer(RenderType.entityTranslucent(this.getTextureLocation(e)));
         }

         this.getParentModel().copyPropertiesTo(this.model);
         this.model.prepareMobModel(e, swing, swingAmp, pTick);
         this.model.setupAnim(e, swing, swingAmp, age, yaw, pitch);
         this.model.renderToBuffer(pose, vertexconsumer, light, LivingEntityRenderer.getOverlayCoords(e, 0.0F),
                 1.0F, 1.0F, 1.0F, 1.0F);
      }
   }

   @Override
   protected ResourceLocation getTextureLocation(T pEntity) {
      return super.getTextureLocation(pEntity);
   }
}