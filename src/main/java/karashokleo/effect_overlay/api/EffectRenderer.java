package karashokleo.effect_overlay.api;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;

import java.util.ArrayList;

public interface EffectRenderer
{
    Event<BeforeRender> BEFORE_RENDER = EventFactory.createArrayBacked(BeforeRender.class, beforeRenders -> (entity, renderers) ->
    {
        boolean shouldRender = true;
        for (BeforeRender beforeRender : beforeRenders)
            if (!beforeRender.beforeRender(entity, renderers))
                shouldRender = false;
        return shouldRender;
    });

    default void adjustPosition(int index, int n)
    {
    }

    void render(MatrixStack pose, VertexConsumerProvider buffer, float tickDelta, Camera camera, EntityRenderDispatcher dispatcher);

    interface BeforeRender
    {
        /*
         * 返回值为false时取消后续渲染
         * */
        boolean beforeRender(LivingEntity entity, ArrayList<EffectRenderer> renderers);
    }
}
