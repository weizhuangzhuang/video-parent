package com.wzz.video.utils;

public class VideoJSONResult {
    // 响应业务状态
    private Integer status;

    // 响应消息
    private String msg;

    // 响应中的数据
    private Object data;

    private String ok;	// 不使用

    public static VideoJSONResult build(Integer status, String msg, Object data) {
        return new VideoJSONResult(status, msg, data);
    }

    public static VideoJSONResult ok(Object data) {
        return new VideoJSONResult(data);
    }

    public static VideoJSONResult ok() {
        return new VideoJSONResult(null);
    }

    public static VideoJSONResult errorMsg(String msg) {
        return new VideoJSONResult(500, msg, null);
    }

    public static VideoJSONResult errorMap(Object data) {
        return new VideoJSONResult(501, "error", data);
    }

    public static VideoJSONResult errorTokenMsg(String msg) {
        return new VideoJSONResult(502, msg, null);
    }

    public static VideoJSONResult errorException(String msg) {
        return new VideoJSONResult(555, msg, null);
    }

    public VideoJSONResult() {

    }

    public VideoJSONResult(Integer status, String msg, Object data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    public VideoJSONResult(Object data) {
        this.status = 200;
        this.msg = "OK";
        this.data = data;
    }

    public Boolean isOK() {
        return this.status == 200;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getOk() {
        return ok;
    }

    public void setOk(String ok) {
        this.ok = ok;
    }
}
