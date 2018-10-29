/*
 * Part of the Tinkers Forging Mod by alcatrazEscapee
 * Work under Copyright. Licensed under the GPL-3.0.
 * See the project LICENSE.md for more information.
 */

package com.alcatrazescapee.tinkersforging.common.network;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import com.alcatrazescapee.tinkersforging.common.container.ContainerTinkersAnvil;
import io.netty.buffer.ByteBuf;

public class PacketAnvilButton implements IMessage
{
    private int buttonId;

    // no args constructor required for forge
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
            EntityPlayerMP serverPlayer = ctx.getServerHandler().player;
            if (serverPlayer.openContainer instanceof ContainerTinkersAnvil)
            {
                ContainerTinkersAnvil container = (ContainerTinkersAnvil) serverPlayer.openContainer;
                serverPlayer.getServerWorld().addScheduledTask(() ->
                        container.onReceivePacket(message.buttonId)
                );
            }
            return null;
        }
    }
}
