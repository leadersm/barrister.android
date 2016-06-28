package com.lsm.barrister.data.entity;

import java.io.Serializable;

/**
 * Created by lvshimin on 16/5/20.
 * 咨询案件领域（类型）
 * 房产纠纷
 * 合同纠纷
 * 劳动用工
 * 交通事故
 * 公司相关
 * 婚姻家庭
 * 工商赔偿
 * 知识产权
 * 债券债务
 * 刑事辩护
 */
public class BusinessArea extends Filter implements Serializable{

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
