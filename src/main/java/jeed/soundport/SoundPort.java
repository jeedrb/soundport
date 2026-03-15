package jeed.soundport;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.FolderResourcePack;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.resources.ResourcePackRepository;
import net.minecraft.init.Blocks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.io.File;
import java.util.List;

@Mod(modid = SoundPort.MODID, version = SoundPort.VERSION)
public class SoundPort
{
    public static final String MODID = "soundport";
    public static final String VERSION = "0.7.3";
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        File modFolder = new File(event.getModConfigurationDirectory().getParentFile(), "resourcepacks/SoundPort");
        SoundDownloader.soundDownload(modFolder);
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new SoundPortEvents());
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        Minecraft theCraft = Minecraft.getMinecraft();
        theCraft.getResourcePackRepository().updateRepositoryEntriesAll();

        theCraft.refreshResources();
    }
}
