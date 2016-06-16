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
import com.lsm.barrister.data.io.IO.BindBankcardResult;
import com.lsm.barrister.utils.DateFormatUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by lvshimin on 16/5/27.
 */
public class Test {


    /**
     * 我的账户
     * @return
     */
    public static IO.GetAccountResult getMyAccount(){
        IO.GetAccountResult result = new IO.GetAccountResult();
        result.resultCode = 200;
        result.resultMsg = "success";

        Account account = new Account();
        account.setBankCardBindStatus(Account.CARD_STATUS_BOUND);
        account.setRemainingBalance("1000");
        account.setTotalIncome("1500");
        Account.BankCard bankCard = new Account.BankCard();
        bankCard.setBankCardAddress("富力城支行");
        bankCard.setLogoName("icon_bank_zs.png");
        bankCard.setCardType("储蓄卡");
        bankCard.setBankCardNum("6225 8801 4204 7506");
        bankCard.setBankCardName("招商银行");
        bankCard.setBankPhone("13671057132");
        account.setBankCard(bankCard);

        result.account = account;

        return result;
    }




    public static IO.LoginResult getLoginResult(){
        IO.LoginResult result = new IO.LoginResult();
        result.resultCode = 200;
        result.resultMsg = "success";

        User user = new User();
        user.setIntroduction("其实我是个律师");
        user.setId("1");
        user.setEmail("78783606@qq.com");
        user.setArea("北京市，朝阳区");
        user.setCompany("xx律师事务所");
        user.setAddress("北京市，西直门，D10-11");
        user.setAge("30");
        user.setGender("男");
        user.setGoodAt("交通事故|离婚纠纷|财产纠纷");
        user.setVerifyCode("867920");
        user.setLocation("39,107");
        user.setName("世民");
        user.setNickname("The Fox");
        user.setVerifyStatus(User.STATUS_UNAUTHERIZED);
        user.setState("北京市");
        user.setCity("朝阳区");
        user.setUserIcon("");
        user.setWorkingStartYear("1995-01-01");

        result.user = user;

        return result;
    }

    public static IO.GetVerifyCodeResult getVerifyCodeResult(){
        IO.GetVerifyCodeResult result = new IO.GetVerifyCodeResult();
        result.resultCode = 200;
        result.resultMsg = "success";
        result.verifyCode = "";
        return result;
    }


    public static BindBankcardResult getBindcardResult(){
        BindBankcardResult result = new BindBankcardResult();
        result.resultCode = 200;
        result.resultMsg = "success";
        Account.BankCard bankCard = new Account.BankCard();
        bankCard.setBankPhone("13671057132");
        bankCard.setBankCardName("招商银行");
        bankCard.setBankCardNum("6225880142047506");
        bankCard.setCardType("储蓄卡");
        bankCard.setBankCardAddress("招商银行北京分行富丽城支行");
        return result;
    }



    public static IO.HomeResult getHomeResult(){
        IO.HomeResult result = new IO.HomeResult();
        result.resultCode = 200;
        result.resultMsg = "success";

        result.status = User.ORDER_STATUS_CAN;
        result.remainingBalance = "1005.9";
        result.totalIncome = "1500";
        result.orderQty = 200;

        List<OrderItem> orderItems = getMyOrdersResult(3).orderItems;
        List<Case> caseList = getCaseListResult(3).cases;

        result.todoList = orderItems;
        result.caseList = caseList;

        return  result;
    }


    public static IO.GetAppVersionResult getAppVersionResult(){
        IO.GetAppVersionResult result = new IO.GetAppVersionResult();
        result.resultCode = 200;
        result.resultMsg = "success";
        Version version = new Version();
        version.setUrl("");
        version.setVersionCode(10);
        version.setVersionInfo("更新测试");
        version.setVersionName("1.0.1");
        result.version = version;
        return  result;
    }


    public static List<Ad> getAds(){
        List<Ad> ads = new ArrayList<>();
        for(int i=0;i<3;i++){
            Ad temp = new Ad();
            temp.setTitle("test");
            temp.setDate(DateFormatUtils.format(new Date()));
            temp.setImage("https://ss1.baidu.com/6ONXsjip0QIZ8tyhnq/it/u=2539625029,4256537847&fm=80");
            temp.setUrl("http://cnews.chinadaily.com.cn/2016-05/27/content_25498712.htm");
            ads.add(temp);
        }
        return ads;
    }

    public static IO.GetLunboAdsResult getLunoAdsResult(){
        IO.GetLunboAdsResult result = new IO.GetLunboAdsResult();
        result.resultCode = 200;
        result.resultMsg = "success";
        result.ads = getAds();
        return  result;
    }

    public static IO.GetAppointmentSettingsResult getAppointmentSettingsResult(){

        IO.GetAppointmentSettingsResult result = new IO.GetAppointmentSettingsResult();
        result.resultCode = 200;
        result.resultMsg = "success";
        List<AppointmentSetting> appointmentSettings = new ArrayList<>();

        Date today = new Date();
        for(int i=0;i<6;i++){
            AppointmentSetting setting = new AppointmentSetting();
            setting.setDate(DateFormatUtils.format(new Date(today.getTime()+i*24*3600*1000),"yyyy-MM-dd"));
            setting.setSettings("0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1");
            appointmentSettings.add(setting);
        }

        result.appointmentSettings = appointmentSettings;

        return result;
    }


    public static IO.GetAppointmentStatusResult getAppointmentStatusResult(){
        IO.GetAppointmentStatusResult result = new IO.GetAppointmentStatusResult();
        result.resultCode = 200;
        result.resultMsg = "success";
        result.status = User.ORDER_STATUS_CAN;
        return  result;
    }

    public static IO.GetMyMsgsResult getMyMsgsResult(){
        IO.GetMyMsgsResult result = new IO.GetMyMsgsResult();
        result.resultCode = 200;
        result.resultMsg = "success";
        List<Message> msgs = new ArrayList<>();

        for(int i=0;i<20;i++){
            Message msg = new Message();
            msg.setId(""+i);
            msg.setContent("系统即将重启。。。。");
            msg.setTitle("系统消息");
            msg.setType(Message.TYPE_SYSTEM);
            msgs.add(msg);
        }

        result.msgs = msgs;
        result.total = 20;

        return result;
    }

    public static IO.GetMyOrdersResult getMyOrdersResult(int count) {
        IO.GetMyOrdersResult result = new IO.GetMyOrdersResult();
        result.resultCode = 200;
        result.resultMsg = "success";

        List<OrderItem> orderItems = new ArrayList<>();
        for(int i=0;i<count;i++){
            OrderItem item = new OrderItem();
            item.setUserIcon("");
            item.setId(""+i);
            item.setType(OrderItem.TYPE_APPOINTMENT);
            item.setDate("2016-05-29");
            item.setClientPhone("13671057132");
            item.setStatus(OrderDetail.STATUS_WAITING);
            item.setNickname("The Fox");
            item.setCaseType("1");
            orderItems.add(item);
        }
        result.orderItems = orderItems;

        return result;
    }

    public static IO.GetOrderDetailResult getOrderDetailResult(){
        IO.GetOrderDetailResult result = new IO.GetOrderDetailResult();
        result.resultCode = 200;
        result.resultMsg = "success";
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setId("1");
        orderDetail.setCaseType("1");
        orderDetail.setType(OrderItem.TYPE_APPOINTMENT);
        orderDetail.setStatus(OrderDetail.STATUS_WAITING);
        orderDetail.setCustomerIcon("");
        orderDetail.setCustomerNickname("昔日少年");
        orderDetail.setPayTime("2016-05-30 10:19:33");
        orderDetail.setRemarks("备注：我被强奸了。。。哈哈哈");
        orderDetail.setOrderNo(String.valueOf(System.currentTimeMillis()));
        orderDetail.setCustomerPhone("13671057132");
        result.orderDetail = orderDetail;
        return result;
    }


    public static IO.GetStudyListResult getStudyListResult(){
        IO.GetStudyListResult result = new IO.GetStudyListResult();

        result.resultCode = 200;
        result.resultMsg = "success";

        List<LearningItem> items = new ArrayList<>();

        String date = DateFormatUtils.format(new Date());

        for(int i=0;i<20;i++){
            LearningItem item = new LearningItem();
            item.setId(""+i);
            item.setDate(date);
            item.setTag("微电影");
            item.setThumb("");
            item.setTitle("学习中心标题测试");
            item.setUrl("http://www.baidu.com");
            items.add(item);

        }

        result.items = items;
        result.total = 20;

        return result;
    }


    public static IO.GetUpdateUserResult GetUpdateUserResult(){
        IO.GetUpdateUserResult result = new IO.GetUpdateUserResult();

        result.resultCode = 200;
        result.resultMsg = "success";

        User user = new User();
        user.setIntroduction("其实我是个律师");
        user.setId("1");
        user.setEmail("78783606@qq.com");
        user.setArea("北京市，朝阳区");
        user.setCompany("xx律师事务所");
        user.setAddress("北京市，西直门，D10-11");
        user.setAge("30");
        user.setGender("男");
        user.setGoodAt("交通事故|离婚纠纷|财产纠纷");
        user.setVerifyCode("867920");
        user.setLocation("39,107");
        user.setName("世民");
        user.setNickname("The Fox");
        user.setVerifyStatus(User.STATUS_UNAUTHERIZED);
        user.setState("北京市");
        user.setCity("朝阳区");
        user.setUserIcon("");
        user.setWorkingStartYear("1995-01-01");

        result.user = user;
        return result;
    }

    public static IO.GetBankInfoResult getBankInfoResult(){
        IO.GetBankInfoResult result = new IO.GetBankInfoResult();
        result.resultCode = 200;
        result.resultMsg = "success";

        IO.GetBankInfoResult.Data data = new IO.GetBankInfoResult.Data();
        data.cardtype="贷记卡";
        data.cardlength="16";
        data.cardprefixnum="518710";
        data.cardname="MASTER信用卡";
        data.bankname="招商银行信用卡中心";
        data.banknum="03080010";

        result.status = Account.CARD_STATUS_BOUND;

        result.data = data;

        return result;
    }

    public static IO.GetIncomeDetailListResult getIncomeDetailListResult(){
        IO.GetIncomeDetailListResult result = new IO.GetIncomeDetailListResult();
        result.resultCode = 200;
        result.resultMsg = "success";
        List<IncomeDetailItem> incomeDetails = new ArrayList<>();
        for(int i=0;i<10;i++){
            IncomeDetailItem item = new IncomeDetailItem();
            item.setId(String.valueOf(i));
            item.setSerialNum(String.valueOf(System.currentTimeMillis()));
            item.setDate(DateFormatUtils.format(new Date()));
            item.setMoney("100");
            item.setOrderId(String.valueOf(1));
            item.setType(IncomeDetailItem.TYPE_ORDER);
            incomeDetails.add(item);
        }
        result.incomeDetails = incomeDetails;
        result.total = 20;

        return result;
    }



    /**
     * 案源列表
     * @return
     */
    public static IO.GetCaseListResult getCaseListResult(int count){
        IO.GetCaseListResult result = new IO.GetCaseListResult();
        result.resultCode = 200;
        result.resultMsg = "success";
        List<Case> cases = new ArrayList<>();
        for(int i=0;i<count;i++){
            Case cs = new Case();
            cs.setId(String.valueOf(i));
            cs.setTitle("我杀人了");
            cs.setCaseInfo("案件信息详情");
            cs.setArea("北京");
            cs.setAddTime(DateFormatUtils.format(new Date()));
            cases.add(cs);
        }
        result.cases = cases;
        result.total = 10;
        return result;
    }


}
