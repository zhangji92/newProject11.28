package com.yoka.mrskin.model.managers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;

import com.aijifu.skintest.api.SkinData;
import com.yoka.mrskin.addimage.BitmapUtil;
import com.yoka.mrskin.model.managers.base.YKManager;

/**
 * 将肌肤测试的各个部位的结果图保存在本地
 * 
 * @author z l l
 * 
 */
public class YKSavaSkinImageManager extends YKManager
{
    private static YKSavaSkinImageManager singleton = null;
    private static Object lock = new Object();
    private static final String RIGHT_FACE = "right_face_";
    private static final String LEFT_FACE = "left_face_";
    private static final String HEAD = "head_";
    private static final String JAW = "jaw_";
    private static final String PART_T = "part_t_";
    private static final String TYPE = ".jpg";
    private ArrayList<Bitmap> mRightBitmaps, mLeftBitmaps, mHeadBitmaps,
            mJawBitmaps, mPartTBitmaps;
    private ArrayList<String> mRightPaths, mLeftPaths, mHeadPaths, mJawPaths,
            mPartTPaths;
    private ArrayList<ArrayList<String>> mWholeList;
    private String[] item = { "origin", "color", "moisture", "uniformity",
            "holes", "microgroove", "stain" };
    private File mFile;
    private String mOrignPath;

    public static YKSavaSkinImageManager getInstance() {
        synchronized (lock) {
            if (singleton == null) {
                singleton = new YKSavaSkinImageManager();
            }
        }
        return singleton;
    }

    public ArrayList<ArrayList<String>> getWholeFilePath() {
        mWholeList = new ArrayList<ArrayList<String>>();
        if (mRightPaths != null) {
            mWholeList.add(mRightPaths);
        }
        if (mLeftPaths != null) {
            mWholeList.add(mLeftPaths);
        }
        if (mHeadPaths != null) {
            mWholeList.add(mHeadPaths);
        }
        if (mJawPaths != null) {
            mWholeList.add(mJawPaths);
        }
        if (mPartTPaths != null) {
            mWholeList.add(mPartTPaths);
        }
        return mWholeList;
    }

    public String getWholeOriginimgPath() {
        if (mOrignPath != null) {
            return mOrignPath;
        }
        return null;
    }

    public ArrayList<String> getRightImgPath() {
        if (mRightPaths != null) {
            return mRightPaths;
        }
        return null;
    }

    public ArrayList<String> getLeftImgPath() {
        if (mLeftPaths != null) {
            return mLeftPaths;
        }
        return null;
    }

    public ArrayList<String> getHeadImgPath() {
        if (mHeadPaths != null) {
            return mHeadPaths;
        }
        return null;
    }

    public ArrayList<String> getJawImgPath() {
        if (mJawPaths != null) {
            return mJawPaths;
        }
        return null;
    }

    public ArrayList<String> getPartTImgPath() {
        if (mPartTPaths != null) {
            return mPartTPaths;
        }
        return null;
    }

    public void initBitmapList(Context context, SkinData data) {
        mRightPaths = new ArrayList<String>();
        mLeftPaths = new ArrayList<String>();
        mHeadPaths = new ArrayList<String>();
        mJawPaths = new ArrayList<String>();
        mPartTPaths = new ArrayList<String>();

//        mRightBitmaps = new ArrayList<Bitmap>();
//        mRightBitmaps.add(data.getFaceRight().getOrigin().getImg());
//        mRightBitmaps.add(data.getFaceRight().getColor().getImg());
//        mRightBitmaps.add(data.getFaceRight().getMoisture().getImg());
//        mRightBitmaps.add(data.getFaceRight().getUniformity().getImg());
//        mRightBitmaps.add(data.getFaceRight().getHoles().getImg());
//        mRightBitmaps.add(data.getFaceRight().getMicrogroove().getImg());
//        mRightBitmaps.add(data.getFaceRight().getStain().getImg());
//
//        mLeftBitmaps = new ArrayList<Bitmap>();
//        mLeftBitmaps.add(data.getFaceLeft().getOrigin().getImg());
//        mLeftBitmaps.add(data.getFaceLeft().getColor().getImg());
//        mLeftBitmaps.add(data.getFaceLeft().getMoisture().getImg());
//        mLeftBitmaps.add(data.getFaceLeft().getUniformity().getImg());
//        mLeftBitmaps.add(data.getFaceLeft().getHoles().getImg());
//        mLeftBitmaps.add(data.getFaceLeft().getMicrogroove().getImg());
//        mLeftBitmaps.add(data.getFaceLeft().getStain().getImg());
//
//        mHeadBitmaps = new ArrayList<Bitmap>();
//        mHeadBitmaps.add(data.getHead().getOrigin().getImg());
//        mHeadBitmaps.add(data.getHead().getColor().getImg());
//        mHeadBitmaps.add(data.getHead().getMoisture().getImg());
//        mHeadBitmaps.add(data.getHead().getUniformity().getImg());
//        mHeadBitmaps.add(data.getHead().getHoles().getImg());
//        mHeadBitmaps.add(data.getHead().getMicrogroove().getImg());
//        mHeadBitmaps.add(data.getHead().getStain().getImg());
//
//        mJawBitmaps = new ArrayList<Bitmap>();
//        mJawBitmaps.add(data.getJaw().getOrigin().getImg());
//        mJawBitmaps.add(data.getJaw().getColor().getImg());
//        mJawBitmaps.add(data.getJaw().getMoisture().getImg());
//        mJawBitmaps.add(data.getJaw().getUniformity().getImg());
//        mJawBitmaps.add(data.getJaw().getHoles().getImg());
//        mJawBitmaps.add(data.getJaw().getMicrogroove().getImg());
//        mJawBitmaps.add(data.getJaw().getStain().getImg());
//
//        mPartTBitmaps = new ArrayList<Bitmap>();
//        mPartTBitmaps.add(data.getPartT().getOrigin().getImg());
//        mPartTBitmaps.add(data.getPartT().getColor().getImg());
//        mPartTBitmaps.add(data.getPartT().getMoisture().getImg());
//        mPartTBitmaps.add(data.getPartT().getUniformity().getImg());
//        mPartTBitmaps.add(data.getPartT().getHoles().getImg());
//        mPartTBitmaps.add(data.getPartT().getMicrogroove().getImg());
//        mPartTBitmaps.add(data.getPartT().getStain().getImg());

        if (data != null) {
            saveImage(context, data);
            releaseData();
            System.gc();
        }
    }

    private void saveImage(Context context, SkinData data) {
        mFile = new File(Environment.getExternalStorageDirectory() + "/"
                + "fujunskinimage", "whole_origin" + TYPE);
//        try {
////            BitmapUtil.saveImageToSD(context, mFile.getAbsolutePath(),
////                    data.getOriginImg(), 80);
//            mOrignPath = mFile.getAbsolutePath();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        for (int i = 0; i < mRightBitmaps.size(); i++) {
            mFile = new File(Environment.getExternalStorageDirectory() + "/"
                    + "fujunskinimage", RIGHT_FACE + item[i] + TYPE);
            try {
                BitmapUtil.saveImageToSD(context, mFile.getAbsolutePath(),
                        mRightBitmaps.get(i), 100);
                mRightPaths.add(mFile.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        for (int i = 0; i < mLeftBitmaps.size(); i++) {
            mFile = new File(Environment.getExternalStorageDirectory() + "/"
                    + "fujunskinimage", LEFT_FACE + item[i] + TYPE);
            try {
                BitmapUtil.saveImageToSD(context, mFile.getAbsolutePath(),
                        mLeftBitmaps.get(i), 100);
                mLeftPaths.add(mFile.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        for (int i = 0; i < mHeadBitmaps.size(); i++) {
            mFile = new File(Environment.getExternalStorageDirectory() + "/"
                    + "fujunskinimage", HEAD + item[i] + TYPE);
            try {
                BitmapUtil.saveImageToSD(context, mFile.getAbsolutePath(),
                        mHeadBitmaps.get(i), 100);
                mHeadPaths.add(mFile.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        for (int i = 0; i < mJawBitmaps.size(); i++) {
            mFile = new File(Environment.getExternalStorageDirectory() + "/"
                    + "fujunskinimage", JAW + item[i] + TYPE);
            try {
                BitmapUtil.saveImageToSD(context, mFile.getAbsolutePath(),
                        mJawBitmaps.get(i), 100);
                mJawPaths.add(mFile.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        for (int i = 0; i < mPartTBitmaps.size(); i++) {
            mFile = new File(Environment.getExternalStorageDirectory() + "/"
                    + "fujunskinimage", PART_T + item[i] + TYPE);
            try {
                BitmapUtil.saveImageToSD(context, mFile.getAbsolutePath(),
                        mPartTBitmaps.get(i), 100);
                mPartTPaths.add(mFile.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void deleteFile() {
        if (mOrignPath != null) {
            deleteFile(mOrignPath);
        }

        if (mRightPaths != null) {
            for (int i = 0; i < mRightPaths.size(); i++) {
                deleteFile(mRightPaths.get(i));
            }
        }

        if (mLeftPaths != null) {
            for (int i = 0; i < mLeftPaths.size(); i++) {
                deleteFile(mLeftPaths.get(i));
            }
        }

        if (mHeadPaths != null) {
            for (int i = 0; i < mHeadPaths.size(); i++) {
                deleteFile(mHeadPaths.get(i));
            }
        }

        if (mJawPaths != null) {
            for (int i = 0; i < mJawPaths.size(); i++) {
                deleteFile(mJawPaths.get(i));
            }
        }

        if (mPartTPaths != null) {
            for (int i = 0; i < mPartTPaths.size(); i++) {
                deleteFile(mPartTPaths.get(i));
            }
        }
    }

    public boolean deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.isFile() && file.exists()) {
            return file.delete();
        }
        return false;
    }

    public void releaseData() {
        if (mRightBitmaps != null) {
            for (int i = 0; i < mRightBitmaps.size(); i++) {
                mRightBitmaps.get(i).recycle();
                @SuppressWarnings("unused")
                Bitmap temp = mRightBitmaps.get(i);
                temp = null;
            }
            mRightBitmaps.clear();
            mRightBitmaps = null;
        }
        if (mLeftBitmaps != null) {
            for (int i = 0; i < mLeftBitmaps.size(); i++) {
                mLeftBitmaps.get(i).recycle();
            }
            mLeftBitmaps.clear();
            mLeftBitmaps = null;
        }
        if (mHeadBitmaps != null) {
            for (int i = 0; i < mHeadBitmaps.size(); i++) {
                mHeadBitmaps.get(i).recycle();
            }
            mHeadBitmaps.clear();
            mHeadBitmaps = null;
        }
        if (mJawBitmaps != null) {
            for (int i = 0; i < mJawBitmaps.size(); i++) {
                mJawBitmaps.get(i).recycle();
            }
            mJawBitmaps.clear();
            mJawBitmaps = null;
        }
        if (mPartTBitmaps != null) {
            for (int i = 0; i < mPartTBitmaps.size(); i++) {
                mPartTBitmaps.get(i).recycle();
            }
            mPartTBitmaps.clear();
            mPartTBitmaps = null;
        }

        // if (mRightPaths != null) {
        // mRightPaths.clear();
        // mRightPaths = null;
        // }
        // if (mLeftPaths != null) {
        // mLeftPaths.clear();
        // mLeftPaths = null;
        // }
        // if (mHeadPaths != null) {
        // mHeadPaths.clear();
        // mHeadPaths = null;
        // }
        // if (mJawPaths != null) {
        // mJawPaths.clear();
        // mJawPaths = null;
        // }
        // if (mPartTPaths != null) {
        // mPartTPaths.clear();
        // mPartTPaths = null;
        // }
        // if (mWholeList != null) {
        // mWholeList.clear();
        // mWholeList = null;
        // }
        // if (mFile != null) {
        // mFile = null;
        // }
        // if (mOrignPath != null) {
        // mOrignPath = null;
        // }
    }
}
