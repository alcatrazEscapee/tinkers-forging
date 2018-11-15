/*
 * Part of the Tinkers Forging Mod by alcatrazEscapee
 * Work under Copyright. Licensed under the GPL-3.0.
 * See the project LICENSE.md for more information.
 */

package com.alcatrazescapee.tinkersforging.client.particle;

import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static com.alcatrazescapee.tinkersforging.common.blocks.BlockForge.FACING;
import static com.alcatrazescapee.tinkersforging.util.property.IPileBlock.LAYERS;

@SideOnly(Side.CLIENT)
public class ParticleForgeFlame extends Particle
{
    public static void generateCharcoalForge(World world, BlockPos pos, Random rand)
    {
        Minecraft.getMinecraft().effectRenderer.addEffect(new ParticleForgeFlame(world, pos.getX() + 0.5d + 0.45d * (rand.nextFloat() - rand.nextFloat()), pos.getY() + 0.1d * world.getBlockState(pos).getValue(LAYERS), pos.getZ() + 0.5d + 0.45d * (rand.nextFloat() - rand.nextFloat()), 0d, 0.01d, 0d));
    }

    public static void generateForge(World world, BlockPos pos, IBlockState state, Random rand)
    {
        EnumFacing face = state.getValue(FACING);
        double x = pos.getX(), z = pos.getZ();
        if (face.getAxis() == EnumFacing.Axis.X)
        {
            if (face == EnumFacing.EAST) x++;
            x += 0.1 * (rand.nextFloat() - rand.nextFloat());
            z += 0.5 + 0.5 * (rand.nextFloat() - rand.nextFloat());
        }
        else
        {
            if (face == EnumFacing.NORTH) z++;
            z += 0.1 * (rand.nextFloat() - rand.nextFloat());
            x += 0.5 + 0.5 * (rand.nextFloat() - rand.nextFloat());
        }
        Minecraft.getMinecraft().effectRenderer.addEffect(new ParticleForgeFlame(world, x, pos.getY() + 0.2d * rand.nextFloat(), z, 0d, 0.01d, 0d));
    }

    private ParticleForgeFlame(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn)
    {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);

        setParticleTextureIndex(48);

        this.motionX = xSpeedIn;
        this.motionY = ySpeedIn;
        this.motionZ = zSpeedIn;

        particleMaxAge += 80;
    }

    @Override
    public void onUpdate()
    {
        prevPosX = posX;
        prevPosY = posY;
        prevPosZ = posZ;

        move(motionX, motionY, motionZ);
        motionY *= 0.97d;
        if (particleMaxAge-- <= 0)
        {
            setExpired();
        }
    }
}
