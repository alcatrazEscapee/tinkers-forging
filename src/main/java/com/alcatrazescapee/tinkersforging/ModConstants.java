/*
 * Part of the Tinkers Forging Mod by alcatrazEscapee
 * Work under Copyright. Licensed under the GPL-3.0.
 * See the project LICENSE.md for more information.
 */

package com.alcatrazescapee.tinkersforging;

import com.google.common.base.CaseFormat;
import com.google.common.base.Converter;

@SuppressWarnings("WeakerAccess")
public final class ModConstants
{
    public static final String MOD_ID = "tinkersforging";
    public static final String MOD_NAME = "Tinkers Forging";

    // Versioning / Dependencies
    public static final String VERSION = "GRADLE:VERSION";
    public static final String FORGE_REQUIRED = "required-after:forge@[GRADLE:FORGE_VERSION,15.0.0.0);";
    public static final String ALC_CORE_REQUIRED = "required-after:alcatrazcore@[GRADLE:ALC_CORE_VERSION,2.0.0);";
    public static final String DEPENDENCIES = FORGE_REQUIRED + ALC_CORE_REQUIRED;

    // Util
    public static final Converter<String, String> ORE_DICT_CONVERTER = CaseFormat.UPPER_UNDERSCORE.converterTo(CaseFormat.LOWER_CAMEL);

}
