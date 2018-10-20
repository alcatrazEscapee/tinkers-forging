/*
 * Part of the Tinkers Forging Mod by alcatrazEscapee
 * Work under Copyright. Licensed under the GPL-3.0.
 * See the project LICENSE.md for more information.
 */

package com.alcatrazescapee.tinkersforging;

import net.minecraftforge.common.config.Config;

import static com.alcatrazescapee.tinkersforging.ModConstants.MOD_ID;

@Config(modid = MOD_ID, category = "")
@SuppressWarnings("WeakerAccess")
public final class ModConfig
{
    public static final GeneralConfig GENERAL = new GeneralConfig();
    public static final CompatConfig COMPAT = new CompatConfig();
    public static final ToolsConfig TOOLS = new ToolsConfig();

    public static class GeneralConfig
    {
        public boolean useTinkersConstruct = true;

        private GeneralConfig() {}
    }

    public static class CompatConfig
    {
        public boolean enableIron = true;
        public boolean enableGold = true;
        public boolean enableCopper = true;
        public boolean enableTin = true;
        public boolean enableBronze = true;
        public boolean enableSteel = true;
        public boolean enableSilver = true;
        public boolean enableLead = true;
        public boolean enableAluminium = true;
        public boolean enableAluminiumBrass = true;
        public boolean enableCobalt = true;
        public boolean enableArdite = true;
        public boolean enableManyullyn = true;

        private CompatConfig() {}
    }

    public static class ToolsConfig
    {
        public int tierIron = 2;
        public int tierGold = 1;
        public int tierCopper = 0;
        public int tierTin = 0;
        public int tierBronze = 1;
        public int tierSteel = 3;
        public int tierSilver = 1;
        public int tierLead = 0;
        public int tierAluminium = 0;
        public int tierAluminiumBrass = 1;
        public int tierCobalt = 3;
        public int tierArdite = 3;
        public int tierManyullyn = 4;

        //todo: tool tiers

        private ToolsConfig() {}
    }
}
