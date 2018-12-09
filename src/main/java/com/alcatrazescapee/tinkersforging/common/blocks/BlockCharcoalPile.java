/*
 * Part of the Tinkers Forging Mod by alcatrazEscapee
 * Work under Copyright. Licensed under the GPL-3.0.
 * See the project LICENSE.md for more information.
 */

package com.alcatrazescapee.tinkersforging.common.blocks;

import java.util.Random;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.alcatrazescapee.alcatrazcore.block.BlockCore;
import com.alcatrazescapee.alcatrazcore.util.compat.FireRegistry;
import com.alcatrazescapee.tinkersforging.common.tile.TileCharcoalForge;
import com.alcatrazescapee.tinkersforging.util.property.IPileBlock;

import static com.alcatrazescapee.tinkersforging.util.property.IBurnBlock.LIT;

@ParametersAreNonnullByDefault
public class BlockCharcoalPile extends BlockCore implements IPileBlock
{
    public BlockCharcoalPile()
    {
        super(Material.GROUND);

        setSoundType(SoundType.GROUND);
        setHarvestLevel("shovel", 0);
        setHardness(1.0f);
        setDefaultState(this.blockState.getBaseState().withProperty(LAYERS, 1));
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerModel()
    {
        // No Item Block Model
    }

    @Override
    public IBlockState getStateWithLayers(int layers)
    {
        return getDefaultState().withProperty(LAYERS, layers);
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean isTopSolid(IBlockState state)
    {
        return state.getValue(LAYERS) == 8;
    }

    @Nonnull
    @Override
    @SuppressWarnings("deprecation")
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(LAYERS, meta + 1);
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return state.getValue(LAYERS) - 1;
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean isFullCube(IBlockState state)
    {
        return state.getValue(LAYERS) == 8;
    }

    @Override
    @Nonnull
    @SuppressWarnings("deprecation")
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return PILE_AABB[state.getValue(LAYERS)];
    }

    @Nonnull
    @Override
    @SuppressWarnings("deprecation")
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face)
    {
        return face == EnumFacing.DOWN ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
    }

    @Nullable
    @Override
    @SuppressWarnings("deprecation")
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos)
    {
        int i = blockState.getValue(LAYERS) - 1;
        AxisAlignedBB axisalignedbb = blockState.getBoundingBox(worldIn, pos);
        return new AxisAlignedBB(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ, axisalignedbb.maxX, (double) ((float) i * 0.125F), axisalignedbb.maxZ);
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean isOpaqueCube(IBlockState state)
    {
        return state.getValue(LAYERS) == 8;
    }

    @Override
    @SuppressWarnings("deprecation")
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
    {
        if (!worldIn.isRemote)
        {
            // Try to drop the rock down
            IBlockState stateUnder = worldIn.getBlockState(pos.down());
            if (stateUnder.getBlock() instanceof BlockCharcoalPile)
            {
                int layersAt = state.getValue(LAYERS);
                int layersUnder = stateUnder.getValue(LAYERS);
                if (layersUnder < 8)
                {
                    if (layersUnder + layersAt <= 8)
                    {
                        worldIn.setBlockState(pos.down(), stateUnder.withProperty(LAYERS, layersAt + layersUnder));
                        worldIn.setBlockToAir(pos);
                    }
                    else
                    {
                        worldIn.setBlockState(pos.down(), stateUnder.withProperty(LAYERS, 8));
                        worldIn.setBlockState(pos, state.withProperty(LAYERS, layersAt + layersUnder - 8));
                    }
                }
                return;
            }

            if (!stateUnder.isNormalCube())
            {
                this.dropBlockAsItem(worldIn, pos, state, 0);
                worldIn.setBlockToAir(pos);
            }
        }
    }

    @Override
    @Nonnull
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return Items.COAL;
    }

    @Override
    public int damageDropped(IBlockState state)
    {
        return 1;
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean isFullBlock(IBlockState state)
    {
        return state.getValue(LAYERS) == 8;
    }

    @Nonnull
    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, LAYERS);
    }

    @Override
    public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest)
    {
        this.onBlockHarvested(world, pos, state, player);
        if (player.isCreative())
        {
            return super.removedByPlayer(state, world, pos, player, willHarvest);
        }
        if (!world.isRemote)
        {
            // Shrink layers by one
            int layers = state.getValue(LAYERS);
            if (layers == 1)
            {
                world.setBlockToAir(pos);
            }
            else
            {
                world.setBlockState(pos, state.withProperty(LAYERS, layers - 1));
            }
        }
        return true;
    }

    @Nonnull
    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player)
    {
        return new ItemStack(Items.COAL, 1, 1);
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean isNormalCube(IBlockState state)
    {
        return state.getValue(LAYERS) == 8;
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        ItemStack stack = player.getHeldItem(hand);
        int layers = state.getValue(LAYERS);
        if (layers >= 6 && FireRegistry.isFireStarter(stack) && TileCharcoalForge.isValidSideBlocks(world, pos))
        {
            if (!world.isRemote)
            {
                world.setBlockState(pos, ModBlocks.CHARCOAL_FORGE.getStateWithLayers(layers).withProperty(LIT, true));
                TileCharcoalForge.light(world, pos);
            }
            return true;
        }
        return false;
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean isSideSolid(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side)
    {
        return side == EnumFacing.UP ? isTopSolid(state) : (side == EnumFacing.DOWN);
    }
}
