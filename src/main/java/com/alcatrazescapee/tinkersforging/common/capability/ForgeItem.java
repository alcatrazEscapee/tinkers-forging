/*
 * Part of the Tinkers Forging Mod by alcatrazEscapee
 * Work under Copyright. Licensed under the GPL-3.0.
 * See the project LICENSE.md for more information.
 */

package com.alcatrazescapee.tinkersforging.common.capability;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

import com.alcatrazescapee.tinkersforging.ModConfig;
import com.alcatrazescapee.tinkersforging.common.capability.heat.IHeatRegistry;
import com.alcatrazescapee.tinkersforging.common.recipe.AnvilRecipe;
import com.alcatrazescapee.tinkersforging.util.TickTimer;
import com.alcatrazescapee.tinkersforging.util.forge.ForgeStep;
import com.alcatrazescapee.tinkersforging.util.forge.ForgeSteps;

import static com.alcatrazescapee.tinkersforging.common.capability.CapabilityForgeItem.DEFAULT_MELT_TEMPERATURE;
import static com.alcatrazescapee.tinkersforging.common.capability.CapabilityForgeItem.DEFAULT_WORK_TEMPERATURE;

public class ForgeItem implements IForgeItem, ICapabilitySerializable<NBTTagCompound>
{
    private final ForgeSteps steps;
    private int work;
    private String recipeName;

    private final float meltingTemperature;
    private final float workingTemperature;

    // These are the values from last point of update. They are updated when read from NBT, or when the temperature is set manually.
    private float temperature;
    private long lastUpdateTick;

    private ForgeItem(@Nullable NBTTagCompound nbt, IHeatRegistry heatRegistry)
    {
        steps = new ForgeSteps();

        this.meltingTemperature = heatRegistry.getMeltTemp();
        this.workingTemperature = heatRegistry.getWorkTemp();

        deserializeNBT(nbt);
    }

    public ForgeItem(@Nullable NBTTagCompound nbt, float workingTemperature, float meltingTemperature)
    {
        steps = new ForgeSteps();

        this.meltingTemperature = meltingTemperature;
        this.workingTemperature = workingTemperature;

        deserializeNBT(nbt);
    }

    public ForgeItem(@Nullable ItemStack stack, @Nullable NBTTagCompound nbt)
    {
        this(nbt, CapabilityForgeItem.getHeatRegistry(stack));
    }

    public ForgeItem(@Nullable NBTTagCompound nbt)
    {
        this(nbt, DEFAULT_WORK_TEMPERATURE, DEFAULT_MELT_TEMPERATURE);
    }

    public ForgeItem()
    {
        this(null, DEFAULT_WORK_TEMPERATURE, DEFAULT_MELT_TEMPERATURE);
    }

    @Override
    public int getWork()
    {
        return work;
    }

    @Override
    public void setWork(int work)
    {
        this.work = work;
    }

    @Override
    @Nullable
    public String getRecipeName()
    {
        return recipeName;
    }

    @Override
    public void setRecipe(@Nullable AnvilRecipe recipe)
    {
        recipeName = (recipe == null ? null : recipe.getName());
    }

    @Override
    @Nonnull
    public ForgeSteps getSteps()
    {
        return steps;
    }

    @Override
    public void addStep(ForgeStep step)
    {
        steps.addStep(step);
        work += step.getStepAmount();
    }

    @Override
    public void reset()
    {
        // Note: this will only reset the non-temperature part of this capability
        steps.reset();
        recipeName = null;
        work = 0;
    }

    @Override
    public float getTemperature()
    {
        if (lastUpdateTick == -1)
        {
            return 0;
        }
        else
        {
            final float newTemp = temperature - (float) (TickTimer.getTicks() - lastUpdateTick) * (float) ModConfig.BALANCE.temperatureModifier;
            return newTemp < 0 ? 0 : newTemp;
        }
    }

    @Override
    public void setTemperature(float temperature)
    {
        this.temperature = temperature;
        this.lastUpdateTick = TickTimer.getTicks();
    }

    @Override
    public float getMeltingTemperature()
    {
        return meltingTemperature;
    }

    @Override
    public float getWorkableTemperature()
    {
        return workingTemperature;
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing)
    {
        return capability == CapabilityForgeItem.CAPABILITY;
    }

    @Nullable
    @Override
    @SuppressWarnings("unchecked")
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing)
    {
        return hasCapability(capability, facing) ? (T) this : null;
    }

    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound nbt = new NBTTagCompound();

        nbt.setInteger("work", work);
        nbt.setTag("steps", steps.serializeNBT());
        if (recipeName != null)
        {
            nbt.setString("recipe", recipeName);
        }

        float temperature = getTemperature();
        if (temperature == 0)
        {
            nbt.setFloat("temp", 0);
            nbt.setLong("tick", -1);
        }
        else
        {
            nbt.setFloat("temp", temperature);
            nbt.setLong("tick", TickTimer.getTicks());
        }
        //TinkersForging.getLog().info("Serialization: {}", nbt.toString());
        return nbt;
    }

    @Override
    public void deserializeNBT(@Nullable NBTTagCompound nbt)
    {
        if (nbt != null)
        {
            //if (nbt.hasKey(CapabilityForgeItem.NBT_KEY))
            //{
            //    nbt = nbt.getCompoundTag(CapabilityForgeItem.NBT_KEY);
            //}

            work = nbt.getInteger("work");
            recipeName = nbt.hasKey("recipe") ? nbt.getString("recipe") : null; // stops defaulting to empty string
            steps.deserializeNBT(nbt.getCompoundTag("steps"));

            temperature = nbt.getFloat("temp");
            lastUpdateTick = nbt.getLong("tick");
        }
    }
}
