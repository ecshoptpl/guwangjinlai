package com.jinguanguke.guwangjinlai.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;

/**
 * Created by jin on 16/4/30.
 */
public class ImageUtils {
    public static Bitmap codec(Bitmap src, Bitmap.CompressFormat format,
                               int quality) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        src.compress(format, quality, os);

        byte[] array = os.toByteArray();
        return BitmapFactory.decodeByteArray(array, 0, array.length);
    }
}
