package com.lsm.barrister.ui.activity;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.androidquery.AQuery;
import com.facebook.drawee.view.SimpleDraweeView;
import com.lsm.barrister.R;
import com.lsm.barrister.app.AppConfig;
import com.lsm.barrister.app.Constants;
import com.lsm.barrister.app.UserHelper;
import com.lsm.barrister.data.entity.User;
import com.lsm.barrister.data.io.Action;
import com.lsm.barrister.data.io.app.UploadFilesReq;
import com.lsm.barrister.module.pick.RxImageConverters;
import com.lsm.barrister.module.pick.RxImagePicker;
import com.lsm.barrister.module.pick.Sources;
import com.lsm.barrister.ui.UIHelper;
import com.lsm.barrister.utils.DLog;
import com.lsm.barrister.utils.FileUtils;

import java.io.File;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * 上传职业资格证书页
 * (gnvqs，法律职业资格证书)、（certificate、执业证书）（agreementFile、年检页）（card、身份证）
 */
public class UploadFilesActivity extends BaseActivity {

    private static final String TAG = UploadFilesActivity.class.getSimpleName();


    private int picking = 0x100;

    private static final int PICK_GNVQS = 0x1000;
    private static final int PICK_CERTIFICATE = 0x1001;
    private static final int PICK_YEAR = 0x1002;
    private static final int PICK_CARD = 0x1003;
//    private static final int PICK_CARD_BACK = 0x1004;

    UploadFilesReq mUploadFilesReq;

    //(gnvqs，法律职业资格证书)、（certificate、执业证书）（agreementFile、年检页）（card、身份证）
    File gnvqs;//
    File certificate;
    File year;
    File card;
//    File cardback;

    AQuery aq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_files);

        setupToolbar();

        aq = new AQuery(this);
        aq.id(R.id.btn_file_1).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO gnvqs，选择法律职业资格证书
                picking = PICK_GNVQS;

                showChoosePicDialog();
            }
        });
        aq.id(R.id.btn_file_2).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO certificate、执业证书

                picking = PICK_CERTIFICATE;

                showChoosePicDialog();
            }
        });
        aq.id(R.id.btn_file_3).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO agreementFile、年检页
                picking = PICK_YEAR;

                showChoosePicDialog();
            }
        });
        aq.id(R.id.btn_file_4).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO card、身份证
                picking = PICK_CARD;

                showChoosePicDialog();
            }
        });

        aq.id(R.id.btn_file_5).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO card、身份证反面
//                picking = PICK_CARD_BACK;

                showChoosePicDialog();
            }
        }).gone();

        aq.id(R.id.btn_upload_commit).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doCommit();
            }
        });
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("上传资料");
    }

    /**
     * 提交
     */
    private void doCommit() {
        if (gnvqs == null || !gnvqs.exists()) {
            //(gnvqs，)、（certificate、）（agreementFile、）（card、身份证）
            UIHelper.showToast(getApplicationContext(), "法律职业资格证书没有填写");
            return;
        }
        if (certificate == null || !certificate.exists()) {
            //(gnvqs，)、（certificate、执业证书）（agreementFile、年检页）（card、身份证）
            UIHelper.showToast(getApplicationContext(), "执业证书没有填写");
            return;
        }
        if (year == null || !year.exists()) {
            //(gnvqs，)、（certificate、执业证书）（agreementFile、年检页）（card、身份证）
            UIHelper.showToast(getApplicationContext(), "法律职业资格证书年检页没有填写");
            return;
        }
        if (card == null || !card.exists()) {
            //(gnvqs，)、（certificate、执业证书）（agreementFile、年检页）（card、身份证）
            UIHelper.showToast(getApplicationContext(), "身份证没有填写");
            return;
        }

        mUploadFilesReq = new UploadFilesReq(this, gnvqs, certificate, year, card);
        mUploadFilesReq.execute(new Action.Callback<Boolean>() {
            @Override
            public void progress() {
                progressDialog.setMessage("上传中，请稍候...");
                progressDialog.setCancelable(false);
                progressDialog.show();

            }

            @Override
            public void onError(int errorCode, String msg) {

                progressDialog.dismiss();

                UIHelper.showToast(getApplicationContext(), "上传失败:" + msg);
            }

            @Override
            public void onCompleted(Boolean aBoolean) {
                progressDialog.dismiss();

                UIHelper.showToast(getApplicationContext(), "上传成功，请等待审核");

                //保存状态
                User user = AppConfig.getUser(getApplicationContext());
                user.setVerifyStatus(User.STATUS_VERIFYING);
                AppConfig.setUser(getApplicationContext(), user);

                UserHelper.getInstance().notifyUpdateUser();

                finish();
            }
        });
    }


    /**
     * 显示选择照片对对话框
     * 从相册选择，拍照
     */
    private void showChoosePicDialog() {
        // TODO Auto-generated method stub
        final String[] items = new String[]{getString(R.string.pick_img_from_album), getString(R.string.take_photo)};

        new AlertDialog.Builder(this)
                .setTitle(R.string.title_choose_pic)
                .setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                        doSelectChooseMode(which);
                    }

                }).create().show();
    }


    File mPickingFile = null;

    /**
     * 从相册选择or拍照
     *
     * @param which
     */
    protected void doSelectChooseMode(int which) {

        switch (picking) {
            case PICK_GNVQS:
                mPickingFile = new File(AppConfig.getDir(Constants.imageDir), "flzgzs.jpg");
//                gnvqs = new File(mPickingFile.getAbsolutePath());
                break;
            case PICK_CERTIFICATE:
                mPickingFile = new File(AppConfig.getDir(Constants.imageDir), "zyzs.jpg");
//                certificate = new File(mPickingFile.getAbsolutePath());
                break;
            case PICK_YEAR:
                mPickingFile = new File(AppConfig.getDir(Constants.imageDir), "zyzsnjy.jpg");
//                agreementFile = new File(mPickingFile.getAbsolutePath());
                break;
            case PICK_CARD:
                mPickingFile = new File(AppConfig.getDir(Constants.imageDir), "idcard.jpg");
//                card = new File(mPickingFile.getAbsolutePath());
                break;
//            case PICK_CARD_BACK:
//                mPickingFile = new File(AppConfig.getDir(Constants.imageDir), "idcardbk.jpg");
//                cardback = mPickingFile;
//                break;

        }

        if (which == 0) {//从相册选择

            RxImagePicker.with(this).requestImage(Sources.GALLERY)
                    .flatMap(new Func1<Uri, Observable<File>>() {
                        @Override
                        public Observable<File> call(Uri uri) {
                            return RxImageConverters.uriToFile(getApplicationContext(), uri,createTempFile());
                        }
                    }).subscribe(new Action1<File>() {

                @Override
                public void call(File file) {
                    handleResult(file);
                }
            });

        } else {//拍照

            RxImagePicker.with(this).requestImage(Sources.CAMERA)
                    .flatMap(new Func1<Uri, Observable<File>>() {
                        @Override
                        public Observable<File> call(Uri uri) {
                            return RxImageConverters.uriToFile(getApplicationContext(), uri,createTempFile());
                        }
                    }).subscribe(new Action1<File>() {

                @Override
                public void call(File file) {
                    handleResult(file);
                }
            });

        }
    }

    private File createTempFile() {
        return new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), System.currentTimeMillis() + "_image.jpeg");
    }

    /**
     * 选完照片显示出来
     */
    private void handleResult(File file) {
        new BmTask(file).execute();
    }

    /**
     * 选完照片显示出来
     */
    private void showFile(File file) {

        SimpleDraweeView image = null;

        if(file==null && !file.exists()){
            DLog.e(TAG,"文件没有找到");
            return;
        }

        Uri uri = Uri.fromFile(file);

        switch (picking) {
            case PICK_GNVQS:
                image = (SimpleDraweeView) findViewById(R.id.image_uploadfile_1);
                image.setImageURI(uri);
                gnvqs = file;
                break;
            case PICK_CERTIFICATE:
                image = (SimpleDraweeView) findViewById(R.id.image_uploadfile_2);
                image.setImageURI(uri);
                certificate = file;
                break;
            case PICK_YEAR:
                image = (SimpleDraweeView) findViewById(R.id.image_uploadfile_3);
                image.setImageURI(uri);
                year = file;
                break;
            case PICK_CARD:
                image = (SimpleDraweeView) findViewById(R.id.image_uploadfile_4);
                image.setImageURI(uri);
                card = file;
                break;
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mUploadFilesReq != null && mUploadFilesReq.isLoading()) {
            mUploadFilesReq.cancel();
            mUploadFilesReq = null;
        }
    }

    class BmTask extends AsyncTask<Void,Void,Bitmap> {

        File file;
        Bitmap bitmap;

        public BmTask(File file) {
            this.file = file;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("正在处理图片...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Bitmap doInBackground(Void... params) {

            bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());

            int height = bitmap.getHeight();
            int width = bitmap.getWidth();

            float targetRatio = 0 ;

            Log.d(TAG,"height:"+height+",width:"+width);

            if(height>1920 || width >1080){

//            targetHeight = height = 1920;
//            targetWidth = (int) (height/ratio);

                if(height>1920){
                    targetRatio = (float) 1920/height;
                }else if(width>1080){
                    targetRatio =  (float) 1080/width;
                }

                return FileUtils.ratio(bitmap,targetRatio);
//                return FileUtils.ratio(bitmap,targetRatio*width,targetRatio*height);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bm) {
            super.onPostExecute(bm);

            if(bm!=null){
                FileUtils.saveImageFile(bm, AppConfig.getDir(Constants.imageDir), file.getName(), new FileUtils.FileCallback() {
                    @Override
                    public void onFileCallback(File file) {

                        bitmap = null;
                        progressDialog.dismiss();
                        showFile(file);
                    }
                });
            }else{
                progressDialog.dismiss();
                showFile(file);
                bitmap = null;

            }


        }
    }

}
