package mikuhl.wikitools.handler;

import mikuhl.wikitools.WikiTools;
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

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

import java.awt.image.BufferedImage;

public class EntityRenderHandler {
	
	private boolean rendering;
	
	@SubscribeEvent
	public void onKeyInputEvent(InputEvent.KeyInputEvent event) {
		if (rendering || !WikiTools.RENDER.isKeyDown()) {
			return;
		}
		Minecraft mc = Minecraft.getMinecraft();
		Entity entityHit = mc.objectMouseOver.entityHit;
		// Get the right entity
		EntityLivingBase entity;
		
		if (entityHit instanceof EntityLivingBase) {
			if (entityHit instanceof EntityOtherPlayerMP) {
				entity = new EntityRenderClone(((EntityOtherPlayerMP) entityHit), ModifierHandler.STEVE_MODIFIER);
			}
			else {
				entity = ((EntityLivingBase) EntityList.createEntityFromNBT(entityHit.serializeNBT(), mc.theWorld));
			}
		}
		else if (ModifierHandler.SELF_MODIFIER) {
			entity = new EntityRenderClone(mc.thePlayer, ModifierHandler.STEVE_MODIFIER);
		}
		else {
			entity = null;
		}
		if (entity == null) {
			return;
		}
		rendering = true;
		WikiTools.sendMessage("&a[WikiTools] &fRendering has started please wait.");
		
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
		
		if (ModifierHandler.HELDITEM_MODIFIER) {
			entity.setCurrentItemOrArmor(0, null);
		}
		if (ModifierHandler.ARMOR_MODIFIER) {
			entity.setCurrentItemOrArmor(1, null);
			entity.setCurrentItemOrArmor(2, null);
			entity.setCurrentItemOrArmor(3, null);
			entity.setCurrentItemOrArmor(4, null);
		}
		else if (ModifierHandler.ENCHANT_MODIFIER) {
			for (ItemStack itemStack : entity.getInventory()) {
				if (itemStack != null) {
					itemStack.getTagCompound().removeTag("ench");
				}
			}
		}
		int displayWidth = mc.displayWidth;
        int displayHeight = mc.displayHeight;
        int shortest = Math.min(Math.min(displayWidth, displayHeight), 512);

        Framebuffer framebuffer = FramebufferHelper.createFrameBuffer(displayWidth, displayHeight);

        float scale = 1;
        BufferedImage image = renderEntity(0, scale, entity, framebuffer);
		
		int longest;
		
		while ((longest = Math.max(image.getWidth(), image.getHeight())) != 0 && longest != shortest) {
			scale = shortest / (longest / scale);
			
			if (scale == Double.POSITIVE_INFINITY) {
				break;
			}
			image = renderEntity(shortest, scale, entity, framebuffer);
		}
		FramebufferHelper.saveBuffer(image);
		FramebufferHelper.restoreFrameBuffer(framebuffer);
		
		rendering = false;
		WikiTools.sendMessage("&a[WikiTools] &fRendering done.");
	}
	
	private BufferedImage renderEntity(int height, float scale, EntityLivingBase entity, Framebuffer framebuffer) {
		FramebufferHelper.clearFrameBuffer();
		
		ScaledResolution resolution = new ScaledResolution(Minecraft.getMinecraft());
		
		int posX = resolution.getScaledWidth() / 2;
		int posY = resolution.getScaledHeight() / 2 + (height / resolution.getScaleFactor() / 2);
		
		FramebufferHelper.drawEntityOnScreen(posX, posY, scale, entity);
		
		return FramebufferHelper.trimImage(FramebufferHelper.readImage(framebuffer));
	}
}