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
import net.minecraft.entity.player.EntityPlayer;
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
		
		if (ModifierHandler.ENCHANT_MODIFIER) {
			for (ItemStack itemStack : entity.getInventory()) {
				if (itemStack != null) {
					itemStack.getTagCompound().removeTag("ench");
				}
			}
			if (entity instanceof EntityPlayer) {
				for (ItemStack itemStack : ((EntityPlayer) entity).inventory.armorInventory) {
					if (itemStack != null) {
						itemStack.getTagCompound().removeTag("ench");
					}
				}
			}
		}
		int width = mc.displayWidth;
		int height = mc.displayHeight;
		
		Framebuffer framebuffer = FramebufferHelper.createFrameBuffer(width, height);
		int shortest = Math.min(Math.min(width, height), 515);
		
		double scale = 1;
		BufferedImage image = renderEntity(shortest, scale, entity, framebuffer);
		int longest;
		
		while ((longest = Math.max(image.getWidth(), image.getHeight())) != shortest) {
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
	
	private BufferedImage renderEntity(int height, double scale, EntityLivingBase entity, Framebuffer framebuffer) {
		FramebufferHelper.clearFrameBuffer();
		
		ScaledResolution resolution = new ScaledResolution(Minecraft.getMinecraft());
		
		int posX = resolution.getScaledWidth() / 2;
		int posY = resolution.getScaledHeight() / 2 + (height / resolution.getScaleFactor() / 2);
		
		FramebufferHelper.drawEntityOnScreen(posX, posY, scale, entity);
		
		return FramebufferHelper.trimImage(FramebufferHelper.readImage(framebuffer));
	}
}