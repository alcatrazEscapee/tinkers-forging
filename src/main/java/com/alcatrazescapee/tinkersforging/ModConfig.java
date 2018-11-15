/*
 * Part of the Tinkers Forging Mod by alcatrazEscapee
 * Work under Copyright. Licensed under the GPL-3.0.
 * See the project LICENSE.md for more information.
 */

package com.alcatrazescapee.tinkersforging;

import net.minecraftforge.common.config.Config;

import static com.alcatrazescapee.tinkersforging.TinkersForging.MOD_ID;

@Config(modid = MOD_ID, category = "")
@SuppressWarnings("WeakerAccess")
public final class ModConfig
{
    public static final GeneralConfig GENERAL = new GeneralConfig();
    public static final ToolsConfig TOOLS = new ToolsConfig();

    public static class GeneralConfig
    {
        @Config.Name("Tinker's Construct Compat")
        @Config.RequiresMcRestart
        @Config.Comment({"Should this mod default to using Tinker's Construct tool parts, if it is enabled?",
                "If true and Tinker's Construct is found, no new tool parts will be registered, and all tool part recipes will use Tinker's parts",
                "If false, or if Tinker's Construct is not found, Tinker's Forging will use its own tool parts for recipes."})
        public boolean useTinkersConstruct = true;

        @Config.Name("Construct's Armory Compat")
        @Config.RequiresMcRestart
        @Config.Comment("Should this mod add recipes for Construct's Armory's armor parts, if it is enabled?")
        public boolean useConstructsArmory = true;

        @Config.Name("Respect Tiers")
        @Config.RequiresMcRestart
        @Config.Comment("If this is true, you will not be able to use a lower tier anvil to make a higher tier item. Tiers of different anvils types are configurable.")
        public boolean respectTiers = true;

        @Config.Name("Remove Crafting Recipes")
        @Config.RequiresMcRestart
        @Config.Comment("If this is true, any recipes that are added to the anvil that have a crafting equivalent (i.e. a shovel) will have their normal crafting recipes removed")
        public boolean removeCraftingRecipes = true;

        @Config.Name("Enable Advanced Temperature Tooltips")
        @Config.Comment("If this is true, you will be able to see the exact temperature (in °C) of any items - including their exact workable and melting temperatures")
        public boolean enableAdvancedTemperatureTooltips = false;

        @Config.Name("General Temperature Modifier")
        @Config.RangeDouble(min = 0, max = 10)
        @Config.Comment("A modifier for how fast items heat up and cool down. Higher values = faster heat transfer.")
        public double temperatureModifier = 0.4;

        @Config.Name("Charcoal Forge Temperature Modifier")
        @Config.RangeDouble(min = 0, max = 10)
        @Config.Comment("The modifier for how effective the charcoal forge is at heating up items. Higher values = faster heating")
        public double charcoalForgeTemperatureModifier = 0.8;

        @Config.Name("Charcoal Forge Fuel Modifier")
        @Config.RangeDouble(min = 0.1, max = 10)
        @Config.Comment("How long a single piece of charcoal lasts in the charcoal forge, relative to in a furnace. Higher values = fuel lasts longer")
        public double charcoalForgeFuelModifier = 1.0;

        @Config.Name("Brick Forge Temperature Modifier")
        @Config.RangeDouble(min = 0, max = 10)
        @Config.Comment("The modifier for how effective the brick forge is at heating up items. Higher values = faster heating")
        public double forgeTemperatureModifier = 1.4;

        @Config.Name("Brick Forge Fuel Multiplier")
        @Config.RangeDouble(min = 0.1, max = 10)
        @Config.Comment("The modifier for how fuel efficient the brick forge is, relative to a furnace. Higher values = fuel lasts longer")
        public double forgeFuelModifier = 1.0;

        private GeneralConfig() {}
    }

    public static class ToolsConfig
    {
        @Config.Comment("Tier of the Copper Anvil (if enabled)")
        @Config.RangeInt(min = 0, max = 5)
        @Config.Name("Tier - Copper")
        public int tierCopper = 0;

        @Config.Comment("Tier of the Tin Anvil (if enabled)")
        @Config.RangeInt(min = 0, max = 5)
        @Config.Name("Tier - Tin")
        public int tierTin = 0;

        @Config.Comment("Tier of the Bronze Anvil (if enabled)")
        @Config.RangeInt(min = 0, max = 5)
        @Config.Name("Tier - Bronze")
        public int tierBronze = 1;

        @Config.Comment("Tier of the Steel Anvil (if enabled)")
        @Config.RangeInt(min = 0, max = 5)
        @Config.Name("Tier - Steel")
        public int tierSteel = 3;

        @Config.Comment("Tier of the Silver Anvil (if enabled)")
        @Config.RangeInt(min = 0, max = 5)
        @Config.Name("Tier - Silver")
        public int tierSilver = 1;

        @Config.Comment("Tier of the Lead Anvil (if enabled)")
        @Config.RangeInt(min = 0, max = 5)
        @Config.Name("Tier - Lead")
        public int tierLead = 0;

        @Config.Comment("Tier of the Aluminium Anvil (if enabled)")
        @Config.RangeInt(min = 0, max = 5)
        @Config.Name("Tier - Aluminium")
        public int tierAluminium = 0;

        @Config.Comment("Tier of the Cobalt Anvil (if enabled)")
        @Config.RangeInt(min = 0, max = 5)
        @Config.Name("Tier - Cobalt")
        public int tierCobalt = 3;

        @Config.Comment("Tier of the Ardite Anvil (if enabled)")
        @Config.RangeInt(min = 0, max = 5)
        @Config.Name("Tier - Ardite")
        public int tierArdite = 3;

        @Config.Comment("Tier of the Manyullyn Anvil (if enabled)")
        @Config.RangeInt(min = 0, max = 5)
        @Config.Name("Tier - Manyullyn")
        public int tierManyullyn = 4;

        @Config.Comment("Tier of the Invar Anvil (if enabled)")
        @Config.RangeInt(min = 0, max = 5)
        @Config.Name("Tier - Invar")
        public int tierInvar = 3;

        @Config.Comment("Tier of the Brass Anvil (if enabled)")
        @Config.RangeInt(min = 0, max = 5)
        @Config.Name("Tier - Brass")
        public int tierBrass = 1;

        @Config.Comment("Tier of the Mithril Anvil (if enabled)")
        @Config.RangeInt(min = 0, max = 5)
        @Config.Name("Tier - Mithril")
        public int tierMithril = 4;

        @Config.Comment("Tier of the Electrum Anvil (if enabled)")
        @Config.RangeInt(min = 0, max = 5)
        @Config.Name("Tier - Electrum")
        public int tierElectrum = 1;

        @Config.Comment("Tier of the Pig Iron  Anvil (if enabled)")
        @Config.RangeInt(min = 0, max = 5)
        @Config.Name("Tier - Pig Iron")
        public int tierPigiron = 2;

        private ToolsConfig() {}
    }
}
