package com.lsm.barrister.data.entity;

import java.io.Serializable;

/**
 * Created by lvshimin on 16/5/20.
 * 业务类型（合同起草与审核、法律文书、律师函、法律意见书、法律论证、案件代理）
 * 用户点击后跳转到对领域律师列表页
 */
public class BusinessType extends Filter implements Serializable{


    String desc;
    String icon;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    boolean isChecked;

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
