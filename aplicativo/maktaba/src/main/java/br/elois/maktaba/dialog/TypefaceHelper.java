package br.elois.maktaba.dialog;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v4.util.SimpleArrayMap;

/**
 * Created by leo on 13/04/16.
 */

public class TypefaceHelper {

    private static final SimpleArrayMap<String, Typeface> cache = new SimpleArrayMap<>();

    public static Typeface get(Context c, String name) {
        synchronized (cache) {
            if (!cache.containsKey(name)) {
                try {
                    Typeface t = Typeface.createFromAsset(
                            c.getAssets(), String.format("fonts/%s", name));
                    cache.put(name, t);
                    return t;
                } catch (RuntimeException e) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        try {
                            c.getResources().getFont(c.getResources().getIdentifier(name, "font", c.getPackageName()));
                        }catch (Resources.NotFoundException f){
                            f.printStackTrace();
                            return null;
                        }
                    }
                    return null;
                }
            }
            return cache.get(name);
        }
    }
}