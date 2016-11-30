package com.jrd.loan.bean;

import java.io.Serializable;

/**
 * 我的优惠券——未使用
 *
 * @author Aaron
 */
public class NotUsedCoupon implements Serializable {

    private static final long serialVersionUID = 1L;
    private String hbEntId;// 红包实体ID
    private String hbType;// 1,现金券、2,加息券
    private String bagVal;// 红包值
    private String source;// 来源说明
    private String expireTime;// 失效时间（使用期限为生成时生成红包实体时设定）
    private String effectTime;// 生效时间
    private String oprateTime;// 使用时间
    private String finishTime;// 结算时间
    private String hbDesc;// 红包描述
    private String useStatus;// 红包使用状态（0未使用、1正使用、2已领取、3已失效）
    private String hbStatus;// 红包状态（0未激活、1冻结、2临时冻结、3已激活）

    public NotUsedCoupon() {
        super();
    }

    public NotUsedCoupon(String hbEntId, String hbType, String bagVal, String source, String expireTime, String effectTime, String oprateTime, String finishTime, String hbDesc, String useStatus, String hbStatus) {
        super();
        this.hbEntId = hbEntId;
        this.hbType = hbType;
        this.bagVal = bagVal;
        this.source = source;
        this.expireTime = expireTime;
        this.effectTime = effectTime;
        this.oprateTime = oprateTime;
        this.finishTime = finishTime;
        this.hbDesc = hbDesc;
        this.useStatus = useStatus;
        this.hbStatus = hbStatus;
    }

    public String getHbEntId() {
        return hbEntId;
    }

    public void setHbEntId(String hbEntId) {
        this.hbEntId = hbEntId;
    }

    public String getHbType() {
        return hbType;
    }

    public void setHbType(String hbType) {
        this.hbType = hbType;
    }

    public String getBagVal() {
        return bagVal;
    }

    public void setBagVal(String bagVal) {
        this.bagVal = bagVal;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(String expireTime) {
        this.expireTime = expireTime;
    }

    public String getEffectTime() {
        return effectTime;
    }

    public void setEffectTime(String effectTime) {
        this.effectTime = effectTime;
    }

    public String getOprateTime() {
        return oprateTime;
    }

    public void setOprateTime(String oprateTime) {
        this.oprateTime = oprateTime;
    }

    public String getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(String finishTime) {
        this.finishTime = finishTime;
    }

    public String getHbDesc() {
        return hbDesc;
    }

    public void setHbDesc(String hbDesc) {
        this.hbDesc = hbDesc;
    }

    public String getUseStatus() {
        return useStatus;
    }

    public void setUseStatus(String useStatus) {
        this.useStatus = useStatus;
    }

    public String getHbStatus() {
        return hbStatus;
    }

    public void setHbStatus(String hbStatus) {
        this.hbStatus = hbStatus;
    }

    @Override
    public String toString() {
        return "NotUsedCoupon [hbEntId=" + hbEntId + ", hbType=" + hbType + ", bagVal=" + bagVal + ", source=" + source + ", expireTime=" + expireTime + ", effectTime=" + effectTime + ", oprateTime=" + oprateTime + ", finishTime=" + finishTime + ", hbDesc=" + hbDesc + ", useStatus=" + useStatus
                + ", hbStatus=" + hbStatus + "]";
    }


}
