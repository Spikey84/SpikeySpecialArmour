package me.spikey.specialarmour.utils;

public record ByteArrays(byte[] index, byte[] levels) {

    public byte[] getIndex() {
        return index;
    }

    public byte[] getLevels() {
        return levels;
    }
}
