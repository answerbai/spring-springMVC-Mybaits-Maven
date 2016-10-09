package com.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

public class IOUtil {

    private static final int DEFAULT_INITIAL_BUFFER_SIZE = 1 * 1024; // 1 kB
    private static final String DEFAULT_ENCODING = "UTF-8";// 默认编码

    /**
     * 将输入流按照字符编码转为字符串。
     * 
     * @param instream
     * @param encoding
     * @return
     * @throws IOException
     */
    public static String toString(InputStream instream, String encoding) throws IOException {
        return toString(instream, encoding, DEFAULT_INITIAL_BUFFER_SIZE);
    }

    /**
     * 
     * 将输入流按照字符编码转为字符串。 如果知道输入流字节数contentLength（如Content-Length头），则可以根据contentLength分配buffer大小，提高效率，
     * 当不知道contentLength，此参数也可以作为buffer大小
     * 例如，如果已知输入流大小为80B，那么contentLength参数应为80+1=81，更可以提高效率。多分配1B可以区分缓冲区满与全部读取完毕。
     * 
     * @param instream 输入流
     * @param encoding 字符编码格式
     * @param contentLength 预计输入流字节数（buffer大小）
     * @return
     * @throws IOException
     */
    public static String toString(InputStream instream, String encoding, int contentLength)
            throws IOException {
        if (instream == null) {
            return null;
        }
        if (encoding == null) {
            encoding = DEFAULT_ENCODING;
        }
        if (contentLength < 2) {
            contentLength = DEFAULT_INITIAL_BUFFER_SIZE;
        }
        byte[] buffer = new byte[contentLength];
        int len = instream.read(buffer);
        if (len <= 0) {
            return "";
        }
        if (len < contentLength) {
            try {
                return new String(buffer, 0, len, encoding);
            } catch (UnsupportedEncodingException e) {
                return new String(buffer, 0, len, DEFAULT_ENCODING);
            }
        } else {
            ByteArrayOutputStream outstream = new ByteArrayOutputStream(2 * contentLength);
            outstream.write(buffer, 0, len);
            while ((len = instream.read(buffer)) > 0) {
                outstream.write(buffer, 0, len);
            }
            outstream.close();// no effect
            try {
                return outstream.toString(encoding);
            } catch (UnsupportedEncodingException e) {
                return outstream.toString(DEFAULT_ENCODING);
            }
        }
    }

}
