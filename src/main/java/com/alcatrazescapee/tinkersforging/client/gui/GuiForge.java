/*
 * Part of the Tinkers Forging Mod by alcatrazEscapee
 * Work under Copyright. Licensed under the GPL-3.0.
 * See the project LICENSE.md for more information.
 */

package com.alcatrazescapee.tinkersforging.client.gui;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;

import com.alcatrazescapee.alcatrazcore.client.gui.GuiContainerTileCore;
import com.alcatrazescapee.tinkersforging.common.blocks.ModBlocks;
import com.alcatrazescapee.tinkersforging.common.tile.TileForge;

import static com.alcatrazescapee.tinkersforging.ModConstants.MOD_ID;

public class GuiForge extends GuiContainerTileCore<TileForge>
{
    private static final ResourceLocation BACKGROUND = new ResourceLocation(MOD_ID, "textures/gui/forge.png");

    public GuiForge(TileForge tile, Container container, InventoryPlayer playerInv)
    {
        super(tile, container, playerInv, BACKGROUND, ModBlocks.FORGE.getTranslationKey());
    }
}
