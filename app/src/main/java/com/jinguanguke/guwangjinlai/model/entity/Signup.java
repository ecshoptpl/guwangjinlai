package com.jinguanguke.guwangjinlai.model.entity;

/**
 * Created by jin on 16/4/21.
 */
public class Signup {

    /**
     * err_code : 0
     * err_msg : success
     * data : false
     */

    private String err_code;
    private String err_msg;
    private boolean data;

    public String getErr_code() {
        return err_code;
    }

    public void setErr_code(String err_code) {
        this.err_code = err_code;
    }

    public String getErr_msg() {
        return err_msg;
    }

    public void setErr_msg(String err_msg) {
        this.err_msg = err_msg;
    }

    public boolean isData() {
        return data;
    }

    public void setData(boolean data) {
        this.data = data;
    }
}
