package com.example.mypet.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Log;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *     小米......
 */


public class MiPictureUtil {
    private static final String TAG = "MiPictureUtil";

    public static boolean isXiaomi(){
        String carrier = android.os.Build.MANUFACTURER;
        final Pattern XIAOMI = Pattern.compile("xiaomi",Pattern.CASE_INSENSITIVE);
        Matcher matcher = XIAOMI.matcher(carrier);
        return matcher.find();
    }

    private static Uri getPictureUri(Context mContext, Uri uri) {
        if (uri.getScheme().equals("file")) {
            String path = uri.getEncodedPath();
            if (path != null) {
                path = Uri.decode(path);
                ContentResolver cr =mContext.getContentResolver();
                StringBuffer buff = new StringBuffer();
                buff.append("(").append(MediaStore.Images.ImageColumns.DATA).append("=")
                        .append("'" + path + "'").append(")");
                Cursor cur = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        new String[] { MediaStore.Images.ImageColumns._ID },
                        buff.toString(), null, null);
                int index = 0;
                for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {
                    index = cur.getColumnIndex(MediaStore.Images.ImageColumns._ID);
                    // set _id value
                    index = cur.getInt(index);
                }
                Log.e(TAG,"in getPictureUri->"+index);
                if (index == 0) {
                    // do nothing
                } else {

                    Uri uri_temp = Uri
                            .parse("content://media/external/images/media/"
                                    + index);
                    if (uri_temp != null) {
                        uri = uri_temp;
                    }
                }
            }
        }
        return uri;
    }


    public static Uri getUri(Context context, Uri uri){
            File file = new File(getRealPathFromURI(context,uri));
            return FileProvider.getUriForFile(context,
                    "com.example.mypet.fileprovider", file);
    }

    /**
     * 根据 Uri 获取文件所在的位置
     *
     * @param context
     * @param uri
     * @return
     */

    public static String getRealPathFromURI(Context context, Uri uri) {
        Uri contentUri = getPictureUri(context,uri);
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

}
