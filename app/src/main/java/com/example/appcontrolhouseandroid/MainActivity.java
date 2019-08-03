package com.example.appcontrolhouseandroid;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import io.reactivex.disposables.Disposable;

import io.reactivex.subjects.PublishSubject;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    //Variables para funcionamiento de la app
    private GlobalState state;
    private PublishSubject<String> publishSubject;
    private Disposable subscription;

    //Variables para UI
    private Button botonIrLuces;
    private Button botonIrAlarma;
    private Button botonIrSetup;
    private TextView textoConectado;
    private Button botonAlerta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        state = ((GlobalState) getApplicationContext());

        if(state.isPrimeraVez()){
            state.constructor();
            state.setPrimeraVez(false);
        }

        botonIrLuces= (Button) findViewById(R.id.botonIrAlarma);
        botonIrAlarma= (Button) findViewById(R.id.botonIrLuces);
        botonIrSetup= (Button) findViewById(R.id.botonSetup);
        textoConectado= (TextView) findViewById(R.id.textoConectado);
        botonAlerta= (Button) findViewById(R.id.botonAlertaMainActivity);

    }

    @Override
    protected void onStart() {
        // In KITKAT (4.4) and next releases, hide the virtual buttons
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            hideVirtualButtons();
        }
        super.onStart();
        state = ((GlobalState) getApplicationContext());
        publishSubject = state.getPublish();

        subscription = publishSubject.subscribe(i -> {
            //System.out.println("recibo en Mainctivity OnStart " + i);
            analizarObserver(i);
        });

    }

    private void hideVirtualButtons() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            // In KITKAT (4.4) and next releases, hide the virtual buttons
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                hideVirtualButtons();
            }
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.botonIrLuces: {
                Intent main2= new Intent(view.getContext(),LucesActivity.class);
                subscription.dispose();
                startActivity(main2);
                break;
            }

            case R.id.botonSetup: {
                Intent main2= new Intent(view.getContext(),SetupActivity.class);
                //aviso que voy a entrar a modo configuracion
                String data="conf\r\n";
                byte[] buffer=data.getBytes();
                try {
                    state.getSocket().getOutputStream().write(buffer);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                subscription.dispose();
                startActivity(main2);
                break;
            }

            case R.id.botonIrAlarma: {
                System.out.println("toque alarma");
                Intent alarma= new Intent(view.getContext(),AlarmaActivity.class);
                subscription.dispose();
                startActivity(alarma);
                break;
            }
            case R.id.botonAlertaMainActivity:{
                Intent alarma= new Intent(view.getContext(),AlarmaActivity.class);
                subscription.dispose();
                startActivity(alarma);
                break;
            }


        }
    }

    public void analizarObserver(String i){
        switch (i){
            case "s1ac":{
                state.setSensorMovimiento1("s1ac");
                if(state.isAlarmaActivaSensor()){
                    state.playSonido("tiempoDesactivar");
                    botonAlerta.setText("SENSOR MOVIMIENTO DE PLANTA BAJA");
                    botonAlerta.setVisibility(View.VISIBLE);
                }
                break;
            }


            case "s1de":{
                state.setSensorMovimiento1("s1de");
                break;
            }

            case "s2ac":{
                state.setSensorMovimiento2("s2ac");
                if(state.isAlarmaActivaSensor()){
                    state.playSonido("tiempoDesactivar");
                    botonAlerta.setText("SENSOR MOVIMIENTO DE PLANTA ALTA");
                    botonAlerta.setVisibility(View.VISIBLE);
                }
                break;
            }


            case "s2de":{
                state.setSensorMovimiento2("s2de");
                break;
            }

            case "m1ac":{
                if(state.isAlarmaActivaMagnetico()&& state.isTiempoParaSonarActivo()==false){
                    state.setTiempoParaSonarActivo(true);
                    state.playSonido("tiempoDesactivar");
                    botonAlerta.setText("PUERTA PRINCIPAL ");
                    botonAlerta.setVisibility(View.VISIBLE);
                }
                state.setSensorMagnetico1("m1ac");
                break;
            }


            case "m1de":{
                state.setSensorMagnetico1("m1de");
                break;
            }

            case "m2ac":{
                if(state.isAlarmaActivaMagnetico()&& state.isTiempoParaSonarActivo()==false){
                    state.setTiempoParaSonarActivo(true);
                    state.playSonido("tiempoDesactivar");
                    botonAlerta.setText("PUERTA TRASERA");
                    botonAlerta.setVisibility(View.VISIBLE);
                }
                state.setSensorMagnetico2("m2ac");
                break;
            }


            case "m2de":{
                state.setSensorMagnetico2("m2de");
                break;
            }

            case "m3ac":{
                if(state.isAlarmaActivaMagnetico()&& state.isTiempoParaSonarActivo()==false){
                    state.setTiempoParaSonarActivo(true);
                    state.playSonido("tiempoDesactivar");
                    botonAlerta.setText("PUERTA CUARTO");
                    botonAlerta.setVisibility(View.VISIBLE);
                }
                state.setSensorMagnetico3("m3ac");
                break;
            }


            case "m3de":{
                state.setSensorMagnetico3("m3de");
                break;
            }
        }
    }


}
