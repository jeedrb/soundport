package jeed.soundport;

import ibxm.Player;
import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.*;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3i;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.EntityInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.Sys;

import java.util.List;


public class SoundPortEvents {

    private static final String soundArray[] = { // array of sounds to use
        "", // 0 BLANK FOR OTHER BLOCKS AND ITEMS
        "soundport:wood.place", // 1 WOOD
        "soundport:wool.place", // 2 CLOTH/WOOL
        "soundport:green.place", // 3 GRASS/CROPS/GREEN
        "soundport:stone.place", // 4 STONE/IRON
        "soundport:witch.throw" // 5 POTION THROW
    };

//    // witch hurt sound
//    @SubscribeEvent
//    public void onWitchHurt(LivingHurtEvent event) {
//        if (event.entityLiving != null && event.entityLiving.worldObj != null && event.entityLiving.worldObj.isRemote && event.entityLiving instanceof EntityWitch) { // check if it exists, exists in the world, exists on the server, and is a witch
//            if (event.entityLiving.getHealth() - event.ammount > 0) { // checks if it's about to die so it can do witch.death instead
//                event.entityLiving.worldObj.playSoundAtEntity(event.entityLiving,"soundport:witch.hurt",1.0F, 0.8F + (event.entityLiving.getRNG().nextFloat() * 0.4F));
//            }
//        }
//    }
//
//    // witch death sound
//    @SubscribeEvent
//    public void onWitchDeath(LivingDeathEvent event) {
//        if (event.entityLiving != null && event.entityLiving.worldObj != null && event.entityLiving.worldObj.isRemote && event.entityLiving instanceof EntityWitch) { // check if it exists, exists in the world, exists on the server, and is a witch
//            event.entityLiving.worldObj.playSoundAtEntity(event.entityLiving,"soundport:witch.death",1.0F, 0.8F + (event.entityLiving.getRNG().nextFloat() * 0.4F));
//        }
//    }
//
//    // witch just existing and chilling
//    @SubscribeEvent
//    public void onWitchUpdate(LivingEvent.LivingUpdateEvent event) {
//        if (event.entityLiving != null && event.entityLiving.worldObj != null && event.entityLiving.worldObj.isRemote && event.entityLiving instanceof EntityWitch) {
//            EntityWitch witch = (EntityWitch) event.entityLiving;
//            float pitch = 0.8F + (event.entityLiving.getRNG().nextFloat() * 0.4F); // random pitch 0.8-1.2
//
//            // 1% chance every tick to do an evil as freak witch idle noise, yikes!
//            if (witch.getRNG().nextInt(100) == 67) witch.worldObj.playSoundAtEntity(witch,"soundport:witch.idle",1.0F, pitch);
//
//            // sound when drinking a potion to heal
//            if (witch.getDataWatcher().getWatchableObjectByte(21) == 1) if (witch.ticksExisted % 10 == 0) witch.worldObj.playSoundAtEntity(witch,"soundport:witch.drink",1.0F, pitch);
//
//        }
//    }



    // witch doing anything time
    @SubscribeEvent
    public void onWitchUpdate (LivingEvent.LivingUpdateEvent event) {
        if (!event.entityLiving.worldObj.isRemote) return; // leave if not on the client

        if (event.entityLiving instanceof EntityWitch) { // check if it's a witch
            EntityWitch thisWitch = (EntityWitch) event.entityLiving;

            float pitch = 0.8F + (thisWitch.worldObj.rand.nextFloat() * 0.4F);
            double x = thisWitch.posX;
            double y = thisWitch.posY;
            double z = thisWitch.posZ;

            int overlapCheck = 0, swung = 0;

            if (thisWitch.deathTime == 1) { // witch SMACK
                thisWitch.worldObj.playSound(x, y, z, "soundport:witch.death", 1.0F, pitch, false);
            } else if (thisWitch.hurtTime == thisWitch.maxHurtTime && thisWitch.hurtTime > 0 && thisWitch.getHealth() > 0) { // witch VANQUISH
                thisWitch.worldObj.playSound(x, y, z, "soundport:witch.hurt", 1.0F, pitch, false);
            } else if (thisWitch.getDataWatcher().getWatchableObjectByte(21) == 1) { // witch getting CRUNK at the CLERB
                if (thisWitch.ticksExisted % 10 == 0) {
                    thisWitch.worldObj.playSound(x, y, z, "soundport:witch.drink", 1.0F, pitch, false);
                }
            } else if (thisWitch.ticksExisted == 1) { // witch spawning in
//                thisWitch.worldObj.playSound(x, y, z, "soundport:witch.idle", 1.0F, pitch, false); // disabled bc they all yell when loading into a world with multiple nearby
            } else if (thisWitch.worldObj.rand.nextInt(100) == 67) { // witch IDLE, this can be any number for 1% so 67 because #lmao
                if (thisWitch.ticksExisted > overlapCheck + 80) { // use the overlap check
                    thisWitch.worldObj.playSound(x, y, z, "soundport:witch.idle", 1.0F, pitch, false);
                    overlapCheck = thisWitch.ticksExisted;
                }
            }

//           if (thisWitch.getDataWatcher().getWatchableObjectByte(21) == 2) {
//               System.out.println("hi");
//           } else if (thisWitch.swingProgress > 0) {
//               System.out.println("sup");
//           } else if (thisWitch.isSwingInProgress) {
//               System.out.println("yo");
//           }

        }
    }

//    @SubscribeEvent
//    public void onWitchThrow(EntityJoinWorldEvent event) {
//        if (!event.world.isRemote || !(event.entity instanceof EntityPotion)) return;
//
//        EntityPotion potion = (EntityPotion) event.entity;
//
//        // 1. Only fire on the first tick
//        if (potion.ticksExisted == 0) {
//            // 2. Index 10 is the raw Entity ID of the shooter
//            int ownerId = potion.getDataWatcher().getWatchableObjectInt(10);
//
//            if (ownerId > 0) {
//                Entity thrower = event.world.getEntityByID(ownerId);
//
//                // 3. ZERO FALSE POSITIVES: The server explicitly linked this ID
//                if (thrower instanceof EntityWitch) {
//                    float pitch = 0.8F + (event.world.rand.nextFloat() * 0.4F);
//                    event.world.playSound(potion.posX, potion.posY, potion.posZ,
//                        "soundport:witch.throw", 1.0F, pitch, false);
//                    System.out.println("Verified Witch Throw by ID");
//                }
//            }
//        }
//    }

//    @SubscribeEvent
//    public void onPotionUpdate(EntityJoinWorldEvent event) {
//        if (!event.entity.worldObj.isRemote) return;
//        if (!(event.entity instanceof EntityPotion)) return;
//
//        System.out.println("hi " + ((EntityPotion) event.entity).getThrower());
//
//        EntityPotion thisPotion = (EntityPotion) event.entity;
//
//        int throwerId = thisPotion.getDataWatcher().getWatchableObjectInt(10);
//        Entity thrower = event.world.getEntityByID(throwerId);
//
//        if (thisPotion.ticksExisted < 10) {
//            if (thrower instanceof EntityWitch) {
//                float pitch = 0.8F + (thisPotion.worldObj.rand.nextFloat() * 0.4F);
//                double x = thisPotion.posX;
//                double y = thisPotion.posY;
//                double z = thisPotion.posZ;
//
//                System.out.println("SO CLOSE");
//                thisPotion.worldObj.playSound(x, y, z, "soundport:witch.throw", 1.0F, pitch, false);
//            }
//        }
//    }


//    @SubscribeEvent
//    public void onWitchAttack(LivingAttackEvent event) {
//
//    }

    // placing blocks I DID NOT HAVE FUN MAKING ALL THE CHECKS
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

        if (clickedBlock.hasTileEntity(event.world.getBlockState(event.pos)) && !event.entityPlayer.isSneaking()) // leave if we're opening a gui
            return;

        if (clickedBlock instanceof BlockSkull || clickedBlock instanceof BlockLilyPad || clickedBlock instanceof BlockCrops) // blacklist for some blocks you can't place on/from
            return;

        if (heldItem instanceof ItemDoor) { // check if ground is solid with two spaces above
            BlockPos targetUp = targetPos.up();
            BlockPos targetDown = targetPos.down();
            if (event.face != EnumFacing.UP || !event.world.getBlockState(targetUp).getBlock().isReplaceable(event.world, targetUp) || !event.world.isSideSolid(targetDown, EnumFacing.UP))
                return;
        } else if (heldItem instanceof ItemBed) { // check if grounds are solid with space above
            EnumFacing targetHori = event.entityPlayer.getHorizontalFacing();
            BlockPos targetSide = targetPos.offset(targetHori);
            BlockPos targetDown = targetPos.down();
            BlockPos targetSideDown = targetSide.down();
            if (event.face != EnumFacing.UP || !event.world.getBlockState(targetSide).getBlock().isReplaceable(event.world, targetSide) || !event.world.isSideSolid(targetDown, EnumFacing.UP) || !event.world.isSideSolid(targetSideDown, EnumFacing.UP))
                return;
        } else if (heldItem instanceof ItemSign || heldItem instanceof ItemSkull) { // check we're clicking any face besides bottom
            if (event.face == EnumFacing.DOWN)
                return;
        } else if (heldItem instanceof ItemSeeds || heldItem == Items.carrot || heldItem == Items.potato) { // check if we're placing on top face of farmland
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
//            System.out.println("wood" + x + " " + y + " " + z);
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

//        // placing entities, just witch potions rn
//        @SubscribeEvent
//        public void onEntityLoad(EntityJoinWorldEvent event) {
//            if (!event.world.isRemote) return;
//
//            if (!(event.entity instanceof EntityPotion)) return; // leave if not a potion
//            System.out.println("pot alert");
//            if (!(((EntityPotion) event.entity).getThrower() instanceof EntityWitch)) return; // leave if not thrown by a witch
//            System.out.println("pot alert super");
//
//            int soundCategory = 0;
//            float pitch = 0.8F + (event.world.rand.nextFloat() * 0.4F);
//            double x = event.entity.posX;
//            double y = event.entity.posY;
//            double z = event.entity.posZ;
//
//            if (event.entity instanceof EntityPainting || event.entity instanceof EntityItemFrame) { // soft entities
//                soundCategory = 0; // disabled
//            } else if (event.entity instanceof EntityArmorStand) { // hard entities
//                soundCategory = 0; // disabled
//            } else if (event.entity instanceof EntityPotion && ((EntityPotion) event.entity).getThrower() instanceof EntityWitch) { // witch throwing potion
//                soundCategory = 5;
//            } else {
//                return;
//            }
//
//            if (soundCategory == 0) return; // leave if not set, currently excludes all but witch potion throwing
//
//            System.out.println(soundArray[soundCategory].toString() + " " + x + " " + y + " " + z);
//            event.world.playSound(x, y, z, soundArray[soundCategory].toString(), 1.0F, pitch, false);
//            System.out.println("potion yuh");
//        }

    // player-placed entities
    @SubscribeEvent
    public void onEntityPlace(PlayerInteractEvent event) {
        if (!event.world.isRemote || event.action != PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK || event.entityPlayer.getHeldItem() == null)
            return;

        Item heldItem = event.entityPlayer.getHeldItem().getItem();
        int soundCategory = 0;
        float pitch = 0.8F + (event.world.rand.nextFloat() * 0.4F);
        double x = event.pos.getX();
        double y = event.pos.getY();
        double z = event.pos.getZ();

        BlockPos placeFace = event.pos.offset(event.face); // get the block face we're clicking
//        AxisAlignedBB fitCheck = new AxisAlignedBB(placeFace.getX(), placeFace.getY(), placeFace.getZ(), placeFace.getX() + 1, placeFace.getY() + 1, placeFace.getZ() + 1);
        AxisAlignedBB fitCheck = new AxisAlignedBB(placeFace, placeFace.add(1, 1, 1)); // bounding box for small entities
        AxisAlignedBB fitTall = new AxisAlignedBB(placeFace, placeFace.add(1, 2, 1)); // bounding box for tall entities
        List<EntityHanging> hanging = event.world.getEntitiesWithinAABB(EntityHanging.class, fitCheck); // list of entities in the small box
        List<Entity> crowd = event.world.getEntitiesWithinAABB(Entity.class, fitTall); // list of entities in the tall box

        if (heldItem == Items.item_frame) {
            if (event.face == EnumFacing.UP || event.face == EnumFacing.DOWN) // if looking at top or bottom of block
                return;
            for (EntityHanging hanged : hanging)
                if (hanged instanceof EntityItemFrame && hanged.facingDirection == event.face) // check every entity in the small box if it's an item frame
                    return;

        } else if (heldItem == Items.painting) {
            if (event.face == EnumFacing.UP || event.face == EnumFacing.DOWN) // if looking at top or bottom of block
                return;
            for (EntityHanging hanged : hanging)
                if (hanged.facingDirection == event.face || (hanged instanceof EntityPainting && hanged.facingDirection.getOpposite() != event.face)) // same as item frame check but exclusion for paintings on the opposite face
                    return;
        } else if (heldItem == Items.armor_stand) {
            if (!crowd.isEmpty()) // just see if anything is in the tall box
                return;
        }

        if (heldItem == Items.item_frame || heldItem == Items.painting) {
            soundCategory = 2;
        } else if (heldItem == Items.armor_stand) {
            soundCategory = 4;
        }

        event.world.playSound(x, y, z, soundArray[soundCategory].toString(), 1.0F, pitch, false);
    }

    // hitting/breaking entities
    @SubscribeEvent
    public void onEntityBreak(AttackEntityEvent event) {
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

        System.out.println(soundArray[soundCategory].toString() + " " + x + " " + y + " " + z);
        event.target.worldObj.playSound(x, y, z, soundArray[soundCategory].toString(), 1.0F, pitch, false);
    }

    // interacting with entities, but just item frames rn
    @SubscribeEvent
    public void onEntityInteract(EntityInteractEvent event) {
        if (!event.entity.worldObj.isRemote) return;

        int soundCategory = 0;
        float pitch = 0.8F + (event.entity.worldObj.rand.nextFloat() * 0.4F);
        double x = event.entity.posX;
        double y = event.entity.posY;
        double z = event.entity.posZ;

        if (event.target instanceof EntityItemFrame) {
            soundCategory = 2;
        } else if (event.target instanceof EntityArmorStand) { // doesn't work without a LOT of extra work
            soundCategory = 0; // disabling
        } else {
            return;
        }

        if (soundCategory == 0) return; // leave if unset/disabled

        System.out.println(soundArray[soundCategory].toString() + " " + x + " " + y + " " + z);
//        event.target.playSound(soundArray[soundCategory].toString(), 1.0F, pitch);
        event.target.worldObj.playSound(x, y, z, soundArray[soundCategory].toString(), 1.0F, pitch, false);
    }
}
