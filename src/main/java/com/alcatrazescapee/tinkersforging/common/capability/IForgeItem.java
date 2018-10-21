/*
 * Part of the Tinkers Forging Mod by alcatrazEscapee
 * Work under Copyright. Licensed under the GPL-3.0.
 * See the project LICENSE.md for more information.
 */

package com.alcatrazescapee.tinkersforging.common.capability;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

import com.alcatrazescapee.tinkersforging.common.recipe.AnvilRecipe;
import com.alcatrazescapee.tinkersforging.util.forge.ForgeStep;
import com.alcatrazescapee.tinkersforging.util.forge.ForgeSteps;

public interface IForgeItem extends INBTSerializable<NBTTagCompound>
{
    int getWork();

    void setWork(int work);

    @Nullable
    String getRecipeName();

    void setRecipe(@Nullable AnvilRecipe recipe);

    @Nonnull
    ForgeSteps getSteps();

    void addStep(ForgeStep step);
}
