package karashokleo.effect_overlay.api;

import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

// 原 DelayedEntityRenderer
public class IconEffectRenderer implements EffectRenderer
{
    public static IconEffectRenderer icon(LivingEntity entity, Identifier id)
    {
        return icon(entity, IconRenderRegion.identity(), id);
    }

    public static IconEffectRenderer icon(LivingEntity entity, IconRenderRegion r, Identifier id)
    {
        return new IconEffectRenderer(entity, r, id, 0, 0, 1, 1);
    }

    protected LivingEntity entity;
    protected IconRenderRegion region;
    protected Identifier id;
    protected float tx, ty, tw, th;

    public IconEffectRenderer(LivingEntity entity, IconRenderRegion region, Identifier id, float tx, float ty, float tw, float th)
    {
        this.entity = entity;
        this.region = region;
        this.id = id;
        this.tx = tx;
        this.ty = ty;
        this.tw = tw;
        this.th = th;
    }

    @Override
    public void adjustPosition(int index, int n)
    {
        int w = (int) Math.ceil(Math.sqrt(n));
        int h = (int) Math.ceil(n * 1d / w);
        int iy = index / w;
        int iw = Math.min(w, n - iy * w);
        int ix = index - iy * w;
        region.resize(IconRenderRegion.of(w, ix, iy, iw, h));
    }

    public void render(MatrixStack pose, VertexConsumerProvider buffer, float tickDelta, Camera camera, EntityRenderDispatcher dispatcher)
    {
        Vec3d d = dispatcher.getRenderer(entity).getPositionOffset(entity, tickDelta)
                .subtract(camera.getPos())
                .add(
                        MathHelper.lerp(tickDelta, entity.lastRenderX, entity.getX()),
                        MathHelper.lerp(tickDelta, entity.lastRenderY, entity.getY()),
                        MathHelper.lerp(tickDelta, entity.lastRenderZ, entity.getZ())
                );

        pose.push();

        pose.translate(d.x, d.y + entity.getHeight(), d.z);
        pose.multiply(camera.getRotation());
        pose.scale(entity.getWidth(), entity.getWidth(), 1F);

        MatrixStack.Entry entry = pose.peek();
        VertexConsumer iVertexBuilder = buffer.getBuffer(get2DIcon());

        float ix0 = (region.x - 0.5f) / region.scale;
        float ix1 = ix0 + 1F;
        float iy0 = (region.y - 0.5f) / region.scale;
        float iy1 = iy0 + 1F;

        iconVertex(entry, iVertexBuilder, ix1, iy0, tx, ty + th);
        iconVertex(entry, iVertexBuilder, ix0, iy0, tx + tw, ty + th);
        iconVertex(entry, iVertexBuilder, ix0, iy1, tx + tw, ty);
        iconVertex(entry, iVertexBuilder, ix1, iy1, tx, ty);
        pose.pop();
    }

    public RenderLayer get2DIcon()
    {
        return RenderLayer.of(
                "entity_body_icon",
                VertexFormats.POSITION_TEXTURE,
                VertexFormat.DrawMode.QUADS, 256, false, true,
                RenderLayer.MultiPhaseParameters.builder()
                        .program(RenderPhase.ENTITY_GLINT_PROGRAM)
                        .texture(new RenderPhase.Texture(id, false, false))
                        .transparency(RenderPhase.ADDITIVE_TRANSPARENCY)
                        .depthTest(RenderPhase.ALWAYS_DEPTH_TEST)
                        .build(false)
        );
    }

    private static void iconVertex(MatrixStack.Entry entry, VertexConsumer builder, float x, float y, float u, float v)
    {
        builder.vertex(entry.getPositionMatrix(), x, y, 0)
                .texture(u, v)
                .normal(entry.getNormalMatrix(), 0.0F, 1.0F, 0.0F)
                .next();
    }
}
