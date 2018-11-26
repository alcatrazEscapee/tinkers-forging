/*
 * Part of the Tinkers Forging Mod by alcatrazEscapee
 * Work under Copyright. Licensed under the GPL-3.0.
 * See the project LICENSE.md for more information.
 */

package com.alcatrazescapee.tinkersforging.common.network;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.common.network.ByteBufUtils;

import com.alcatrazescapee.alcatrazcore.network.PacketTContainerUpdate;
import com.alcatrazescapee.tinkersforging.common.capability.CapabilityForgeItem;
import com.alcatrazescapee.tinkersforging.common.capability.IForgeItem;
import io.netty.buffer.ByteBuf;

@ParametersAreNonnullByDefault
public class PacketUpdateForgeItem extends PacketTContainerUpdate<IForgeItem, NBTTagCompound>
{
    @SuppressWarnings("unused")
    public PacketUpdateForgeItem()
    {
        super(CapabilityForgeItem.CAPABILITY);
    }

    public PacketUpdateForgeItem(int windowID, int slotNumber, IForgeItem instance)
    {
        super(CapabilityForgeItem.CAPABILITY, null, windowID, slotNumber, instance);
    }

    public PacketUpdateForgeItem(int windowID, NonNullList<ItemStack> items)
    {
        super(CapabilityForgeItem.CAPABILITY, null, windowID, items);
    }

    @Override
    protected NBTTagCompound readCapability(IForgeItem instance)
    {
        return instance.serializeNBT();
    }

    @Override
    protected void serializeCapability(ByteBuf buf, NBTTagCompound nbt)
    {
        ByteBufUtils.writeTag(buf, nbt);
    }

    @Override
    protected NBTTagCompound deserializeCapability(ByteBuf buf)
    {
        return ByteBufUtils.readTag(buf);
    }

    public static class Handler extends PacketTContainerUpdate.Handler<IForgeItem, NBTTagCompound, PacketUpdateForgeItem>
    {
        @Override
        public void applyCapability(ItemStack stack, IForgeItem instance, NBTTagCompound nbt)
        {
            instance.deserializeNBT(nbt);
        }
    }
}
