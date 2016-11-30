package com.jrd.loan.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 投资记录实体类
 *
 * @author Aaron
 */
public class InvestRecordBean extends BaseBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private ArrayList<ProjectInvestsList> projectInvests;

    public ArrayList<ProjectInvestsList> getProjectInvests() {
        return projectInvests;
    }

    public void setProjectInvests(ArrayList<ProjectInvestsList> projectInvests) {
        this.projectInvests = projectInvests;
    }

}
