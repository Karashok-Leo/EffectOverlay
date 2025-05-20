import net.fabricmc.api.ModInitializer;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class TestMod implements ModInitializer
{
    public static final String MOD_ID = "effect-overlay-test";

    @Override
    public void onInitialize()
    {
        for (int i = 0; i < 6; i++)
            Registry.register(Registries.STATUS_EFFECT, new Identifier(MOD_ID, "test_" + i), new TestEffect());
    }
}
