/*
 * Part of the Tinkers Forging Mod by alcatrazEscapee
 * Work under Copyright. Licensed under the GPL-3.0.
 * See the project LICENSE.md for more information.
 */

package com.alcatrazescapee.tinkersforging.common.capability;

import javax.annotation.Nullable;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

import com.alcatrazescapee.tinkersforging.ModConfig;

import static com.alcatrazescapee.alcatrazcore.util.CoreHelpers.getNull;
import static com.alcatrazescapee.tinkersforging.ModConstants.MOD_ID;

public final class CapabilityForgeItem
{
    @CapabilityInject(IForgeItem.class)
    public static final Capability<IForgeItem> CAPABILITY = getNull();
    public static final ResourceLocation KEY = new ResourceLocation(MOD_ID, "forge_item");

    public static void preInit()
    {
        CapabilityManager.INSTANCE.register(IForgeItem.class, new Storage(), ForgeItem::new);
    }

    public static float adjustTemperature(float temperature, long lastUpdateTick)
    {
        final float newTemp = temperature - (Calendar.getWorldTime() - lastUpdateTick) * (float) ModConfig.GENERAL.temperatureModifier;
        return newTemp < 0 ? 0 : newTemp;
    }

    public static class Calendar
    {
        private static long worldTime;

        public static long getWorldTime()
        {
            return worldTime;
        }

        public static void setWorldTime(long worldTime)
        {
            Calendar.worldTime = worldTime;
        }
    }

    public static class Storage implements Capability.IStorage<IForgeItem>
    {
        @Nullable
        @Override
        public NBTBase writeNBT(Capability<IForgeItem> capability, IForgeItem instance, EnumFacing side)
        {
            return null;
        }

        @Override
        public void readNBT(Capability<IForgeItem> capability, IForgeItem instance, EnumFacing side, NBTBase nbt)
        {

        }
    }
}
