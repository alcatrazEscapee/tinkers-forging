/*
 * Part of the Tinkers Forging Mod by alcatrazEscapee
 * Work under Copyright. Licensed under the GPL-3.0.
 * See the project LICENSE.md for more information.
 */

package com.alcatrazescapee.tinkersforging.client.render;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;

import com.alcatrazescapee.tinkersforging.common.tile.TileTinkersAnvil;

public class TESRTinkersAnvil extends TileEntitySpecialRenderer<TileTinkersAnvil>
{
    @Override
    public void render(TileTinkersAnvil te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
    {
        super.render(te, x, y, z, partialTicks, destroyStage, alpha);

        // todo: render the item on top of the anvil?
    }

    @Override
    public boolean isGlobalRenderer(TileTinkersAnvil te)
    {
        return false;
    }
}
