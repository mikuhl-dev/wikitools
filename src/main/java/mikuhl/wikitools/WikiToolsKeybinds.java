package mikuhl.wikitools;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.input.Keyboard;

public class WikiToolsKeybinds {

    private static String category = "mikuhl.wikitool.category";
    public static KeyBinding RENDER = new KeyBinding(
            "mikuhl.wikitool.render.description",
            Keyboard.KEY_M,
            category
    );
    public static KeyBinding STEVE_MODIFIER = new KeyBinding(
            "mikuhl.wikitool.steve.modifier.description",
            Keyboard.KEY_RSHIFT,
            category
    );
    public static KeyBinding ENCHANT_MODIFIER = new KeyBinding(
            "mikuhl.wikitool.enchant.modifier.description",
            Keyboard.KEY_RCONTROL,
            category
    );
    public static KeyBinding SELF_MODIFIER = new KeyBinding(
            "mikuhl.wikitool.self.modifier.description",
            Keyboard.KEY_RMENU,
            category
    );
    public static KeyBinding INVISIBLE_MODIFIER = new KeyBinding(
            "mikuhl.wikitool.invisible.modifier.description",
            Keyboard.KEY_APPS,
            category
    );
    public static KeyBinding COPY_NBT = new KeyBinding(
            "mikuhl.wikitool.copy.nbt.description",
            Keyboard.KEY_N,
            category
    );

    public static void init() {
        ClientRegistry.registerKeyBinding(RENDER);
        ClientRegistry.registerKeyBinding(STEVE_MODIFIER);
        ClientRegistry.registerKeyBinding(ENCHANT_MODIFIER);
        ClientRegistry.registerKeyBinding(SELF_MODIFIER);
        ClientRegistry.registerKeyBinding(INVISIBLE_MODIFIER);
        ClientRegistry.registerKeyBinding(COPY_NBT);
    }
}
