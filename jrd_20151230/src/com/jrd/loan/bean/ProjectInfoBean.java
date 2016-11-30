package com.jrd.loan.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 首页标的信息
 *
 * @author Aaron
 */
public class ProjectInfoBean extends BaseBean implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private List<ProjectList> projectList;

    public List<ProjectList> getProjectList() {
        return projectList;
    }

    public void setProjectList(List<ProjectList> projectList) {
        this.projectList = projectList;
    }

}
