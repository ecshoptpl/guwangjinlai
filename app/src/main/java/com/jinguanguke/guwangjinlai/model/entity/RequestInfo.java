package com.jinguanguke.guwangjinlai.model.entity;

import java.io.Serializable;

/**
 * Created by jin on 16/4/23.
 */
public class RequestInfo implements Serializable {

    /**
     * err_code : 1
     * err_msg : 手机号已存在
     */

    public int err_code;
    public String err_msg;

    @Override
    public String toString() {
        return "RequestInfo{" +
                " err_code=" + err_code + '\'' +
                ", err_msg=" + err_msg +
                '}';
    }
}
