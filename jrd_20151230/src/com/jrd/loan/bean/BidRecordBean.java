package com.jrd.loan.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 投标记录
 *
 * @author Aaron
 */
public class BidRecordBean extends BaseBean implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private List<InvestsList> invests;

    public List<InvestsList> getInvests() {
        return invests;
    }

    public void setInvests(List<InvestsList> invests) {
        this.invests = invests;
    }


}
