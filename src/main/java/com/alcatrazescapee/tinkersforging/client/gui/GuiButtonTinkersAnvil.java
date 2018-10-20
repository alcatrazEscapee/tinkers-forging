/*
 * Part of the Tinkers Forging Mod by alcatrazEscapee
 * Work under Copyright. Licensed under the GPL-3.0.
 * See the project LICENSE.md for more information.
 */

package com.alcatrazescapee.tinkersforging.client.gui;

import javax.annotation.Nonnull;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.alcatrazescapee.tinkersforging.util.forge.ForgeStep;

import static com.alcatrazescapee.tinkersforging.ModConstants.MOD_ID;
import static com.alcatrazescapee.tinkersforging.client.gui.GuiTinkersAnvil.BACKGROUND;

@SideOnly(Side.CLIENT)
public class GuiButtonTinkersAnvil extends GuiButton
{
    private final int textureU;
    private final int textureV;
    private final String tooltip;

    GuiButtonTinkersAnvil(int id, int x, int y, int width, int height, int textureU, int textureV)
    {
        super(id, x, y, width, height, "");
        this.textureU = textureU;
        this.textureV = textureV;

        tooltip = null;
    }

    GuiButtonTinkersAnvil(int id, int backgroundX, int backgroundY, ForgeStep step)
    {
        super(id, backgroundX + step.getX(), backgroundY + step.getY(), 20, 20, "");
        this.textureU = step.getTexU();
        this.textureV = step.getTexV();

        tooltip = MOD_ID + ".tooltip." + step.name().toLowerCase();
    }

    @Override
    public void drawButton(@Nonnull Minecraft mc, int mouseX, int mouseY, float partialTicks)
    {
        if (this.visible)
        {
            GlStateManager.color(1, 1, 1, 1);
            mc.getTextureManager().bindTexture(BACKGROUND);
            hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
            drawModalRectWithCustomSizedTexture(x, y, textureU, textureV, width, height, 256, 256);
            mouseDragged(mc, mouseX, mouseY);
        }
    }

    boolean hasTooltip()
    {
        return tooltip != null;
    }

    String getTooltip()
    {
        return tooltip;
    }
}
