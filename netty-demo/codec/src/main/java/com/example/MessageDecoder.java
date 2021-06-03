package com.example;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;


public class MessageDecoder extends ByteToMessageDecoder {


    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (in.readableBytes() < CommonProtocol.FRAME_SIZE) {
            return;
        }
        in.markReaderIndex();
        int readerIndex = in.readerIndex();
        byte magic = in.readByte();
        if (magic != CommonProtocol.MAGIC) {
            ctx.close();
            throw new RuntimeException("msg magic not match");
        }
        int cmd = in.readInt();
        int invokerId = in.readInt();
        int bodyLength = in.readInt();
        if (in.readableBytes() < bodyLength + Integer.BYTES) {
            in.resetReaderIndex();
            return;
        }
        byte[] body = new byte[bodyLength];
        in.readBytes(body);
        int crc32 = in.readInt();

        byte[] crc32Bytes = new byte[CommonProtocol.FRAME_SIZE + bodyLength];
        in.getBytes(readerIndex, crc32Bytes);
        if (crc32 != Crc32Util.crc32(crc32Bytes)) {
            ctx.close();
            throw new RuntimeException("crc32 check sum error");
        }
        CommonProtocol commonProtocol = new CommonProtocol();
        commonProtocol.setCmd(cmd);
        commonProtocol.setInvokerId(invokerId);
        commonProtocol.setBodyLength(bodyLength);
        commonProtocol.setBody(body);
        commonProtocol.setCrc32(crc32);
        out.add(commonProtocol);
    }
}
