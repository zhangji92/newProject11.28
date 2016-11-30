package com.yoka.mrskin.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.util.Log;

public class YKFile
{

    /**
     * 文件保存与读取功能实现类
     * 
     * @author Administrator
     * 
     *         2010-6-28 下午08:15:18
     */
    private static final String TAG = "YKFile";

    private static Context context = null;

    public static void setContext(Context c) {
        context = c;
    }

    /**
     * 保存文件
     * 
     * @param fileName
     *            文件名
     * @param content
     *            文件内容
     */
    public static void save(String fileName, byte[] data) {
        if (context == null)
            return;
        if (data == null)
            return;

        if (context == null)
            return;

        // 由于页面输入的都是文本信息，所以当文件名不是以.txt后缀名结尾时，自动加上.txt后缀
        if (!fileName.endsWith(".txt")) {
            fileName = fileName + ".txt";
        }

        Log.e(TAG, fileName);

        // Context.MODE_PRIVATE：为默认操作模式，代表该文件是私有数据，只能被应用本身访问，在该模式下，写入的内容会覆盖原文件的内容，如果想把新写入的内容追加到原文件中。可以使用Context.MODE_APPEND
        // Context.MODE_APPEND：模式会检查文件是否存在，存在就往文件追加内容，否则就创建新文件。
        // Context.MODE_WORLD_READABLE和Context.MODE_WORLD_WRITEABLE用来控制其他应用是否有权限读写该文件。
        // MODE_WORLD_READABLE：表示当前文件可以被其他应用读取；MODE_WORLD_WRITEABLE：表示当前文件可以被其他应用写入。
        // 如果希望文件被其他应用读和写，可以传入：
        // openFileOutput("output.txt", Context.MODE_WORLD_READABLE +
        // Context.MODE_WORLD_WRITEABLE);
        FileOutputStream fos = null;
        try {
            fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            fos.write(data);
        } catch (Exception e) {
        } finally {
            try {
                fos.close();
            } catch (Exception e) {
            }
        }
    }

    /**
     * 读取文件内容
     * 
     * @param fileName
     *            文件名
     * @return 文件内容
     * @throws Exception
     */
    public static byte[] read(String fileName) {
        if (context == null)
            return null;

        byte[] data = null;
        // 由于页面输入的都是文本信息，所以当文件名不是以.txt后缀名结尾时，自动加上.txt后缀
        if (!fileName.endsWith(".txt")) {
            fileName = fileName + ".txt";
        }

        FileInputStream fis = null;
        ByteArrayOutputStream baos = null;
        try {
            fis = context.openFileInput(fileName);
            baos = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int len = 0;

            // 将读取后的数据放置在内存中---ByteArrayOutputStream
            while ((len = fis.read(buf)) != -1) {
                baos.write(buf, 0, len);
            }

            data = baos.toByteArray();
        } catch (Exception e) {

        } finally {
            try {
                fis.close();
                baos.close();
            } catch (Exception e) {
            }
        }

        // 返回内存中存储的数据
        return data;

    }

    public static boolean remove(String fileName) {
        if (context == null)
            return false;
        // 由于页面输入的都是文本信息，所以当文件名不是以.txt后缀名结尾时，自动加上.txt后缀
        if (!fileName.endsWith(".txt")) {
            fileName = fileName + ".txt";
        }
        return context.deleteFile(fileName);
    }
    
    /**
     * 保存文件到指定的路径下面
     * @param bitmap
     */
    @SuppressLint("SdCardPath")
    public static String saveCroppedImage(Bitmap bmp) {
        File file = new File("/sdcard/myFolder");
        if (!file.exists())
            file.mkdir();

        file = new File("/sdcard/temp.jpg".trim());
        String fileName = file.getName();
        String mName = fileName.substring(0, fileName.lastIndexOf("."));
        String sName = fileName.substring(fileName.lastIndexOf("."));

        // /sdcard/myFolder/temp_cropped.jpg
        String newFilePath = "/sdcard/myFolder" + "/" + mName + "_cropped" + sName;
        file = new File(newFilePath);
        try {
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(CompressFormat.JPEG, 50, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file.getAbsolutePath();
    }

}
