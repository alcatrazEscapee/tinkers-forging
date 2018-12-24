/*
 * Part of the Tinkers Forging Mod by alcatrazEscapee
 * Work under Copyright. Licensed under the GPL-3.0.
 * See the project LICENSE.md for more information.
 */

package com.alcatrazescapee.tinkersforging.integration;

import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import com.alcatrazescapee.tinkersforging.ModConfig;
import com.alcatrazescapee.tinkersforging.TinkersForging;
import com.alcatrazescapee.tinkersforging.common.blocks.ModBlocks;
import vazkii.patchouli.api.PatchouliAPI;
import vazkii.patchouli.api.PatchouliAPI.IPatchouliAPI;

import static com.alcatrazescapee.tinkersforging.TinkersForging.MOD_ID;
import static com.alcatrazescapee.tinkersforging.util.property.IBurnBlock.LIT;
import static com.alcatrazescapee.tinkersforging.util.property.IPileBlock.LAYERS;

public final class PatchouliIntegration
{
    private static IPatchouliAPI api = null;

    @Optional.Method(modid = "patchouli")
    public static void init()
    {
        // Register charcoal forge multiblock
        getAPI().registerMultiblock(new ResourceLocation(MOD_ID, "charcoal_forge"),
                getAPI().makeMultiblock(new String[][] {{"   ", " 0 ", "   "}, {" S ", "SCS", "_S_"}},
                        'S', getAPI().predicateMatcher(Blocks.STONE, x -> x.isNormalCube() && x.getMaterial() == Material.ROCK),
                        'C', getAPI().predicateMatcher(ModBlocks.CHARCOAL_FORGE.getStateWithLayers(6).withProperty(LIT, true), x -> x.getBlock() == ModBlocks.CHARCOAL_PILE && x.getValue(LAYERS) >= 6),
                        ' ', getAPI().anyMatcher(),
                        '0', getAPI().anyMatcher())).setSymmetrical(true);

        // Set config flags
        getAPI().setConfigFlag(MOD_ID + ":tool_parts", !Loader.isModLoaded("tconstruct") || !ModConfig.GENERAL.useTinkersConstruct);
        getAPI().setConfigFlag(MOD_ID + ":tooltip_advanced", ModConfig.BALANCE.enableAdvancedTemperatureTooltips);
    }

    @Optional.Method(modid = "patchouli")
    public static void resetTooltipFlag()
    {
        getAPI().setConfigFlag(MOD_ID + ":tooltip_advanced", ModConfig.BALANCE.enableAdvancedTemperatureTooltips);
        getAPI().reloadBookContents();
    }

    @Optional.Method(modid = "patchouli")
    public static void registerRecipes(RegistryEvent.Register<IRecipe> event)
    {
        ResourceLocation loc = new ResourceLocation(MOD_ID, "guide_book");
        ItemStack output = getAPI().getBookStack(MOD_ID + ":book");
        event.getRegistry().register(new ShapelessOreRecipe(loc, output, Items.BOOK, "hammer").setRegistryName(loc));
    }

    private static IPatchouliAPI getAPI()
    {
        if (api == null)
        {
            api = PatchouliAPI.instance;
            if (api.isStub())
            {
                TinkersForging.getLog().warn("Failed to intercept Patchouli API. Problems may occur");
            }
        }
        return api;
    }

}
