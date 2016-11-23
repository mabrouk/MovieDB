package com.mabrouk.moviedb.common;

import android.media.MediaScannerConnection;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * Created by VPN on 11/30/2016.
 */

public class ExternalStorageUtil {

    private ExternalStorageUtil() {
        throw new IllegalStateException("ExternalStorageUtil shouldn't be instantiated and worked with only through the static methods");
    }

    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    public static boolean copyImageIntoExternalImageDirectory(File imageFile) {
        try {
            File galleryDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            File movieDBGalleryDir = new File(galleryDir, "MovieDB");
            if(!movieDBGalleryDir.exists() && !movieDBGalleryDir.mkdirs())
                return false;
            File newFile = new File(movieDBGalleryDir, imageFile.getName());
            if(!newFile.createNewFile())
                return false;
            copyFiles(imageFile, newFile);
            MediaScannerConnection.scanFile(MovieDBApplication.getInstance(),
                    new String[]{newFile.getPath()}, null, null);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static void copyFiles(File from, File to) throws IOException{
        FileInputStream fromInputStream = new FileInputStream(from);
        FileOutputStream toOutputStream = new FileOutputStream(to);
        FileChannel fromChannel = fromInputStream.getChannel();
        FileChannel toChannel = toOutputStream.getChannel();
        fromChannel.transferTo(0, fromChannel.size(), toChannel);
        fromInputStream.close();
        toOutputStream.close();
    }
}
