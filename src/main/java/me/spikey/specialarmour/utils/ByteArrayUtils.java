package me.spikey.specialarmour.utils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import me.spikey.specialarmour.customEffects.Effect;
import me.spikey.specialarmour.customEffects.EffectManager;
import org.apache.commons.lang.ArrayUtils;

import java.util.HashMap;
import java.util.List;

public class ByteArrayUtils {

    public static ByteArrays encode(ByteArrays original, byte id, byte level) {
        List<Byte> ids = Lists.newArrayList(ArrayUtils.toObject(original.getIndex()));
        List<Byte> levels = Lists.newArrayList(ArrayUtils.toObject(original.getLevels()));

        ids.add(id);
        levels.add(level);

        return new ByteArrays(ArrayUtils.toPrimitive(ids.toArray(new Byte[0])), ArrayUtils.toPrimitive(levels.toArray(new Byte[0])));
    }

    public static ByteArrays encode(byte id, byte level) {
        return new ByteArrays(new byte[]{id}, new byte[]{level});
    }

    public static HashMap<Effect, Byte> decode(ByteArrays original, EffectManager effectManager) {
        HashMap<Effect, Byte> output = Maps.newHashMap();

        byte[] ids = original.getIndex();
        byte[] levels = original.getLevels();

        for (int x = 0; x < ids.length; x++) {
            output.put(effectManager.getEffectFromID(ids[x]), levels[x]);
        }

        return output;
    }

    public static ByteArrays remove(ByteArrays original, byte id) {
        byte[] ids = original.getIndex();
        byte[] levels = original.getLevels();

        for (int x = 0; x < ids.length; x++) {
            if (id != ids[x]) continue;
            ids = ArrayUtils.remove(ids, x);
            levels = ArrayUtils.remove(levels, x);
            break;
        }

        return new ByteArrays(ids, levels);
    }
}
