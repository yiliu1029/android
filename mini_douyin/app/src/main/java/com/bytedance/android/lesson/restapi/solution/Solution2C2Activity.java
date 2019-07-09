package com.bytedance.android.lesson.restapi.solution;

import android.content.Intent;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bytedance.android.lesson.restapi.solution.bean.Cat;
import com.bytedance.android.lesson.restapi.solution.bean.Feed;
import com.bytedance.android.lesson.restapi.solution.bean.FeedResponse;
import com.bytedance.android.lesson.restapi.solution.bean.PostVideoResponse;
import com.bytedance.android.lesson.restapi.solution.newtork.ICatService;
import com.bytedance.android.lesson.restapi.solution.newtork.IMiniDouyinService;
import com.bytedance.android.lesson.restapi.solution.utils.ResourceUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class Solution2C2Activity extends AppCompatActivity {

    private static final int PICK_IMAGE = 1;
    private static final int PICK_VIDEO = 2;
    private static final String TAG = "Solution2C2Activity";
    private RecyclerView mRv;
    private List<Feed> mFeeds = new ArrayList<>();
    public Uri mSelectedImage;
    private Uri mSelectedVideo;
    public Button mBtn;
    private Button mBtnRefresh;

    private static final int REQUEST_CAMERA_RECORD_AUDIO_STORAGE = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solution2_c2);

        initRecyclerView();
        initBtns();
        checkpermission();

        Button take=findViewById(R.id.btntake);
        take.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    Log.d(TAG,"turn to cus");
                    Intent startIntent=new Intent(Solution2C2Activity.this, CustomCameraActivity.class);
                    startActivity(startIntent);

            }
        });
    }


    private void checkpermission(){
        int permissionCheck = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
        if(permissionCheck != PERMISSION_GRANTED){
            Log.d("TAG","dont have this permission");
            ActivityCompat.requestPermissions(this,new String []{Manifest.permission.READ_EXTERNAL_STORAGE},1);
        }
    }
    private void initBtns() {
        mBtn = findViewById(R.id.btn);
        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                String s = mBtn.getText().toString();
                if (getString(R.string.select_an_image).equals(s)) {
                    chooseImage();
                } else if (getString(R.string.select_a_video).equals(s)) {
                    chooseVideo();
                } else if (getString(R.string.post_it).equals(s)) {
                    if (mSelectedVideo != null && mSelectedImage != null) {
                        postVideo();
                    } else {
                        throw new IllegalArgumentException("error data uri, mSelectedVideo = " + mSelectedVideo + ", mSelectedImage = " + mSelectedImage);
                    }
                } else if ((getString(R.string.success_try_refresh).equals(s))) {
                    mBtn.setText(R.string.select_an_image);
                }
            }
        });

        mBtnRefresh = findViewById(R.id.btn_refresh);
    }

    private void initRecyclerView() {
        mRv = findViewById(R.id.rv);
        mRv.setLayoutManager(new LinearLayoutManager(this));
        mRv.setAdapter(new RecyclerView.Adapter() {
            @NonNull @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                ImageView imageView = new ImageView(viewGroup.getContext());
                imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                imageView.setAdjustViewBounds(true);
                return new Solution2C1Activity.MyViewHolder(imageView);
            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                ImageView iv = (ImageView) viewHolder.itemView;

                // -C2 (10) Uncomment these 2 lines, assign image url of Feed to this url variable
                String url = mFeeds.get(i).get_image_url();
                Glide.with(iv.getContext()).load(url).into(iv);
            }

            @Override public int getItemCount() {
                return mFeeds.size();
            }
        });
    }

    public void chooseImage() {
        // -C2 (4) Start Activity to select an image
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"),PICK_IMAGE);
    }


    public void chooseVideo() {
        // -C2 (5) Start Activity to select a video
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Viedo"),PICK_VIDEO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d(TAG, "onActivityResult() called with: requestCode = [" + requestCode + "], resultCode = [" + resultCode + "], data = [" + data + "]");

        if (resultCode == RESULT_OK && null != data) {

            if (requestCode == PICK_IMAGE) {
                mSelectedImage = data.getData();
                Log.d(TAG, "selectedImage = " + mSelectedImage);
                mBtn.setText(R.string.select_a_video);
            } else if (requestCode == PICK_VIDEO) {
                mSelectedVideo = data.getData();
                Log.d(TAG, "mSelectedVideo = " + mSelectedVideo);
                mBtn.setText(R.string.post_it);
            }
        }
    }

    private MultipartBody.Part getMultipartFromUri(String name, Uri uri) {
        // if NullPointerException thrown, try to allow storage permission in system settings
        File f = new File(ResourceUtils.getRealPath(Solution2C2Activity.this, uri));
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), f);
        return MultipartBody.Part.createFormData(name, f.getName(), requestFile);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 1){
            if(grantResults[0] == PERMISSION_GRANTED){
            }else{
                Toast toast = Toast.makeText(getApplicationContext(),"上传视频需要存储权限，不能上传视频",Toast.LENGTH_LONG);
                toast.show();
            }
        }
        return;
    }

    private void postVideo() {
        mBtn.setText("POSTING...");
        mBtn.setEnabled(false);



        // -C2 (6) Send Request to post a video with its cover image
        // if success, make a text Toast and show
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://test.androidcamp.bytedance.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        MultipartBody.Part aImage = getMultipartFromUri("cover_image",mSelectedImage);
        MultipartBody.Part aVideo = getMultipartFromUri("video", mSelectedVideo);
        Call<PostVideoResponse> call = retrofit.create(IMiniDouyinService.class).postVideo(aImage,aVideo);
        call.enqueue(new Callback<PostVideoResponse>() {
            @Override
            public void onResponse(Call<PostVideoResponse> call, Response<PostVideoResponse> response) {
                if(response.isSuccessful()){
                    Log.d("TAG","upload get response success");
                    PostVideoResponse postVideoResponse = response.body();
                    if(postVideoResponse != null && postVideoResponse.getSuccess()){
                        Log.d("TAG","upload success");
                            Toast toast = Toast.makeText(getApplicationContext(), "上传成功！", Toast.LENGTH_SHORT);
                            toast.show();
                            mBtn.setText(R.string.select_an_image);
                            mBtn.setEnabled(true);
                    }else{
                        Log.d("TAG","upload failed");
                        Toast toast = Toast.makeText(getApplicationContext(),"上传失败",Toast.LENGTH_SHORT);
                        toast.show();
                        mBtn.setText(R.string.select_an_image);
                        mBtn.setEnabled(true);
                    }
                }else{
                    Log.d("TAG","upload get response failed");
                    Toast toast = Toast.makeText(getApplicationContext(),"上传失败",Toast.LENGTH_SHORT);
                    toast.show();
                    mBtn.setText(R.string.select_an_image);
                    mBtn.setEnabled(true);
                }
            }

            @Override
            public void onFailure(Call<PostVideoResponse> call, Throwable t) {

            }
        });
    }

    public void fetchFeed(View view) {
        mBtnRefresh.setText("requesting...");
        mBtnRefresh.setEnabled(false);

        // -C2 (9) Send Request to fetch feed
        // if success, assign data to mFeeds and call mRv.getAdapter().notifyDataSetChanged()
        // don't forget to call resetRefreshBtn() after response received
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://test.androidcamp.bytedance.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        IMiniDouyinService request = retrofit.create(IMiniDouyinService.class);
        Call<FeedResponse> call = request.getfeed();
        call.enqueue(new Callback<FeedResponse>() {
            @Override
            public void onResponse(Call<FeedResponse> call, Response<FeedResponse> response) {
                if(response.isSuccessful()){
                    Log.d("TAG","response isSuccess");
                    FeedResponse listfeed = response.body();
                    if(listfeed!=null && listfeed.getSuccess()){
                        List<Feed> feeds = listfeed.getFeeds();
                        mFeeds = feeds;
                        mRv.getAdapter().notifyDataSetChanged();
                    }
                    resetRefreshBtn();
                }else{
                    Log.d("TAG","response failure");
                    resetRefreshBtn();
                }
            }

            @Override
            public void onFailure(Call<FeedResponse> call, Throwable t) {

            }
        });
    }

    private void resetRefreshBtn() {
        mBtnRefresh.setText(R.string.refresh_feed);
        mBtnRefresh.setEnabled(true);
    }
}
