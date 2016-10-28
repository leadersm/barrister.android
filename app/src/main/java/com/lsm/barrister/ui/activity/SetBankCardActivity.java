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
import com.lsm.barrister.app.UserHelper;
import com.lsm.barrister.data.entity.Account;
import com.lsm.barrister.data.io.Action;
import com.lsm.barrister.data.io.IO;
import com.lsm.barrister.data.io.app.BindBankCardReq;
import com.lsm.barrister.module.pick.RxImageConverters;
import com.lsm.barrister.module.pick.RxImagePicker;
import com.lsm.barrister.module.pick.Sources;
import com.lsm.barrister.ui.UIHelper;
import com.lsm.barrister.utils.FileUtils;

import java.io.File;
import java.util.List;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;


/**
 * 设置银行卡页
 */
public class SetBankCardActivity extends BaseActivity {

    private static final String TAG = SetBankCardActivity.class.getSimpleName();

    public static final String KEY = "bankcard";
    private static final int MY_SCAN_REQUEST_CODE = 0x1001;

    Account.BankCard bankCard;
    AQuery aq;
    List<AppConfig.Bank> banks;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_bankcard);
        setupToolbar();
        aq = new AQuery(this);
        aq.id(R.id.btn_setbank_image).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doScanCard();
            }


        });
        aq.id(R.id.btn_commit).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doCommit();
            }
        });

        banks = AppConfig.getInstance().getBanks();
    }

    private void doScanCard() {
//        Intent scanIntent = new Intent(this, CardIOActivity.class);
//
//        // customize these values to suit your needs.
//        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_EXPIRY, true); // default: false
//        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_CVV, false); // default: false
//        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_POSTAL_CODE, false); // default: false
//        scanIntent.putExtra(CardIOActivity.EXTRA_RETURN_CARD_IMAGE, true); // default: false
//
//        // MY_SCAN_REQUEST_CODE is arbitrary and is only used within this activity.
//        startActivityForResult(scanIntent, MY_SCAN_REQUEST_CODE);

        showChoosePicDialog();

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

        mPickingFile = new File(AppConfig.getDir(Constants.imageDir), "CN_DLS_BANK_CARD.jpg");

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

    private void showFile(File file) {

        SimpleDraweeView card = (SimpleDraweeView) findViewById(R.id.image_setbank);
        card.setImageURI(Uri.fromFile(file));

        this.file = file;

    }

    /**
     * 选完照片显示出来
     */
    private void handleResult(File file) {
        new BmTask(file).execute();
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

    File file;

    private void doCommit() {

        String cardNum = aq.id(R.id.et_bankcard_num).getEditable().toString();
        String cardholderName = aq.id(R.id.et_bankcard_holder_name).getEditable().toString();
        String bankName = aq.id(R.id.et_bankcard_bankname).getEditable().toString();
        String bankType = aq.id(R.id.et_bankcard_type).getEditable().toString();
        String bankAddress = aq.id(R.id.et_bankcard_bankaddress).getEditable().toString();
        String phone = aq.id(R.id.et_bankcard_phone).getEditable().toString();


        if(TextUtils.isEmpty(cardNum)
                ||TextUtils.isEmpty(cardholderName)
                ||TextUtils.isEmpty(bankName)
                ||TextUtils.isEmpty(bankAddress)
                ||TextUtils.isEmpty(bankType)
                ||TextUtils.isEmpty(phone)){

            UIHelper.showToast(getApplicationContext(),"请填写完整信息");

            return;
        }

        String logoName = "";

        for(AppConfig.Bank bank :banks){
            if(bankName.startsWith(bank.name)){
                logoName = bank.icon;
                break;
            }
        }

        new BindBankCardReq(this, cardNum, cardholderName,phone,bankType, bankName, bankAddress,logoName,file).execute(new Action.Callback<IO.BindBankcardResult>() {
            @Override
            public void progress() {
                progressDialog.setMessage(getString(R.string.tip_loading));
                progressDialog.setCancelable(false);
                progressDialog.show();
                aq.id(R.id.btn_commit).enabled(false);
            }

            @Override
            public void onError(int errorCode, String msg) {
                progressDialog.dismiss();
                aq.id(R.id.btn_commit).enabled(true);
                UIHelper.showToast(getApplicationContext(),"上传失败："+msg);
            }

            @Override
            public void onCompleted(IO.BindBankcardResult result) {
                progressDialog.dismiss();

                if(result!=null){

                    UIHelper.showToast(getApplicationContext(),"上传成功");

                    UserHelper.getInstance().setAccount(result.account);
                    UserHelper.getInstance().updateAccount();

                    finish();
                }

            }
        });
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.mybankcard);
    }
}
