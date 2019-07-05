package com.bytedance.android.lesson.restapi.solution.bean;

import com.google.gson.annotations.SerializedName;

/**
 * @author Xavier.S
 * @date 2019.01.18 17:53
 */
public class PostVideoResponse {

    // TODO-C2 (3) Implement your PostVideoResponse Bean here according to the response json
    @SerializedName("success")
    private boolean success;

    @SerializedName("item")
    private ResponseItem item;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public ResponseItem getItem() {
        return item;
    }

    public void setItem(ResponseItem item) {
        this.item = item;
    }

    @Override
    public String toString() {
        return "PostVideoResponse{" +
                "success=" + success +
                ", item=" + item +
                '}';
    }
}
