package me.spikey.specialarmour.utils;

public class ByteArrays {
    private byte[] index;
    private byte[] levels;

    public ByteArrays(byte[] index, byte[] levels) {
        this.index = index;

        this.levels = levels;
    }

    public byte[] getIndex() {
        return index;
    }

    public byte[] getLevels() {
        return levels;
    }
}
