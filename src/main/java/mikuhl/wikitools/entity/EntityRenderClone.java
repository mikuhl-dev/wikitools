package mikuhl.wikitools.entity;

import com.mojang.authlib.GameProfile;

import mikuhl.wikitools.handler.ModifierHandler;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.util.ResourceLocation;

import java.util.UUID;

public class EntityRenderClone extends AbstractClientPlayer {
	
	private static final GameProfile STEVE_GAME_PROFILE = new GameProfile(new UUID(0, 0), "");
	
	private ResourceLocation locationSkin;
	
	public EntityRenderClone(AbstractClientPlayer clone, boolean steve) {
		super(clone.worldObj, steve ? STEVE_GAME_PROFILE : clone.getGameProfile());
		
		locationSkin = steve ? super.getLocationSkin() : clone.getLocationSkin();
		
		clonePlayer(clone, true);
		
		if (ModifierHandler.INVISIBLE_MODIFIER) {
			setInvisible(true);
		}
	}
	
	@Override
	public ResourceLocation getLocationSkin() {
		return locationSkin;
	}
}