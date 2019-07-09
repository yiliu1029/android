package com.bytedance.android.lesson.restapi.solution.bean;

/**
 * @author Xavier.S
 * @date 2019.01.17 18:08
 */
public class Cat {
//[{"breeds":[],"id":"cn0","url":"https://cdn2.thecatapi.com/images/cn0.jpg","width":612,"height":612}]


    // -C1 (1) Implement your Cat Bean here according to the response json

    private String url;

    public String getUrl(){
        return url;
    }
}
