/*
 * Part of the Tinkers Forging Mod by alcatrazEscapee
 * Work under Copyright. Licensed under the GPL-3.0.
 * See the project LICENSE.md for more information.
 */

package com.alcatrazescapee.tinkersforging.common.recipe;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.registries.IForgeRegistryModifiable;

import com.alcatrazescapee.alcatrazcore.inventory.crafting.InventoryCraftingEmpty;
import com.alcatrazescapee.alcatrazcore.util.CoreHelpers;
import com.alcatrazescapee.alcatrazcore.util.collections.ImmutablePair;
import com.alcatrazescapee.tinkersforging.ModConfig;
import com.alcatrazescapee.tinkersforging.ModConstants;
import com.alcatrazescapee.tinkersforging.common.blocks.BlockTinkersAnvil;
import com.alcatrazescapee.tinkersforging.common.items.ItemHammer;
import com.alcatrazescapee.tinkersforging.common.items.ItemToolHead;
import com.alcatrazescapee.tinkersforging.util.ItemType;
import com.alcatrazescapee.tinkersforging.util.Metal;

import static com.alcatrazescapee.alcatrazcore.util.OreDictionaryHelper.UPPER_UNDERSCORE_TO_LOWER_CAMEL;
import static com.alcatrazescapee.tinkersforging.ModConstants.MOD_ID;

@ParametersAreNonnullByDefault
public final class ModRecipes
{
    public static final AnvilRecipeManager ANVIL = new AnvilRecipeManager();
    private static final List<Runnable> CT_ACTIONS = new ArrayList<>();

    public static void init()
    {
        // Tool Head + Armor Recipes are registered during crafting recipe construction

        // Hammer Recipes
        for (Metal metal : Metal.values())
        {
            if (metal.isEnabled())
            {
                // Hammer Recipe!
                ItemStack output = ItemToolHead.get(ItemType.HAMMER_HEAD, metal, 1);
                String inputOre = UPPER_UNDERSCORE_TO_LOWER_CAMEL.convert("INGOT_" + metal.name());

                if (!output.isEmpty() && inputOre != null && OreDictionary.doesOreNameExist(inputOre))
                    ANVIL.add(new AnvilRecipe(output, inputOre, ItemType.HAMMER_HEAD.getAmount(), metal.getTier(), ItemType.HAMMER_HEAD.getRules()));
            }
        }

        if (Loader.isModLoaded("tconstruct"))
        {
            for (ItemType type : ItemType.values())
            {
                for (Metal metal : Metal.values())
                {
                    if (!metal.isTinkersMetal()) continue;
                    ItemStack output = getTinkersPartFor(type, metal);
                    String inputOre = UPPER_UNDERSCORE_TO_LOWER_CAMEL.convert("INGOT_" + metal.name());
                    if (!output.isEmpty() && inputOre != null && OreDictionary.doesOreNameExist(inputOre))
                        ANVIL.add(new AnvilRecipe(output, inputOre, type.getAmount(), metal.getTier(), type.getRules()));
                }
            }
        }
    }

    public static void postInit()
    {
        CT_ACTIONS.forEach(Runnable::run);
    }

    public static void addRecipeAction(Runnable action)
    {
        CT_ACTIONS.add(action);
    }

    public static void registerRecipes(RegistryEvent.Register<IRecipe> event)
    {
        IForgeRegistryModifiable<IRecipe> r = (IForgeRegistryModifiable<IRecipe>) event.getRegistry();
        Collection<IRecipe> recipes = r.getValuesCollection();

        for (Metal metal : Metal.values())
        {
            if (metal.isEnabled())
            {
                // Setup
                String metalIngotName = UPPER_UNDERSCORE_TO_LOWER_CAMEL.convert("INGOT_" + metal.name());
                String metalBlockName = UPPER_UNDERSCORE_TO_LOWER_CAMEL.convert("BLOCK_" + metal.name());
                if (metalIngotName == null || metalBlockName == null) continue;

                if (metal == Metal.DIAMOND && !OreDictionary.doesOreNameExist(metalIngotName))
                {
                    metalIngotName = "gemDiamond";
                }

                NonNullList<ItemStack> ingots = OreDictionary.getOres(metalIngotName, false);
                if (ingots.isEmpty()) continue;

                // Try with each ingot to create a tool of each type
                for (ItemType type : ItemType.values())
                {
                    // Ignore types that don't have an associated recipe
                    if (!type.isRecipeType()) continue;
                    // Ignore types that were not added due to Tinker Construct compat
                    if (type.isItemType() && ModConfig.GENERAL.useTinkersConstruct && Loader.isModLoaded("tconstruct"))
                        continue;

                    ImmutablePair<IRecipe, ItemStack> result = getToolRecipeFor(recipes, type, ingots);
                    if (result != null)
                    {
                        ResourceLocation loc = new ResourceLocation(MOD_ID, (metal.name() + "_" + type.name()).toLowerCase());
                        ItemStack output = (type.isArmorType()) ? result.getValue() : ItemToolHead.get(type, metal, 1);

                        // register the tool part recipe for non-armor types
                        if (!type.isArmorType())
                            r.register(new ShapedOreRecipe(loc, result.getValue(), "H", "S", 'S', "stickWood", 'H', output).setRegistryName(loc));

                        // register the anvil recipe
                        ANVIL.add(new AnvilRecipe(output.copy(), metalIngotName, type.getAmount(), metal.getTier(), type.getRules()));

                        // un-register the old recipe
                        if (ModConfig.GENERAL.removeCraftingRecipes)
                            r.remove(result.getKey().getRegistryName());
                    }
                }

                // Hammer Head -> Hammer recipes
                ItemStack hammer = ItemHammer.get(metal, 1);
                ItemStack hammerHead = ItemToolHead.get(ItemType.HAMMER_HEAD, metal, 1);
                if (!hammer.isEmpty() && !hammerHead.isEmpty())
                {
                    ResourceLocation loc = new ResourceLocation(MOD_ID, "hammer_" + metal.name().toLowerCase());
                    r.register(new ShapedOreRecipe(loc, hammer, "H", "S", 'S', "stickWood", 'H', hammerHead).setRegistryName(loc));
                }

                // Anvil recipes
                ItemStack anvil = BlockTinkersAnvil.get(metal, 1);
                if (!anvil.isEmpty())
                {
                    ResourceLocation loc = new ResourceLocation(MOD_ID, "anvil_" + metal.name().toLowerCase());
                    r.register(new ShapedOreRecipe(loc, anvil, "IBI", " I ", "III", 'B', metalBlockName, 'I', metalIngotName).setRegistryName(loc));
                }
            }
        }
    }

    @Nullable
    private static ImmutablePair<IRecipe, ItemStack> getToolRecipeFor(Collection<IRecipe> recipes, ItemType type, NonNullList<ItemStack> ingots)
    {
        ItemStack stick = new ItemStack(Items.STICK);
        InventoryCraftingEmpty tempCrafting = new InventoryCraftingEmpty(3, 3);
        for (ItemStack ingot : ingots)
        {
            if (!type.isArmorType())
            {
                tempCrafting.setInventorySlotContents(4, stick);
                tempCrafting.setInventorySlotContents(7, stick);
            }
            for (int pos : getIngotPositionsFor(type))
                tempCrafting.setInventorySlotContents(pos, ingot);

            //noinspection ConstantConditions
            IRecipe recipe = recipes.stream().filter(x -> x.matches(tempCrafting, null)).findFirst().orElse(null);
            if (recipe != null)
            {
                return ImmutablePair.of(recipe, recipe.getCraftingResult(tempCrafting));
            }
        }
        return null;
    }

    @Nonnull
    private static ItemStack getTinkersPartFor(ItemType type, Metal metal)
    {
        if (!type.isRecipeType() && type != ItemType.HAMMER_HEAD)
        {
            // Tinkers Tool
            String metalName = metal.name().toLowerCase();
            String toolName = type.name().toLowerCase().substring(3);

            ItemStack stack = CoreHelpers.getStackByRegistryName("tconstruct:" + toolName);
            if (!stack.isEmpty())
            {
                NBTTagCompound nbt = new NBTTagCompound();
                nbt.setString(ModConstants.MATERIAL_NBT_KEY, metalName);
                stack.setTagCompound(nbt);
            }
            return stack;
        }
        return ItemStack.EMPTY;
    }

    @Nonnull
    private static int[] getIngotPositionsFor(ItemType type)
    {
        /*
         *  Slots:
         *  0 1 2
         *  3 4 5
         *  6 7 8
         */
        switch (type)
        {
            case PICKAXE_HEAD:
                return new int[] {0, 1, 2};
            case SHOVEL_HEAD:
                return new int[] {1};
            case HOE_HEAD:
                return new int[] {1, 2};
            case SWORD_BLADE:
                return new int[] {1, 4};
            case AXE_HEAD:
                return new int[] {1, 2, 5};
            case HELMET:
            case HAMMER_HEAD:
                return new int[] {0, 1, 2, 3, 5};
            case CHESTPLATE:
                return new int[] {0, 2, 3, 4, 5, 6, 7, 8};
            case BOOTS:
                return new int[] {3, 5, 6, 8};
            case LEGGINGS:
                return new int[] {0, 1, 2, 3, 5, 6, 8};
            default:
                return new int[0];
        }
    }
}
