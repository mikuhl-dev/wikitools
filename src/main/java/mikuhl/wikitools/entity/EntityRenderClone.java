package mikuhl.wikitools.entity;

import com.mojang.authlib.GameProfile;
import mikuhl.wikitools.WikiToolsKeybinds;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.util.ResourceLocation;

import java.util.UUID;

public class EntityRenderClone extends AbstractClientPlayer {

    private static GameProfile steveGameProfile = new GameProfile(new UUID(0, 0), "");

    private ResourceLocation locationSkin;

    public EntityRenderClone(AbstractClientPlayer clone, boolean steve) {
        super(clone.worldObj, steve ? steveGameProfile : clone.getGameProfile());
        locationSkin = (steve ? this : clone).getLocationSkin();
        this.clonePlayer(clone, true);
        if (WikiToolsKeybinds.INVISIBLE_MODIFIER.isKeyDown()) this.setInvisible(true);
    }

    @Override
    public ResourceLocation getLocationSkin() {
        if (locationSkin != null) {
            return locationSkin;
        }
        return super.getLocationSkin();
    }
}
