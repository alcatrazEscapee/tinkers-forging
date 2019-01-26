/*
 * Part of the Tinkers Forging Mod by alcatrazEscapee
 * Work under Copyright. Licensed under the GPL-3.0.
 * See the project LICENSE.md for more information.
 */

package com.alcatrazescapee.tinkersforging.common.capability;

import java.util.ArrayList;
import java.util.List;
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

import com.alcatrazescapee.alcatrazcore.inventory.ingredient.IRecipeIngredient;
import com.alcatrazescapee.alcatrazcore.network.capability.CapabilityContainerListenerManager;
import com.alcatrazescapee.tinkersforging.ModConfig;
import com.alcatrazescapee.tinkersforging.common.capability.heat.IHeatRegistry;
import com.alcatrazescapee.tinkersforging.common.capability.heat.IngredientHeatRegistry;
import com.alcatrazescapee.tinkersforging.common.container.ContainerListenerForgeItem;
import com.alcatrazescapee.tinkersforging.util.material.MaterialRegistry;

import static com.alcatrazescapee.alcatrazcore.util.CoreHelpers.getNull;
import static com.alcatrazescapee.tinkersforging.TinkersForging.MOD_ID;

@ParametersAreNonnullByDefault
public final class CapabilityForgeItem
{
    @CapabilityInject(IForgeItem.class)
    public static final Capability<IForgeItem> CAPABILITY = getNull();

    public static final float MAX_TEMPERATURE = 1500f;
    public static final float DEFAULT_MELT_TEMPERATURE = 1400f;
    public static final float DEFAULT_WORK_TEMPERATURE = 800f;

    private static final ResourceLocation KEY = new ResourceLocation(MOD_ID, "forge_item");
    private static final List<IHeatRegistry> HEAT_REGISTRY = new ArrayList<>();
    private static final IHeatRegistry DEFAULT = new IHeatRegistry.Impl();

    public static void preInit()
    {
        // Register Capability
        CapabilityManager.INSTANCE.register(IForgeItem.class, new DumbStorage(), ForgeItem::new);

        // Register Sync Handler
        CapabilityContainerListenerManager.registerContainerListenerFactory(ContainerListenerForgeItem::new);

        // Add default metal entries
        HEAT_REGISTRY.addAll(MaterialRegistry.getAllMaterials());
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
        HEAT_REGISTRY.add(new IngredientHeatRegistry(ingredient, workingTemperature, meltingTemperature));
    }

    /**
     * Adds capabilities to an item stack based on the item -> capability registry
     */
    public static void addCapabilityToStack(AttachCapabilitiesEvent<ItemStack> event, ItemStack stack)
    {
        for (IHeatRegistry r : HEAT_REGISTRY)
        {
            if (r.test(stack))
            {
                event.addCapability(KEY, new ForgeItem(stack.getTagCompound(), r.getWorkTemp(), r.getMeltTemp()));
                return;
            }
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
}
