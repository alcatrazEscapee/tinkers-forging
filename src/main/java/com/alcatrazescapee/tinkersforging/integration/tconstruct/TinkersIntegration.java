/*
 * Part of the Tinkers Forging Mod by alcatrazEscapee
 * Work under Copyright. Licensed under the GPL-3.0.
 * See the project LICENSE.md for more information.
 */

package com.alcatrazescapee.tinkersforging.integration.tconstruct;

import net.minecraftforge.fml.common.Optional;

import slimeknights.tconstruct.common.config.Config;

import static com.alcatrazescapee.tinkersforging.TinkersForging.MOD_ID;

public final class TinkersIntegration
{
    private static final String TILE_TINKERS_ANVIL = MOD_ID + ":tinkers_anvil";

    @Optional.Method(modid = "tconstruct")
    public static void init()
    {
        updateSharedConfig();
    }

    @Optional.Method(modid = "tconstruct")
    public static void updateSharedConfig()
    {
        // Hey Tinkers Construct, stop trying to access the tinker's anvil display inventory slot!
        Config.craftingStationBlacklist.add(TILE_TINKERS_ANVIL);
    }
}
