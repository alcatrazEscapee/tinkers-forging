/*
 * Part of the Tinkers Forging Mod by alcatrazEscapee
 * Work under Copyright. Licensed under the GPL-3.0.
 * See the project LICENSE.md for more information.
 */

package com.alcatrazescapee.tinkersforging.integration;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;

import com.alcatrazescapee.alcatrazcore.inventory.ingredient.IRecipeIngredient;
import com.alcatrazescapee.tinkersforging.TinkersForging;
import com.alcatrazescapee.tinkersforging.common.capability.CapabilityForgeItem;
import com.alcatrazescapee.tinkersforging.common.recipe.AnvilRecipe;
import com.alcatrazescapee.tinkersforging.common.recipe.ModRecipes;
import com.alcatrazescapee.tinkersforging.util.forge.ForgeRule;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.oredict.IOreDictEntry;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@SuppressWarnings("unused")
@ZenClass("mods.TinkersForging.Anvil")
public final class CraftTweakerIntegration
{
    @ZenMethod
    public static void addRecipe(final IIngredient input, final IItemStack output, final int tier, final String... ruleNames)
    {
        final AnvilRecipe recipe;
        final ItemStack outputStack = toStack(output);
        final List<ForgeRule> rules = new ArrayList<>(ruleNames.length);
        for (String ruleName : ruleNames)
        {
            try
            {
                final ForgeRule rule = ForgeRule.valueOf(ruleName.toUpperCase());
                rules.add(rule);
            }
            catch (IllegalArgumentException e)
            {
                TinkersForging.getLog().warn("Illegal rule name {} specified in craft tweaker recipe!", ruleName);
                return;
            }
        }
        if (rules.size() <= 0 || rules.size() > 3)
        {
            TinkersForging.getLog().warn("Illegal number of rules {} specified in craft tweaker recipe!", rules.size());
            return;
        }
        if (input instanceof IOreDictEntry)
        {
            final IOreDictEntry ore = (IOreDictEntry) input;
            recipe = new AnvilRecipe(outputStack, ore.getName(), ore.getAmount(), tier, rules.toArray(new ForgeRule[0]));
        }
        else
        {
            recipe = new AnvilRecipe(outputStack, toStack(input), tier, rules.toArray(new ForgeRule[0]));
        }
        CraftTweakerAPI.apply(new IAction()
        {
            @Override
            public void apply()
            {
                ModRecipes.addRecipeAction(() -> ModRecipes.ANVIL.add(recipe));
            }

            @Override
            public String describe()
            {
                return "Adding Anvil recipe for " + recipe.getName() + "\n";
            }
        });
    }

    @ZenMethod
    public static void removeRecipe(final IItemStack output)
    {
        final ItemStack stack = toStack(output);
        CraftTweakerAPI.apply(new IAction()
        {
            @Override
            public void apply()
            {
                ModRecipes.addRecipeAction(() -> ModRecipes.ANVIL.remove(stack));
            }

            @Override
            public String describe()
            {
                return "Removing Anvil recipe for " + stack.getDisplayName() + "\n";
            }
        });
    }

    @ZenMethod
    public static void addItemHeat(final IIngredient input, final int workingTemperature, final int meltingTemperature)
    {
        final IRecipeIngredient ingredient;
        if (input instanceof IOreDictEntry)
        {
            final IOreDictEntry ore = (IOreDictEntry) input;
            ingredient = IRecipeIngredient.of(ore.getName());
        }
        else
        {
            ingredient = IRecipeIngredient.of(toStack(input));
        }
        CraftTweakerAPI.apply(new IAction()
        {
            @Override
            public void apply()
            {
                CapabilityForgeItem.registerStackCapability(ingredient, (float) workingTemperature, (float) meltingTemperature);
            }

            @Override
            public String describe()
            {
                return "Adding heat registry for " + ingredient.getName() + "\n";
            }
        });
    }

    @Nonnull
    private static ItemStack toStack(final IIngredient ingredient)
    {
        if (!(ingredient instanceof IItemStack))
            return ItemStack.EMPTY;
        final Object obj = ingredient.getInternal();
        return obj instanceof ItemStack ? (ItemStack) obj : ItemStack.EMPTY;
    }
}
