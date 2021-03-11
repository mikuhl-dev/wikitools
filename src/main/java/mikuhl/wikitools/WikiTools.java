package mikuhl.wikitools;

import org.lwjgl.input.Keyboard;

import mikuhl.wikitools.handler.CopyNBTHandler;
import mikuhl.wikitools.handler.EntityRenderHandler;
import mikuhl.wikitools.handler.ModifierHandler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.ChatComponentText;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

@Mod(modid = "WikiTools", version = "1.1.1", clientSideOnly = true)
public class WikiTools {
	
	public static void sendMessage(String message) {
		Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(message.replace('\u0026', '\u00a7')));
	}
	
	public static final KeyBinding COPY_NBT;
	public static final KeyBinding RENDER;
	public static final KeyBinding ENCHANT_MODIFIER;
	public static final KeyBinding STEVE_MODIFIER;
	public static final KeyBinding INVISIBLE_MODIFIER;
	public static final KeyBinding HELDITEM_MODIFIER;
	public static final KeyBinding SELF_MODIFIER;
	public static final KeyBinding ARMOR_MODIFIER;
	
	static {
		String mod_name = "WikiTools";
		
		COPY_NBT = new KeyBinding("Copy NBT", Keyboard.KEY_NUMPAD7, mod_name);
		RENDER = new KeyBinding("Render", Keyboard.KEY_NUMPAD8, mod_name);
		ENCHANT_MODIFIER = new KeyBinding("Enchant Modifier", Keyboard.KEY_NUMPAD4, mod_name);
		STEVE_MODIFIER = new KeyBinding("Steve Modifier", Keyboard.KEY_NUMPAD5, mod_name);
		INVISIBLE_MODIFIER = new KeyBinding("Invisible Modifier", Keyboard.KEY_NUMPAD6, mod_name);
		HELDITEM_MODIFIER = new KeyBinding("Item Held Modifier", Keyboard.KEY_NUMPAD1, mod_name);
		SELF_MODIFIER = new KeyBinding("Self Modifier", Keyboard.KEY_NUMPAD2, mod_name);
		ARMOR_MODIFIER = new KeyBinding("Armor Modifier", Keyboard.KEY_NUMPAD3, mod_name);
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(new ModifierHandler());
		MinecraftForge.EVENT_BUS.register(new CopyNBTHandler());
		MinecraftForge.EVENT_BUS.register(new EntityRenderHandler());
		
		ClientRegistry.registerKeyBinding(COPY_NBT);
		ClientRegistry.registerKeyBinding(RENDER);
		ClientRegistry.registerKeyBinding(ENCHANT_MODIFIER);
		ClientRegistry.registerKeyBinding(STEVE_MODIFIER);
		ClientRegistry.registerKeyBinding(INVISIBLE_MODIFIER);
		ClientRegistry.registerKeyBinding(HELDITEM_MODIFIER);
		ClientRegistry.registerKeyBinding(SELF_MODIFIER);
		ClientRegistry.registerKeyBinding(ARMOR_MODIFIER);
	}
}
