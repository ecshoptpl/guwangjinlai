package com.jinguanguke.guwangjinlai.model.entity;

import java.util.List;

/**
 * Created by jin on 16/4/30.
 */
public class Arctiny {

    /**
     * err_code : 0
     * err_msg : success
     * data : [{"id":"299"}]
     */

    private String err_code;
    private String err_msg;
    /**
     * id : 299
     */

    private java.util.List<DataBean> data;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        private String id;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }
}
