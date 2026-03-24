package jeed.soundport;

import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import java.io.File;
//import net.minecraftforge.fml.common.SidedProxy

@Mod(modid = SoundPort.MODID, version = SoundPort.VERSION, clientSideOnly = true)
public class SoundPort
{
    public static final String MODID = "soundport";
    public static final String VERSION = "0.9";
    
    @SidedProxy(clientSide = "jeed.soundport.SoundPortClientProxy", serverSide = "jeed.soundport.SoundPortCommonProxy")
    public static SoundPortCommonProxy proxy;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        File modFolder = new File(event.getModConfigurationDirectory().getParentFile(), "resourcepacks/SoundPort");
        SoundDownloader.soundDownload(modFolder);
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        Minecraft theCraft = Minecraft.getMinecraft();
        theCraft.getResourcePackRepository().updateRepositoryEntriesAll();
        theCraft.refreshResources();
    }
}
