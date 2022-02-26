package me.spikey.specialarmour.customEffects;

import java.awt.*;

public interface Effect {
    String name();
    byte id();
    Color color();
    boolean stack();
}
