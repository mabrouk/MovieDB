package com.mabrouk.moviedb.common;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.Element;
import android.support.v8.renderscript.RenderScript;
import android.support.v8.renderscript.ScriptIntrinsicBlur;

import java.io.File;

/**
 * Created by VPN on 11/30/2016.
 */

public class BitmapUtils {

    public static BitmapFactory.Options bitmapOptionsForSize(File file, int width, int height) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(file.getAbsolutePath(), options);
        int sample = 1;
        int bitmapWidth = options.outWidth;
        int bitmapHeight = options.outHeight;
        int imageViewHeight = width;
        int imageViewWidth = height;

        if (bitmapHeight > imageViewHeight || bitmapWidth > imageViewWidth) {
            final int halfHeight = bitmapHeight / 2;
            final int halfWidth = bitmapWidth / 2;

            while ((halfHeight / sample) >= imageViewHeight
                    && (halfWidth / sample) >= imageViewWidth) {
                sample *= 2;
            }
        }
        options.inSampleSize = sample;
        options.inJustDecodeBounds = false;
        return options;
    }

    public static Bitmap blurBitmap(Bitmap bitmap){
        Bitmap outBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        RenderScript rs = RenderScript.create(MovieDBApplication.getInstance());
        ScriptIntrinsicBlur blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        blurScript.setRadius(25.f);

        Allocation allIn = Allocation.createFromBitmap(rs, bitmap);
        Allocation allOut = Allocation.createFromBitmap(rs, outBitmap);
        blurScript.setInput(allIn);
        blurScript.forEach(allOut);

        allOut.copyTo(outBitmap);

        rs.destroy();
        return outBitmap;
    }

}
