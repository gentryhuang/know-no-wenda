
package com.know.wenda.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * GzipUtil
 *
 * @author hlb
 */
public class GzipUtil {
    private static final int buffLen = 1024;

    /**
     * 压缩数据
     */
    public static byte[] compress(byte[] data) throws Exception {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        GZIPOutputStream out = new GZIPOutputStream(bytes);
        out.write(data);
        out.finish();
        return bytes.toByteArray();
    }

    /**
     * 解压数据
     */
    public static byte[] decompress(byte[] compressedData) throws Exception {
        return decompress(new ByteArrayInputStream(compressedData), compressedData.length);
    }

    /**
     * 解压数据
     */
    public static byte[] decompress(InputStream inputStream) throws IOException {
        return decompress(inputStream, buffLen);
    }

    private static byte[] decompress(InputStream inputStream, int len) throws IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream(len);
        GZIPInputStream in = new GZIPInputStream(inputStream);
        byte[] buffer = new byte[len];

        while (true) {
            int bytesRead = in.read(buffer, 0, buffer.length);
            if (bytesRead < 0) {
                return bytes.toByteArray();
            }

            bytes.write(buffer, 0, bytesRead);
        }

    }
}