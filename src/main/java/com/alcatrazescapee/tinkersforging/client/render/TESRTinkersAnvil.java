/*
 * Part of the Tinkers Forging Mod by alcatrazEscapee
 * Work under Copyright. Licensed under the GPL-3.0.
 * See the project LICENSE.md for more information.
 */

package com.alcatrazescapee.tinkersforging.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import com.alcatrazescapee.tinkersforging.common.tile.TileTinkersAnvil;

import static com.alcatrazescapee.tinkersforging.common.tile.TileTinkersAnvil.SLOT_HAMMER;

@SideOnly(Side.CLIENT)
public class TESRTinkersAnvil extends TileEntitySpecialRenderer<TileTinkersAnvil>
{
    @Override
    public void render(TileTinkersAnvil tile, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
    {
        super.render(tile, x, y, z, partialTicks, destroyStage, alpha);

        IItemHandler cap = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
        if (cap != null)
        {
            int rotation = tile.getBlockMetadata();

            // Current Item
            ItemStack stack = cap.getStackInSlot(SLOT_HAMMER);
            GlStateManager.pushMatrix();
            GlStateManager.translate(x + 0.5, y + 0.03125D + 0.6875, z + 0.5);
            GlStateManager.scale(0.35f, 0.35f, 0.35f);
            GlStateManager.rotate(90f, 1f, 0f, 0f);
            GlStateManager.rotate(90f * (float) rotation, 0f, 0f, 1f);
            GlStateManager.translate(-0.7, 0, 0);
            Minecraft.getMinecraft().getRenderItem().renderItem(stack, ItemCameraTransforms.TransformType.FIXED);
            GlStateManager.popMatrix();

        }
    }

    @Override
    public boolean isGlobalRenderer(TileTinkersAnvil te)
    {
        return false;
    }
}
