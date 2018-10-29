/*
 * Part of the Tinkers Forging Mod by alcatrazEscapee
 * Work under Copyright. Licensed under the GPL-3.0.
 * See the project LICENSE.md for more information.
 */

package com.alcatrazescapee.tinkersforging.common.recipe;

import java.util.Random;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.item.ItemStack;

import com.alcatrazescapee.alcatrazcore.inventory.recipe.RecipeCore;
import com.alcatrazescapee.tinkersforging.util.forge.ForgeRule;
import com.alcatrazescapee.tinkersforging.util.forge.ForgeSteps;

@ParametersAreNonnullByDefault
public class AnvilRecipe extends RecipeCore
{
    private static final Random RANDOM = new Random();

    private final ForgeRule[] rules;
    private final int minTier;

    private int workingSeed = 0;

    public AnvilRecipe(ItemStack outputStack, String inputOre, int inputAmount, int minTier, ForgeRule... rules)
    {
        super(outputStack, inputOre, inputAmount);

        this.rules = rules;
        this.minTier = minTier;
    }

    public AnvilRecipe(ItemStack outputStack, ItemStack inputStack, int minTier, ForgeRule... rules)
    {
        super(outputStack, inputStack);

        this.rules = rules;
        this.minTier = minTier;
    }

    @Override
    @Nonnull
    public String getName()
    {
        return outputStack.getDisplayName();
    }

    @Nonnull
    public ForgeRule[] getRules()
    {
        return rules;
    }

    public int getTier()
    {
        return minTier;
    }

    public int getWorkingTarget(long seed)
    {
        RANDOM.setSeed(seed + workingSeed);
        return 30 + RANDOM.nextInt(90);
    }

    public boolean stepsMatch(ForgeSteps steps)
    {
        for (ForgeRule rule : rules)
        {
            if (!rule.matches(steps))
                return false;
        }
        return true;
    }

    @Nonnull
    AnvilRecipe withSeed(int seed)
    {
        this.workingSeed = seed;
        return this;
    }

}
