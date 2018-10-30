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
    public static final ToolsConfig TOOLS = new ToolsConfig();

    public static class GeneralConfig
    {
        @Config.RequiresMcRestart
        @Config.Comment({"Should this mod default to using Tinker's Construct metals, if they are enabled?",
                "If true and Tinker's Construct is found, no new tool parts will be registered, and all tool part recipes will use Tinker's Materials",
                "If false, or if Tinker's Construct is not found, Tinker's Forging will use its own tool parts for recipes."})
        public boolean useTinkersConstruct = true;

        @Config.RequiresMcRestart
        @Config.Comment("If this is true, you will not be able to use a lower tier anvil to make a higher tier item. Tiers of different anvils types are configurable.")
        public boolean respectTiers = true;

        @Config.RequiresMcRestart
        @Config.Comment("If this is true, any recipes that are added to the anvil that have a crafting equivalent (i.e. a shovel) will have their normal crafting recipes removed")
        public boolean removeCraftingRecipes = true;

        private GeneralConfig() {}
    }

    public static class ToolsConfig
    {
        @Config.Comment("Tier of the Iron Anvil")
        @Config.RangeInt(min = 0, max = 5)
        public int tierIron = 2;

        @Config.Comment("Tier of the Gold Anvil")
        @Config.RangeInt(min = 0, max = 5)
        public int tierGold = 1;

        @Config.Comment("Tier of the Copper Anvil (if enabled)")
        @Config.RangeInt(min = 0, max = 5)
        public int tierCopper = 0;

        @Config.Comment("Tier of the Tin Anvil (if enabled)")
        @Config.RangeInt(min = 0, max = 5)
        public int tierTin = 0;

        @Config.Comment("Tier of the Bronze Anvil (if enabled)")
        @Config.RangeInt(min = 0, max = 5)
        public int tierBronze = 1;

        @Config.Comment("Tier of the Steel Anvil (if enabled)")
        @Config.RangeInt(min = 0, max = 5)
        public int tierSteel = 3;

        @Config.Comment("Tier of the Silver Anvil (if enabled)")
        @Config.RangeInt(min = 0, max = 5)
        public int tierSilver = 1;

        @Config.Comment("Tier of the Lead Anvil (if enabled)")
        @Config.RangeInt(min = 0, max = 5)
        public int tierLead = 0;

        @Config.Comment("Tier of the Aluminium Anvil (if enabled)")
        @Config.RangeInt(min = 0, max = 5)
        public int tierAluminium = 0;

        @Config.Comment("Tier of the Cobalt Anvil (if enabled)")
        @Config.RangeInt(min = 0, max = 5)
        public int tierCobalt = 3;

        @Config.Comment("Tier of the Ardite Anvil (if enabled)")
        @Config.RangeInt(min = 0, max = 5)
        public int tierArdite = 3;

        @Config.Comment("Tier of the Manyullyn Anvil (if enabled)")
        @Config.RangeInt(min = 0, max = 5)
        public int tierManyullyn = 4;

        @Config.Comment("Tier of the Diamond Anvil")
        @Config.RangeInt(min = 0, max = 5)
        public int tierDiamond = 3;

        @Config.Comment("Tier of the Invar Anvil (if enabled)")
        @Config.RangeInt(min = 0, max = 5)
        public int tierInvar = 3;

        @Config.Comment("Tier of the Brass Anvil (if enabled)")
        @Config.RangeInt(min = 0, max = 5)
        public int tierBrass = 1;

        @Config.Comment("Tier of the Mithril Anvil (if enabled)")
        @Config.RangeInt(min = 0, max = 5)
        public int tierMithril = 4;

        @Config.Comment("Tier of the Electrum Anvil (if enabled)")
        @Config.RangeInt(min = 0, max = 5)
        public int tierElectrum = 1;

        @Config.Comment("Tier of the Pig Iron  Anvil (if enabled)")
        @Config.RangeInt(min = 0, max = 5)
        public int tierPigiron = 2;

        private ToolsConfig() {}
    }
}
