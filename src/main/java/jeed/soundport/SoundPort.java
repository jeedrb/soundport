package jeed.soundport;

import net.minecraft.init.Blocks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

@Mod(modid = SoundPort.MODID, version = SoundPort.VERSION)
public class SoundPort
{
    public static final String MODID = "soundport";
    public static final String VERSION = "0.2";
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
		// some example code
        System.out.println("DWERK BLOCK >> "+Blocks.dirt.getUnlocalizedName());

        MinecraftForge.EVENT_BUS.register(new SoundPortEvents());
    }
}
