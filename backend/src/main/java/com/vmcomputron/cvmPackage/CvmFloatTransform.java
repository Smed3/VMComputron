//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.vmcomputron.cvmPackage;

import java.nio.ByteBuffer;

public class CvmFloatTransform {
    public CvmFloatTransform() {
    }

    static float floatFrom(int lowValue, int highValue) {
        ByteBuffer buffIntToFloat = ByteBuffer.allocate(12).putInt(lowValue + (highValue << 16));
        return buffIntToFloat.getFloat(0);
    }

    static int lowShortOf(float value) {
        ByteBuffer buffFloatInt = ByteBuffer.allocate(12).putFloat(value);
        return buffFloatInt.getInt(2) >> 16 & '\uffff';
    }

    static int highShortOf(float value) {
        ByteBuffer buffFloatInt = ByteBuffer.allocate(12).putFloat(value);
        return buffFloatInt.getInt(0) >> 16 & '\uffff';
    }
}
