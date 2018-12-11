package com.miracle.sample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.miracle.logdog.Logdog;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_1)
    public void addLog1(View view) {
        TestBean testBean = new TestBean();
        testBean.setArgs("test_id");
        testBean.setArgs("test_id");
        testBean.setArgs("test_device_id");
        testBean.setArgs("test_version");
        Logdog.log(testBean);
    }

    @OnClick(R.id.commit_btn)
    public void push(View view) {
        Logdog.pushMultiPart(2);
    }


}
