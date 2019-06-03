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
import net.minecraft.util.StringUtils;
import net.minecraftforge.fml.common.network.ByteBufUtils;

import com.alcatrazescapee.alcatrazcore.inventory.recipe.RecipeCore;
import com.alcatrazescapee.alcatrazcore.util.CoreHelpers;
import com.alcatrazescapee.tinkersforging.ModConfig;
import com.alcatrazescapee.tinkersforging.TinkersForging;
import com.alcatrazescapee.tinkersforging.common.capability.IForgeItem;
import com.alcatrazescapee.tinkersforging.util.forge.ForgeRule;
import com.alcatrazescapee.tinkersforging.util.forge.ForgeSteps;
import io.netty.buffer.ByteBuf;

@ParametersAreNonnullByDefault
public class AnvilRecipe extends RecipeCore
{
    static boolean assertValid(AnvilRecipe recipe)
    {
        if (StringUtils.isNullOrEmpty(recipe.recipeName))
        {
            TinkersForging.getLog().warn("Recipe is invalid with empty name");
            return false;
        }
        if (recipe.outputStack.isEmpty())
        {
            TinkersForging.getLog().warn("Output is empty!");
            return false;
        }
        if (recipe.rules.length == 0 || recipe.rules.length > 3)
        {
            TinkersForging.getLog().warn("Rules are invalid length!");
            return false;
        }
        return true;
    }

    public static AnvilRecipe fromSerialized(ByteBuf buffer)
    {
        int minTier = buffer.readInt();
        int seed = buffer.readInt();

        ItemStack output = ByteBufUtils.readItemStack(buffer);

        int numRules = buffer.readInt();
        ForgeRule[] rules = new ForgeRule[numRules];
        for (int i = 0; i < numRules; i++)
        {
            rules[i] = ForgeRule.valueOf(buffer.readInt());
        }

        return new AnvilRecipe(output, minTier, rules).withSeed(seed);
    }

    private static final Random RANDOM = new Random();

    private final ForgeRule[] rules;
    private final int minTier;
    private final String recipeName;

    private int workingSeed = 0;

    public AnvilRecipe(ItemStack outputStack, String inputOre, int inputAmount, int minTier, ForgeRule... rules)
    {
        super(outputStack, inputOre, inputAmount);

        this.rules = rules;
        this.minTier = minTier;
        this.recipeName = outputStack.serializeNBT().toString();
    }

    public AnvilRecipe(ItemStack outputStack, ItemStack inputStack, int minTier, ForgeRule... rules)
    {
        super(outputStack, inputStack);

        this.rules = rules;
        this.minTier = ModConfig.GENERAL.respectTiers ? minTier : Integer.MIN_VALUE;
        this.recipeName = outputStack.serializeNBT().toString();
    }

    private AnvilRecipe(ItemStack outputStack, int minTier, ForgeRule... rules)
    {
        // Only created on client
        super(outputStack, ItemStack.EMPTY);

        this.minTier = ModConfig.GENERAL.respectTiers ? minTier : Integer.MIN_VALUE;
        this.rules = rules;
        this.recipeName = "client:" + outputStack.serializeNBT().toString();
    }

    @Override
    @Nonnull
    public String getName()
    {
        return recipeName;
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
        int target = 32 + RANDOM.nextInt(IForgeItem.MAX_WORK - 64);
        if (target == IForgeItem.DEFAULT_WORK)
        {
            target += 20 * (RANDOM.nextBoolean() ? -1 : 1);
        }
        return target;
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

    public void serialize(ByteBuf buffer)
    {
        // Numbers
        buffer.writeInt(minTier);
        buffer.writeInt(workingSeed);

        // Output
        ByteBufUtils.writeItemStack(buffer, outputStack);

        // Rules
        buffer.writeInt(rules.length);
        for (ForgeRule rule : rules)
            buffer.writeInt(ForgeRule.getID(rule));
    }

    public boolean matchesOutput(ItemStack output)
    {
        return CoreHelpers.doStacksMatch(outputStack, output);
    }

    @Nonnull
    AnvilRecipe withSeed(int seed)
    {
        this.workingSeed = seed;
        return this;
    }

}
