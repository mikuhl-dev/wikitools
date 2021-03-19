package mikuhl.wikitools.entity;

import mikuhl.wikitools.handler.ModifierHandler;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.util.ResourceLocation;

public class EntityRenderClone extends AbstractClientPlayer {
	
	private static final ResourceLocation STEVE_SKIN = new ResourceLocation("textures/entity/steve.png");
		
	private ResourceLocation locationSkin;
	
	public EntityRenderClone(AbstractClientPlayer clone, boolean steve) {
		super(clone.worldObj, clone.getGameProfile());
		
		locationSkin = steve ? STEVE_SKIN : clone.getLocationSkin();
		
		clonePlayer(clone, true);
		setInvisible(ModifierHandler.INVISIBLE_MODIFIER);
	}
	
	@Override
	public ResourceLocation getLocationSkin() {
		return locationSkin;
	}
}