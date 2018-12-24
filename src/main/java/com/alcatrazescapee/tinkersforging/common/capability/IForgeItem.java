/*
 * Part of the Tinkers Forging Mod by alcatrazEscapee
 * Work under Copyright. Licensed under the GPL-3.0.
 * See the project LICENSE.md for more information.
 */

package com.alcatrazescapee.tinkersforging.common.capability;

import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.client.resources.I18n;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.alcatrazescapee.tinkersforging.ModConfig;
import com.alcatrazescapee.tinkersforging.common.capability.heat.Heat;
import com.alcatrazescapee.tinkersforging.common.recipe.AnvilRecipe;
import com.alcatrazescapee.tinkersforging.util.forge.ForgeStep;
import com.alcatrazescapee.tinkersforging.util.forge.ForgeSteps;

import static com.alcatrazescapee.tinkersforging.TinkersForging.MOD_ID;

public interface IForgeItem extends INBTSerializable<NBTTagCompound>
{
    // Work / Forging Related

    int getWork();

    void setWork(int work);

    @Nullable
    String getRecipeName();

    void setRecipe(@Nullable AnvilRecipe recipe);

    @Nonnull
    ForgeSteps getSteps();

    void addStep(ForgeStep step);

    void reset();

    // Temperature Mechanics

    float getTemperature();

    void setTemperature(float temperature);

    float getMeltingTemperature();

    float getWorkableTemperature();

    default boolean isMolten()
    {
        return getMeltingTemperature() < getTemperature();
    }

    default boolean isWorkable()
    {
        return getWorkableTemperature() < getTemperature() && !isMolten();
    }

    @SideOnly(Side.CLIENT)
    default void addTooltipInfo(List<String> tooltips)
    {
        float temp = getTemperature();
        if (temp < 1f) return;

        String tooltip;
        if (ModConfig.BALANCE.enableAdvancedTemperatureTooltips)
        {
            // Temperature number
            tooltip = Heat.getColorFor(temp) + I18n.format(MOD_ID + ".tooltip.temperature_advanced", (int) temp);
            if (isWorkable())
            {
                // Danger / Workable tag
                if (temp > 0.8 * (getMeltingTemperature() - getWorkableTemperature()) + getWorkableTemperature())
                    tooltip += TextFormatting.WHITE + I18n.format(MOD_ID + ".tooltip.temperature_danger");
                else
                    tooltip += TextFormatting.WHITE + I18n.format(MOD_ID + ".tooltip.temperature_can_work");

                // Melts at number
                tooltip += I18n.format(MOD_ID + ".tooltip.temperature_melts_at_advanced", (int) getMeltingTemperature());
            }
            else
            {
                // Workable at number
                tooltip += TextFormatting.WHITE + I18n.format(MOD_ID + ".tooltip.temperature_works_at_advanced", (int) getWorkableTemperature());
            }
        }
        else
        {
            // Temperature color
            tooltip = Heat.getTooltipFor(temp);

            if (isWorkable())
            {
                // Danger / Workable tag
                if (temp > 0.8 * (getMeltingTemperature() - getWorkableTemperature()) + getWorkableTemperature())
                    tooltip += TextFormatting.WHITE + I18n.format(MOD_ID + ".tooltip.temperature_danger");
                else
                    tooltip += TextFormatting.WHITE + I18n.format(MOD_ID + ".tooltip.temperature_can_work");
            }
        }
        tooltips.add(tooltip);
    }
}
