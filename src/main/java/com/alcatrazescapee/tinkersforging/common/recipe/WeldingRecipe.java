/*
 * Part of the Tinkers Forging Mod by alcatrazEscapee
 * Work under Copyright. Licensed under the GPL-3.0.
 * See the project LICENSE.md for more information.
 */

package com.alcatrazescapee.tinkersforging.common.recipe;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.item.ItemStack;

import com.alcatrazescapee.alcatrazcore.inventory.ingredient.IRecipeIngredient;
import com.alcatrazescapee.alcatrazcore.inventory.recipe.RecipeCore;

@ParametersAreNonnullByDefault
public class WeldingRecipe extends RecipeCore
{
    public WeldingRecipe(ItemStack outputStack, String oreInput)
    {
        super(outputStack, oreInput, 1);
    }

    @Override
    public boolean test(Object input)
    {
        return false;
    }

    @Override
    public boolean test(Object... inputs)
    {
        if (inputs.length == 2)
        {
            return ingredient.test(inputs[0]) && ingredient.test(inputs[1]);
        }
        return false;
    }

    @Override
    public boolean matches(Object input)
    {
        return false;
    }

    @Override
    public boolean matches(Object... inputs)
    {
        if (inputs.length == 2)
        {
            if (inputs[0] instanceof IRecipeIngredient && inputs[1] instanceof IRecipeIngredient)
            {
                return ingredient.matches((IRecipeIngredient) inputs[0]) && ingredient.matches((IRecipeIngredient) inputs[1]);
            }
        }
        return false;
    }
}
