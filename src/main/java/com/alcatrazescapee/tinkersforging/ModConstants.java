/*
 * Part of the Tinkers Forging Mod by alcatrazEscapee
 * Work under Copyright. Licensed under the GPL-3.0.
 * See the project LICENSE.md for more information.
 */

package com.alcatrazescapee.tinkersforging;

@SuppressWarnings("WeakerAccess")
public final class ModConstants
{
    public static final String MOD_ID = "tinkersforging";
    public static final String MOD_NAME = "Tinkers Forging";

    // Tinkers Construct
    public static final String MATERIAL_NBT_KEY = "Material";
    public static final String VERSION = "GRADLE:VERSION";
    private static final String FORGE_MAX = "15.0.0.0";

    // Versioning
    private static final String ALC_MIN = "1.0.2";
    private static final String ALC_MAX = "1.2.0";
    private static final String FORGE_MIN = "14.23.4.2705";
    public static final String DEPENDENCIES = "required-after:forge@[" + FORGE_MIN + "," + FORGE_MAX + ");" +
            "required-after:alcatrazcore@[" + ALC_MIN + "," + ALC_MAX + ");" +
            "after:tconstruct;";
}
