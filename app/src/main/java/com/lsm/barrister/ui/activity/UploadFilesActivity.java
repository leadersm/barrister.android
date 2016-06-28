package com.lsm.barrister.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
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
import com.lsm.barrister.ui.UIHelper;
import com.lsm.barrister.utils.DLog;
import com.lsm.barrister.utils.FileUtils;

import java.io.File;

/**
 * 上传职业资格证书页
 * (gnvqs，法律职业资格证书)、（certificate、执业证书）（year、年检页）（card、身份证）
 */
public class UploadFilesActivity extends BaseActivity {

    private static final String TAG = UploadFilesActivity.class.getSimpleName();


    private int picking = 0x100;

    private static final int PICK_GNVQS = 0x100;
    private static final int PICK_CERTIFICATE = 0x101;
    private static final int PICK_YEAR = 0x102;
    private static final int PICK_CARD = 0x103;
    private static final int PICK_CARD_BACK = 0x104;

    UploadFilesReq mUploadFilesReq;

    //(gnvqs，法律职业资格证书)、（certificate、执业证书）（year、年检页）（card、身份证）
    File gnvqs;//
    File certificate;
    File year;
    File card;
    File cardback;

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
                //TODO year、年检页
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
                picking = PICK_CARD_BACK;

                showChoosePicDialog();
            }
        });
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
            //(gnvqs，)、（certificate、）（year、）（card、身份证）
            UIHelper.showToast(getApplicationContext(), "法律职业资格证书没有填写");
            return;
        }
        if (certificate == null || !certificate.exists()) {
            //(gnvqs，)、（certificate、执业证书）（year、年检页）（card、身份证）
            UIHelper.showToast(getApplicationContext(), "执业证书没有填写");
            return;
        }
        if (year == null || !year.exists()) {
            //(gnvqs，)、（certificate、执业证书）（year、年检页）（card、身份证）
            UIHelper.showToast(getApplicationContext(), "法律职业资格证书年检页没有填写");
            return;
        }
        if (card == null || !card.exists()) {
            //(gnvqs，)、（certificate、执业证书）（year、年检页）（card、身份证）
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
                gnvqs = mPickingFile;
                break;
            case PICK_CERTIFICATE:
                mPickingFile = new File(AppConfig.getDir(Constants.imageDir), "zyzs.jpg");
                certificate = mPickingFile;
                break;
            case PICK_YEAR:
                mPickingFile = new File(AppConfig.getDir(Constants.imageDir), "zyzsnjy.jpg");
                year = mPickingFile;
                break;
            case PICK_CARD:
                mPickingFile = new File(AppConfig.getDir(Constants.imageDir), "idcard.jpg");
                card = mPickingFile;
                break;
            case PICK_CARD_BACK:
                mPickingFile = new File(AppConfig.getDir(Constants.imageDir), "idcardbk.jpg");
                cardback = mPickingFile;
                break;

        }

        if (which == 0) {//从相册选择

            pickFromGallery();
        } else {//拍照

            pickFromCamera(mPickingFile);

        }
    }

    /*
     * 从相册获取
     */
    private void pickFromGallery() {
        // 激活系统图库，选择一张图片
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        // 开启一个带有返回值的Activity，请求码为REQUEST_CODE_GALLERY
        startActivityForResult(intent, Constants.REQUEST_CODE_GALLERY);
    }

    /*
     * 从相机获取
     */
    private void pickFromCamera(File tempFile) {
        // 激活相机
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        // 判断存储卡是否可以用，可用进行存储
        if (AppConfig.isSDCardEnabled()) {

            // 从文件中创建uri
            Uri uri = Uri.fromFile(tempFile);

            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);

        } else {
            UIHelper.showToast(getApplicationContext(), R.string.tip_sdcard_error);
            return;
        }

        // 开启一个带有返回值的Activity，请求码为REQUEST_CODE_CAPTURE
        startActivityForResult(intent, Constants.REQUEST_CODE_CAPTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        DLog.d(TAG, "onActivityResult:" + resultCode + ",requestCode:" + requestCode);

        if (resultCode == RESULT_OK) {
            if (requestCode == Constants.REQUEST_CODE_GALLERY) {
                // 从相册返回的数据
                if (data != null) {

                    // 得到图片的全路径
                    Uri uri = data.getData();
                    showFile(uri);


                } else {
                    DLog.e(TAG, "没有返回图片");
                }

            } else if (requestCode == Constants.REQUEST_CODE_CAPTURE) {

                // 从相机返回的数据
                if (AppConfig.isSDCardEnabled()) {
                    showFile(Uri.fromFile(mPickingFile));
                } else {
                    UIHelper.showToast(getApplicationContext(), R.string.tip_sdcard_error);
                }

            }
        } else {
            DLog.e(TAG, "onActivityResult failed...");
        }

    }

    /**
     * 选完照片显示出来
     *
     * @param uri
     */
    private void showFile(Uri uri) {

        String name = mPickingFile.getName();

        SimpleDraweeView image = null;

        switch (picking) {
            case PICK_GNVQS:
                image = (SimpleDraweeView) findViewById(R.id.image_uploadfile_1);
                image.setImageURI(uri);
                if (gnvqs == null) {
                    gnvqs = new File(uri.toString());
                }
                break;
            case PICK_CERTIFICATE:
                image = (SimpleDraweeView) findViewById(R.id.image_uploadfile_2);
                image.setImageURI(uri);
                if (certificate == null) {
                    certificate = new File(uri.toString());
                }
                break;
            case PICK_YEAR:
                image = (SimpleDraweeView) findViewById(R.id.image_uploadfile_3);
                image.setImageURI(uri);
                if (year == null) {
                    year = new File(uri.toString());
                }
                break;
            case PICK_CARD:
                image = (SimpleDraweeView) findViewById(R.id.image_uploadfile_4);
                image.setImageURI(uri);
                if (card == null) {
                    card = new File(uri.toString());
                }
                break;
            case PICK_CARD_BACK:
                image = (SimpleDraweeView) findViewById(R.id.image_uploadfile_5);
                image.setImageURI(uri);
                if (cardback == null) {
                    cardback = new File(uri.toString());
                }
                break;
        }

        FileUtils.saveImageFile(image, name, new FileUtils.FileCallback() {
            @Override
            public void onFileCallback(File file) {
                DLog.d(TAG, "onSaveFileCallback:" + file.getAbsolutePath());
            }
        });

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mUploadFilesReq != null && mUploadFilesReq.isLoading()) {
            mUploadFilesReq.cancel();
            mUploadFilesReq = null;
        }
    }
}
