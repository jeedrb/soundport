package jeed.soundport;

import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.File;

@SideOnly(Side.CLIENT)
public class SoundPortClientProxy extends SoundPortCommonProxy {

    @Override
    public void init() {
        MinecraftForge.EVENT_BUS.register(new SoundPortEvents());
    }



//    @Mod.EventHandler
//    public void init(FMLInitializationEvent event) {
//        MinecraftForge.EVENT_BUS.register(new SoundPortEvents());
//    }

//    @Mod.EventHandler
//    public void postInit(FMLPostInitializationEvent event) {
//        Minecraft theCraft = Minecraft.getMinecraft();
//        theCraft.getResourcePackRepository().updateRepositoryEntriesAll();
//
//        theCraft.refreshResources();
//    }
}
