package com.yuzhou.basic.jvm.bytecode.type;

import java.util.Arrays;

public class U2 {

    private final byte[] value;

    public U2(byte b1, byte b2) {
        this.value = new byte[]{b1, b2};
    }

    public Integer toInt() {
        return (this.value[0] & 0xff) << 8 | (this.value[1] & 0xff);
    }

    public String toHex() {
        char[] hexChar = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        StringBuilder hexStr = new StringBuilder();
        for (int i = 1; i >= 0; i--) {
            int v = this.value[i] & 0xff;
            while (v > 0) {
                int c = v % 16;
                v = v >>> 4;
                hexStr.insert(0, hexChar[c]);
            }
            if ((hexStr.length() & 0x01) == 1) {
                hexStr.insert(0, '0');
            }
        }
        return "0x" + (hexStr.length() == 0 ? "00" : hexStr.toString());
    }

    @Override
    public String toString() {
        return this.toHex();
    }

    public static void main(String[] args) {
        U2 u2 = new U2(Byte.parseByte("1"),Byte.parseByte("2"));
        System.out.println(u2.toHex());
        System.out.println(u2.toInt());
    }
}
