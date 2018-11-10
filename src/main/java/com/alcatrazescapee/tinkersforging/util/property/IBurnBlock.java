/*
 * Part of the Tinkers Forging Mod by alcatrazEscapee
 * Work under Copyright. Licensed under the GPL-3.0.
 * See the project LICENSE.md for more information.
 */

package com.alcatrazescapee.tinkersforging.util.property;

import net.minecraft.block.properties.PropertyBool;

public interface IBurnBlock
{
    // This is a bit stupid putting this here, but I am allowed to have a bit of stupidity in this code
    PropertyBool LIT = PropertyBool.create("lit");
}
