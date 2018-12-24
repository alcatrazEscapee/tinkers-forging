package com.alcatrazescapee.tinkersforging.common.capability.heat;

import net.minecraft.item.ItemStack;

import com.alcatrazescapee.alcatrazcore.inventory.ingredient.IRecipeIngredient;

public class IngredientHeatRegistry implements IHeatRegistry
{
    private final IRecipeIngredient ingredient;
    private final float workTemp, meltTemp;

    public IngredientHeatRegistry(IRecipeIngredient ingredient, float workTemp, float meltTemp)
    {
        this.ingredient = ingredient;
        this.workTemp = workTemp;
        this.meltTemp = meltTemp;
    }

    @Override
    public boolean test(ItemStack stack)
    {
        return ingredient.testIgnoreCount(stack);
    }

    @Override
    public float getMeltTemp()
    {
        return meltTemp;
    }

    @Override
    public float getWorkTemp()
    {
        return workTemp;
    }
}
