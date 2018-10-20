/*
 * Part of the Tinkers Forging Mod by alcatrazEscapee
 * Work under Copyright. Licensed under the GPL-3.0.
 * See the project LICENSE.md for more information.
 */

package com.alcatrazescapee.tinkersforging.util.forge;

import javax.annotation.Nullable;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public enum ForgeStep
{
    HIT_LIGHT(-3, 8, 66, 176, 0),
    HIT_MEDIUM(-6, 30, 66, 196, 0),
    HIT_HARD(-9, 8, 88, 216, 0),
    DRAW(-15, 30, 88, 236, 0),
    PUNCH(2, 126, 66, 176, 20),
    BEND(7, 148, 66, 196, 20),
    UPSET(13, 126, 88, 216, 20),
    SHRINK(16, 148, 88, 236, 20);

    private static ForgeStep[] values = values();

    @Nullable
    public static ForgeStep valueOf(int id)
    {
        return id >= 0 && id < values.length ? values[id] : null;
    }

    private final int stepAmount;

    private final int xPos;
    private final int yPos;
    private final int textureU;
    private final int textureV;

    ForgeStep(int stepAmount, int xPos, int yPos, int textureU, int textureV)
    {
        this.stepAmount = stepAmount;
        this.xPos = xPos;
        this.yPos = yPos;
        this.textureU = textureU;
        this.textureV = textureV;
    }

    public int getStepAmount()
    {
        return stepAmount;
    }

    @SideOnly(Side.CLIENT)
    public int getX()
    {
        return xPos;
    }

    @SideOnly(Side.CLIENT)
    public int getY()
    {
        return yPos;
    }

    @SideOnly(Side.CLIENT)
    public int getTexU()
    {
        return textureU;
    }

    @SideOnly(Side.CLIENT)
    public int getTexV()
    {
        return textureV;
    }
}
