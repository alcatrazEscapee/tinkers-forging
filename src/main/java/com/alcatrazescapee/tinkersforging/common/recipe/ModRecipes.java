/*
 * Part of the Tinkers Forging Mod by alcatrazEscapee
 * Work under Copyright. Licensed under the GPL-3.0.
 * See the project LICENSE.md for more information.
 */

package com.alcatrazescapee.tinkersforging.common.recipe;

import java.util.Collection;
import javax.annotation.Nullable;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.registries.IForgeRegistryModifiable;

import com.alcatrazescapee.alcatrazcore.inventory.crafting.InventoryCraftingEmpty;
import com.alcatrazescapee.alcatrazcore.inventory.recipe.IRecipeManager;
import com.alcatrazescapee.alcatrazcore.inventory.recipe.RecipeManager;
import com.alcatrazescapee.tinkersforging.ModConstants;
import com.alcatrazescapee.tinkersforging.common.items.ItemToolHead;
import com.alcatrazescapee.tinkersforging.util.ItemType;
import com.alcatrazescapee.tinkersforging.util.Metal;

import static com.alcatrazescapee.tinkersforging.ModConstants.MOD_ID;
import static com.alcatrazescapee.tinkersforging.ModConstants.ORE_DICT_CONVERTER;

public final class ModRecipes
{
    public static final AnvilRecipeManager ANVIL = new AnvilRecipeManager();
    public static final IRecipeManager<WeldingRecipe> WELDING = new RecipeManager<>();

    public static void init()
    {
        // todo: register forging recipes

        for (ItemType type : ItemType.values())
        {
            for (Metal metal : Metal.values())
            {
                ANVIL.add(new AnvilRecipe(ItemToolHead.get(type, metal, 1), ORE_DICT_CONVERTER.convert("INGOT_" + metal.name()), 1, type.getRules()));
            }
        }

        // todo: register welding recipes

        WELDING.add(new WeldingRecipe(new ItemStack(Items.FLINT_AND_STEEL), "ingotIron"));

        // todo: tcon integration for recipes
    }

    public static void postInit()
    {
        // todo: craft tweaker
    }

    public static void registerRecipes(RegistryEvent.Register<IRecipe> event)
    {
        IForgeRegistryModifiable<IRecipe> r = (IForgeRegistryModifiable<IRecipe>) event.getRegistry();
        Collection<IRecipe> recipes = r.getValuesCollection();

        InventoryCraftingEmpty tempCrafting = new InventoryCraftingEmpty(3, 3);

        for (Metal metal : Metal.values())
        {
            if (metal.isEnabled())
            {
                NonNullList<ItemStack> ingots = OreDictionary.getOres(ModConstants.ORE_DICT_CONVERTER.convert("INGOT_" + metal.name()), false);

                // Try with each ingot to create a tool of each type
                for (ItemType type : ItemType.values())
                {
                    IRecipe recipe = getToolRecipeFor(recipes, tempCrafting, type, ingots);
                    if (recipe != null)
                    {
                        ItemStack result = recipe.getCraftingResult(tempCrafting);

                        ResourceLocation loc = new ResourceLocation(MOD_ID, (metal.name() + "_" + type.name()).toLowerCase());
                        r.register(new ShapedOreRecipe(loc, result, "H", "S", 'S', "stickWood", 'H', ItemToolHead.get(type, metal)).setRegistryName(loc));
                    }
                }

            }
        }
    }

    @Nullable
    private static IRecipe getToolRecipeFor(Collection<IRecipe> recipes, InventoryCraftingEmpty tempCrafting, ItemType type, NonNullList<ItemStack> ingots)
    {
        ItemStack stick = new ItemStack(Items.STICK);
        for (ItemStack ingot : ingots)
        {
            type.setCraftingPattern(tempCrafting, ingot);
            tempCrafting.setInventorySlotContents(4, stick);
            tempCrafting.setInventorySlotContents(7, stick);

            //noinspection ConstantConditions
            IRecipe recipe = recipes.stream().filter(x -> x.matches(tempCrafting, null)).findFirst().orElse(null);
            if (recipe != null)
            {
                return recipe;
            }
        }
        return null;
    }
}
