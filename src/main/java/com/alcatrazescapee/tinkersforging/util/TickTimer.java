/*
 * Part of the Tinkers Forging Mod by alcatrazEscapee
 * Work under Copyright. Licensed under the GPL-3.0.
 * See the project LICENSE.md for more information.
 */

package com.alcatrazescapee.tinkersforging.util;

public final class TickTimer
{
    private static long ticks;

    public static void reset()
    {
        ticks = 0L;
    }

    public static long getTicks()
    {
        return ticks;
    }

    public static void update(long ticks)
    {
        TickTimer.ticks = ticks;
    }
}
