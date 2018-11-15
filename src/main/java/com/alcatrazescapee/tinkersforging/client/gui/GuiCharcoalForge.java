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
import com.alcatrazescapee.tinkersforging.ModConfig;
import com.alcatrazescapee.tinkersforging.common.tile.TileCharcoalForge;

import static com.alcatrazescapee.tinkersforging.TinkersForging.MOD_ID;
import static com.alcatrazescapee.tinkersforging.common.blocks.BlockCharcoalForge.LIT;
import static com.alcatrazescapee.tinkersforging.common.capability.CapabilityForgeItem.MAX_TEMPERATURE;
import static com.alcatrazescapee.tinkersforging.common.tile.TileCharcoalForge.*;

public class GuiCharcoalForge extends GuiContainerTileCore<TileCharcoalForge>
{
    private static final ResourceLocation BACKGROUND = new ResourceLocation(MOD_ID, "textures/gui/charcoal_forge.png");

    public GuiCharcoalForge(TileCharcoalForge tile, Container container, InventoryPlayer playerInv, String titleKey)
    {
        super(tile, container, playerInv, BACKGROUND, titleKey);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);

        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;

        int fuelTicksRemaining = tile.getField(FIELD_FUEL);
        if (fuelTicksRemaining > 0 && tile.getWorld().getBlockState(tile.getPos()).getValue(LIT))
        {
            // Draw burn time
            int burnTime = (int) Math.round(14 * fuelTicksRemaining / (FUEL_TICKS_MAX * ModConfig.GENERAL.charcoalForgeFuelModifier));
            drawTexturedModalRect(x + 80, y + 56 - burnTime, 176, 14 - burnTime, 14, burnTime);
        }

        int temperature = tile.getField(FIELD_TEMPERATURE);
        if (temperature > 0)
        {
            // Draw temperature
            int scaledTemp = Math.round(30 * temperature / MAX_TEMPERATURE);
            drawTexturedModalRect(x + 20, y + 61 - scaledTemp, 190, 30 - scaledTemp, 10, scaledTemp);
        }

        int charcoalLayers = (tile.getBlockMetadata() % 8 + 1) * 2;
        if (charcoalLayers > 0)
        {
            drawTexturedModalRect(x + 80, y + 75 - charcoalLayers, 200, 16 - charcoalLayers, 16, charcoalLayers);
        }
    }
}
