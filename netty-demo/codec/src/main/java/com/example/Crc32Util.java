package com.example;

import java.util.zip.CRC32;

public class Crc32Util {

    public static int crc32(byte[] bytes) {
        CRC32 crc32 = new CRC32();
        crc32.update(bytes);
        return (int) crc32.getValue();
    }

}
