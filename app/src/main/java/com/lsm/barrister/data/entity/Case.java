package com.lsm.barrister.data.entity;

import java.io.Serializable;

/**
 * Created by lvshimin on 16/5/29.
 */
public class Case implements Serializable{

    //idle (刚上传)>客服确认>发布中>（律师支付）代理中> 已签约（更新）> 已结算
    //idle (刚上传)>客服确认>发布中>（律师支付）代理中> （律师退回）发布中

    public static final String STATUS_CONSULTING = "case.status.consulting";//咨询
    public static final String STATUS_INTERVIEW = "case.status.interview";//面谈
    public static final String STATUS_SIGNATORY = "case.status.signatory";//签约
    public static final String STATUS_FOLLOWUP = "case.status.followup";//跟进
    public static final String STATUS_CLEARING = "case.status.clearing";//结算

    //====================================
    public static final String STATUS_0_INIT = "case.status.init";//刚刚上传（或刚被退回），等待审核
    public static final String STATUS_1_PUBLISHED = "case.status.published";//客服审核确认,发布
    public static final String STATUS_2_WAIT_UPDATE = "case.status.agency";//代理中，等待更新进度
    public static final String STATUS_3_WAIT_CLEARING = "case.status.clearing";//已代理，等待结算
    public static final String STATUS_4_WAIT_CLEARED = "case.status.cleared";//结束，已结算
    public static final String STATUS_5_CLOSE = "case.status.close";//关闭

    String id;
    String caseTypeId;//案件类型id
    String caseTypeName;//案件类型名称

    String status;//状态 ：咨询>面谈>签约>跟进>结算
    //时间
    String addTime;
    //标题
    String title;
    //联系人
    String contact;
    //联系方式
    String contactPhone;
    //地区
    String area;
    String caseInfo;//案情

    //案件管理人
    String caseManager;

    String linkLawyer;//对接（咨询）律师
    String linkInfo;//对接（咨询）信息
    String linkTime;//对接（咨询）时间

    String followUpUser;//跟进人
    String followUpInfo;//跟进描述
    String followupTime;//跟进时间

    String interviewer;//面谈（人）
    String interviewInfo;//面谈信息
    String interviewTime;//面谈时间

    String signatory;//签约（人）
    String signatoryInfo;//签约信息
    String signatoryTime;//签约时间

    String clearingUser;//结算人
    String clearingInfo;//结算信息
    String clearingTime;//结算时间

    //后期入账（金额？）
    String income;

    double contractMoney;//合同金额
    double percentagePayment;//(平台分成金额)
    String company;//公司
    String email;//邮箱

    double price;//案源购买价格 16元

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getCaseInfo() {
        return caseInfo;
    }

    public void setCaseInfo(String caseInfo) {
        this.caseInfo = caseInfo;
    }

    public String getCaseManager() {
        return caseManager;
    }

    public void setCaseManager(String caseManager) {
        this.caseManager = caseManager;
    }


    public String getInterviewer() {
        return interviewer;
    }

    public void setInterviewer(String interviewer) {
        this.interviewer = interviewer;
    }

    public String getSignatory() {
        return signatory;
    }

    public void setSignatory(String signatory) {
        this.signatory = signatory;
    }

    public String getIncome() {
        return income;
    }

    public void setIncome(String income) {
        this.income = income;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFollowupTime() {
        return followupTime;
    }

    public void setFollowupTime(String followupTime) {
        this.followupTime = followupTime;
    }

    public String getInterviewTime() {
        return interviewTime;
    }

    public void setInterviewTime(String interviewTime) {
        this.interviewTime = interviewTime;
    }

    public String getSignatoryTime() {
        return signatoryTime;
    }

    public void setSignatoryTime(String signatoryTime) {
        this.signatoryTime = signatoryTime;
    }

    public String getClearingTime() {
        return clearingTime;
    }

    public void setClearingTime(String clearingTime) {
        this.clearingTime = clearingTime;
    }

    public String getCaseTypeId() {
        return caseTypeId;
    }

    public void setCaseTypeId(String caseTypeId) {
        this.caseTypeId = caseTypeId;
    }

    public String getCaseTypeName() {
        return caseTypeName;
    }

    public void setCaseTypeName(String caseTypeName) {
        this.caseTypeName = caseTypeName;
    }

    public String getLinkLawyer() {
        return linkLawyer;
    }

    public void setLinkLawyer(String linkLawyer) {
        this.linkLawyer = linkLawyer;
    }

    public String getLinkInfo() {
        return linkInfo;
    }

    public void setLinkInfo(String linkInfo) {
        this.linkInfo = linkInfo;
    }

    public String getLinkTime() {
        return linkTime;
    }

    public void setLinkTime(String linkTime) {
        this.linkTime = linkTime;
    }

    public String getFollowUpUser() {
        return followUpUser;
    }

    public void setFollowUpUser(String followUpUser) {
        this.followUpUser = followUpUser;
    }

    public String getFollowUpInfo() {
        return followUpInfo;
    }

    public void setFollowUpInfo(String followUpInfo) {
        this.followUpInfo = followUpInfo;
    }

    public String getInterviewInfo() {
        return interviewInfo;
    }

    public void setInterviewInfo(String interviewInfo) {
        this.interviewInfo = interviewInfo;
    }

    public String getSignatoryInfo() {
        return signatoryInfo;
    }

    public void setSignatoryInfo(String signatoryInfo) {
        this.signatoryInfo = signatoryInfo;
    }

    public String getClearingUser() {
        return clearingUser;
    }

    public void setClearingUser(String clearingUser) {
        this.clearingUser = clearingUser;
    }

    public String getClearingInfo() {
        return clearingInfo;
    }

    public void setClearingInfo(String clearingInfo) {
        this.clearingInfo = clearingInfo;
    }


    public double getContractMoney() {
        return contractMoney;
    }

    public void setContractMoney(double contractMoney) {
        this.contractMoney = contractMoney;
    }

    public double getPercentagePayment() {
        return percentagePayment;
    }

    public void setPercentagePayment(double percentagePayment) {
        this.percentagePayment = percentagePayment;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
