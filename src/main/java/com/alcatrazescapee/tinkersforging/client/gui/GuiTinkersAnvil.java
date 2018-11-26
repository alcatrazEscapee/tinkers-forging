/*
 * Part of the Tinkers Forging Mod by alcatrazEscapee
 * Work under Copyright. Licensed under the GPL-3.0.
 * See the project LICENSE.md for more information.
 */

package com.alcatrazescapee.tinkersforging.client.gui;

import java.io.IOException;
import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.alcatrazescapee.alcatrazcore.client.gui.GuiContainerTileCore;
import com.alcatrazescapee.tinkersforging.TinkersForging;
import com.alcatrazescapee.tinkersforging.common.network.PacketAnvilButton;
import com.alcatrazescapee.tinkersforging.common.tile.TileTinkersAnvil;
import com.alcatrazescapee.tinkersforging.integration.jei.JEIIntegration;
import com.alcatrazescapee.tinkersforging.util.forge.ForgeRule;
import com.alcatrazescapee.tinkersforging.util.forge.ForgeStep;
import com.alcatrazescapee.tinkersforging.util.forge.ForgeSteps;

import static com.alcatrazescapee.tinkersforging.TinkersForging.MOD_ID;
import static com.alcatrazescapee.tinkersforging.common.tile.TileTinkersAnvil.*;

@SideOnly(Side.CLIENT)
@ParametersAreNonnullByDefault
public class GuiTinkersAnvil extends GuiContainerTileCore<TileTinkersAnvil>
{
    static final ResourceLocation BACKGROUND = new ResourceLocation(MOD_ID, "textures/gui/tinkers_anvil.png");

    public GuiTinkersAnvil(TileTinkersAnvil tile, String translationKey, Container container, InventoryPlayer playerInv)
    {
        super(tile, container, playerInv, BACKGROUND, translationKey);

        this.ySize = 222;
    }

    @Override
    public void initGui()
    {
        super.initGui();
        int id = -1;
        // Draw buttons here
        for (ForgeStep step : ForgeStep.values())
        {
            addButton(new GuiButtonTinkersAnvil(++id, guiLeft, guiTop, step));
        }

        addButton(new GuiButtonTinkersAnvil(++id, guiLeft + 63, guiTop + 21, false));
        addButton(new GuiButtonTinkersAnvil(++id, guiLeft + 103, guiTop + 21, true));
    }

    @Override
    protected void renderHoveredToolTip(int mouseX, int mouseY)
    {
        // Rule tooltips
        int x = guiLeft + 57;
        int y = guiTop + 42;

        for (int i = FIELD_FIRST_RULE; i <= FIELD_THIRD_RULE; i++)
        {
            if (mouseX >= x && mouseY >= y && mouseX < x + 18 && mouseY < y + 24)
            {
                ForgeRule rule = ForgeRule.valueOf(tile.getField(i));
                if (rule != null)
                {
                    drawHoveringText(I18n.format(MOD_ID + ".tooltip." + rule.name().toLowerCase()), mouseX, mouseY);
                }
            }
            x += 22;
        }

        // Step Button Tooltips
        for (GuiButton button : buttonList)
        {
            if (button instanceof GuiButtonTinkersAnvil && ((GuiButtonTinkersAnvil) button).hasTooltip())
            {
                if (button.isMouseOver())
                {
                    drawHoveringText(I18n.format(((GuiButtonTinkersAnvil) button).getTooltip()), mouseX, mouseY);
                }
            }
        }
        super.renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);

        // JEI Question Mark Icon
        if (JEIIntegration.isEnabled())
            drawTexturedModalRect(guiLeft + 165, guiTop + 6, 246, 40, 5, 7);

        // Progress + Target
        int progress = tile.getField(TileTinkersAnvil.FIELD_PROGRESS);
        drawTexturedModalRect(guiLeft + 10 + progress, guiTop + 112, 196, 40, 7, 6);

        int target = tile.getField(TileTinkersAnvil.FIELD_TARGET);
        drawTexturedModalRect(guiLeft + 10 + target, guiTop + 119, 203, 40, 7, 6);

        // Last Three Steps
        for (int i = FIELD_LAST_STEP; i <= FIELD_THIRD_STEP; i++)
        {
            ForgeStep step = ForgeStep.valueOf(tile.getField(i));
            if (step != null)
            {
                int xOffset = 22 * (2 - i + FIELD_LAST_STEP);
                drawTexturedModalRect(guiLeft + 59 + xOffset, guiTop + 69, step.getTexU() + 3, step.getTexV() + 3, 14, 14);
            }
        }

        ForgeSteps steps = tile.getSteps();

        // Rules
        for (int i = FIELD_FIRST_RULE; i <= FIELD_THIRD_RULE; i++)
        {
            ForgeRule rule = ForgeRule.valueOf(tile.getField(i));
            if (rule != null)
            {
                int xOffset = 22 * (i - FIELD_FIRST_RULE);
                // The rule icon
                drawTexturedModalRect(guiLeft + 59 + xOffset, guiTop + 44, rule.getIconU(), rule.getIconV(), 14, 14);
                // The color / border
                drawTexturedModalRect(guiLeft + 57 + xOffset, guiTop + 42, rule.getOutlineU() + (rule.matches(steps) ? 0 : 18), rule.getOutlineV(), 18, 24);
            }
        }

    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException
    {
        // Handle gui buttons being clicked here
        if (button instanceof GuiButtonTinkersAnvil)
        {
            TinkersForging.getNetwork().sendToServer(new PacketAnvilButton(button.id));
            if (button.id == 8)
            {
                tile.cycleForgeRecipe(false);
            }
            else if (button.id == 9)
            {
                tile.cycleForgeRecipe(true);
            }
        }
        super.actionPerformed(button);
    }
}
