package jeed.soundport;

import ibxm.Player;
import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.*;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3i;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.EntityInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;


public class SoundPortEvents {

    private static final String soundArray[] = {
        "", // 0 BLANK FOR OTHER BLOCKS AND ITEMS
        "soundport:wood.place", // 1 WOOD
        "soundport:wool.place", // 2 CLOTH/WOOL
        "soundport:green.place", // 3 GRASS/CROPS/GREEN
        "soundport:stone.place", // 4 STONE/IRON
        "soundport:witch.throw" // 5 POTION THROW
    };

    // witch hurt sound
    @SubscribeEvent
    public void onWitchHurt(LivingHurtEvent event) {
        if (event.entityLiving != null && event.entityLiving.worldObj != null && event.entityLiving.worldObj.isRemote && event.entityLiving instanceof EntityWitch) { // check if it exists, exists in the world, exists on the server, and is a witch
            if (event.entityLiving.getHealth() - event.ammount > 0) { // checks if it's about to die so it can do witch.death instead
                event.entityLiving.worldObj.playSoundAtEntity(event.entityLiving,"soundport:witch.hurt",1.0F, 0.8F + (event.entityLiving.getRNG().nextFloat() * 0.4F));
            }
        }
    }

    // witch death sound
    @SubscribeEvent
    public void onWitchDeath(LivingDeathEvent event) {
        if (event.entityLiving != null && event.entityLiving.worldObj != null && event.entityLiving.worldObj.isRemote && event.entityLiving instanceof EntityWitch) { // check if it exists, exists in the world, exists on the server, and is a witch
            event.entityLiving.worldObj.playSoundAtEntity(event.entityLiving,"soundport:witch.death",1.0F, 0.8F + (event.entityLiving.getRNG().nextFloat() * 0.4F));
        }
    }

    // witch just existing and chilling
    @SubscribeEvent
    public void onWitchUpdate(LivingEvent.LivingUpdateEvent event) {
        if (event.entityLiving != null && event.entityLiving.worldObj != null && event.entityLiving.worldObj.isRemote && event.entityLiving instanceof EntityWitch) {
            EntityWitch witch = (EntityWitch) event.entityLiving;
            float pitch = 0.8F + (event.entityLiving.getRNG().nextFloat() * 0.4F); // random pitch 0.8-1.2

            // 1% chance every tick to do an evil as freak witch idle noise, yikes!
            if (witch.getRNG().nextInt(100) == 67) witch.worldObj.playSoundAtEntity(witch,"soundport:witch.idle",1.0F, pitch);

            // sound when drinking a potion to heal
            if (witch.getDataWatcher().getWatchableObjectByte(21) == 1) if (witch.ticksExisted % 10 == 0) witch.worldObj.playSoundAtEntity(witch,"soundport:witch.drink",1.0F, pitch);

        }
    }

    @SubscribeEvent
    public void onBlockPlace(PlayerInteractEvent event) {
        if (!event.world.isRemote || event.action != PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK || event.entityPlayer.getHeldItem() == null) return; // abort

//        Block block = event.placedBlock.getBlock(); // get the block
//        Item heldItem = event.player.getHeldItem().getItem(); // get the item if it's a dumb stupid kind of block
        ItemStack heldStack = event.entityPlayer.getHeldItem();
        Item heldItem = event.entityPlayer.getHeldItem().getItem();

        BlockPos targetPos = event.pos.offset(event.face);
//        IBlockState targetState = event.world.getBlockState(targetPos);
//        BlockPos targetUp = event.world.getBlockState(targetPos);
//        EnumFacing targetFace = event.entityPlayer.getHorizontalFacing();

//        if (!targetState.getBlock().isReplaceable(event.world, targetPos)) return;
        if  (!event.world.getBlockState(targetPos).getBlock().isReplaceable(event.world, targetPos)) return;
//        if (!event.world.isAirBlock(targetPos) && !event.world.getBlockState(targetPos).getBlock().isReplaceable(event.world, targetPos)) {
//            return;
//        }


//        if (!event.world.getBlockState())

//        Block block = event.entityPlayer.something?


//        if (block == null || heldItem == null) return; // bye
        if (heldItem == null) return;

        int soundCategory = 0, blockedExtra = 0;
        float pitch = 0.8F + (event.world.rand.nextFloat() * 0.4F);
        double x = event.pos.getX() + 0.5D;
        double y = event.pos.getY() + 0.5D;
        double z = event.pos.getZ() + 0.5D;

//        if (heldItem instanceof ItemDoor) {
//            BlockPos targetUp = targetPos.up();
//            if (!event.world.getBlockState(targetUp).getBlock().isReplaceable(event.world, targetUp))
//                return;
//        } else if (heldItem instanceof ItemBed) {
//            EnumFacing targetHori = event.entityPlayer.getHorizontalFacing();
//            BlockPos targetSide = targetPos.offset(targetHori);
//            EnumFacing targetFacing = event.face;
////            BlockPos targetFace
//            BlockPos targetSideDown = targetSide.down();
//            if (!event.world.getBlockState(targetSide).getBlock().isReplaceable(event.world, targetSide))
//                return;
//        } else if (heldItem instanceof ItemSign) {
//
//        }

//        boolean canUse = heldStack.getItem().onItemUse(heldStack, event.entityPlayer, event.world, event.pos, event.face, 0.5F, 0.5F, 0.5F);
//        if (!canUse) return;

        Block clickedBlock = event.world.getBlockState(event.pos).getBlock();

        if (clickedBlock.hasTileEntity(event.world.getBlockState(event.pos)) && !event.entityPlayer.isSneaking())
            return;

        if (clickedBlock instanceof BlockSkull || clickedBlock instanceof BlockLilyPad || clickedBlock instanceof BlockCrops)
            return;

        if (heldItem instanceof ItemDoor) {
            BlockPos targetUp = targetPos.up();
            BlockPos targetDown = targetPos.down();
            if (event.face != EnumFacing.UP || !event.world.getBlockState(targetUp).getBlock().isReplaceable(event.world, targetUp) || !event.world.isSideSolid(targetDown, EnumFacing.UP))
                return;
        } else if (heldItem instanceof ItemBed) {
            EnumFacing targetHori = event.entityPlayer.getHorizontalFacing();
            BlockPos targetSide = targetPos.offset(targetHori);
            BlockPos targetDown = targetPos.down();
            BlockPos targetSideDown = targetSide.down();
            if (event.face != EnumFacing.UP || !event.world.getBlockState(targetSide).getBlock().isReplaceable(event.world, targetSide) || !event.world.isSideSolid(targetDown, EnumFacing.UP) || !event.world.isSideSolid(targetSideDown, EnumFacing.UP))
                return;
        } else if (heldItem instanceof ItemSign || heldItem instanceof ItemSkull) {
            if (event.face == EnumFacing.DOWN)
                return;
        } else if (heldItem instanceof ItemSeeds || heldItem == Items.carrot || heldItem == Items.potato) {
            Block targetBelow = event.world.getBlockState(event.pos).getBlock();
            if (event.face != EnumFacing.UP || !(targetBelow instanceof BlockFarmland)) {
                return;
            }
        }

//        if (heldItem instanceof ItemDoor || heldItem instanceof ItemBed) {
//            if (event.face != EnumFacing.UP)
//                return;
//        }

        if ((heldItem instanceof ItemDoor && !(heldItem == Items.iron_door)) || heldItem instanceof ItemBed || heldItem instanceof ItemBanner || heldItem instanceof ItemSign) {
            soundCategory = 1;
            System.out.println("wood" + x + " " + y + " " + z);
        } else if (heldItem instanceof ItemSeeds || heldItem instanceof ItemLilyPad || heldItem == Items.carrot || heldItem == Items.potato) {
            soundCategory = 3;
        } else if (heldItem == Items.iron_door || heldItem instanceof ItemSkull) {
            soundCategory = 4;
        } else {
            return;
        }

        event.world.playSound(x, y, z, soundArray[soundCategory].toString(),1.0F, pitch, false);
        System.out.println(soundArray[soundCategory].toString() + " " + x + " " + y + " " + z);
    }

    @SubscribeEvent
    public void onEntityPlace(EntityJoinWorldEvent event) { // placing entities
        if (!event.world.isRemote) return;

        int soundCategory = 0;
        float pitch = 0.8F + (event.world.rand.nextFloat() * 0.4F);
        double x = event.entity.posX + 0.5D;
        double y = event.entity.posY + 0.5D;
        double z = event.entity.posZ + 0.5D;

        if (event.entity instanceof EntityPainting || event.entity instanceof  EntityItemFrame) {
            soundCategory = 2;
        } else if (event.entity instanceof EntityArmorStand) {
            soundCategory = 4;
        } else if (event.entity instanceof EntityPotion && ((EntityPotion) event.entity).getThrower() instanceof EntityWitch) {
            soundCategory = 5;
        } else {
            return;
        }

        event.world.playSoundEffect(x, y, z, soundArray[soundCategory].toString(), 1.0F, pitch);
    }

    @SubscribeEvent
    public void onEntityBreak(AttackEntityEvent event) { // hitting/breaking entities
        if (!event.entity.worldObj.isRemote) return;

        int soundCategory = 0;
        float pitch = 0.8F + (event.entity.worldObj.rand.nextFloat() * 0.4F);
        double x = event.entity.posX; // DON'T add 0.5D to these since they're entities and aren't on the grid
        double y = event.entity.posY;
        double z = event.entity.posZ;

        if (event.target instanceof EntityItemFrame || event.target instanceof EntityPainting) {
            soundCategory = 2;
        } else if (event.target instanceof EntityArmorStand) {
            soundCategory = 4;
        } else {
            return;
        }

        event.entity.worldObj.playSoundEffect(x, y, z, soundArray[soundCategory].toString(), 1.0F, pitch);
    }

    @SubscribeEvent
    public void onEntityInteract(EntityInteractEvent event) { // interacting with entities
        if (!event.entity.worldObj.isRemote) return;

        int soundCategory = 0;
        float pitch = 0.8F + (event.entity.worldObj.rand.nextFloat() * 0.4F);
        double x = event.entity.posX;
        double y = event.entity.posY;
        double z = event.entity.posZ;

        if (event.target instanceof EntityItemFrame) {
            soundCategory = 2;
        } else if (event.target instanceof EntityArmorStand) { // doesn't work without a LOT of extra work
            soundCategory = 4;
        } else {
            return;
        }

        event.entity.worldObj.playSoundEffect(x, y, z, soundArray[soundCategory].toString(), 1.0F, pitch);
    }
}
