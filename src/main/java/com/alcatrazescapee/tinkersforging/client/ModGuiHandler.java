/*
 * Part of the Tinkers Forging Mod by alcatrazEscapee
 * Work under Copyright. Licensed under the GPL-3.0.
 * See the project LICENSE.md for more information.
 */

package com.alcatrazescapee.tinkersforging.client;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

import com.alcatrazescapee.alcatrazcore.util.CoreHelpers;
import com.alcatrazescapee.tinkersforging.client.gui.GuiForge;
import com.alcatrazescapee.tinkersforging.client.gui.GuiTinkersAnvil;
import com.alcatrazescapee.tinkersforging.common.container.ContainerForge;
import com.alcatrazescapee.tinkersforging.common.container.ContainerTinkersAnvil;
import com.alcatrazescapee.tinkersforging.common.tile.TileForge;
import com.alcatrazescapee.tinkersforging.common.tile.TileTinkersAnvil;

public final class ModGuiHandler implements IGuiHandler
{
    public static final int TINKERS_ANVIL = 0;
    public static final int FORGE = 1;

    @Nullable
    @Override
    @SuppressWarnings("ConstantConditions")
    public Container getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        BlockPos pos = new BlockPos(x, y, z);
        switch (ID)
        {
            case TINKERS_ANVIL:
                return new ContainerTinkersAnvil(player, CoreHelpers.getTE(world, pos, TileTinkersAnvil.class));
            case FORGE:
                return new ContainerForge(player.inventory, CoreHelpers.getTE(world, pos, TileForge.class));
            default:
                return null;
        }
    }

    @Nullable
    @Override
    @SuppressWarnings("ConstantConditions")
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        BlockPos pos = new BlockPos(x, y, z);
        Container container = getServerGuiElement(ID, player, world, x, y, z);
        switch (ID)
        {
            case TINKERS_ANVIL:
                Block block = world.getBlockState(pos).getBlock();
                return new GuiTinkersAnvil(CoreHelpers.getTE(world, pos, TileTinkersAnvil.class), block.getTranslationKey(), container, player.inventory);
            case FORGE:
                return new GuiForge(CoreHelpers.getTE(world, pos, TileForge.class), container, player.inventory);
            default:
                return null;
        }
    }
}
