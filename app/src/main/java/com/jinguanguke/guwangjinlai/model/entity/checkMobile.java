package com.jinguanguke.guwangjinlai.model.entity;

/**
 * Created by jin on 16/4/23.
 */
public class checkMobile {

    /**
     * err_code : 1
     * err_msg : 没有手机号
     */

    private int err_code;
    private String err_msg;

    public int getErr_code() {
        return err_code;
    }

    public void setErr_code(int err_code) {
        this.err_code = err_code;
    }

    public String getErr_msg() {
        return err_msg;
    }

    public void setErr_msg(String err_msg) {
        this.err_msg = err_msg;
    }
}
