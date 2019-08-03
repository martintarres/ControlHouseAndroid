package com.example.appcontrolhouseandroid;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;


public class Main2Activity extends AppCompatActivity implements View.OnClickListener{

    OutputStream ons;
    InputStream ins;
    GlobalState state;
    PublishSubject<String> publishSubject;
    Disposable subscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        System.out.println("activity2");
        state= ((GlobalState) getApplicationContext());

        try {
            ons=state.getSocket().getOutputStream();
            ins=state.getSocket().getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

        publishSubject=state.getPublish();
        subscription = publishSubject.subscribe(i -> System.out.println("recibo en main2 " + i));
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {


        }
    }
}
