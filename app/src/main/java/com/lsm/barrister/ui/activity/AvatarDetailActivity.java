package com.lsm.barrister.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;

import com.androidquery.AQuery;
import com.facebook.drawee.view.SimpleDraweeView;
import com.lsm.barrister.R;
import com.lsm.barrister.app.AppConfig;
import com.lsm.barrister.app.Constants;
import com.lsm.barrister.app.UserHelper;
import com.lsm.barrister.data.entity.User;
import com.lsm.barrister.data.io.Action;
import com.lsm.barrister.data.io.app.LoginReq;
import com.lsm.barrister.data.io.app.UploadUserIconReq;
import com.lsm.barrister.ui.UIHelper;
import com.lsm.barrister.utils.DLog;
import com.lsm.barrister.utils.FileUtils;

import java.io.File;

/****
 * 用户详情信息页
 */
public class AvatarDetailActivity extends BaseActivity implements UserHelper.UserActionListener{

    private static final String TAG = AvatarDetailActivity.class.getSimpleName();

    AQuery aq;

    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avatar_detail);
        aq = new AQuery(this);

        setupToolbar();

        user = AppConfig.getUser(this);

        if (user == null) {
            System.err.println("没有找到用户。。。");
            return;
        }

        DLog.d(TAG, user.toString());

        init();

        UserHelper.getInstance().addOnUserActionListener(this);

        updateVerifyStatus();
    }

    private void updateVerifyStatus() {
        if(!user.getVerifyStatus().equals(User.STATUS_SUCCESS)){
            new LoginReq(this,user.getPhone(),user.getVerifyCode()).execute(new Action.Callback<User>() {

                @Override
                public void progress() {

                }

                @Override
                public void onError(int errorCode, String msg) {

                }

                @Override
                public void onCompleted(User user) {

                    if(user!=null){

                        AppConfig.setUser(getApplicationContext(),user);

                        AvatarDetailActivity.this.user = user;

                        UserHelper.getInstance().notifyUpdateUser();
                    }
                }
            });
        }
    }

    private void init() {
        aq.id(R.id.tv_userdetail_name).text(user.getName());
        aq.id(R.id.btn_user_name).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(user.getName())) {
                    UIHelper.goModifyInfoActivity(AvatarDetailActivity.this, User.KEY_NAME, user.getName());
                }
            }
        });

        if (!TextUtils.isEmpty(user.getUserIcon())) {
            SimpleDraweeView userIconView = (SimpleDraweeView) findViewById(R.id.image_userdetail_icon);
            userIconView.setImageURI(Uri.parse(user.getUserIcon()));
        }

        //头像
        aq.id(R.id.btn_user_icon).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChoosePicDialog();
            }
        });

        aq.id(R.id.tv_userdetail_phone).text(user.getPhone());

        //电话
        aq.id(R.id.btn_user_phone).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        aq.id(R.id.tv_userdetail_email).text(user.getEmail());

        //邮箱
        aq.id(R.id.btn_user_email).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO 邮箱
                UIHelper.goModifyInfoActivity(AvatarDetailActivity.this, User.KEY_EMAIL, user.getEmail());
            }
        });


        aq.id(R.id.tv_userdetail_area).text(user.getArea());
        //地区
        aq.id(R.id.btn_user_area).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO 地区（省+市）
                UIHelper.goModifyInfoActivity(AvatarDetailActivity.this, User.KEY_AREA, user.getArea());
            }
        });

        aq.id(R.id.tv_userdetail_goodat).text(user.getGoodAt());
        //擅长
        aq.id(R.id.btn_user_goodat).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO 邮箱
                UIHelper.goModifyBizActivity(AvatarDetailActivity.this,User.KEY_GOOD_AT);
            }
        });

        aq.id(R.id.tv_userdetail_company).text(user.getLawOffice());
        //律所
        aq.id(R.id.btn_user_company).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO 律所
                UIHelper.goModifyInfoActivity(AvatarDetailActivity.this, User.KEY_COMPANY, user.getLawOffice());
            }
        });

        String statusString = null;
        int statusColor = Color.parseColor("#cccccc");
        if (TextUtils.isEmpty(user.getVerifyStatus()) || user.getVerifyStatus().equals(User.STATUS_UNAUTHERIZED)) {
            statusString = "未认证";
            statusColor = Color.parseColor("#cccccc");
        } else if (user.getVerifyStatus().equals(User.STATUS_SUCCESS)) {
            statusString = "认证成功";
            statusColor = Color.parseColor("#31F77E");//0x31F77E;
        } else if (user.getVerifyStatus().equals(User.STATUS_VERIFYING)) {
            statusString = "验证中";
            statusColor = Color.parseColor("#59E1FA");//0x59E1FA;
        } else if (user.getVerifyStatus().equals(User.STATUS_FAILED)) {
            statusString = "认证失败";
            statusColor =  Color.parseColor("#FF0000");//0xFF0000;
        }

        aq.id(R.id.tv_userdetail_verify_status).text(statusString).textColor(statusColor);

        //资质上传
        aq.id(R.id.btn_user_upload_files).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (user.getVerifyStatus().equals(User.STATUS_UNAUTHERIZED) || user.getVerifyStatus().equals(User.STATUS_FAILED)) {
                    UIHelper.goUploadFilesActitiy(AvatarDetailActivity.this);
                }
            }
        });


        //工作年限需要重新计算，后台给出参加工作时间，根据当前年-开始时间
        aq.id(R.id.tv_userdetail_workinglife).text(user.getEmploymentYears());
        //从业时间
        aq.id(R.id.btn_user_working_life).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO 邮箱employmentYears
                UIHelper.goModifyInfoActivity(AvatarDetailActivity.this, User.KEY_EMPLOYMENT_YEAR, user.getEmploymentYears());
            }
        });

        aq.id(R.id.tv_userdetail_introduction).text(user.getIntroduction());
        //个人简介
        aq.id(R.id.btn_user_introduction).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO 个人简介
                UIHelper.goModifyInfoActivity(AvatarDetailActivity.this, User.KEY_INTRODUCTION, user.getIntroduction());
            }
        });
    }

    private void updateUserInfo() {
        aq.id(R.id.tv_userdetail_name).text(user.getName());

        if (!TextUtils.isEmpty(user.getUserIcon())) {
            SimpleDraweeView userIconView = (SimpleDraweeView) findViewById(R.id.image_userdetail_icon);
            userIconView.setImageURI(Uri.parse(user.getUserIcon()));
        }

        //头像
        aq.id(R.id.btn_user_icon).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChoosePicDialog();
            }
        });

        aq.id(R.id.tv_userdetail_phone).text(user.getPhone());

        //电话
        aq.id(R.id.btn_user_phone).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        aq.id(R.id.tv_userdetail_email).text(user.getEmail());

        aq.id(R.id.tv_userdetail_area).text(user.getArea());

        aq.id(R.id.tv_userdetail_goodat).text(user.getGoodAt());

        aq.id(R.id.tv_userdetail_company).text(user.getLawOffice());

        String statusString = null;
        int statusColor = Color.parseColor("#cccccc");
        if (TextUtils.isEmpty(user.getVerifyStatus()) || user.getVerifyStatus().equals(User.STATUS_UNAUTHERIZED)) {
            statusString = "未认证";
            statusColor = Color.parseColor("#cccccc");
        } else if (user.getVerifyStatus().equals(User.STATUS_SUCCESS)) {
            statusString = "认证成功";
            statusColor = Color.parseColor("#31F77E");//0x31F77E;
        } else if (user.getVerifyStatus().equals(User.STATUS_VERIFYING)) {
            statusString = "验证中";
            statusColor = Color.parseColor("#59E1FA");//0x59E1FA;
        } else if (user.getVerifyStatus().equals(User.STATUS_FAILED)) {
            statusString = "认证失败";
            statusColor =  Color.parseColor("#FF0000");//0xFF0000;
        }

        aq.id(R.id.tv_userdetail_verify_status).text(statusString).textColor(statusColor);

        //资质上传
        aq.id(R.id.btn_user_upload_files).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (user.getVerifyStatus().equals(User.STATUS_UNAUTHERIZED)) {
                    UIHelper.goUploadFilesActitiy(AvatarDetailActivity.this);
                } else if (user.getVerifyStatus().equals(User.STATUS_SUCCESS)) {
                } else if (user.getVerifyStatus().equals(User.STATUS_VERIFYING)) {
                } else if (user.getVerifyStatus().equals(User.STATUS_FAILED)) {
                    UIHelper.goUploadFilesActitiy(AvatarDetailActivity.this);
                }
            }
        });

        //工作年限需要重新计算，后台给出参加工作时间，根据当前年-开始时间
        aq.id(R.id.tv_userdetail_workinglife).text(user.getEmploymentYears());


        aq.id(R.id.tv_userdetail_introduction).text(user.getIntroduction());
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.title_user_detail);
    }


    @Override
    protected void onResume() {
        super.onResume();
        user = AppConfig.getUser(this);
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


    /**
     * 从相册选择or拍照
     *
     * @param which
     */
    protected void doSelectChooseMode(int which) {
        tempFile = new File(AppConfig.getDir(Constants.imageDir), "temp.jpg");

        if (which == 0) {//从相册选择
            pickFromGallery();
        } else {//拍照
            pickFromCamera();
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

    File tempFile;

    /*
     * 从相机获取
     */
    private void pickFromCamera() {
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
                    crop(uri);
                } else {
                    DLog.e(TAG, "没有返回图片");
                }

            } else if (requestCode == Constants.REQUEST_CODE_CAPTURE) {
                // 从相机返回的数据
                if (AppConfig.isSDCardEnabled()) {
                    crop(Uri.fromFile(tempFile));
                } else {
                    UIHelper.showToast(getApplicationContext(), R.string.tip_sdcard_error);
                }

            } else if (requestCode == Constants.REQUEST_CODE_CROP) {
                // 从剪切图片返回的数据

                SimpleDraweeView avatar = (SimpleDraweeView) findViewById(R.id.image_userdetail_icon);
                avatar.setImageURI(Uri.fromFile(tempFile));

                try {
                    upload(tempFile);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else if (requestCode == Constants.REQUEST_CODE_FROM_USER_DETAIL) {

                String key = data.getStringExtra(Constants.KEY);
                String content = data.getStringExtra(Constants.KEY_CONTENT);

                switch (key) {
                    case User.KEY_NAME:
                        user.setName(content);
                        aq.id(R.id.tv_userdetail_name).text(user.getName());
                        break;
                    case User.KEY_COMPANY:
                        user.setLawOffice(content);
                        aq.id(R.id.tv_userdetail_company).text(user.getLawOffice());
                        break;
                    case User.KEY_AREA:
                        user.setArea(content);
                        aq.id(R.id.tv_userdetail_area).text(user.getArea());
                        break;
                    case User.KEY_EMAIL:
                        user.setEmail(content);
                        aq.id(R.id.tv_userdetail_email).text(user.getEmail());
                        break;
                    case User.KEY_INTRODUCTION:
                        user.setIntroduction(content);
                        aq.id(R.id.tv_userdetail_introduction).text(user.getIntroduction());
                        break;
                    case User.KEY_EMPLOYMENT_YEAR:
                        user.setEmploymentYears(content);
                        aq.id(R.id.tv_userdetail_workinglife).text(user.getEmploymentYears());
                        break;
                }

                AppConfig.setUser(getApplicationContext(), user);

                UserHelper.getInstance().notifyUpdateUser();

            }
        } else {
            DLog.e(TAG, "onActivityResult failed...");
        }

    }

    private void upload(File file) throws Exception {

        new UploadUserIconReq(this, file).execute(new Action.Callback<User>() {

            @Override
            public void progress() {

            }

            @Override
            public void onError(int errorCode, String msg) {
                DLog.e(TAG, msg);
                //失败，提示是否重新上传
                uploadFailed();
            }

            @Override
            public void onCompleted(User user) {

                if (user != null) {

                    //上传成功
                    FileUtils.deleteFile(tempFile);

                    AvatarDetailActivity.this.user.setUserIcon(user.getUserIcon());

                    AppConfig.setUser(AvatarDetailActivity.this, AvatarDetailActivity.this.user);

                    //更新用户信息
                    UserHelper.getInstance().notifyUpdateUser();

                }
            }
        });

    }

    private void uploadFailed() {
        //失败，提示是否重新上传
        new AlertDialog.Builder(AvatarDetailActivity.this)
                .setTitle(R.string.title_dialog_upload_failed)
                .setMessage(R.string.msg_dialog_upload_failed)
                .setPositiveButton(R.string.cancel, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setNegativeButton(R.string.ok, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                try {
                    upload(tempFile);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                dialog.dismiss();
            }
        }).create().show();
    }

    /*
     * 剪切图片
     */
    private void crop(final Uri uri) {
        // 裁剪图片意图
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // 裁剪框的比例，1：1
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // 裁剪后输出图片的尺寸大小
        intent.putExtra("outputX", 144);
        intent.putExtra("outputY", 144);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
        intent.putExtra("outputFormat", "JPEG");// 图片格式
        intent.putExtra("noFaceDetection", true);// 取消人脸识别
        intent.putExtra("return-data", false);

        // 开启一个带有返回值的Activity，请求码为REQUEST_CODE_CROP
        startActivityForResult(intent, Constants.REQUEST_CODE_CROP);
    }


    @Override
    public void onSSOLoginCallback(User user) {

    }

    @Override
    public void onLoginCallback(User user) {

    }

    @Override
    public void onLogoutCallback() {

    }

    @Override
    public void onUpdateUser() {
        this.user = AppConfig.getUser(this);
        updateUserInfo();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UserHelper.getInstance().removeListener(this);
    }
}
