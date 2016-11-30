package com.jrd.loan.bean;

import java.io.Serializable;

/**
 * 7付你信息实体
 *
 * @author Aaron
 */
public class SfnInfoBean extends BaseBean implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private SevenInfo sevenInfo;

    public SevenInfo getSevenInfo() {
        return sevenInfo;
    }

    public void setSevenInfo(SevenInfo sevenInfo) {
        this.sevenInfo = sevenInfo;
    }

}
