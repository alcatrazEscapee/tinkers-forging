/*
 * Part of the Tinkers Forging Mod by alcatrazEscapee
 * Work under Copyright. Licensed under the GPL-3.0.
 * See the project LICENSE.md for more information.
 */

package com.alcatrazescapee.tinkersforging;

import net.minecraftforge.common.config.Config;

import static com.alcatrazescapee.tinkersforging.TinkersForging.MOD_ID;

@SuppressWarnings("WeakerAccess")
@Config(modid = MOD_ID, category = "")
public final class ModConfig
{
    public static final GeneralConfig GENERAL = new GeneralConfig();
    public static final BalanceConfig BALANCE = new BalanceConfig();

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


        @Config.Name("No Tree Punching Compat")
        @Config.Comment("Should this mod add tool parts and recipes for No Tree Punching's tools, if it is enabled?")
        @Config.RequiresMcRestart
        public boolean enableNoTreePunchingCompat = true;

        @Config.Name("Respect Tiers")
        @Config.RequiresMcRestart
        @Config.Comment("If this is true, you will not be able to use a lower tier anvil to make a higher tier item. Tiers of different anvils types are configurable.")
        public boolean respectTiers = true;

        @Config.Name("Remove Crafting Recipes")
        @Config.RequiresMcRestart
        @Config.Comment("If this is true, any recipes that are added to the anvil that have a crafting equivalent (i.e. a shovel) will have their normal crafting recipes removed")
        public boolean removeCraftingRecipes = true;

        @Config.Name("Force-Enabled Materials")
        @Config.RequiresMcRestart
        @Config.Comment({"This is a way to force Tinker's Forging to recognize other materials from other mods if they don't automatically get found.", "WARNING: This can cause broken recipes / items if you use it incorrectly. Only use it if you know what you are doing."})
        public String[] forceEnabledMetals = {};

        private GeneralConfig() {}
    }

    public static class BalanceConfig
    {
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

        @Config.Name("Enable Advanced Temperature Tooltips")
        @Config.Comment("If this is true, you will be able to see the exact temperature (in °C) of any items - including their exact workable and melting temperatures")
        public boolean enableAdvancedTemperatureTooltips = false;

        @Config.RangeInt(min = 0, max = 10)
        @Config.Name("Forge Target Range")
        @Config.Comment("The range that the work pointer needs to be in range of the target pointer for a forging to complete.")
        public int forgeTargetRange = 0;

        @Config.Name("Forge Target Range Tier Modifier")
        @Config.RangeInt(min = 0, max = 10)
        @Config.Comment({"Does the tier of the metal affect the target range? Lower tier metals have a larger range by this value.", "Full calculation is [total range] = [target range] + (5 - [tier]) * [range tier modifier]"})
        public int forgeTierRangeMod = 0;

        @Config.Name("Forging Gives Experience")
        @Config.Comment("Should forging items on the Tinker's Anvil grant experience?")
        public boolean forgeExperienceEnabled = true;

        @Config.Name("Forging Experience Modifier")
        @Config.RangeDouble(min = 0, max = 100)
        @Config.Comment("Modifier for experience given by forging items on the Tinker's Anvil. Note higher tier items give more experience.")
        public double forgeExperienceModifier = 3;

        @Config.Name("Tinker's Construct Casting Heats Items")
        @Config.Comment("Should Casting items in a Tinker's Construct casting table bring them to max temperature?")
        public boolean tinkersConstructCastingTemperature = true;

        private BalanceConfig() {}
    }
}
