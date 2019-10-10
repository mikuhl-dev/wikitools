package mikuhl.wikitools.handler;

import mikuhl.wikitools.WikiToolsKeybinds;
import mikuhl.wikitools.helper.ClipboardHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class CopyNBTHandler {

    @SubscribeEvent
    public void onKeyInputEvent(InputEvent.KeyInputEvent event) {

        if (!WikiToolsKeybinds.COPY_NBT.isKeyDown()) return;

        Minecraft minecraft = Minecraft.getMinecraft();
        MovingObjectPosition objectMouseOver = minecraft.objectMouseOver;
        Entity entity = objectMouseOver.entityHit;

        if (entity != null) {
            if (entity instanceof EntityOtherPlayerMP) {
                NBTTagCompound nbt = new NBTTagCompound();
                entity.writeToNBT(nbt);
                ClipboardHelper.setClipboard(nbt);
            } else {
                ClipboardHelper.setClipboard(entity.serializeNBT());
            }
        } else {
            BlockPos pos = objectMouseOver.getBlockPos();
            TileEntity tile = minecraft.theWorld.getTileEntity(pos);
            if (tile != null) ClipboardHelper.setClipboard(tile.serializeNBT());
        }
    }

    @SubscribeEvent()
    public void onKeyboardInputEvent(GuiScreenEvent.KeyboardInputEvent event) {
        // Keybinds don't register in GUI's
        if (!Keyboard.isKeyDown(WikiToolsKeybinds.COPY_NBT.getKeyCode())) return;
        if (event.gui instanceof GuiContainer) {
            Slot slot = ((GuiContainer) event.gui).getSlotUnderMouse();
            if (slot == null) return;
            if (!slot.getHasStack()) return;
            ClipboardHelper.setClipboard(slot.getStack().serializeNBT());
        }
    }
}
