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
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.androidquery.AQuery;
import com.facebook.drawee.view.SimpleDraweeView;
import com.lsm.barrister.R;
import com.lsm.barrister.app.AppConfig;
import com.lsm.barrister.app.Constants;
import com.lsm.barrister.data.entity.Case;
import com.lsm.barrister.data.io.Action;
import com.lsm.barrister.data.io.app.UpdateCaseProgressReq;
import com.lsm.barrister.module.pick.RxImageConverters;
import com.lsm.barrister.module.pick.RxImagePicker;
import com.lsm.barrister.module.pick.Sources;
import com.lsm.barrister.ui.UIHelper;
import com.lsm.barrister.utils.FileUtils;

import java.io.File;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by lvshimin on 16/8/20.
 *
 * 更新，上传
 * caseId（案源id），hasContract(是否已签订合同),money(合同金额),pic（合同照片）
 */
public class UpdateCaseActivity extends BaseActivity{

    private static final String TAG = UpdateCaseActivity.class.getSimpleName();

    AQuery aq;

    Case item;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_case_progress);
        setupToolbar();

        item = (Case) getIntent().getSerializableExtra("item");

        aq = new AQuery(this);
        aq.id(R.id.cb_update_case).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean isChecked = aq.id(R.id.cb_update_case).isChecked();

                aq.id(R.id.et_update_money).enabled(isChecked);
//                aq.id(R.id.et_update_address).enabled(isChecked);
//                aq.id(R.id.et_update_company).enabled(isChecked);
                aq.id(R.id.btn_choose_agreement_pic).enabled(isChecked);

            }
        });

        aq.id(R.id.btn_commit).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doCommit();
            }
        });

        aq.id(R.id.btn_choose_agreement_pic).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChoosePicDialog();
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

    boolean isLoading = false;

    private void doCommit() {

        if(isLoading){
            return;
        }

        if(!aq.id(R.id.cb_update_case).isChecked()){
            return;
        }

        //金额
        String money = aq.id(R.id.et_update_money).getEditable().toString();
        if(TextUtils.isEmpty(money)){
            UIHelper.showToast(getApplicationContext(),getString(R.string.tip_empty_bill_money));
            return;
        }

        //平台分成金额
        String percentagePayment = aq.id(R.id.et_update_percentage_money).getEditable().toString();
        if(TextUtils.isEmpty(percentagePayment)){
            UIHelper.showToast(getApplicationContext(),getString(R.string.tip_empty_bill_percentage_money));
            return;
        }
//
//        //地址
//        String address = aq.id(R.id.et_update_address).getEditable().toString();
//        if(TextUtils.isEmpty(address)){
//            UIHelper.showToast(getApplicationContext(),getString(R.string.tip_empty_bill_address));
//            return;
//        }

        new UpdateCaseProgressReq(this,item.getId(),true,money,percentagePayment,agreementFile).execute(new Action.Callback<Boolean>() {

            @Override
            public void progress() {
                isLoading = true;
                progressDialog.setMessage(getString(R.string.loading));
                progressDialog.show();
            }

            @Override
            public void onError(int errorCode, String msg) {
                isLoading = false;
                progressDialog.dismiss();
            }

            @Override
            public void onCompleted(Boolean aBoolean) {
                progressDialog.dismiss();
                isLoading = false;
                UIHelper.showToast(getApplicationContext(),"提交成功");
                finish();
            }
        });

    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.update_case);
    }

    /**
     * 从相册选择or拍照
     *
     * @param which
     */
    protected void doSelectChooseMode(int which) {

        agreementFile = new File(AppConfig.getDir(Constants.imageDir), "CN_DLS_"+item.getId()+".jpg");

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


    File agreementFile;

    /**
     * 选完照片显示出来
     */
    private void handleResult(File file) {
        new BmTask(file).execute();
    }

    class BmTask extends AsyncTask<Void,Void,Bitmap>{

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
//                return FileUtils.getSmallBitmap(file.getAbsolutePath(),(int)targetRatio*width,(int)targetRatio*height);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bm) {
            super.onPostExecute(bm);

            final SimpleDraweeView image = (SimpleDraweeView) findViewById(R.id.image_update_agreement);

            if(bm!=null){
                FileUtils.saveImageFile(bm, AppConfig.getDir(Constants.imageDir), file.getName(), new FileUtils.FileCallback() {
                    @Override
                    public void onFileCallback(File file) {

                        bitmap = null;

                        progressDialog.dismiss();
                        image.setImageURI(Uri.fromFile(file));
                        agreementFile = file;
                    }
                });
            }else{
                progressDialog.dismiss();
                image.setImageURI(Uri.fromFile(file));
                agreementFile = file;
                bitmap = null;

            }


        }


    }

    private File createTempFile() {
        return new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), System.currentTimeMillis() + "_image.jpeg");
    }

}
