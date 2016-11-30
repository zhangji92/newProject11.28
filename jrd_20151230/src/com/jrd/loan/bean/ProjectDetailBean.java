package com.jrd.loan.bean;

import java.io.Serializable;

/**
 * 项目介绍
 *
 * @author Jacky
 */
public class ProjectDetailBean extends BaseBean implements Serializable {
    private static final long serialVersionUID = 1L;
    private ProjectList projectInfo;
    private FirstInvestInfo firstInvestInfo;

    public ProjectList getProjectInfo() {
        return projectInfo;
    }

    public void setProjectInfo(ProjectList projectInfo) {
        this.projectInfo = projectInfo;
    }

    public FirstInvestInfo getFirstInvestInfo() {
        return firstInvestInfo;
    }

    public void setFirstInvestInfo(FirstInvestInfo firstInvestInfo) {
        this.firstInvestInfo = firstInvestInfo;
    }
}
