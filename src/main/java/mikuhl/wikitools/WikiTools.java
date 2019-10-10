package mikuhl.wikitools;

import mikuhl.wikitools.proxy.CommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

@Mod(modid = WikiTools.MODID, version = WikiTools.VERSION)
public class WikiTools {
    public static final String MODID = "wikitools";
    public static final String VERSION = "1.0";

    @SidedProxy(serverSide = "mikuhl.wikitools.proxy.CommonProxy", clientSide = "mikuhl.wikitools.proxy.ClientProxy")
    public static CommonProxy proxy;

    @EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
    }
}
