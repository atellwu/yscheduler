package com.yeahmobi.yscheduler.storage.controller;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

/**
 * 页面与服务器交换数据的处理结果<br>
 * 如果还要其他信息，可以通过put方法设置到Json中，以pop取出。
 *
 * @author Abel.Cui
 * @date 2015/3/10
 */
public class ActionResult implements Serializable {

    private static final long     serialVersionUID = 1L;
    public static final String    KEY_SUCC         = "success";
    public static final String    KEY_DATA         = "data";
    public static final String    KEY_ERRMSG       = "errmsg";
    public static final String    KEY_VERSION      = "version";

    protected Map<String, Object> result;

    public ActionResult() {
        this.result = new HashMap<String, Object>();
        this.result.put(KEY_SUCC, Boolean.TRUE);
    }

    public ActionResult(boolean succ, String msg) {
        this.result = new HashMap<String, Object>();
        this.result.put(KEY_SUCC, Boolean.valueOf(succ));
    }

    public boolean isSuccess() {
        Boolean success = (Boolean) this.result.get(KEY_SUCC);
        return ((success != null) && success.booleanValue());
    }

    public void success() {
        this.result.put(KEY_SUCC, Boolean.TRUE);
    }

    public void fail() {
        this.result.put(KEY_SUCC, Boolean.FALSE);
    }

    public void success(long version) {
        this.result.put(KEY_SUCC, Boolean.TRUE);
        JSONObject data = new JSONObject();
        data.put(KEY_VERSION, version);
        this.result.put(KEY_DATA, data);
    }

    public void fail(String errmsg) {
        this.result.put(KEY_SUCC, Boolean.FALSE);
        this.result.put(KEY_ERRMSG, errmsg);
    }

    public void put(String key, Object value) {
        this.result.put(key, value);
    }

    public Map<String, Object> getResult() {
        return this.result;
    }

    @Override
    public String toString() {
        return toJSON().toString();
    }

    public JSONObject toJSON() {
        return (JSONObject) JSONObject.toJSON(this.result);
    }

    public String toJSONString() {
        return this.toString();
    }
}
