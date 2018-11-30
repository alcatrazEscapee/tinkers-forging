/*
 * Part of the Tinkers Forging Mod by alcatrazEscapee
 * Work under Copyright. Licensed under the GPL-3.0.
 * See the project LICENSE.md for more information.
 */

package com.alcatrazescapee.tinkersforging.common.capability;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.oredict.OreDictionary;

import com.alcatrazescapee.alcatrazcore.inventory.ingredient.IRecipeIngredient;
import com.alcatrazescapee.alcatrazcore.network.capability.CapabilityContainerListenerManager;
import com.alcatrazescapee.tinkersforging.ModConfig;
import com.alcatrazescapee.tinkersforging.common.container.ContainerListenerForgeItem;

import static com.alcatrazescapee.alcatrazcore.util.CoreHelpers.getNull;
import static com.alcatrazescapee.tinkersforging.TinkersForging.MOD_ID;

@ParametersAreNonnullByDefault
public final class CapabilityForgeItem
{
    @CapabilityInject(IForgeItem.class)
    public static final Capability<IForgeItem> CAPABILITY = getNull();
    public static final ResourceLocation KEY = new ResourceLocation(MOD_ID, "forge_item");

    public static final float MAX_TEMPERATURE = 1500f;
    public static final float DEFAULT_MELT_TEMPERATURE = 1400f;
    public static final float DEFAULT_WORK_TEMPERATURE = 800f;

    private static final List<ForgeItemRegistry> EXTRA_CAPABILITIES = new ArrayList<>();

    public static void preInit()
    {
        // Register Capability
        CapabilityManager.INSTANCE.register(IForgeItem.class, new DumbStorage(), () -> new ForgeItem(null, null));

        // Register Sync Handler
        CapabilityContainerListenerManager.registerContainerListenerFactory(ContainerListenerForgeItem::new);
    }

    /**
     * Used by the Tinker's Anvil to clear a stack if it hasn't been worked yet
     */
    public static void clearStackCheckRecipe(ItemStack stack)
    {
        IForgeItem cap = stack.getCapability(CapabilityForgeItem.CAPABILITY, null);
        if (cap != null)
        {
            if (cap.getRecipeName() == null || cap.getWork() == 0)
            {
                cap.reset();
            }
        }
    }

    /**
     * Use this to increase the heat on an item stack
     */
    public static void addTemp(IForgeItem cap, float modifier)
    {
        final float temp = cap.getTemperature() + modifier * (float) ModConfig.BALANCE.temperatureModifier;
        cap.setTemperature(temp > MAX_TEMPERATURE ? MAX_TEMPERATURE : temp);
    }

    /**
     * Use this to register a special heat application (the capability will take priority over default ones)
     *
     * @param ingredient         A predicate to check if the item stack should have this capability
     * @param workingTemperature the working temperature of the capability
     * @param meltingTemperature the melting temperature of the capability
     */
    public static void registerStackCapability(IRecipeIngredient ingredient, float workingTemperature, float meltingTemperature)
    {
        EXTRA_CAPABILITIES.add(new ForgeItemRegistry(ingredient, workingTemperature, meltingTemperature));
    }

    /**
     * Adds capabilities to an item stack based on the item -> capability registry
     *
     * @return true if a capability was added
     */
    public static boolean addCapabilityToStack(AttachCapabilitiesEvent<ItemStack> event, ItemStack stack)
    {
        for (ForgeItemRegistry r : EXTRA_CAPABILITIES)
        {
            if (r.test(stack))
            {
                event.addCapability(KEY, new ForgeItem(stack.getTagCompound(), r.workingTemperature, r.meltingTemperature));
                return true;
            }
        }
        return false;
    }

    static float getMeltingTemperature(@Nullable ItemStack stack)
    {
        if (stack != null)
        {
            int[] ids = OreDictionary.getOreIDs(stack);
            for (int id : ids)
            {
                // Try and catch all common metal items
                String name = OreDictionary.getOreName(id);
                if (name.startsWith("ingot") || name.startsWith("block"))
                {
                    return getMetalTemperature(name.substring(5).toLowerCase());
                }
                else if (name.startsWith("nugget"))
                {
                    return getMetalTemperature(name.substring(6).toLowerCase());
                }
            }
        }
        return DEFAULT_MELT_TEMPERATURE;
    }

    static float getWorkingTemperature(@Nullable ItemStack stack)
    {
        if (stack != null)
        {
            float meltTemp = getMeltingTemperature(stack);
            if (meltTemp == DEFAULT_MELT_TEMPERATURE)
                return DEFAULT_WORK_TEMPERATURE;
            else if (meltTemp > 1500)
                return 1350;
            else if (meltTemp >= 1000)
                return meltTemp - 400;
            else if (meltTemp >= 500)
                return meltTemp - 250;
            else if (meltTemp >= 300)
                return meltTemp - 150;
            else if (meltTemp >= 200)
                return 100;
            else
                return 0;
        }
        return DEFAULT_WORK_TEMPERATURE;
    }

    private static float getMetalTemperature(String name)
    {
        // This is roughly based on real melting points of metals
        switch (name)
        {
            case "tin": // 231.9
                return 300;
            case "lead": // 327.5
                return 350;
            case "zinc": // 419.5
            case "pigiron": // ???
                return 400;
            case "aluminium": // 660.3
                return 700;
            case "electrum": // 912.8
            case "brass": // 920.0
                return 900;
            case "bronze": // 950.0
            case "silver": // 961.8
                return 950;
            case "copper": // 1085.0
                return 1000;
            case "gold": // 1064.0
                return 1100;
            case "steel": // 1370.0
                return 1350;
            case "nickel": // 1455.0
            case "invar": // 1427.0
                return 1450;
            case "cobalt": // 1495.0
            case "iron": // 1538.0
                return 1600;
            case "ardite": // ???
            case "manyullyn": // ???
                return 2100;
            case "mithril": // ???
                return 3200;
            case "diamond": // 4726.9
                return 4700;
            default:
                return DEFAULT_MELT_TEMPERATURE;
        }
    }

    // This is not for usage; it will not do anything.
    private static class DumbStorage implements Capability.IStorage<IForgeItem>
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

    private static class ForgeItemRegistry implements Predicate<ItemStack>
    {
        private final IRecipeIngredient ingredient;
        private final float workingTemperature, meltingTemperature;

        private ForgeItemRegistry(IRecipeIngredient ingredient, float workingTemperature, float meltingTemperature)
        {
            this.ingredient = ingredient;
            this.workingTemperature = workingTemperature;
            this.meltingTemperature = meltingTemperature;
        }

        @Override
        public boolean test(ItemStack stack)
        {
            return ingredient.testIgnoreCount(stack);
        }
    }
}
