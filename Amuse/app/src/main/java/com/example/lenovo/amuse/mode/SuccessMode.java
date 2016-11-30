package com.example.lenovo.amuse.mode;

import net.tsz.afinal.annotation.sqlite.Table;

import static android.R.attr.id;

/**
 * Created by lenovo on 2016/9/27.
 * 登陆成功
 */

public class SuccessMode {

    /**
     * code : 10000
     * message : 成功
     * resultCode : {"Id":"91","mobile":"17073353257","nickname":"猴子","imgUrl":"/data/upfiles/00001.png","gender":"1","age":"20","address":"","token":"MGQxYWIyYzc4NjIxMjk2ZDY2Y2NiMTc4NjY5M2M0MTc=","ry_token":"q+WtlB9DZVVmJcjLC72OCseLtn+1Si63plIwhNlkqKGRM6C5wcXlKgVQLtS6lSNunHpDSvmFfLE3L0DdJ+ftzA==","attention_count":"0","fans_count":"0"}
     */

    private String code;
    private String message;
    /**
     * Id : 91
     * mobile : 17073353257
     * nickname : 猴子
     * imgUrl : /data/upfiles/00001.png
     * gender : 1
     * age : 20
     * address :
     * token : MGQxYWIyYzc4NjIxMjk2ZDY2Y2NiMTc4NjY5M2M0MTc=
     * ry_token : q+WtlB9DZVVmJcjLC72OCseLtn+1Si63plIwhNlkqKGRM6C5wcXlKgVQLtS6lSNunHpDSvmFfLE3L0DdJ+ftzA==
     * attention_count : 0
     * fans_count : 0
     */

    private ResultCodeBean resultCode;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ResultCodeBean getResultCode() {
        return resultCode;
    }

    public void setResultCode(ResultCodeBean resultCode) {
        this.resultCode = resultCode;
    }


}
