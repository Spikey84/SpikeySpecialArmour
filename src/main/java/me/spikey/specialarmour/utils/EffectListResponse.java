package me.spikey.specialarmour.utils;

import me.spikey.specialarmour.customEffects.Effect;

import java.util.HashMap;

public record EffectListResponse(EffectChangeUtil.response response,
                                 HashMap<Effect, Byte> effects) {
}
