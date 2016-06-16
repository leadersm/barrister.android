package com.lsm.barrister.data.entity;

import java.io.Serializable;

/**
 * Created by lvshimin on 16/5/8.
 * 账户对象：余额，总收入，银行卡信息
 */
public class Account implements Serializable{

    public static final String CARD_STATUS_NOT_BOUND = "0";//未绑定
    public static final String CARD_STATUS_BOUND = "1";//已绑定

    String remainingBalance;//余额
    String totalIncome;//总收入

    String bankCardBindStatus;//银行卡绑定状态，0 未绑定 1 已绑定

    BankCard bankCard;//银行卡

    /**
     * Created by lvshimin on 16/5/8.
     * 银行卡
     */
    public static class BankCard implements Serializable{

        String bankCardNum;//银行卡号
        String bankCardAddress;//开户行 ：中国工商银行北京分行广渠路支行。。
        String bankCardName;//银行名称 ：工行，农行……

        String cardType;//银行卡类型
        String logoName;//银行LOGO
        String bankPhone;//手机号

        @Override
        public String toString() {
            return "BankCard{" +
                    "bankCardNum='" + bankCardNum + '\'' +
                    ", bankCardAddress='" + bankCardAddress + '\'' +
                    ", bankCardName='" + bankCardName + '\'' +
                    ", cardType='" + cardType + '\'' +
                    ", logoName='" + logoName + '\'' +
                    ", bankPhone='" + bankPhone + '\'' +
                    '}';
        }

        public String getBankCardNum() {
            return bankCardNum;
        }

        public void setBankCardNum(String bankCardNum) {
            this.bankCardNum = bankCardNum;
        }

        public String getBankCardAddress() {
            return bankCardAddress;
        }

        public void setBankCardAddress(String bankCardAddress) {
            this.bankCardAddress = bankCardAddress;
        }

        public String getBankCardName() {
            return bankCardName;
        }

        public void setBankCardName(String bankCardName) {
            this.bankCardName = bankCardName;
        }

        public String getBankPhone() {
            return bankPhone;
        }

        public void setBankPhone(String bankPhone) {
            this.bankPhone = bankPhone;
        }

        public String getCardType() {
            return cardType;
        }

        public void setCardType(String cardType) {
            this.cardType = cardType;
        }

        public String getLogoName() {
            return logoName;
        }

        public void setLogoName(String logoName) {
            this.logoName = logoName;
        }
    }

    public String getRemainingBalance() {
        return remainingBalance;
    }

    public void setRemainingBalance(String remainingBalance) {
        this.remainingBalance = remainingBalance;
    }

    public String getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(String totalIncome) {
        this.totalIncome = totalIncome;
    }

    public String getBankCardBindStatus() {
        return bankCardBindStatus;
    }

    public void setBankCardBindStatus(String bankCardBindStatus) {
        this.bankCardBindStatus = bankCardBindStatus;
    }

    public BankCard getBankCard() {
        return bankCard;
    }

    public void setBankCard(BankCard bankCard) {
        this.bankCard = bankCard;
    }
}
