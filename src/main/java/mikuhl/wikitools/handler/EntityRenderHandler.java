package mikuhl.wikitools.handler;

import mikuhl.wikitools.WikiToolsKeybinds;
import mikuhl.wikitools.entity.EntityRenderClone;
import mikuhl.wikitools.helper.FramebufferHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

import java.awt.image.BufferedImage;

import static jdk.nashorn.internal.objects.Global.Infinity;

public class EntityRenderHandler {

    private static boolean rendering = false;

    @SubscribeEvent
    public void onKeyInputEvent(InputEvent.KeyInputEvent event) {

        if (!WikiToolsKeybinds.RENDER.isKeyDown()) return;

        if (rendering) return;

        Minecraft minecraft = Minecraft.getMinecraft();

        MovingObjectPosition objectMouseOver = minecraft.objectMouseOver;
        Entity entityHit = objectMouseOver.entityHit;

        boolean steve = WikiToolsKeybinds.STEVE_MODIFIER.isKeyDown();

        // Get the right entity
        EntityLivingBase entity = null;
        if (entityHit instanceof EntityLivingBase) {
            if (entityHit instanceof EntityOtherPlayerMP) {
                entity = new EntityRenderClone(((EntityOtherPlayerMP) entityHit), steve);
            } else {
                NBTTagCompound nbt = entityHit.serializeNBT();
                entity = ((EntityLivingBase) EntityList.createEntityFromNBT(nbt, minecraft.theWorld));
            }
        } else if (WikiToolsKeybinds.SELF_MODIFIER.isKeyDown()) {
            entity = new EntityRenderClone(minecraft.thePlayer, steve);
        }

        if (entity == null) return;

        rendering = true;

        // Disable red glow
        entity.hurtTime = 0;


        // Disable name tag
        entity.posX = Double.MAX_VALUE;
        entity.posY = Double.MAX_VALUE;
        entity.posZ = Double.MAX_VALUE;

        // Set rotation
        entity.renderYawOffset = 0;
        entity.rotationYaw = 0;
        entity.rotationPitch = 0;
        entity.rotationYawHead = entity.rotationYaw;
        entity.prevRotationYawHead = entity.rotationYaw;

        if (WikiToolsKeybinds.ENCHANT_MODIFIER.isKeyDown()) {
            for (ItemStack itemStack : entity.getInventory()) {
                if (itemStack != null) itemStack.getTagCompound().removeTag("ench");
            }
        }


        int displayWidth = minecraft.displayWidth;
        int displayHeight = minecraft.displayHeight;
        int shortest = Math.min(Math.min(displayWidth, displayHeight), 512);

        Framebuffer framebuffer = FramebufferHelper.createFrameBuffer(displayWidth, displayHeight);

        float scale = 1;
        BufferedImage bufferedImage = renderEntity(0, scale, entity, framebuffer);
        int longest = getLongest(bufferedImage);

        if (longest != 0) {
            // Some mobs require extremely fine scaling to equal exactly our size.
            // Be ok with 1 pixel smaller if it cant find the exact scale fast enough.
            while (longest != shortest && longest != shortest - 1) {
                scale = shortest / (longest / scale);
                if (scale == Infinity) break;
                bufferedImage = renderEntity(shortest, scale, entity, framebuffer);
                longest = getLongest(bufferedImage);
            }

            FramebufferHelper.saveBuffer(bufferedImage);
            FramebufferHelper.restoreFrameBuffer(framebuffer);
        }

        rendering = false;

    }

    private BufferedImage renderEntity(int height, float scale, EntityLivingBase entity, Framebuffer framebuffer) {

        FramebufferHelper.clearFrameBuffer();

        ScaledResolution resolution = new ScaledResolution(Minecraft.getMinecraft());
        int posX = resolution.getScaledWidth() / 2;
        int posY = resolution.getScaledHeight() / 2 + (height / resolution.getScaleFactor() / 2);

        FramebufferHelper.drawEntityOnScreen(posX, posY, scale, entity);
        BufferedImage readImage = FramebufferHelper.readImage(framebuffer);
        BufferedImage trimImage = FramebufferHelper.trimImage(readImage);

        return trimImage;
    }

    private int getLongest(BufferedImage image) {
        return Math.max(image.getWidth(), image.getHeight());
    }

}
