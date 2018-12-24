/*
 * Part of the Tinkers Forging Mod by alcatrazEscapee
 * Work under Copyright. Licensed under the GPL-3.0.
 * See the project LICENSE.md for more information.
 */

package com.alcatrazescapee.tinkersforging;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.alcatrazescapee.alcatrazcore.AlcatrazCore;
import com.alcatrazescapee.alcatrazcore.util.CoreHelpers;
import com.alcatrazescapee.alcatrazcore.util.RegistryHelper;
import com.alcatrazescapee.tinkersforging.common.blocks.BlockCharcoalForge;
import com.alcatrazescapee.tinkersforging.common.blocks.BlockCharcoalPile;
import com.alcatrazescapee.tinkersforging.common.blocks.ModBlocks;
import com.alcatrazescapee.tinkersforging.common.capability.CapabilityForgeItem;
import com.alcatrazescapee.tinkersforging.common.capability.IForgeItem;
import com.alcatrazescapee.tinkersforging.common.recipe.ModRecipes;
import com.alcatrazescapee.tinkersforging.integration.PatchouliIntegration;
import com.alcatrazescapee.tinkersforging.integration.TinkersIntegration;
import com.alcatrazescapee.tinkersforging.util.TickTimer;
import slimeknights.tconstruct.smeltery.events.TinkerCastingEvent;

import static com.alcatrazescapee.tinkersforging.TinkersForging.MOD_ID;

@Mod.EventBusSubscriber(modid = MOD_ID)
@SuppressWarnings("unused")
public final class ModEventHandler
{
    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event)
    {
        RegistryHelper.get(MOD_ID).initItems(event);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void registerRecipes(RegistryEvent.Register<IRecipe> event)
    {
        ModRecipes.registerRecipes(event);
    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event)
    {
        RegistryHelper.get(MOD_ID).initBlocks(event);
    }

    @SubscribeEvent
    public static void registerSounds(RegistryEvent.Register<SoundEvent> event)
    {
        RegistryHelper.get(MOD_ID).initSounds(event);
    }

    @SubscribeEvent
    public static void onConfigChangedEvent(ConfigChangedEvent.OnConfigChangedEvent event)
    {
        if (event.getModID().equals(MOD_ID))
        {
            ConfigManager.sync(MOD_ID, Config.Type.INSTANCE);

            if (Loader.isModLoaded("patchouli"))
            {
                PatchouliIntegration.resetTooltipFlag();
            }

            if (Loader.isModLoaded("tconstruct"))
            {
                TinkersIntegration.updateSharedConfig();
            }
        }
        else if (event.getModID().equals("tconstruct"))
        {
            // Super-duper safe
            if (Loader.isModLoaded("tconstruct"))
            {
                TinkersIntegration.updateSharedConfig();
            }
        }
    }

    @SubscribeEvent
    public static void attachItemCapabilities(AttachCapabilitiesEvent<ItemStack> event)
    {
        ItemStack stack = event.getObject();

        if (!stack.hasCapability(CapabilityForgeItem.CAPABILITY, null))
        {
            CapabilityForgeItem.addCapabilityToStack(event, stack);
        }
    }

    @SubscribeEvent
    public static void onWorldTick(TickEvent.WorldTickEvent event)
    {
        if (event.phase == TickEvent.Phase.END)
        {
            TickTimer.update(event.world.getTotalWorldTime());
        }
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void onClientTick(TickEvent.ClientTickEvent event)
    {
        if (event.phase == TickEvent.Phase.END && !Minecraft.getMinecraft().isGamePaused() && Minecraft.getMinecraft().player != null)
        {
            TickTimer.update(AlcatrazCore.getProxy().getClientWorld().getTotalWorldTime());
        }
    }

    @SubscribeEvent
    @Optional.Method(modid = "tconstruct")
    public static void onTinkersCastingEvent(TinkerCastingEvent.OnCasted event)
    {
        IForgeItem cap = event.output.getCapability(CapabilityForgeItem.CAPABILITY, null);
        if (cap != null)
        {
            cap.setTemperature(cap.getMeltingTemperature() - 1f);
        }
    }

    @SubscribeEvent
    public static void onRightClickBlockEvent(PlayerInteractEvent.RightClickBlock event)
    {
        ItemStack stack = event.getItemStack();

        if (!stack.isEmpty() && CoreHelpers.doesStackMatchOre(stack, "charcoal"))
        {
            World world = event.getWorld();
            BlockPos pos = event.getPos();
            EntityPlayer player = event.getEntityPlayer();

            EnumFacing face = event.getFace();
            if (face == null) return;

            IBlockState state = world.getBlockState(pos);

            if (face == EnumFacing.UP && state.getBlock() == ModBlocks.CHARCOAL_PILE || state.getBlock() == ModBlocks.CHARCOAL_FORGE)
            {
                // Since these aren't the same property
                IProperty<Integer> layerProperty = state.getBlock() == ModBlocks.CHARCOAL_PILE ? BlockCharcoalPile.LAYERS : BlockCharcoalForge.LAYERS;
                int layers = state.getValue(layerProperty);
                // Add to an existing pile block
                if (layers < 8 && world.checkNoEntityCollision(ModBlocks.CHARCOAL_PILE.getBoundingBox(ModBlocks.CHARCOAL_PILE.getStateWithLayers(layers + 1), world, pos).offset(pos)))
                {
                    world.setBlockState(pos, state.withProperty(layerProperty, layers + 1));
                    player.setHeldItem(event.getHand(), CoreHelpers.consumeItem(player, stack));
                    world.playSound(null, pos, SoundEvents.BLOCK_GRAVEL_PLACE, SoundCategory.BLOCKS, 1.0f, 0.5f);

                    // Don't try and add on top
                    event.setCanceled(true);
                    event.setCancellationResult(EnumActionResult.SUCCESS);
                    return;
                }
            }

            if (world.getBlockState(pos.down().offset(face)).isNormalCube() && world.mayPlace(Blocks.STONE, pos.offset(face), false, face, player))
            {
                // Create a new charcoal pile
                if (!world.isRemote)
                {
                    world.setBlockState(pos.offset(face), ModBlocks.CHARCOAL_PILE.getDefaultState());

                    player.setHeldItem(event.getHand(), CoreHelpers.consumeItem(player, stack));
                    world.playSound(null, pos.offset(face), SoundEvents.BLOCK_GRAVEL_PLACE, SoundCategory.BLOCKS, 1.0f, 0.5f);

                    event.setCanceled(true);
                    event.setCancellationResult(EnumActionResult.SUCCESS);
                }
            }
        }
    }
}
