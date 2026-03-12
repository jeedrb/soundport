package jeed.soundport;

import net.minecraft.entity.monster.EntityWitch;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class SoundPortEvents {

    // witch hurt sound
    @SubscribeEvent
    public void onWitchHurt(LivingHurtEvent event) {
        if (event.entityLiving != null && event.entityLiving.worldObj != null && !event.entityLiving.worldObj.isRemote) { // check if it exists
            if (event.entityLiving instanceof EntityWitch) { // is it a witch
                if (event.entityLiving.getHealth() - event.ammount > 0) { // waiter one hurt sound please!
                    event.entityLiving.worldObj.playSoundAtEntity(
                            event.entityLiving,
                            "soundport:witch.hurt",
                            1.0F,
                            // random pitch 0.8-1.2
                            0.8F + (event.entityLiving.getRNG().nextFloat() * 0.4F)
                    );
                }
            }
        }
    }

    // witch death sound
    @SubscribeEvent
    public void onWitchDeath(LivingDeathEvent event) {
        if (event.entityLiving != null && event.entityLiving.worldObj != null && !event.entityLiving.worldObj.isRemote) { // check if it exists
            if (event.entityLiving instanceof EntityWitch) { // is it a witch
                event.entityLiving.worldObj.playSoundAtEntity( // waiter one death sound please!
                        event.entityLiving,
                        "soundport:witch.death",
                        1.0F,
                        // random pitch 0.8-1.2
                        0.8F + (event.entityLiving.getRNG().nextFloat() * 0.4F)
                );
            }
        }
    }
}
