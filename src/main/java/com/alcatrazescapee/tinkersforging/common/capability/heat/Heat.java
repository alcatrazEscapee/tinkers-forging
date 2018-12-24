/*
 * Part of the Tinkers Forging Mod by alcatrazEscapee
 * Work under Copyright. Licensed under the GPL-3.0.
 * See the project LICENSE.md for more information.
 */

package com.alcatrazescapee.tinkersforging.common.capability.heat;

import java.util.Arrays;
import javax.annotation.Nonnull;

import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static com.alcatrazescapee.tinkersforging.TinkersForging.MOD_ID;

public enum Heat
{
    HOT(1f, 100f, TextFormatting.DARK_GRAY),
    VERY_HOT(100f, 200f, TextFormatting.DARK_GRAY),
    DARK_RED(200f, 400f, TextFormatting.DARK_RED),
    FAINT_RED(400f, 600f, TextFormatting.RED),
    BRIGHT_RED(600f, 900f, TextFormatting.RED),
    ORANGE(900f, 1050f, TextFormatting.GOLD),
    YELLOW(1050f, 1200f, TextFormatting.YELLOW),
    YELLOW_WHITE(1200f, 1350f, TextFormatting.YELLOW),
    WHITE(1350f, 1499f, TextFormatting.WHITE),
    BRILLIANT_WHITE(1500f, Float.MAX_VALUE, TextFormatting.WHITE);

    public static String getColorFor(float temperature)
    {
        Heat heat = Arrays.stream(values())
                .filter(x -> x.min <= temperature && temperature < x.max)
                .findFirst()
                .orElse(BRILLIANT_WHITE);
        return "" + heat.format;
    }

    @Nonnull
    @SideOnly(Side.CLIENT)
    public static String getTooltipFor(float temperature)
    {
        Heat heat = Arrays.stream(values())
                .filter(x -> x.min <= temperature && temperature < x.max)
                .findFirst()
                .orElse(BRILLIANT_WHITE);

        StringBuilder b = new StringBuilder();
        b.append(I18n.format(MOD_ID + ".tooltip.temperature_" + heat.name().toLowerCase()));
        if (heat != BRILLIANT_WHITE)
        {
            for (int i = 1; i <= 4; i++)
            {
                if (temperature <= heat.min + ((float) i * 0.2f) * (heat.max - heat.min))
                    continue;
                b.append("\u2605");
            }
        }
        return heat.format + b.toString();
    }

    private final float min;
    private final float max;
    private final TextFormatting format;

    Heat(float min, float max, TextFormatting format)
    {
        this.min = min;
        this.max = max;
        this.format = format;
    }

    public boolean inRange(float temperature)
    {
        return min <= temperature && temperature < max;
    }
}
