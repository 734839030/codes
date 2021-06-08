package com.example;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

public class CommonProtocol implements Serializable {

    public static final byte MAGIC = 0x12;
    public static final int MAX_FRAME_SIZE = 20480;
    public static final int FRAME_SIZE = Byte.BYTES + Integer.BYTES * 3;
    private static final byte[] EMPTY_BODY = new byte[0];
    private static final AtomicInteger ATOMIC_INTEGER = new AtomicInteger();
    private byte magic = MAGIC;
    private int cmd;
    private int invokerId;
    private int bodyLength;
    private byte[] body;
    // 收发报自动处理
    private int crc32;

    public CommonProtocol() {
    }

    public CommonProtocol(int cmd, byte[] body) {
        this(cmd, ATOMIC_INTEGER.incrementAndGet(), body);
    }

    public CommonProtocol(int cmd, int invokerId, byte[] body) {
        this.cmd = cmd;
        this.invokerId = invokerId;
        this.body = body != null ? body : EMPTY_BODY;
        bodyLength = this.body.length;
    }

    public int getTotalLength() {
        return FRAME_SIZE + bodyLength + Integer.BYTES;
    }

    public byte getMagic() {
        return magic;
    }

    public void setMagic(byte magic) {
        this.magic = magic;
    }

    public int getCmd() {
        return cmd;
    }

    public void setCmd(int cmd) {
        this.cmd = cmd;
    }

    public int getInvokerId() {
        return invokerId;
    }

    public void setInvokerId(int invokerId) {
        this.invokerId = invokerId;
    }

    public int getBodyLength() {
        return bodyLength;
    }

    public void setBodyLength(int bodyLength) {
        this.bodyLength = bodyLength;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body != null ? body : EMPTY_BODY;
        this.bodyLength = this.body.length;
    }

    public int getCrc32() {
        return crc32;
    }

    public void setCrc32(int crc32) {
        this.crc32 = crc32;
    }

}
