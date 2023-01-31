package com.yuzhou.basic.jvm.bytecode.type;

import java.util.Arrays;

public class U4 {

    private final byte[] value;

    public U4(byte b1, byte b2, byte b3, byte b4) {
        this.value = new byte[]{b1, b2, b3, b4};
    }

    public Integer toInt() {
        int a = (this.value[0] & 0xff) << 24;
        a |= (this.value[1] & 0xff) << 16;
        a |= (this.value[2] & 0xff) << 8;
        a |= this.value[3] & 0xff;
        return a;
    }

    public String toHex() {
        char[] hexChar = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        StringBuilder hexStr = new StringBuilder();
        for (int i = 3; i >= 0; i--) {
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
}
