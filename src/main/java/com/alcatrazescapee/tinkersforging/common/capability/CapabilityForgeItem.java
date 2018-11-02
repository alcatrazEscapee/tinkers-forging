/*
 * Part of the Tinkers Forging Mod by alcatrazEscapee
 * Work under Copyright. Licensed under the GPL-3.0.
 * See the project LICENSE.md for more information.
 */

package com.alcatrazescapee.tinkersforging.common.capability;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

import com.alcatrazescapee.tinkersforging.ModConfig;

import static com.alcatrazescapee.alcatrazcore.util.CoreHelpers.getNull;
import static com.alcatrazescapee.tinkersforging.ModConstants.MOD_ID;

@ParametersAreNonnullByDefault
public final class CapabilityForgeItem
{
    @CapabilityInject(IForgeItem.class)
    public static final Capability<IForgeItem> CAPABILITY = getNull();
    public static final String NBT_KEY = MOD_ID + ":forge_item";
    public static final ResourceLocation KEY = new ResourceLocation(MOD_ID, "forge_item");

    public static final float MAX_TEMPERATURE = 1500f;

    public static void preInit()
    {
        CapabilityManager.INSTANCE.register(IForgeItem.class, new DumbStorage(), () -> new ForgeItem(null));
    }

    public static void clearStackCheckRecipe(ItemStack stack)
    {
        IForgeItem cap = stack.getCapability(CapabilityForgeItem.CAPABILITY, null);
        if (cap != null && (cap.getRecipeName() == null || cap.getWork() == 0))
        {
            clearStack(stack, cap);
        }
    }

    public static void clearStack(ItemStack stack)
    {
        IForgeItem cap = stack.getCapability(CapabilityForgeItem.CAPABILITY, null);
        if (cap != null)
        {
            clearStack(stack, cap);
        }
    }

    /**
     * Use this to increase the heat on an item stack
     */
    public static void addTemp(ItemStack stack, IForgeItem cap, float modifier)
    {
        final float temp = cap.getTemperature() + modifier * (float) ModConfig.GENERAL.temperatureModifier;
        cap.setTemperature(temp > MAX_TEMPERATURE ? MAX_TEMPERATURE : temp);
        stack.setTagCompound(cap.serializeNBT());
    }

    private static void clearStack(ItemStack stack, IForgeItem cap)
    {
        cap.reset();
        stack.removeSubCompound(CapabilityForgeItem.NBT_KEY);
        NBTTagCompound nbt = stack.getTagCompound();
        if (nbt != null && nbt.isEmpty())
        {
            stack.setTagCompound(null);
        }
    }

    // This is not for usage; it will not do anything.
    static class DumbStorage implements Capability.IStorage<IForgeItem>
    {
        @Nullable
        @Override
        public NBTBase writeNBT(Capability<IForgeItem> capability, IForgeItem instance, EnumFacing side)
        {
            return null;
        }

        @Override
        public void readNBT(Capability<IForgeItem> capability, IForgeItem instance, EnumFacing side, NBTBase nbtBase)
        {
        }
    }
}
