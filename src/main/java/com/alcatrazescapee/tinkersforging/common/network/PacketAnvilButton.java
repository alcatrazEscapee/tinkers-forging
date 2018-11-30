/*
 * Part of the Tinkers Forging Mod by alcatrazEscapee
 * Work under Copyright. Licensed under the GPL-3.0.
 * See the project LICENSE.md for more information.
 */

package com.alcatrazescapee.tinkersforging.common.network;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import com.alcatrazescapee.alcatrazcore.AlcatrazCore;
import com.alcatrazescapee.tinkersforging.common.container.ContainerTinkersAnvil;
import io.netty.buffer.ByteBuf;

public class PacketAnvilButton implements IMessage
{
    private int buttonId;

    @SuppressWarnings("unused")
    public PacketAnvilButton() {}

    public PacketAnvilButton(int buttonId)
    {
        this.buttonId = buttonId;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        buttonId = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(buttonId);
    }

    public static class Handler implements IMessageHandler<PacketAnvilButton, IMessage>
    {
        @Override
        public IMessage onMessage(PacketAnvilButton message, MessageContext ctx)
        {
            EntityPlayer player = AlcatrazCore.getProxy().getPlayer(ctx);
            if (player.openContainer instanceof ContainerTinkersAnvil)
            {
                ContainerTinkersAnvil container = (ContainerTinkersAnvil) player.openContainer;
                AlcatrazCore.getProxy().getThreadListener(ctx).addScheduledTask(() -> container.onReceivePacket(message.buttonId));
            }
            return null;
        }
    }
}
