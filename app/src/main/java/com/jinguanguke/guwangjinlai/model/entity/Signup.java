package com.jinguanguke.guwangjinlai.model.entity;

import java.util.List;

/**
 * Created by jin on 16/4/21.
 */
public class Signup {

    /**
     * err_code : 0
     * err_msg : success
     * data : {"items":[{"id":"3"}],"max_id":"3","min_id":"3"}
     */

    private String err_code;
    private String err_msg;
    /**
     * items : [{"id":"3"}]
     * max_id : 3
     * min_id : 3
     */

    private DataBean data;

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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private String max_id;
        private String min_id;
        /**
         * id : 3
         */

        private List<ItemsBean> items;

        public String getMax_id() {
            return max_id;
        }

        public void setMax_id(String max_id) {
            this.max_id = max_id;
        }

        public String getMin_id() {
            return min_id;
        }

        public void setMin_id(String min_id) {
            this.min_id = min_id;
        }

        public List<ItemsBean> getItems() {
            return items;
        }

        public void setItems(List<ItemsBean> items) {
            this.items = items;
        }

        public static class ItemsBean {
            private String user_id;

            public String getUser_id() {
                return user_id;
            }

            public void setId(String id) {
                this.user_id = id;
            }
        }
    }
}
