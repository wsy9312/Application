package com.sd.storage.ui.main.picture;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.FileProvider;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;


import com.sd.storage.BuildConfig;
import com.sd.storage.R;
import com.sd.storage.util.FileUtils;
import com.sd.storage.util.ImageFactory;
import com.sd.storage.util.ToastUtils;


import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 拍照，选择照片 fragment
 */

public class PictureFragmentDialogV2 extends DialogFragment implements View.OnClickListener{

    public static final String TAG = "tag";

    private static final int PERMISSION_REQUEST_CODE_CAMEAR = 11;

    private static final int RESULT_CAPTURE_IMAGE = 1;
    private static final int RESULT_CHOOSE_PHOTO = 2;
    private static final int CROP_CODE = 3;

    private Uri pictureUri;
    private Uri cropImageUri;

    private Context mContext;
    private boolean isClip = true;
    private String picturePath;

    private int outputX = 500, outputY = 500;
    private int aspectX = 1, aspectY = 1;
    private ImageFactory imageFactory;

    private OnResultPicturPathListener mOnResultPicturPathListener;

    public PictureFragmentDialogV2() {
    }

    public static PictureFragmentDialogV2 getInstance(boolean isClip, int outputX, int outputY, int aspectX, int aspectY) {
        PictureFragmentDialogV2 dialogFragment = new PictureFragmentDialogV2();
        dialogFragment.isClip = isClip;
        dialogFragment.outputX = outputX;
        dialogFragment.outputY = outputY;
        dialogFragment.aspectX = aspectX;
        dialogFragment.aspectY = aspectY;
        return dialogFragment;
    }

    public static PictureFragmentDialogV2 getInstance() {
        PictureFragmentDialogV2 dialogFragment = new PictureFragmentDialogV2();
        dialogFragment.isClip = false;
        return dialogFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Dialog_NoActionBar_MinWidth);
        mContext = getActivity();
        imageFactory = new ImageFactory();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_camera_control, container, false);
        view.findViewById(R.id.btn_open_camera).setOnClickListener(this);
        view.findViewById(R.id.btn_choose_img).setOnClickListener(this);
        view.findViewById(R.id.btn_cancel).setOnClickListener(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Window dialogWindow = getDialog().getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        dialogWindow.setWindowAnimations(R.style.dialogstyle); // 添加动画
        WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        lp.width = getResources().getDisplayMetrics().widthPixels; // 宽度

        dialogWindow.setAttributes(lp);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if(activity instanceof OnResultPicturPathListener){
            mOnResultPicturPathListener = (OnResultPicturPathListener) activity;
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_open_camera) {// 吊起相机
            openCamera();

        } else if (i == R.id.btn_choose_img) {// 选择拍照
            openPictures();

        } else if (i == R.id.btn_cancel) {// 取消
            dismiss();

        }
    }

    /**
     * 拍照
     */
    public void openCamera() {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {

            File dir = new File(Environment.getExternalStorageDirectory() + "/" + getString(R.string.app_name) + "/pictures");
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File file = new File(dir, new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".png");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                pictureUri = FileProvider.getUriForFile(mContext, BuildConfig.APPLICATION_ID + ".provider", file);
            }else{
                pictureUri = Uri.fromFile(file);
            }

            picturePath = file.getPath();

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if(getActivity().checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED ||
                        getActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED ||
                        getActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                    requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE_CAMEAR);
                }else{
                    startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE).putExtra(MediaStore.EXTRA_OUTPUT, pictureUri), RESULT_CAPTURE_IMAGE);
                }
            }else{
                startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE).putExtra(MediaStore.EXTRA_OUTPUT, pictureUri), RESULT_CAPTURE_IMAGE);
            }
        } else {
            ToastUtils.showBaseToast(getString(R.string.sdcard_missing), getActivity());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == PERMISSION_REQUEST_CODE_CAMEAR){
            int grantResult = grantResults[0];
            if(grantResult == PackageManager.PERMISSION_GRANTED){
                startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE).putExtra(MediaStore.EXTRA_OUTPUT, pictureUri), RESULT_CAPTURE_IMAGE);
            }
        }
    }

    /**
     * 从图库选择图片
     */
    public void openPictures() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, RESULT_CHOOSE_PHOTO);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case RESULT_CAPTURE_IMAGE:
                if (Activity.RESULT_OK == resultCode) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        pictureUri = getImageContentUri(new File(picturePath));
                    }
                    if (this.isClip) {
                        startPhotoZoom(this.pictureUri);
                    } else {
                        setPicToViewNoClip();
                    }
                }
                break;
            case RESULT_CHOOSE_PHOTO:
                if (Activity.RESULT_OK == resultCode) {
                    this.pictureUri = data.getData();
                    this.picturePath = FileUtils.getUriFilePath(mContext, this.pictureUri);

                    if (this.isClip) {
                        startPhotoZoom(this.pictureUri);
                    } else {
                        setPicToViewNoClip();
                    }
                }
                break;
            case CROP_CODE:
                if (Activity.RESULT_OK == resultCode) {
                    setPicToView(cropImageUri);
                }
        }
    }

    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    public void startPhotoZoom(Uri uri) {

        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            File dir = new File(Environment.getExternalStorageDirectory() + "/" + getString(R.string.app_name) + "/pictures/crop");
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File file = new File(dir, new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".png");
            cropImageUri = Uri.fromFile(file);
            Intent intent = new Intent("com.android.camera.action.CROP");
            intent.setDataAndType(uri, "image/*");
            intent.putExtra("crop", "true");
            intent.putExtra("aspectX", aspectX);
            intent.putExtra("aspectY", aspectY);
            intent.putExtra("outputX", outputX);
            intent.putExtra("outputY", outputY);
            intent.putExtra("scale", true);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, cropImageUri);
            intent.putExtra("outputFormat", Bitmap.CompressFormat.PNG.toString());
            intent.putExtra("noFaceDetection", false); // no face detection
            startActivityForResult(intent, CROP_CODE);
        } else {
            ToastUtils.showBaseToast(getString(R.string.sdcard_picture_missing), getActivity());
        }
    }

    public Uri getImageContentUri(File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = mContext.getContentResolver().query( MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[] { MediaStore.Images.Media._ID }, MediaStore.Images.Media.DATA + "=? ", new String[] { filePath }, null);

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return mContext.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }

    /**
     * 保存裁剪之后的图片数据
     *
     * @param uri
     */
    private void setPicToView(Uri uri) {
        Bitmap mBitmap = decodeUriAsBitmap(uri);
        if (null != mBitmap) {
            picturePath = FileUtils.getUriFilePath(mContext, uri);
            saveFile(mBitmap, picturePath);
            mOnResultPicturPathListener.onResultPicturePathClipBitmap(this, picturePath, mBitmap);
        } else {
            ToastUtils.showBaseToast(getString(R.string.get_pictrue_failure), mContext);
        }
    }

    /**
     * uri 转换bitmap
     * @param uri
     * @return
     */
    public Bitmap decodeUriAsBitmap(Uri uri) {
        Bitmap bitmap;
        try {
            bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(uri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return bitmap;
    }

    /**
     * 拍照结果回掉
     * @param mOnResultPicturPathListener
     */
    public void setOnResultPicturPathListener(OnResultPicturPathListener mOnResultPicturPathListener) {
        this.mOnResultPicturPathListener = mOnResultPicturPathListener;
    }

    /**
     * 没剪切保存地址 图片  和文件
     */
    public void setPicToViewNoClip() {

        Bitmap mBitmap = imageFactory.ratio(picturePath, 480, 800);
        // File saveflie = new File(picturePath);
        //使用尺寸压缩过的图片 避免文件过大
        saveFile(mBitmap, picturePath);
        if(null != mOnResultPicturPathListener){
            mOnResultPicturPathListener.onResultPicturePathClipBitmap(this, picturePath, mBitmap);
        }
    }


    /**
     * bitmap 转换为file文件
     *
     * @param bm
     * @param path
     * @return
     */
    public static File saveFile(Bitmap bm, String path) {
//        String myCapturefileName = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".png";
        File myCaptureFile = new File(path);
        if(!myCaptureFile.isDirectory()){
            myCaptureFile.mkdir();
        }
        BufferedOutputStream bos = null;
        try {
            bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
            bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
            bos.flush();
            bos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return myCaptureFile;
    }

    public interface OnResultPicturPathListener {
        // 返回原图path，裁剪后的Bitmap
        void onResultPicturePathClipBitmap(DialogFragment dialogFragment, String picturePath, Bitmap bitmap);
    }

}
