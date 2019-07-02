package chapter.android.aweme.ss.com.homework;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 作业2：一个抖音笔试题：统计页面所有view的个数
 * Tips：ViewGroup有两个API
 * {@link android.view.ViewGroup #getChildAt(int) #getChildCount()}
 * 用一个TextView展示出来
 */
public class Exercises2 extends AppCompatActivity {
    private TextView tv;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.excises2);
        tv=findViewById(R.id.tv2);
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.excises2, null);
        tv.setText(""+getViewCount(view));

    }

    public static int getViewCount(View view) {
        //todo 补全你的代码
        int num=0;
        if(view==null) {
            return 0;
        }
        if (view instanceof ViewGroup) {
            num++;
            for (int i = 0; i < ((ViewGroup)view).getChildCount(); i++) {
                View view2=((ViewGroup)view).getChildAt(i);
                if (view2 instanceof ViewGroup) {
                    num+=getViewCount(view2);
                }
                else {
                    num++;
                }

            }

        }
        return num;
    }
}
