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
}
