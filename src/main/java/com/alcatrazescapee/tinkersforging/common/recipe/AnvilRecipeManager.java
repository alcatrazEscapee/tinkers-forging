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

public class AnvilRecipeManager implements IRecipeManager<AnvilRecipe>
{
    private List<AnvilRecipe> recipes;
    private int seedCounter;

    public AnvilRecipeManager()
    {
        recipes = new ArrayList<>();
        seedCounter = 0;
    }

    @Override
    public void add(AnvilRecipe recipe)
    {
        recipes.add(recipe.withSeed(++seedCounter));
    }

    @Override
    @Nonnull
    public List<AnvilRecipe> getAll()
    {
        return recipes;
    }

    @Nullable
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

    @Override
    public void remove(Object... inputs)
    {
        for (Object input : inputs) remove(input);
    }

    @Override
    public void remove(Object input)
    {
        recipes.removeIf(x -> x.matches(input));
    }

    @Nullable
    public AnvilRecipe getByName(Object input, String name)
    {
        return recipes.stream().filter(x -> x.test(input) && x.getName().equals(name)).findFirst().orElse(null);
    }

    @Nullable
    public AnvilRecipe getPrevious(@Nullable AnvilRecipe recipe, Object input)
    {
        List<AnvilRecipe> list = getAllMatching(input);
        int idx = list.indexOf(recipe);
        if (list.size() == 0)
            return null;
        else if (idx == -1)
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
        int idx = list.indexOf(recipe);
        if (list.size() == 0)
            return null;
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
