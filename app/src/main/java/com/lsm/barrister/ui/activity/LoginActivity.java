package com.lsm.barrister.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.util.AQUtility;
import com.lsm.barrister.R;
import com.lsm.barrister.app.AppConfig;
import com.lsm.barrister.app.Constants;
import com.lsm.barrister.data.entity.User;
import com.lsm.barrister.data.io.Action;
import com.lsm.barrister.data.io.ErrorCode;
import com.lsm.barrister.data.io.app.GetVerifyCodeReq;
import com.lsm.barrister.data.io.app.LoginReq;
import com.lsm.barrister.ui.UIHelper;
import com.lsm.barrister.utils.TextHandler;

import java.util.Locale;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends BaseActivity  {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */

    // UI references.
    private AutoCompleteTextView mAccountView;
    private EditText mVerifyCode;

    GetVerifyCodeReq mGetVerifyCodeReq;
    LoginReq mLoginReq;

    AQuery aq;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setupToolbar();

        aq = new AQuery(this);

        // Set up the login form.
        mAccountView = (AutoCompleteTextView) findViewById(R.id.et_account);

        mVerifyCode = (EditText) findViewById(R.id.et_verfiycode);
        mVerifyCode.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        aq.id(R.id.tv_agree).clicked(new OnClickListener() {
            @Override
            public void onClick(View v) {
                UIHelper.goDocActivity(LoginActivity.this,getString(R.string.title_agree), Constants.DOC_AGREEMENT);
            }
        });

        aq.id(R.id.btn_get_verify_code).clicked(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                doGetVerifyCode();
            }
        });
    }

    /**
     * 请求验证码
     */
    private void doGetVerifyCode() {

        String phone = mAccountView.getText().toString();

        boolean cancel = false;

        View focusView = null;

        // Check for a valid email address.
        if (TextUtils.isEmpty(phone)) {
            mAccountView.setError(getString(R.string.error_field_required));
            focusView = mAccountView;
            cancel = true;
        } else if (!isPhoneValid(phone)) {
            mAccountView.setError(getString(R.string.error_invalid_phone));
            focusView = mAccountView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {

            if(mGetVerifyCodeReq == null){
                mGetVerifyCodeReq = new GetVerifyCodeReq(this,phone);
            }

            mGetVerifyCodeReq.execute(new Action.Callback<Boolean>() {
                @Override
                public void progress() {

                }

                @Override
                public void onError(int errorCode, String msg) {

                    aq.id(R.id.btn_get_verify_code).enabled(true);

                    UIHelper.showToast(getApplicationContext(),msg);

                }

                @Override
                public void onCompleted(Boolean b) {

                    if(b){

                        UIHelper.showToast(getApplicationContext(),"请求已发出，稍后请查看您的手机短信。");

                        aq.id(R.id.btn_get_verify_code).enabled(false);
                        //重置倒计时
                        mVerifyCodeCountDown = 60*1000;
                        //更新btn状态
                        updateBtnStatus();
                    }
                }
            });
        }

    }


    /**
     * 更新验证码按钮状态
     */
    private void updateBtnStatus() {

        if(mVerifyCodeCountDown <= 0){
            aq.id(R.id.btn_get_verify_code).text(R.string.get_verify_code).enabled(true);
            return;
        }

        mVerifyCodeCountDown -= 1000;

        AQUtility.postDelayed(new Runnable() {
            @Override
            public void run() {

                //等待xx秒
                aq.id(R.id.btn_get_verify_code).text(String.format(Locale.CHINA,getString(R.string.wait_second),mVerifyCodeCountDown/1000));

                updateBtnStatus();
            }
        },1000);
    }

    long mVerifyCodeCountDown = 60*1000;


    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {

        // Reset errors.
        mAccountView.setError(null);
        mVerifyCode.setError(null);

        // Store values at the time of the login attempt.
        String phone = mAccountView.getText().toString();
        String verifyCode = mVerifyCode.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(verifyCode) && !isVerifyCodeValid(verifyCode)) {
            mVerifyCode.setError(getString(R.string.error_invalid_password));
            focusView = mVerifyCode;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(phone)) {
            mAccountView.setError(getString(R.string.error_field_required));
            focusView = mAccountView;
            cancel = true;
        } else if (!isPhoneValid(phone)) {
            mAccountView.setError(getString(R.string.error_invalid_phone));
            focusView = mAccountView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            mLoginReq = new LoginReq(getApplicationContext(),phone, verifyCode);
            mLoginReq.execute(new Action.Callback<User>() {
                @Override
                public void progress() {
                    progressDialog.setMessage("正在登录，请稍候...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                }

                @Override
                public void onError(int errorCode, String msg) {
                    progressDialog.dismiss();

                    mLoginReq = null;

                    UIHelper.showToast(getApplicationContext(),msg);

                    if(errorCode == ErrorCode.ERROR_INVALID_VERIFICATION){
                        mVerifyCode.setError(getString(R.string.error_incorrect_password));
                        mVerifyCode.requestFocus();
                    }
                }

                @Override
                public void onCompleted(User user) {

                    progressDialog.dismiss();

                    mLoginReq = null;

                    UIHelper.showToast(getApplicationContext(),getString(R.string.tip_success_login));

                    //保存user，更新界面信息；
                    AppConfig.setUser(getApplicationContext(), user);

                    //跳转到MainActivity
                    UIHelper.goMainActivity(LoginActivity.this);

//                    if(user.getVerifyStatus().equals(User.STATUS_UNAUTHERIZED)){
//
//                        UIHelper.goUserDetailActivity(LoginActivity.this);
//
//                    }else{
//
//                    }

                    finish();

                }
            });
        }
    }

    private boolean isPhoneValid(String phone) {
        //TODO: Replace this with your own logic
        return TextHandler.isMobileNum(phone);
    }

    /**
     * 验证码验证
     * @param password
     * @return
     */
    private boolean isVerifyCodeValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }


}

