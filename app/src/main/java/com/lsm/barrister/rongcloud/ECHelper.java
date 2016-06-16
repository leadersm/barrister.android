package com.lsm.barrister.rongcloud;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.lsm.barrister.R;
import com.lsm.barrister.data.entity.User;
import com.lsm.barrister.ui.activity.VoIPCallActivity;
import com.lsm.barrister.utils.DLog;
import com.yuntongxun.ecsdk.ECDevice;
import com.yuntongxun.ecsdk.ECError;
import com.yuntongxun.ecsdk.ECInitParams;
import com.yuntongxun.ecsdk.ECMessage;
import com.yuntongxun.ecsdk.ECNotifyOptions;
import com.yuntongxun.ecsdk.ECVoIPCallManager;
import com.yuntongxun.ecsdk.ECVoIPSetupManager;
import com.yuntongxun.ecsdk.OnChatReceiveListener;
import com.yuntongxun.ecsdk.SdkErrorCode;
import com.yuntongxun.ecsdk.VideoRatio;
import com.yuntongxun.ecsdk.VoIPCallUserInfo;
import com.yuntongxun.ecsdk.im.ECMessageNotify;
import com.yuntongxun.ecsdk.im.group.ECGroupNoticeMessage;

import java.util.List;

/**
 * Created by lvshimin on 16/3/26.
 */
public class ECHelper implements ECDevice.InitListener , ECDevice.OnECDeviceConnectListener,ECDevice.OnLogoutListener{

    private static final String TAG = ECHelper.class.getSimpleName();

    //    ACCOUNT SID：
    public static String ACCOUNT_SID = "aaf98f894b353559014b4054cd1705db";
    //    AUTH TOKEN：
    public static String AUTH_TOKEN = "0839fad718da4327adcadf25ca54ab00";

    //  (开发) Rest URL：
    public static String SERVER = "https://sandboxapp.cloopen.com:8883";
//  (生产) Rest URL：
//   public static String SERVER = "https://app.cloopen.com:8883";

    public static String APP_ID = "aaf98f8953acd61f0153b2089d3f0719";
    public static String APP_TOKEN = "966d6fe8a690144e71ba98226f6ad68e";


    public static String USER_TEMP_ID1 = "8011846500000002";
    public static String USER_TEMP_ID2 = "8011846500000003";

    public User getUser() {
        return user;
    }

    User user;
    private ECHelper(){
        user = new User();
        user.setId(USER_TEMP_ID2);
        user.setNickname("test user");

        // 获得SDKVoIP呼叫接口
        // 注册VoIP呼叫事件回调监听
        mVoIPCallback = new SubVoIPCallback();

        initNotifyOptions();

    }

    private static ECHelper instance = null;

    public static ECHelper getInstance() {
        if(instance == null){
            instance = new ECHelper();
        }
        return instance;
    }

    Context mContext;

    public Context getContext() {
        return mContext;
    }

    public void setContext(Context mContext) {
        this.mContext = mContext;
    }

    ECNotifyOptions mOptions;

    private void initNotifyOptions() {
        if(mOptions == null) {
            mOptions = new ECNotifyOptions();
        }
        // 设置新消息是否提醒
        mOptions.setNewMsgNotify(true);
        // 设置状态栏通知图标
        mOptions.setIcon(R.drawable.ic_launcher);
        // 设置是否启用勿扰模式（不会声音/震动提醒）
        mOptions.setSilenceEnable(false);
        // 设置勿扰模式时间段（开始小时/开始分钟-结束小时/结束分钟）
        // 小时采用24小时制
        // 如果设置勿扰模式不启用，则设置勿扰时间段无效
        // 当前设置晚上11点到第二天早上8点之间不提醒
        mOptions.setSilenceTime(23, 0, 8, 0);
        // 设置是否震动提醒(如果处于免打扰模式则设置无效，没有震动)
        mOptions.enableShake(true);
        // 设置是否声音提醒(如果处于免打扰模式则设置无效，没有声音)
        mOptions.enableSound(true);
    }



    public void login(Context context) {
        setContext(context);
        DLog.d(TAG, "ECSDK is ready");

        // 设置消息提醒
        ECDevice.setNotifyOptions(mOptions);
        // 设置接收VoIP来电事件通知Intent
        // 呼入界面activity、开发者需修改该类
        Intent intent = new Intent(getInstance().mContext, VoIPCallActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity( getInstance().mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        ECDevice.setPendingIntent(pendingIntent);

        // 设置SDK注册结果回调通知，当第一次初始化注册成功或者失败会通过该引用回调
        // 通知应用SDK注册状态
        // 当网络断开导致SDK断开连接或者重连成功也会通过该设置回调
//        ECDevice.setOnChatReceiveListener(IMChattingHelper.getInstance());
        ECDevice.setOnDeviceConnectListener(this);

        // 设置VOIP 自定义铃声路径
        ECVoIPSetupManager setupManager = ECDevice.getECVoIPSetupManager();
        if(setupManager != null) {
            // 目前支持下面三种路径查找方式
            // 1、如果是assets目录则设置为前缀[assets://]
            setupManager.setInComingRingUrl(true, "assets://phonering.mp3");
            setupManager.setOutGoingRingUrl(true, "assets://phonering.mp3");
            setupManager.setBusyRingTone(true, "assets://playend.mp3");
            // 2、如果是raw目录则设置为前缀[raw://]
            // 3、如果是SDCard目录则设置为前缀[file://]
        }

//        if(ECDevice.getECMeetingManager() != null) {
//            ECDevice.getECMeetingManager().setOnMeetingListener(MeetingMsgReceiver.getInstance());
//        }

        // 构建注册所需要的参数信息
        //5.0.3的SDK初始参数的方法：ECInitParams params = new ECInitParams();5.1.*以上版本如下：
        ECInitParams params = ECInitParams.createParams();
        //自定义登录方式：
        //测试阶段Userid可以填写手机
        params.setUserid(user.getId());
        params.setAppKey(APP_ID);
        params.setToken(APP_TOKEN);
        // 设置登陆验证模式（是否验证密码）NORMAL_AUTH-自定义方式
        params.setAuthType(ECInitParams.LoginAuthType.NORMAL_AUTH);
        // 1代表用户名+密码登陆（可以强制上线，踢掉已经在线的设备）
        // 2代表自动重连注册（如果账号已经在其他设备登录则会提示异地登陆）
        // 3 LoginMode（强制上线：FORCE_LOGIN  默认登录：AUTO）
        params.setMode(ECInitParams.LoginMode.FORCE_LOGIN);


        ECDevice.setOnChatReceiveListener(new OnChatReceiveListener() {
            @Override
            public void OnReceivedMessage(ECMessage msg) {
                // 收到新消息
                DLog.d(TAG, ">>>>>收到新消息");
            }

            @Override
            public void onReceiveMessageNotify(ECMessageNotify ecMessageNotify) {
                DLog.d(TAG, ">>>>>onReceiveMessageNotify");
            }

            @Override
            public void OnReceiveGroupNoticeMessage(ECGroupNoticeMessage notice) {
                // 收到群组通知消息（有人加入、退出...）
                // 可以根据ECGroupNoticeMessage.ECGroupMessageType类型区分不同消息类型
                DLog.d(TAG, ">>>>>OnReceiveGroupNoticeMessage");
            }

            @Override
            public void onOfflineMessageCount(int count) {
                // 登陆成功之后SDK回调该接口通知账号离线消息数
                DLog.d(TAG, ">>>>>onOfflineMessageCount");
            }

            @Override
            public int onGetOfflineMessage() {
                DLog.d(TAG, ">>>>>onGetOfflineMessage");
                return 0;
            }

            @Override
            public void onReceiveOfflineMessage(List msgs) {
                // SDK根据应用设置的离线消息拉去规则通知应用离线消息
                DLog.d(TAG, ">>>>>onReceiveOfflineMessage");
            }

            @Override
            public void onReceiveOfflineMessageCompletion() {
                // SDK通知应用离线消息拉取完成
                DLog.d(TAG, ">>>>>onReceiveOfflineMessageCompletion");
            }

            @Override
            public void onServicePersonVersion(int version) {
                // SDK通知应用当前账号的个人信息版本号
                DLog.d(TAG, ">>>>>onServicePersonVersion");
            }

            @Override
            public void onReceiveDeskMessage(ECMessage ecMessage) {
                DLog.d(TAG, ">>>>>onReceiveDeskMessage");

            }

            @Override
            public void onSoftVersion(String s, int i) {
                DLog.d(TAG, ">>>>>onSoftVersion:" + s);
            }
        });



//        第三步：验证参数是否正确，注册SDK
        if(params.validate()) {
            DLog.d(TAG,"EC do login..");
            // 判断注册参数是否正确
            ECDevice.login(params);
        }else{
            DLog.e(TAG,"EC login params is invalidate");
        }
    }


    public void logout(){
        ECDevice.logout(new ECDevice.OnLogoutListener() {
            @Override
            public void onLogout() {
                // SDK 回调通知当前登出成功
                // 这里可以做一些（与云通讯IM相关的）应用资源的释放工作
                // 如（关闭数据库，释放界面资源和跳转等）
            }
        });
    }

//    3.业务流程
//    (1)客户A呼叫客户B发起请求
//    (2)云通讯服务端收到A请求并把请求转发给B
//    (3)客户B收到请求并应答
//    (4)云通讯服务端收到B应答并转发A
//    (5)A收到应答，通话建立。


    /**
     * 语音呼叫，免费
     * @param to
     * @return
     */
    public String makeVoiceCall(String to){

        initCall();

        mCurrentCallId = ECDevice.getECVoIPCallManager().makeCall(ECVoIPCallManager.CallType.VOICE, to);

        DLog.d(TAG, "mCurrentCallId:" + mCurrentCallId);

        return mCurrentCallId;
    }

    private void initCall() {

        if(mVoIPCallback!=null){
            ECDevice.getECVoIPCallManager().setOnVoIPCallListener(mVoIPCallback);
        }

        // 设置呼叫参数信息[呼叫昵称、呼叫手机号]
        VoIPCallUserInfo info = new VoIPCallUserInfo(user.getNickname() , user.getId());
        ECDevice.getECVoIPSetupManager().setVoIPCallUserInfo(info);
    }

    /**
     * 直接拨打，收费
     * @param to
     * @return
     */
    public String makeDirectCall(String to){

        String mCurrentCallId = ECDevice.getECVoIPCallManager().makeCall(ECVoIPCallManager.CallType.DIRECT, to);

        return mCurrentCallId;
    }

    SubVoIPCallback mVoIPCallback;


    public void onConnect() {
        // 兼容4.0，5.0可不必处理
        DLog.e(TAG, ">>>>>onConnect");
    }

    @Override
    public void onDisconnect(ECError error) {
        // 兼容4.0，5.0可不必处理
        DLog.e(TAG, ">>>>>onDisconnect");
    }

    @Override
    public void onConnectState(ECDevice.ECConnectState state, ECError error) {
        if (state == ECDevice.ECConnectState.CONNECT_FAILED) {
            if (error.errorCode == SdkErrorCode.SDK_KICKED_OFF) {
                //账号异地登陆
                DLog.e(TAG, ">>>>>账号异地登陆");
            } else {
                //连接状态失败
                DLog.e(TAG, ">>>>>连接状态失败");
            }
            return;
        } else if (state == ECDevice.ECConnectState.CONNECT_SUCCESS) {
            // 登陆成功
            DLog.d(TAG, ">>>>>登陆成功");
        }
    }

    @Override
    public void onInitialized() {

    }

    @Override
    public void onError(Exception e) {

    }

    @Override
    public void onLogout() {

    }

    private class SubVoIPCallback implements  ECVoIPCallManager.OnVoIPListener {

        @Override
        public void onVideoRatioChanged(VideoRatio videoRatio) {
            DLog.d(TAG,">>>>>onVideoRatioChanged:");

        }

        @Override
        public void onSwitchCallMediaTypeRequest(String s, ECVoIPCallManager.CallType callType) {
            DLog.d(TAG,">>>>>onSwitchCallMediaTypeRequest:");

        }

        @Override
        public void onSwitchCallMediaTypeResponse(String s, ECVoIPCallManager.CallType callType) {
            DLog.d(TAG,">>>>>onSwitchCallMediaTypeResponse:");

        }

        @Override
        public void onDtmfReceived(String s, char c) {
            DLog.d(TAG,">>>>>onDtmfReceived:");

        }

        @Override
        public void onCallEvents(ECVoIPCallManager.VoIPCall voipCall) {
            // 处理呼叫事件回调
            if(voipCall == null) {
                Log.e("SDKCoreHelper", "handle call event error , voipCall null");
                return ;
            }
            // 根据不同的事件通知类型来处理不同的业务
            ECVoIPCallManager.ECCallState callState = voipCall.callState;

            switch (callState) {
                case ECCALL_PROCEEDING:
                    // 正在连接服务器处理呼叫请求
                    DLog.d(TAG,">>>>>onCallEvents 正在连接服务器处理呼叫请求:");
                    break;
                case ECCALL_ALERTING:
                    // 呼叫到达对方客户端，对方正在振铃
                    DLog.d(TAG,">>>>>onCallEvents 呼叫到达对方客户端，对方正在振铃:");
                    break;
                case ECCALL_ANSWERED:
                    // 对方接听本次呼叫
                    DLog.d(TAG,">>>>>onCallEvents  对方接听本次呼叫:");
                    break;
                case ECCALL_FAILED:
                    // 本次呼叫失败，根据失败原因播放提示音
                    DLog.w(TAG,">>>>>onCallEvents  本次呼叫失败，根据失败原因播放提示音:"+voipCall.reason);
                    break;
                case ECCALL_RELEASED:
                    // 通话释放[完成一次呼叫]
                    DLog.d(TAG,">>>>>onCallEvents 通话释放[完成一次呼叫]:");
                    break;
                default:
                    Log.e("SDKCoreHelper", "handle call event error , callState " + callState);
                    break;
            }
        }
    }


    /***
     * video call，免费
     * @param to
     * @return
     */
    public String makeVideoCall(String to){

        mCurrentCallId = ECDevice.getECVoIPCallManager().makeCall(ECVoIPCallManager.CallType.VIDEO, to);

        return mCurrentCallId;
    }

    public void createSubUser(Context context,String friendlyName){

    }

    public String getCurrentCallId() {
        return mCurrentCallId;
    }

    public void setCurrentCallId(String mCurrentCallId) {
        this.mCurrentCallId = mCurrentCallId;
    }

    String mCurrentCallId;



    public void accept() {
        ECDevice.getECVoIPCallManager().acceptCall(mCurrentCallId);
    }

    public void rejected(){
        ECDevice.getECVoIPCallManager().rejectCall(mCurrentCallId, 1);
    }
}
