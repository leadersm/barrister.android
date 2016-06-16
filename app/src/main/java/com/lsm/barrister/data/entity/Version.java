package com.lsm.barrister.data.entity;

/**
 * Title: Version.java
 * Description:版本信息
 *
 * @author lsm
 * @date 2015-5-7
 */
public class Version {

    int versionCode;//版本号
    String versionName;//版本名称
    String versionInfo;//版本更新信息
    String url;//下载地址

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getVersionInfo() {
        return versionInfo;
    }

    public void setVersionInfo(String versionInfo) {
        this.versionInfo = versionInfo;
    }


    public Version(int versionCode, String versionName, String versionInfo,
                   String url) {
        super();
        this.versionCode = versionCode;
        this.versionName = versionName;
        this.versionInfo = versionInfo;
        this.url = url;
    }

    public Version() {
        super();
    }

    @Override
    public String toString() {
        return "Version [versionCode=" + versionCode + ", versionName=" + versionName + ", versionInfo=" + versionInfo + "]";
    }

}
