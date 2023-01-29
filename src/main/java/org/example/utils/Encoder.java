package org.example.utils;

import java.util.Arrays;
import java.util.Random;

public final class Encoder {
    public static byte[] encode(byte[] bytes){
        var result = new byte[bytes.length + 1];
        result[0] = (byte) new Random().nextInt(256);
        for (int i = 0; i < bytes.length; i++) {
            result[i + 1] = (byte) (result[0] ^ bytes[i]);
        }
        return result;
    }
    public static byte[] decode(byte[] bytes){
        var result = new byte[bytes.length - 1];
        for (int i = 1; i < bytes.length; i++) {
            result[i - 1] = (byte)(bytes[0] ^ bytes[i]);
        }
        return result;
    }
    public static String toHexString(byte[] bytes){
        var stringBuilder = new StringBuilder();
        for (var b : bytes) stringBuilder.append(wrap(Integer.toHexString((b + 256) % 256)));
        return stringBuilder.toString();
    }
    public static byte[] fromHexString(String str){
        var result = new byte[str.length() / 2];
        for (int i = 0; i < str.length() / 2; i++)
            result[i] = (byte) Integer.parseInt(str.substring(i * 2, (i + 1) * 2), 16);
        return result;
    }
    private static String wrap(String a){
        return (a.length() == 1 ? "0" : "") + a;
    }
}
