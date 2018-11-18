/*
 * Part of the Tinkers Forging Mod by alcatrazEscapee
 * Work under Copyright. Licensed under the GPL-3.0.
 * See the project LICENSE.md for more information.
 */

package com.alcatrazescapee.tinkersforging;

import org.apache.logging.log4j.Logger;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

import com.alcatrazescapee.tinkersforging.client.ModGuiHandler;
import com.alcatrazescapee.tinkersforging.common.blocks.ModBlocks;
import com.alcatrazescapee.tinkersforging.common.capability.CapabilityForgeItem;
import com.alcatrazescapee.tinkersforging.common.items.ModItems;
import com.alcatrazescapee.tinkersforging.common.network.PacketAnvilButton;
import com.alcatrazescapee.tinkersforging.common.network.PacketAnvilRecipeUpdate;
import com.alcatrazescapee.tinkersforging.common.recipe.ModRecipes;
import com.alcatrazescapee.tinkersforging.integration.patchouli.PatchouliIntegration;
import com.alcatrazescapee.tinkersforging.integration.tconstruct.TinkersIntegration;
import com.alcatrazescapee.tinkersforging.util.TickTimer;

import static com.alcatrazescapee.tinkersforging.TinkersForging.MOD_ID;
import static com.alcatrazescapee.tinkersforging.TinkersForging.MOD_NAME;

@SuppressWarnings({"WeakerAccess", "unused"})
@Mod(modid = MOD_ID, version = TinkersForging.VERSION, dependencies = TinkersForging.DEPENDENCIES, name = MOD_NAME, useMetadata = true)
public final class TinkersForging
{
    public static final String MOD_ID = "tinkersforging";
    public static final String MOD_NAME = "Tinkers Forging";
    public static final String VERSION = "GRADLE:VERSION";

    // Versioning
    private static final String ALC_MIN = "1.0.2";
    private static final String ALC_MAX = "2.0.0";
    private static final String FORGE_MIN = "14.23.4.2705";
    private static final String FORGE_MAX = "15.0.0.0";

    public static final String DEPENDENCIES = "required-after:forge@[" + FORGE_MIN + "," + FORGE_MAX + ");" + "required-after:alcatrazcore@[" + ALC_MIN + "," + ALC_MAX + ");" + "after:tconstruct;after:alcatrazcore;";

    @Mod.Instance
    private static TinkersForging instance;
    private static Logger logger;
    private static SimpleNetworkWrapper network;

    public static TinkersForging getInstance()
    {
        return instance;
    }

    public static Logger getLog()
    {
        return logger;
    }

    public static SimpleNetworkWrapper getNetwork()
    {
        return network;
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        logger = event.getModLog();
        logger.debug("If you can see this, debug logging is working :)");

        int id = -1;
        network = NetworkRegistry.INSTANCE.newSimpleChannel(MOD_ID);
        network.registerMessage(PacketAnvilButton.Handler.class, PacketAnvilButton.class, ++id, Side.SERVER);
        network.registerMessage(PacketAnvilRecipeUpdate.Handler.class, PacketAnvilRecipeUpdate.class, ++id, Side.CLIENT);

        NetworkRegistry.INSTANCE.registerGuiHandler(this, new ModGuiHandler());

        // Pre-Init Managers
        CapabilityForgeItem.preInit();
        ModBlocks.preInit();
        ModItems.preInit();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
        TickTimer.reset();

        if (Loader.isModLoaded("patchouli"))
        {
            PatchouliIntegration.init();
        }
        if (Loader.isModLoaded("tconstruct"))
        {
            TinkersIntegration.init();
        }

        // Init Managers
        ModItems.init();
        ModBlocks.init();
        ModRecipes.init();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        // Post-Init Managers
        ModRecipes.postInit();
    }
}
