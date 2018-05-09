package com.example.mypet.utils;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Log;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *     小米......
 */


public class PictureUtil {
    private static final String TAG = "PictureUtil";
    private static final String MyPetRootDirectory = Environment.getExternalStorageDirectory() + File.separator+"MyPet";

    public static String getMyPetRootDirectory(){
        return MyPetRootDirectory;
    }

    public static Uri getImageUri(Context context,Intent data){
        String imagePath = null;
        Uri uri = data.getData();
        if(Build.VERSION.SDK_INT >= 19){
            if(DocumentsContract.isDocumentUri(context,uri)){
                String docId = DocumentsContract.getDocumentId(uri);
                if("com.android.providers.media.documents".equals(uri.getAuthority())){
                    String id = docId.split(":")[1];
                    String selection = MediaStore.Images.Media._ID+"="+id;
                    imagePath = getImagePath(context,MediaStore.Images.Media.EXTERNAL_CONTENT_URI,selection);
                }else if("com.android.providers.downloads.documents".equals(uri.getAuthority())){
                    Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),Long.valueOf(docId));
                    imagePath = getImagePath(context,contentUri,null);
                }
            }else if("content".equalsIgnoreCase(uri.getScheme())){
                imagePath = getImagePath(context,uri,null);
            }else if("file".equalsIgnoreCase(uri.getScheme())){
                imagePath = uri.getPath();
            }
        }else{
            uri= data.getData();
            imagePath = getImagePath(context,uri,null);
        }
        File file = new File(imagePath);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(context,
                  "com.example.mypet.fileprovider", file);
        } else {
            uri = Uri.fromFile(file);
        }

        return uri;
    }

    private static String getImagePath(Context context,Uri uri, String selection) {
        String path = null;
        Cursor cursor = context.getContentResolver().query(uri,null,selection,null,null);
        if(cursor != null){
            if(cursor.moveToFirst()){
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    public static void mkdirMyPetRootDirectory(){
        boolean isSdCardExist = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);// 判断sdcard是否存在
        if (isSdCardExist) {
            File MyPetRoot = new File(getMyPetRootDirectory());
            if (!MyPetRoot.exists()) {
                try {
                    MyPetRoot.mkdir();
                    Log.d(TAG, "mkdir success");
                } catch (Exception e) {
                    Log.e(TAG, "exception->" + e.toString());
                }
            }
        }
    }
/*
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
    }*/

    /**
     * 根据 Uri 获取文件所在的位置
     *
     * @param context
     * @param uri
     * @return
     */
/*
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
    }*/

}
