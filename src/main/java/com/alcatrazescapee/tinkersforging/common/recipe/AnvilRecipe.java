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
import net.minecraftforge.fml.common.network.ByteBufUtils;

import com.alcatrazescapee.alcatrazcore.inventory.recipe.RecipeCore;
import com.alcatrazescapee.tinkersforging.util.forge.ForgeRule;
import com.alcatrazescapee.tinkersforging.util.forge.ForgeSteps;
import io.netty.buffer.ByteBuf;

@ParametersAreNonnullByDefault
public class AnvilRecipe extends RecipeCore
{
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

    private AnvilRecipe(ItemStack outputStack, int minTier, ForgeRule... rules)
    {
        super(outputStack, ItemStack.EMPTY);

        this.minTier = minTier;
        this.rules = rules;
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

    @Nonnull
    AnvilRecipe withSeed(int seed)
    {
        this.workingSeed = seed;
        return this;
    }

}
