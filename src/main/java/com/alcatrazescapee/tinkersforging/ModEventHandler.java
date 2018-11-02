/*
 * Part of the Tinkers Forging Mod by alcatrazEscapee
 * Work under Copyright. Licensed under the GPL-3.0.
 * See the project LICENSE.md for more information.
 */

package com.alcatrazescapee.tinkersforging;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import com.alcatrazescapee.alcatrazcore.util.CoreHelpers;
import com.alcatrazescapee.alcatrazcore.util.RegistryHelper;
import com.alcatrazescapee.tinkersforging.common.capability.CapabilityForgeItem;
import com.alcatrazescapee.tinkersforging.common.capability.ForgeItem;
import com.alcatrazescapee.tinkersforging.common.capability.IForgeItem;
import com.alcatrazescapee.tinkersforging.common.recipe.ModRecipes;
import com.alcatrazescapee.tinkersforging.util.TickTimer;
import slimeknights.tconstruct.smeltery.events.TinkerCastingEvent;

import static com.alcatrazescapee.tinkersforging.ModConstants.MOD_ID;

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
        }
    }

    @SubscribeEvent
    public static void attachItemCapabilities(AttachCapabilitiesEvent<ItemStack> event)
    {
        ItemStack stack = event.getObject();

        if (!stack.hasCapability(CapabilityForgeItem.CAPABILITY, null))
        {
            if (CoreHelpers.doesStackMatchOrePrefix(stack, "ingot") || ModRecipes.ANVIL.isRecipe(stack))
            {
                event.addCapability(CapabilityForgeItem.KEY, new ForgeItem(stack.getTagCompound()));
            }
        }
    }

    @SubscribeEvent
    public static void onTickWorldTick(TickEvent.WorldTickEvent event)
    {
        if (event.phase == TickEvent.Phase.END)
        {
            TickTimer.update(event.world.getTotalWorldTime());
        }
    }

    @SubscribeEvent
    @Optional.Method(modid = "tconstruct")
    public static void onTinkersCastingEvent(TinkerCastingEvent.OnCasted event)
    {
        IForgeItem cap = event.output.getCapability(CapabilityForgeItem.CAPABILITY, null);
        if (cap != null && ModConfig.GENERAL.enableTemperatureMechanics)
        {
            cap.setTemperature(cap.getMeltingTemperature() - 1f);
            event.output.setTagInfo(CapabilityForgeItem.NBT_KEY, cap.serializeNBT());
        }
    }
}
