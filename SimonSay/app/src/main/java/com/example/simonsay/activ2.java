package com.example.simonsay;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

public class activ2 extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.medium_level);

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.fade_enter, R.anim.fade_exit);
    }
}