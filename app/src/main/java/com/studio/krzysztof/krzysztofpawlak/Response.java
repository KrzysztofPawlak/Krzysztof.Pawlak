package com.studio.krzysztof.krzysztofpawlak;

import java.util.List;

/**
 * Created by Krzysiek on 2017-01-19.
 */

public class Response {

    private List<ArrayBean> array;

    public List<ArrayBean> getArray() {
        return array;
    }
    public List<ArrayBean> getUrl() {
        return array;
    }
    public void setArray(List<ArrayBean> array) {
        this.array = array;
    }

    public static class ArrayBean {
        /**
         * title : title0
         * desc : desc0
         * url : https://placehold.it/3000?text=item0
         */

        private String title;
        private String desc;
        private String url;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
