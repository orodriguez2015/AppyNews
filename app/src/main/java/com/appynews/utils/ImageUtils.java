package com.appynews.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Clase ImageUtils
 * Created by oscar on 18/06/16.
 */
public class ImageUtils {


    public static Bitmap getImageBitmap(String url) {
        Bitmap bm = null;

        try {
            URL aURL = new URL(url);
            URLConnection conn = aURL.openConnection();
            conn.connect();
            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            bm = BitmapFactory.decodeStream(bis);
            bis.close();
            is.close();
        } catch (IOException e) {
            LogCat.debug("Error getting bitmap: " + e.getMessage());
        }
        return bm;
    }


    /**
    private Bitmap GetImageBitmapFromUrl(String url)
    {
        Bitmap imageBitmap = null;



        using (var webClient = new WebClient())
        {
            var imageBytes = webClient.DownloadData(url);
            if (imageBytes != null && imageBytes.Length > 0)
            {
                imageBitmap = BitmapFactory.DecodeByteArray(imageBytes, 0, imageBytes.Length);
            }
        }

        return imageBitmap;
    }
        **/



}
