package com.lsm.barrister.data.io;

import com.lsm.barrister.data.entity.Account;
import com.lsm.barrister.data.entity.Ad;
import com.lsm.barrister.data.entity.AppointmentSetting;
import com.lsm.barrister.data.entity.Case;
import com.lsm.barrister.data.entity.IncomeDetailItem;
import com.lsm.barrister.data.entity.LearningItem;
import com.lsm.barrister.data.entity.Message;
import com.lsm.barrister.data.entity.OrderDetail;
import com.lsm.barrister.data.entity.OrderItem;
import com.lsm.barrister.data.entity.User;
import com.lsm.barrister.data.entity.Version;

import java.util.List;

/**
 * Created by lvshimin on 16/5/11.
 */
public class IO {

    public static final String SERVER = "http://119.254.167.200:8080/lawerservice/";//GTC.测试
    //	public static final String SERVER = "http://10.0.0.25:8080";//高荣威


    public static final String URL_LOGOUT = SERVER + "logout.do";

    public static final String URL_LOGIN = SERVER + "login.do";

    public static final String URL_GET_BANK_INFO = "http://apis.baidu.com/datatiny/cardinfo/cardinfo";


    /**
     * 登陆返回结果
     */
    public static class LoginResult extends Action.CommonResult {
        public User user;
    }

    public static final String URL_GET_VERIFY_CODE = SERVER + "getVerifyCode.do";
    /**
     * 验证码返回结果
     */
    public static class GetVerifyCodeResult extends Action.CommonResult {
        public String verifyCode;
    }


    public static final String URL_BIND_BANK_CARD = SERVER + "bindBankCard.do";

    /**
     * 绑定银行卡
     */
    public static class BindBankcardResult extends Action.CommonResult {
        public Account.BankCard bankCard;
    }


    public static final String URL_GET_USER_HOME = SERVER + "userHome.do";

    /**
     * HOME
     * 返回值：resultCode，resultMsg , status（接单状态）,orderQty订单数，remainingBalance余额，totalIncome总收入，List<OrderItem> todoList待办事件；
     */
    public static class HomeResult extends Action.CommonResult {
        public String status;
        public String remainingBalance;
        public String totalIncome;
        public int orderQty;
        public List<OrderItem> todoList;
        public List<Case> caseList;
    }


    public static final String URL_FEEDBACK = SERVER + "addFeedback.do";


    public static final String URL_GET_APP_VERSION = SERVER + "getLatestVersion.do";

    /**
     * 获取程序版本更新信息
     */
    public static class GetAppVersionResult extends Action.CommonResult {
        public Version version;
    }

    public static final String URL_HOME_LUNBO_ADS = SERVER + "lunboAds.do";

    public static class GetLunboAdsResult extends Action.CommonResult {
        public List<Ad> ads;
    }


    public static final String URL_GET_MONEY = SERVER + "getMoney.do";

    public static final String URL_GET_MY_ACCOUNT = SERVER + "myAccount.do";


    public static class GetAccountResult extends Action.CommonResult {
        public Account account;
    }

    public static final String URL_GET_MY_APPOINTMENT_SETTINGS = SERVER + "getMyAppointmentSettings.do";

    public static class GetAppointmentSettingsResult extends Action.CommonResult {
        public List<AppointmentSetting> appointmentSettings;
    }

    public static final String URL_GET_APPOINTMENT_STATUS = SERVER + "getMyAppointmentStatus.do";

    public static class GetAppointmentStatusResult extends Action.CommonResult {
        public String status;
    }

    public static final String URL_GET_MY_MSGS = SERVER + "getMyMsgs.do";

    public static class GetMyMsgsResult extends Action.CommonResult {
        public List<Message> msgs;
        public int total;
    }

    public static final String URL_GET_ORDER_LIST = SERVER + "myOrderList.do";

    public static class GetMyOrdersResult extends Action.CommonResult {
        public List<OrderItem> orderItems;
        public int total;
    }

    public static final String URL_GET_ORDER_DETAIL = SERVER + "orderDetail.do";

    public static class GetOrderDetailResult extends Action.CommonResult {
        public OrderDetail orderDetail;
    }


    public static final String URL_GET_STUDY_LIST = SERVER + "getStudyList.do";

    public static class GetStudyListResult extends Action.CommonResult {
        public List<LearningItem> items;
        public int total;
    }


    public static final String URL_MAKE_CALL = SERVER + "makeCall.do";


    public static final String URL_SET_APPOINTMENT_SETTINGS = SERVER + "setAppointmentSettings.do";

    public static final String URL_UPDATE_USER = SERVER + "updateUserInfo.do";

    public static class GetUpdateUserResult extends Action.CommonResult {
        public User user;
    }

    /**
     * 上传用户头像
     */
    public static final String URL_UPLOAD_USERICON = SERVER + "uploadUserIcon.do";

    /**
     * 上传资质文件
     */
    public static final String URL_UPLOAD_FILES = SERVER + "uploadFiles.do";

    public static class GetBankInfoResult extends Action.CommonResult {

        public String status;
        public Data data;

        public static class Data {
            public String cardtype;//": "贷记卡",
            public String cardlength;//": 16,
            public String cardprefixnum;//": "518710",
            public String cardname;//": "MASTER信用卡",
            public String bankname;//": "招商银行信用卡中心",
            public String banknum;//": "03080010"

            @Override
            public String toString() {
                return "Data{" +
                        "cardtype='" + cardtype + '\'' +
                        ", cardlength='" + cardlength + '\'' +
                        ", cardprefixnum='" + cardprefixnum + '\'' +
                        ", cardname='" + cardname + '\'' +
                        ", bankname='" + bankname + '\'' +
                        ", banknum='" + banknum + '\'' +
                        '}';
            }
        }

        @Override
        public String toString() {
            return "GetBankInfoResult{" +
                    "status='" + status + '\'' +
                    ", data=" + data +
                    '}';
        }
    }

    public static final String URL_GET_INCOME_DETAIL_LIST = SERVER + "getIncomeDetailList.do";
    public static class GetIncomeDetailListResult extends Action.CommonResult {
        public List<IncomeDetailItem> incomeDetails;
        public int total;
    }


    //案源列表
    public static final String URL_GET_CASE_LIST = SERVER + "caseList.do";

    /**
     * 案源列表
     */
    public static class GetCaseListResult extends Action.CommonResult {
        public List<Case> cases;
        public int total;
    }

}