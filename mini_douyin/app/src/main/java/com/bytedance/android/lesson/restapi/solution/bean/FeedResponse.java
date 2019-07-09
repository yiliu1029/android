package com.bytedance.android.lesson.restapi.solution.bean;

import java.util.List;

/**
 * @author Xavier.S
 * @date 2019.01.20 14:17
 */
public class FeedResponse {

    // -C2 (2) Implement your FeedResponse Bean here according to the response json
    private List<Feed> feeds;
    private boolean success;
    public List<Feed> getFeeds(){
        return feeds;
    }
    public boolean getSuccess(){
        return success;
    }

}
