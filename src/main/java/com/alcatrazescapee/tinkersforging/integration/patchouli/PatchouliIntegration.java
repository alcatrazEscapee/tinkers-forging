/*
 * Part of the Tinkers Forging Mod by alcatrazEscapee
 * Work under Copyright. Licensed under the GPL-3.0.
 * See the project LICENSE.md for more information.
 */

package com.alcatrazescapee.tinkersforging.integration.patchouli;

import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Optional;

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
    private static IPatchouliAPI api;

    @Optional.Method(modid = "patchouli")
    public static void init()
    {
        api = PatchouliAPI.instance;
        if (api.isStub())
        {
            TinkersForging.getLog().info("Failed to intercept Patchouli API. Book may be questionable");
            return;
        }

        // Register charcoal forge multiblock
        api.registerMultiblock(new ResourceLocation(MOD_ID, "charcoal_forge"),
                api.makeMultiblock(new String[][] {{"   ", " 0 ", "   "}, {" S ", "SCS", "_S_"}},
                        'S', api.predicateMatcher(Blocks.STONE, x -> x.isNormalCube() && x.getMaterial() == Material.ROCK),
                        'C', api.predicateMatcher(ModBlocks.CHARCOAL_FORGE.getStateWithLayers(6).withProperty(LIT, true), x -> x.getBlock() == ModBlocks.CHARCOAL_PILE && x.getValue(LAYERS) >= 6),
                        ' ', api.anyMatcher(),
                        '0', api.anyMatcher())).setSymmetrical(true);

        // Set config flags
        api.setConfigFlag(MOD_ID + ":tool_parts", !Loader.isModLoaded("tconstruct") || !ModConfig.GENERAL.useTinkersConstruct);
        api.setConfigFlag(MOD_ID + ":tinkers_construct", Loader.isModLoaded("tconstruct"));
        api.setConfigFlag(MOD_ID + ":construct_armory", Loader.isModLoaded("conarm"));
        api.setConfigFlag(MOD_ID + ":tooltip_advanced", ModConfig.GENERAL.enableAdvancedTemperatureTooltips);
    }

    @Optional.Method(modid = "patchouli")
    public static void resetTooltipFlag()
    {
        if (api != null && !api.isStub())
        {
            api.setConfigFlag(MOD_ID + ":tooltip_advanced", ModConfig.GENERAL.enableAdvancedTemperatureTooltips);
            api.reloadBookContents();
        }
    }

}
