package com.example;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class MessageEncoder extends MessageToByteEncoder<CommonProtocol> {

    @Override
    protected void encode(ChannelHandlerContext ctx, CommonProtocol msg, ByteBuf out) throws Exception {
        out.markReaderIndex();
        out.writeByte(msg.getMagic());
        out.writeInt(msg.getCmd());
        out.writeInt(msg.getInvokerId());
        out.writeInt(msg.getBodyLength());
        out.writeBytes(msg.getBody());
        byte[] bytes = new byte[out.writerIndex()];
        out.readBytes(bytes);
        out.resetReaderIndex();
        out.writeInt(Crc32Util.crc32(bytes));
    }
}
