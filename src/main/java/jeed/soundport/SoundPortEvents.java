package jeed.soundport;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBanner;
import net.minecraft.block.BlockBed;
import net.minecraft.block.BlockDoor;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.init.Items;
import net.minecraft.item.*;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3i;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
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
    public void onPotionThrow(EntityJoinWorldEvent event) { // put this whole thing into onEntityPlace
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
    public void onBlockPlace(BlockEvent.PlaceEvent event) {
        if (event.world.isRemote) return; // abort if the placement fails ig

        Block block = event.placedBlock.getBlock(); // get the block
        Item heldItem = event.player.getHeldItem().getItem(); // get the item if it's a dumb stupid kind of block

        int soundCategory = 0; // category thingy, default to wood
        String soundArray[] = {
            "", // BLANK FOR OTHER BLOCKS AND ITEMS
            "soundport:wood.place", // 1 WOOD
            "soundport:wool.place", // 2 CLOTH/WOOL
            "soundport:green.place", // 3 GRASS/CROPS/GREEN
            "soundport:stone.place" // 4 STONE/IRON
        };

        float pitch = 0.8F + (event.world.rand.nextFloat() * 0.4F);
        double x = event.pos.getX() + 0.5D;
        double y = event.pos.getY() + 0.5D;
        double z = event.pos.getZ() + 0.5D;

        if ((block instanceof BlockDoor && !(heldItem == Items.iron_door)) || block instanceof BlockBed || block instanceof BlockBanner || event.player.getHeldItem().getItem() == Items.item_frame) {
            soundCategory = 1;
        } else if (heldItem instanceof ItemSeeds) {
            soundCategory = 3;
        } else if (heldItem == Items.iron_door) {
            soundCategory = 4;
        }

        event.world.playSoundEffect( x, y, z, soundArray[soundCategory].toString(),1.0F, pitch);
    }

    @SubscribeEvent
    public void onEntityPlace(EntityJoinWorldEvent event) { // placing entities
        if (event.world.isRemote) return;

        int soundCategory = 0;
        String soundArray[] = {
            "", // BLANK
            "soundport:wood.place", // 1 WOOD
            "soundport:wool.place", // 2 CLOTH/WOOL
        };

        float pitch = 0.8F + (event.world.rand.nextFloat() * 0.4F);
        double x = event.entity.posX + 0.5D;
        double y = event.entity.posY + 0.5D;
        double z = event.entity.posZ + 0.5D;

        if (event.entity instanceof EntityPainting || event.entity instanceof  EntityItemFrame) {
            soundCategory = 2;
        } else if (event.entity instanceof EntityArmorStand) {
            soundCategory = 1;
        } else {
            return;
        }

        event.world.playSoundEffect(x, y, z, soundArray[soundCategory].toString(), 1.0F, pitch);
    }

    @SubscribeEvent
    public void onEntityBreak(AttackEntityEvent event) { // hitting/breaking entities
        if (event.entity.worldObj.isRemote) return;

        int soundCategory = 0;
        String soundArray[] = {
            "", // BLANK
            "soundport:wood.place", // 0 WOOD
            "soundport:wool.place", // 1 CLOTH/WOOL
        };

        float pitch = 0.8F + (event.entity.worldObj.rand.nextFloat() * 0.4F);
        double x = event.entity.posX; // DON'T add 0.5D to these since they're entities and aren't on the grid
        double y = event.entity.posY;
        double z = event.entity.posZ;

        if (event.target instanceof EntityItemFrame || event.target instanceof EntityPainting) {
            soundCategory = 2;
        } else if (event.target instanceof EntityArmorStand) {
            soundCategory = 1;
        } else {
            return;
        }

        event.entity.worldObj.playSoundEffect(x, y, z, soundArray[soundCategory].toString(), 1.0F, pitch);
    }
}
