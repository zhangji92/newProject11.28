package com.yoka.mrskin.model.data;

import org.json.JSONObject;

import com.yoka.mrskin.model.base.YKData;

enum TrialState {
    TRIAL_STATE_APPLICABLE, // 未申请
    TRIAL_STATE_APPLIED, // 已申请
    TRIAL_STATE_APPLY_FAILED, // 申请失败
    TRIAL_STATE_SHIPPED, // 已发货
    TRIAL_STATE_SUMMIT_REPORT, // 待提交报告
    TRIAL_STATE_DONE, // 已完成

    TRIAL_STATE_PENDINIG, // 审核中
};

public class YKTrialProduct extends YKData
{

    /**
     * 
     */
    private static final long serialVersionUID = 696259183928115756L;
    private String mId;
    private String mSpec;
    private YKProduct mProduct;
    private int mAmount;
    private TrialState mState;
    private int mIntState;
    private boolean mIsTrialProduct;
    private String mUrl;
    private YKApplyTime mApplyTime;
    private boolean isChange;
    private boolean isRead = false;
    
    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean isRead) {
        this.isRead = isRead;
    }

    public YKApplyTime getmApplyTime() {
        return mApplyTime;
    }

    public void setmApplyTime(YKApplyTime mApplyTime) {
        this.mApplyTime = mApplyTime;
    }

    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }

    public int getmIntState() {
        return mIntState;
    }

    public void setmIntState(int mIntState) {
        this.mIntState = mIntState;
    }

    public String getmUrl() {
        return mUrl;
    }

    public void setmUrl(String mUrl) {
        this.mUrl = mUrl;
    }

    public String getSpec() {
        return mSpec;
    }

    public void setSpec(String spec) {
        this.mSpec = spec;
    }

    public YKProduct getProduct() {
        return mProduct;
    }

    public void setProduct(YKProduct product) {
        this.mProduct = product;
    }

    public int getAmount() {
        return mAmount;
    }

    public void setAmount(int amount) {
        this.mAmount = amount;
    }

    public TrialState getState() {
        return mState;
    }

    public void setState(TrialState state) {
        this.mState = state;
    }

    public boolean isTrialProduct() {
        return mIsTrialProduct;
    }

    public void setTrialProduct(boolean isTrialProduct) {
        this.mIsTrialProduct = isTrialProduct;
    }

    public boolean isChange() {
        return isChange;
    }

    public void setChange(boolean isChange) {
        this.isChange = isChange;
    }

    public static YKTrialProduct parse(JSONObject object) {
        YKTrialProduct product = new YKTrialProduct();
        if (object != null) {
            product.parseData(object);
        }
        return product;
    }

    protected void parseData(JSONObject object) {

        JSONObject tmpObject = null;
        try {
            mId = object.optString("id");
        } catch (Exception e) {
        }

        try {
            mSpec = object.optString("spec");
        } catch (Exception e) {
        }

        try {
            tmpObject = object.optJSONObject("product");
            mProduct = YKProduct.parse(tmpObject);
        } catch (Exception e) {
        }
        try {
            JSONObject timeObject = object.optJSONObject("apply_time");
            mApplyTime = YKApplyTime.parse(timeObject);
        } catch (Exception e) {
        }

        try {
            mAmount = object.optInt("amount");
        } catch (Exception e) {
        }

        try {
            mIsTrialProduct = object.optBoolean("is_trial_product");
        } catch (Exception e) {
        }

        try {
            int num = object.optInt("ischange");
            if (num == 0) {
                isChange = false;
            } else {
                isChange = true;
            }
        } catch (Exception e) {
        }

        try {
            mUrl = object.optString("url");
        } catch (Exception e) {
        }

        try {
            mIntState = object.optInt("state");
        } catch (Exception e) {
        }

        try {
            int tmpInt = object.optInt("state");
            mState = parseState(tmpInt);
        } catch (Exception e) {
        }
    }

    private TrialState parseState(int number) {
        TrialState state;
        switch (number) {
        case 1:
            state = TrialState.TRIAL_STATE_APPLICABLE;
            break;
        case 2:
            state = TrialState.TRIAL_STATE_APPLIED;
            break;
        case 3:
            state = TrialState.TRIAL_STATE_SHIPPED;
            break;
        case 4:
            state = TrialState.TRIAL_STATE_SUMMIT_REPORT;
            break;
        case 5:
            state = TrialState.TRIAL_STATE_DONE;
            break;
        case 6:
            state = TrialState.TRIAL_STATE_APPLY_FAILED;
            break;
        case 7:
            state = TrialState.TRIAL_STATE_PENDINIG;
            break;

        default:
            state = TrialState.TRIAL_STATE_APPLICABLE;
            break;
        }
        return state;

    }
}
