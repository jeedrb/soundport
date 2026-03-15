package jeed.soundport;

import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.init.Items;
import net.minecraft.item.*;
import net.minecraft.util.BlockPos;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class SoundPortEvents {

    // witch hurt sound
    @SubscribeEvent
    public void onWitchHurt(LivingHurtEvent event) {
        if (event.entityLiving != null && event.entityLiving.worldObj != null && !event.entityLiving.worldObj.isRemote && event.entityLiving instanceof EntityWitch) { // check if it exists, exists in the world, exists on the server, and is a witch
            if (event.entityLiving.getHealth() - event.ammount > 0) { // checks if it's about to die so it can do witch.death instead
                event.entityLiving.worldObj.playSoundAtEntity( // waiter one hurt sound please!
                    event.entityLiving,
                    "soundport:witch.hurt",
                    1.0F,
                    0.8F + (event.entityLiving.getRNG().nextFloat() * 0.4F) // random pitch 0.8-1.2
                );
            }
        }
    }

    // witch death sound
    @SubscribeEvent
    public void onWitchDeath(LivingDeathEvent event) {
        if (event.entityLiving != null && event.entityLiving.worldObj != null && !event.entityLiving.worldObj.isRemote && event.entityLiving instanceof EntityWitch) { // check if it exists, exists in the world, exists on the server, and is a witch
            event.entityLiving.worldObj.playSoundAtEntity( // waiter one death sound please!
                event.entityLiving,
                "soundport:witch.death",
                1.0F,
                0.8F + (event.entityLiving.getRNG().nextFloat() * 0.4F) // random pitch 0.8-1.2
            );
        }
    }

    // witch just existing and chilling
    @SubscribeEvent
    public void onWitchUpdate(LivingEvent.LivingUpdateEvent event) {
        if (event.entityLiving != null && event.entityLiving.worldObj != null && !event.entityLiving.worldObj.isRemote && event.entityLiving instanceof EntityWitch) {
            EntityWitch witch = (EntityWitch) event.entityLiving;
            float pitch = 0.8F + (event.entityLiving.getRNG().nextFloat() * 0.4F); // random pitch 0.8-1.2

            // 1% chance every tick to do an evil as freak witch idle noise, yikes!
            if (witch.getRNG().nextInt(100) == 67) { // this could be anything from 0-99 but 67 is epic sauce so that is my magic number
                witch.worldObj.playSoundAtEntity( // waiter one idle sound please!
                    witch,
                    "soundport:witch.idle",
                    1.0F,
                    pitch
                );
            }


            if (witch.getDataWatcher().getWatchableObjectByte(21) == 1) { // has to be ==1 because it's a bye not bool
                if (witch.ticksExisted % 10 == 0) {
                    witch.worldObj.playSoundAtEntity( // waiter one drinking sound please!
                        witch,
                        "soundport:witch.drink", // use the downloaded drinking sound so we can toggle it with the resource pack
                        1.0F,
                        pitch
                    );
                }
            }
        }
    }

    // throwing mode
    @SubscribeEvent
    public void onPotionThrow(EntityJoinWorldEvent event) {
        if (!event.world.isRemote && event.entity instanceof EntityPotion) {
            EntityPotion potion = (EntityPotion) event.entity; // potion critter

            if (potion.getThrower() != null && potion.getThrower() instanceof EntityWitch) {
                event.world.playSoundAtEntity( // waiter one throwing sound please!
                    potion.getThrower(),
                    "soundport:witch.throw", // use the downloaded throwing sound so we can toggle it with the resource pack
                    1.0F,
                    0.8F + (potion.getThrower().getRNG().nextFloat() * 0.4F) // random pitch 0.8-1.2
                );
            }
        }
    }

    @SubscribeEvent
    public void onWoodPlace(PlayerInteractEvent event) { // doors and beds and armor stands FIX IRON DOORS
        if (!event.world.isRemote && event.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) {
            if (event.entityPlayer.getHeldItem() == null) return;

            if (event.entityPlayer.getHeldItem() != null && (event.entityPlayer.getHeldItem().getItem() instanceof ItemDoor) || (event.entityPlayer.getHeldItem().getItem() instanceof ItemBed) || event.entityPlayer.getHeldItem().getItem() instanceof ItemArmorStand) {
                BlockPos place = event.pos;
                float pitch = 0.8F + (event.world.rand.nextFloat() * 0.4F);

                event.world.playSoundEffect(
                    (double)place.getX() + 0.5D,
                    (double)place.getY() + 0.5D, // maybe 1.0 or 1.5 for door?
                    (double)place.getZ() + 0.5D,
                    "soundport:wood.place",
                    1.0F,
                    pitch
                );
            }
        }
    }

    @SubscribeEvent
    public void onWoolPlace(PlayerInteractEvent event) { // paintings
        if (!event.world.isRemote && event.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) {
            if (event.entityPlayer.getHeldItem() == null) return;

            if (event.entityPlayer.getHeldItem() != null && (event.entityPlayer.getHeldItem().getItem() == Items.painting || event.entityPlayer.getHeldItem().getItem() == Items.item_frame)) {
                BlockPos place = event.pos;
                float pitch = 0.8F + (event.world.rand.nextFloat() * 0.4F);

                event.world.playSoundEffect(
                    (double)place.getX() + 0.5D,
                    (double)place.getY() + 0.5D,
                    (double)place.getZ() + 0.5D,
                    "soundport:wool.place",
                    1.0F,
                    pitch
                );
            }
        }
    }

    @SubscribeEvent
    public void onGreenPlace(PlayerInteractEvent event) { // seeds and lily pads LILY PADS DO NOT WORK FIX THAT NEXT
        if (!event.world.isRemote && event.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) {
            if (event.entityPlayer.getHeldItem() == null) return;

            if (event.entityPlayer.getHeldItem() != null && (event.entityPlayer.getHeldItem().getItem() instanceof ItemLilyPad || event.entityPlayer.getHeldItem().getItem() instanceof ItemSeeds)) {
                BlockPos place = event.pos;
                float pitch = 0.8F + (event.world.rand.nextFloat() * 0.4F);

                event.world.playSoundEffect(
                    (double)place.getX() + 0.5D,
                    (double)place.getY() + 0.5D,
                    (double)place.getZ() + 0.5D,
                    "soundport:green.place",
                    1.0F,
                    pitch
                );
            }
        }
    }
}
