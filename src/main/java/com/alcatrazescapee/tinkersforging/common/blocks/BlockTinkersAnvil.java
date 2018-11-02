/*
 * Part of the Tinkers Forging Mod by alcatrazEscapee
 * Work under Copyright. Licensed under the GPL-3.0.
 * See the project LICENSE.md for more information.
 */

package com.alcatrazescapee.tinkersforging.common.blocks;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.alcatrazescapee.alcatrazcore.block.BlockTileCore;
import com.alcatrazescapee.tinkersforging.TinkersForging;
import com.alcatrazescapee.tinkersforging.client.ModGuiHandler;
import com.alcatrazescapee.tinkersforging.common.tile.TileTinkersAnvil;
import com.alcatrazescapee.tinkersforging.util.Metal;

@ParametersAreNonnullByDefault
public class BlockTinkersAnvil extends BlockTileCore
{
    public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);

    private static final Map<Metal, BlockTinkersAnvil> MAP = new HashMap<>();
    private static final AxisAlignedBB AABB_X = new AxisAlignedBB(0.1875, 0, 0, 0.8125, 0.625, 1);
    private static final AxisAlignedBB AABB_Z = new AxisAlignedBB(0, 0, 0.1875, 1, 0.625, 0.8125);

    @Nullable
    public static BlockTinkersAnvil get(Metal metal)
    {
        return MAP.get(metal);
    }

    @Nonnull
    public static ItemStack get(Metal metal, int amount)
    {
        BlockTinkersAnvil block = get(metal);
        return block == null ? ItemStack.EMPTY : new ItemStack(block, amount);
    }

    public static Collection<BlockTinkersAnvil> getAll()
    {
        return MAP.values();
    }

    private final Metal metal;
    private final int tier;

    public BlockTinkersAnvil(Metal metal)
    {
        super(Material.IRON);

        this.metal = metal;
        this.tier = metal.getTier();
        MAP.put(metal, this);

        setHarvestLevel("pickaxe", metal.getTier());
        setSoundType(SoundType.ANVIL);
        setHardness(3.0f + 1.0f * metal.getTier());
        setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
    }

    public int getTier()
    {
        return tier;
    }

    @SideOnly(Side.CLIENT)
    @SuppressWarnings("ConstantConditions")
    @Override
    public void registerModel()
    {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(this.getRegistryName(), "inventory"));
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        return new TileTinkersAnvil();
    }

    @Nonnull
    public Metal getMetal()
    {
        return metal;
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean isFullBlock(IBlockState state)
    {
        return false;
    }

    @Override
    @Nonnull
    @SuppressWarnings("deprecation")
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(FACING, EnumFacing.byHorizontalIndex(meta));
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return state.getValue(FACING).getHorizontalIndex();
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    @Override
    @Nonnull
    @SuppressWarnings("deprecation")
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return state.getValue(FACING).getAxis() == EnumFacing.Axis.Z ? AABB_Z : AABB_X;
    }

    @SideOnly(Side.CLIENT)
    @Override
    @Nonnull
    @SuppressWarnings("deprecation")
    public AxisAlignedBB getSelectedBoundingBox(IBlockState state, World worldIn, BlockPos pos)
    {
        return getBoundingBox(state, worldIn, pos).offset(pos);
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        if (!worldIn.isRemote)
        {
            if (!playerIn.isSneaking())
            {
                playerIn.openGui(TinkersForging.getInstance(), ModGuiHandler.TINKERS_ANVIL, worldIn, pos.getX(), pos.getY(), pos.getZ());
            }
        }
        return true;
    }

    @Override
    @Nonnull
    public BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, FACING);
    }

    @Override
    public boolean isNormalCube(IBlockState state, IBlockAccess world, BlockPos pos)
    {
        return false;
    }

    @Override
    @Nonnull
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand)
    {
        return getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite());
    }
}
