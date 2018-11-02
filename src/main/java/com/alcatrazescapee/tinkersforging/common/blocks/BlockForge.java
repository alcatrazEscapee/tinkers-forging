/*
 * Part of the Tinkers Forging Mod by alcatrazEscapee
 * Work under Copyright. Licensed under the GPL-3.0.
 * See the project LICENSE.md for more information.
 */

package com.alcatrazescapee.tinkersforging.common.blocks;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import com.alcatrazescapee.alcatrazcore.block.BlockTileCore;
import com.alcatrazescapee.alcatrazcore.util.CoreHelpers;
import com.alcatrazescapee.alcatrazcore.util.compat.FireRegistry;
import com.alcatrazescapee.tinkersforging.TinkersForging;
import com.alcatrazescapee.tinkersforging.client.ModGuiHandler;
import com.alcatrazescapee.tinkersforging.common.tile.TileForge;

@ParametersAreNonnullByDefault
public class BlockForge extends BlockTileCore
{
    public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
    public static final PropertyBool LIT = PropertyBool.create("lit");

    public BlockForge()
    {
        super(Material.ROCK);

        setSoundType(SoundType.STONE);
        setHardness(2.0f);
        setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(LIT, false));
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World world, int i)
    {
        return new TileForge();
    }

    @Override
    @Nonnull
    @SuppressWarnings("deprecation")
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(FACING, EnumFacing.byHorizontalIndex(meta)).withProperty(LIT, meta >= 4);
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return state.getValue(FACING).getHorizontalIndex() + (state.getValue(LIT) ? 4 : 0);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        if (!worldIn.isRemote)
        {
            ItemStack stack = playerIn.getHeldItem(hand);
            if (FireRegistry.isFireStarter(stack) && !worldIn.getBlockState(pos).getValue(LIT))
            {
                stack.damageItem(1, playerIn);
                TileForge tile = CoreHelpers.getTE(worldIn, pos, TileForge.class);
                if (tile != null)
                {
                    worldIn.setBlockState(pos, state.withProperty(LIT, tile.tryLight()));
                }
            }
            else if (!playerIn.isSneaking())
            {
                playerIn.openGui(TinkersForging.getInstance(), ModGuiHandler.FORGE, worldIn, pos.getX(), pos.getY(), pos.getZ());
            }
        }
        return true;
    }

    @Override
    @Nonnull
    public BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, FACING, LIT);
    }

    @Override
    @Nonnull
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand)
    {
        return getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite());
    }
}
