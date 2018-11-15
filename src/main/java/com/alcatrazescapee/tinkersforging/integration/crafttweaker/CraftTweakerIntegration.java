/*
 * Part of the Tinkers Forging Mod by alcatrazEscapee
 * Work under Copyright. Licensed under the GPL-3.0.
 * See the project LICENSE.md for more information.
 */

package com.alcatrazescapee.tinkersforging.integration.crafttweaker;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;

import com.alcatrazescapee.alcatrazcore.inventory.ingredient.IRecipeIngredient;
import com.alcatrazescapee.tinkersforging.TinkersForging;
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
@ZenClass("mods.tinkersforging.Anvil")
public final class CraftTweakerIntegration
{
    @ZenMethod
    public static void add(IIngredient input, IItemStack output, int tier, String... ruleNames)
    {
        AnvilRecipe recipe;
        ItemStack outputStack = toStack(output);
        List<ForgeRule> rules = new ArrayList<>(ruleNames.length);
        for (String ruleName : ruleNames)
        {
            try
            {
                ForgeRule rule = ForgeRule.valueOf(ruleName.toUpperCase());
                rules.add(rule);
            }
            catch (IllegalArgumentException e)
            {
                TinkersForging.getLog().warn("Illegal rule name {} specified in craft tweaker recipe!", ruleName);
            }
        }
        if (input instanceof IOreDictEntry)
        {
            IOreDictEntry ore = (IOreDictEntry) input;
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
                return "Adding Anvil recipe for " + recipe.getName();
            }
        });
    }

    @ZenMethod
    public static void remove(IItemStack output)
    {
        ItemStack stack = toStack(output);
        CraftTweakerAPI.apply(new IAction()
        {
            @Override
            public void apply()
            {
                ModRecipes.addRecipeAction(() -> ModRecipes.ANVIL.remove(IRecipeIngredient.of(stack)));
            }

            @Override
            public String describe()
            {
                return "Removing Fire pit recipe for " + stack.getDisplayName();
            }
        });
    }


    @Nonnull
    private static ItemStack toStack(IIngredient ingredient)
    {
        if (!(ingredient instanceof IItemStack))
            return ItemStack.EMPTY;
        Object obj = ingredient.getInternal();
        return obj instanceof ItemStack ? (ItemStack) obj : ItemStack.EMPTY;
    }
}
