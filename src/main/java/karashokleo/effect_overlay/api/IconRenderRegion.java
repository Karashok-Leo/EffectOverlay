package karashokleo.effect_overlay.api;

public class IconRenderRegion
{
    public static IconRenderRegion identity()
    {
        return new IconRenderRegion(0, 0, 1);
    }

    public static IconRenderRegion of(int r, int ix, int iy, int w, int h)
    {
        float y = ((r - h) / 2f + iy) / r;
        float x = ((r - w) / 2f + ix) / r;
        return new IconRenderRegion(x, y, 1f / r);
    }

    public float x, y, scale;

    public IconRenderRegion(float x, float y, float scale)
    {
        this.x = x;
        this.y = y;
        this.scale = scale;
    }

    public void resize(IconRenderRegion inner)
    {
        this.x = this.x * inner.scale + inner.x;
        this.y = this.y * inner.scale + inner.y;
        this.scale = this.scale * inner.scale;
    }
}
