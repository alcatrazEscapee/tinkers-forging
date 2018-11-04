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
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.alcatrazescapee.alcatrazcore.block.BlockTileCore;
import com.alcatrazescapee.alcatrazcore.util.CoreHelpers;
import com.alcatrazescapee.alcatrazcore.util.compat.FireRegistry;
import com.alcatrazescapee.tinkersforging.TinkersForging;
import com.alcatrazescapee.tinkersforging.client.ModGuiHandler;
import com.alcatrazescapee.tinkersforging.common.tile.TileCharcoalForge;
import com.alcatrazescapee.tinkersforging.util.property.IBurnBlock;
import com.alcatrazescapee.tinkersforging.util.property.IPileBlock;

@ParametersAreNonnullByDefault
public class BlockCharcoalForge extends BlockTileCore implements IPileBlock, IBurnBlock
{
    public BlockCharcoalForge()
    {
        super(Material.GROUND);

        setSoundType(SoundType.GROUND);
        setHardness(1.0F);
        setHarvestLevel("shovel", 0);
        setTickRandomly(true);
        this.setDefaultState(this.blockState.getBaseState().withProperty(LAYERS, 1).withProperty(LIT, true));
    }

    @Override
    public IBlockState getStateWithLayers(int layers)
    {
        return getDefaultState().withProperty(LAYERS, layers);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World world, int i)
    {
        return new TileCharcoalForge();
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerModel()
    {
        // No Item Block
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
    {
        if (!worldIn.isRemote)
        {
            // Breaks block if the block under it breaks.
            IBlockState stateUnder = worldIn.getBlockState(pos.down());
            if (!stateUnder.isNormalCube())
            {
                this.dropBlockAsItem(worldIn, pos, state, 0);
                worldIn.setBlockToAir(pos);
                return;
            }
            TileCharcoalForge tile = CoreHelpers.getTE(worldIn, pos, TileCharcoalForge.class);
            if (tile != null)
            {
                tile.updateClosedState(worldIn, pos);
                // todo: this
                //((TileEntityForge) te).closed = updateSideBlocks(worldIn, pos);
            }
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean isTopSolid(IBlockState state)
    {
        return state.getValue(LAYERS) == 8;
    }

    @Override
    @Nonnull
    @SuppressWarnings("deprecation")
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(LAYERS, meta % 8 + 1).withProperty(LIT, meta > 7);
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return state.getValue(LAYERS) + (state.getValue(LIT) ? 7 : -1);
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean isFullCube(IBlockState state)
    {
        return state.getValue(LAYERS) == 8 && !state.getValue(LIT);
    }

    @Nonnull
    @Override
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
        return state.getValue(LAYERS) == 8 && !state.getValue(LIT);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
    {
        if (stateIn.getValue(LIT))
        {
            worldIn.playSound((double) pos.getX() + 0.5D, (double) pos.getY(), (double) pos.getZ() + 0.5D, SoundEvents.BLOCK_FIRE_AMBIENT, SoundCategory.BLOCKS, 1.0F, 1.0F, false);

            if (rand.nextFloat() <= 0.3)
            {
                // todo: generate flame particle
                //NoTreePunching.proxy.generateParticle(worldIn, pos, 1);
            }
        }
    }

    @Nonnull
    @Override
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
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        ItemStack stack = player.getHeldItem(hand);
        if (FireRegistry.isFireStarter(stack))
        {
            if (!world.isRemote)
            {
                world.setBlockState(pos, state.withProperty(LIT, true));
                TileCharcoalForge.lightNearbyForges(world, pos);
                stack.damageItem(1, player);
                world.playSound(null, pos, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.PLAYERS, 1.0F, 1.0F);

                TileCharcoalForge tile = CoreHelpers.getTE(world, pos, TileCharcoalForge.class);
                if (tile != null)
                {
                    // todo: this
                    tile.updateClosedState(world, pos);
                    //((TileEntityForge) te).closed = updateSideBlocks(world, pos);
                }
            }
            return true;
        }
        else if (!player.isSneaking())
        {
            player.openGui(TinkersForging.getInstance(), ModGuiHandler.CHARCOAL_FORGE, world, pos.getX(), pos.getY(), pos.getZ());
            return true;
        }
        return false;
    }

    @Override
    @Nonnull
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, LAYERS, LIT);
    }

    @Override
    public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos)
    {
        return state.getValue(LIT) ? 15 : 0;
    }

    @Override
    public boolean isBurning(IBlockAccess world, BlockPos pos)
    {
        return world.getBlockState(pos).getValue(LIT);
    }

    @Override
    public int quantityDropped(IBlockState state, int fortune, Random random)
    {
        return state.getValue(LAYERS);
    }

    @Nonnull
    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player)
    {
        return new ItemStack(Items.COAL, 1, 1);
    }

    @Override
    public void onEntityWalk(World worldIn, BlockPos pos, Entity entityIn) {
        IBlockState state = worldIn.getBlockState(pos);
        if (!entityIn.isImmuneToFire() && entityIn instanceof EntityLivingBase && state.getValue(LIT))
        {
            entityIn.attackEntityFrom(DamageSource.IN_FIRE, 2.0F);
        }

        super.onEntityWalk(worldIn, pos, entityIn);
    }
}
