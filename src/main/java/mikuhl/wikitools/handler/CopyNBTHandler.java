package mikuhl.wikitools.handler;

import mikuhl.wikitools.WikiTools;
import mikuhl.wikitools.helper.ClipboardHelper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.Slot;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;

import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

import org.lwjgl.input.Keyboard;

public class CopyNBTHandler {
	
	@SubscribeEvent
	public void onKeyInputEvent(InputEvent.KeyInputEvent event) {
		if (WikiTools.COPY_NBT.isKeyDown()) {
			Minecraft mc = Minecraft.getMinecraft();
			MovingObjectPosition objectMouseOver = mc.objectMouseOver;
			Entity entity = objectMouseOver.entityHit;
			
			if (entity != null) {
				if (entity instanceof EntityOtherPlayerMP) {
					NBTTagCompound nbt = new NBTTagCompound();
					entity.writeToNBT(nbt);
					
					ClipboardHelper.setClipboard(nbt);
				}
				else {
					ClipboardHelper.setClipboard(entity.serializeNBT());
				}
			}
			else {
				TileEntity tile = mc.theWorld.getTileEntity(objectMouseOver.getBlockPos());
				
				if (tile != null) {
					ClipboardHelper.setClipboard(tile.serializeNBT());
				}
			}
		}
	}
	
	@SubscribeEvent()
	public void onKeyboardInputEvent(GuiScreenEvent.KeyboardInputEvent event) {
		// Keybinds don't register in GUI's
		if (Keyboard.isKeyDown(WikiTools.COPY_NBT.getKeyCode()) && event.gui instanceof GuiContainer) {
			Slot slot = ((GuiContainer) event.gui).getSlotUnderMouse();
			
			if (slot != null && slot.getHasStack()) {
				ClipboardHelper.setClipboard(slot.getStack().serializeNBT());
			}
		}
	}
}
