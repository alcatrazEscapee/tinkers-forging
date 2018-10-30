/*
 * Part of the Tinkers Forging Mod by alcatrazEscapee
 * Work under Copyright. Licensed under the GPL-3.0.
 * See the project LICENSE.md for more information.
 */

package com.alcatrazescapee.tinkersforging.common.recipe;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.alcatrazescapee.alcatrazcore.inventory.recipe.IRecipeManager;
import com.alcatrazescapee.tinkersforging.TinkersForging;

public class AnvilRecipeManager implements IRecipeManager<AnvilRecipe>
{
    private final List<AnvilRecipe> recipes;
    private int seedCounter;

    AnvilRecipeManager()
    {
        recipes = new ArrayList<>();
        seedCounter = 0;
    }

    @Override
    public void add(AnvilRecipe recipe)
    {
        for (AnvilRecipe r : recipes)
        {
            if (r.getName().equals(recipe.getName()))
            {
                TinkersForging.getLog().warn("Duplicate recipe name '{}' found. This may cause problems!", recipe.getName());
                break;
            }
        }
        if (AnvilRecipe.assertValid(recipe))
        {
            recipes.add(recipe.withSeed(++seedCounter));
        }
    }

    @Override
    @Nonnull
    public List<AnvilRecipe> getAll()
    {
        return recipes;
    }

    @Nullable
    @Deprecated
    @Override
    public AnvilRecipe get(Object... inputs)
    {
        return null;
    }

    @Nullable
    @Override
    public AnvilRecipe get(Object input)
    {
        return recipes.stream().filter(x -> x.test(input)).findFirst().orElse(null);
    }

    @Deprecated
    @Override
    public void remove(Object... objects)
    {
    }

    @Override
    public void remove(Object input)
    {
        recipes.removeIf(x -> x.matches(input));
    }

    @Nullable
    public AnvilRecipe getByName(@Nullable String name)
    {
        return recipes.stream().filter(x -> x.getName().equals(name)).findFirst().orElse(null);
    }

    @Nullable
    public AnvilRecipe getPrevious(@Nullable AnvilRecipe recipe, Object input)
    {
        List<AnvilRecipe> list = getAllMatching(input);
        if (list.size() == 0)
            return null;

        int idx = list.indexOf(recipe);
        if (idx == -1)
            return recipe;
        else if (idx == 0)
            return list.get(list.size() - 1);
        else
            return list.get(idx - 1);
    }

    @Nullable
    public AnvilRecipe getNext(@Nullable AnvilRecipe recipe, Object input)
    {
        List<AnvilRecipe> list = getAllMatching(input);
        if (list.size() == 0)
            return null;

        int idx = list.indexOf(recipe);
        if (idx == -1)
            return recipe;
        else if (idx + 1 >= list.size())
            return list.get(0);
        else
            return list.get(idx + 1);
    }

    private List<AnvilRecipe> getAllMatching(Object input)
    {
        return recipes.stream().filter(x -> x.test(input)).collect(Collectors.toList());
    }
}
