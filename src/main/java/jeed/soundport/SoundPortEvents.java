package jeed.soundport;

import net.minecraft.entity.monster.EntityWitch;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class SoundPortEvents {
    @SubscribeEvent
    public void onWitchHurt(LivingHurtEvent event) {
        if (event.entityLiving != null && !event.entityLiving.worldObj.isRemote) {
            if (event.entityLiving instanceof EntityWitch) {
                if (event.entityLiving.getHealth() - event.ammount > 0) {
                    event.entityLiving.worldObj.playSoundAtEntity(
                            event.entityLiving,
                            "soundport:witch.hurt",
                            1.0F,
                            1.0F
                    );
                }
            }
        }
    }
    @SubscribeEvent
    public void onWitchDeath(LivingDeathEvent event) {
        if (event.entityLiving instanceof EntityWitch) {
            if (event.entityLiving != null && event.entityLiving.worldObj != null) {
                event.entityLiving.worldObj.playSoundAtEntity(
                        event.entityLiving,
                        "soundport:witch.death",
                        1.0F,
                        1.0F
                );
            }
        }
    }
}
