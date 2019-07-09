package com.bytedance.android.lesson.restapi.solution;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CAMERA_RECORD_AUDIO_STORAGE = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button permision=findViewById(R.id.btn_permision);
        permision.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                        ContextCompat.checkSelfPermission(MainActivity.this,
                                Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED ||
                        ContextCompat.checkSelfPermission(MainActivity.this,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.CAMERA,
                                    Manifest.permission.RECORD_AUDIO,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            REQUEST_CAMERA_RECORD_AUDIO_STORAGE);

                }
            }
        });
    }

    public void Solution2C1(View view) {
        startActivity(new Intent(this, Solution2C1Activity.class));
    }

    public void Solution2C2(View view) {
        startActivity(new Intent(this, Solution2C2Activity.class));

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (permissions.length <= 0 && grantResults.length <= 0) {
            return;
        }
        switch (requestCode) {
            case REQUEST_CAMERA_RECORD_AUDIO_STORAGE:
                for (int i = 0; i < grantResults.length; i++) {
                    int state = grantResults[i];
                    if (state == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(MainActivity.this, permissions[i] + " permission granted",
                                Toast.LENGTH_SHORT).show();
                    } else if (state == PackageManager.PERMISSION_DENIED) {
                        Toast.makeText(MainActivity.this, permissions[i] + " permission denied",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                startActivity(new Intent(MainActivity.this, Solution2C2Activity.class));
                break;
        }
    }

}
