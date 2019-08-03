package com.example.appcontrolhouseandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;

import java.io.IOException;

import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;

public class AlarmaActivity extends AppCompatActivity implements View.OnClickListener {

    //Variables para funcionamiento de la app
    private GlobalState state;
    private PublishSubject<String> publishSubject;
    private Disposable subscription;

    //Variables para UI
    private Button textoPlantaBaja;
    private Button textoPlantaAlta;
    private Button textoPuertaPrincipal;
    private Button textoPuertaTrasera;
    private Button textoPuertaCuarto;
    private Drawable imagenTilde;
    private Drawable imagenCruz;
    private Button boton0;
    private Button boton00;
    private Button boton1;
    private Button boton2;
    private Button boton3;
    private Button boton4;
    private Button boton5;
    private Button boton6;
    private Button boton7;
    private Button boton8;
    private Button boton9;
    private Button botonActivarAlarma;
    private ImageButton botonDelete;
    private ImageButton botonIrPantallaPrincipalAlarma;
    private TextView textoAlarmaActiva;
    private TextView textoNumerosPulsadosAlarma;
    private RadioButton radioButtonPuertasSensores;
    private RadioButton radioButtonPuertas;


    //variables metodos internos
    private int contadorNumerosAgregados=0;
    private String claveArmandose="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarma);


        imagenTilde= this.getResources().getDrawable(R.drawable.round_done_black_48);
        imagenCruz= this.getResources().getDrawable(R.drawable.round_clear_black_48);

        boton0 = (Button) findViewById(R.id.boton0Alarma);
        boton00 = (Button) findViewById(R.id.boton00Alarma);
        boton1 = (Button) findViewById(R.id.boton1Alarma);
        boton2 = (Button) findViewById(R.id.boton2Alarma);
        boton3 = (Button) findViewById(R.id.boton3Alarma);
        boton4 = (Button) findViewById(R.id.boton4Alarma);
        boton5 = (Button) findViewById(R.id.boton5Alarma);
        boton6 = (Button) findViewById(R.id.boton6Alarma);
        boton7 = (Button) findViewById(R.id.boton7Alarma);
        boton8 = (Button) findViewById(R.id.boton8Alarma);
        boton9 = (Button) findViewById(R.id.boton9Alarma);
        botonDelete = (ImageButton) findViewById(R.id.botonDeleteAlarma);
        botonIrPantallaPrincipalAlarma = (ImageButton) findViewById(R.id.botonIrPantallaPrincipalAlarma);
        botonActivarAlarma = (Button) findViewById(R.id.botonActivarAlarma);


        textoPlantaBaja = (Button) findViewById(R.id.textoPlantaBaja);
        textoPlantaAlta = (Button) findViewById(R.id.textoPlantaAlta);
        textoPuertaPrincipal = (Button) findViewById(R.id.textoPuertaPrincipal);
        textoPuertaTrasera = (Button) findViewById(R.id.textoPuertaTrasera);
        textoPuertaCuarto = (Button) findViewById(R.id.textoPuertaCuarto);
        textoAlarmaActiva = (TextView) findViewById(R.id.textoAlarmaActiva);
        textoNumerosPulsadosAlarma= (TextView) findViewById(R.id.textoNumerosPulsadosAlarma);

        radioButtonPuertas= (RadioButton) findViewById(R.id.radioButtonPuertas);
        radioButtonPuertasSensores= (RadioButton) findViewById(R.id.radioButtonPuertasSensores);
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
            //System.out.println("recibo en AlarmaActivity OnStart " + i);
            analizarObserver(i);
        });

        if(state.isAlarmaActivaSensor() || state.isAlarmaActivaMagnetico()){
            botonActivarAlarma.setVisibility(View.INVISIBLE);
            textoAlarmaActiva.setVisibility(View.VISIBLE);
        }else{
            botonActivarAlarma.setVisibility(View.VISIBLE);
            textoAlarmaActiva.setVisibility(View.INVISIBLE);
        }

        if(state.getSensorMovimiento1().equals("s1ac")){
            textoPlantaBaja.setCompoundDrawablesWithIntrinsicBounds(null,null,imagenCruz,null);
        }if(state.getSensorMovimiento1().equals("s1de")){
            textoPlantaBaja.setCompoundDrawablesWithIntrinsicBounds(null,null,imagenTilde,null);
        }

        if(state.getSensorMovimiento2().equals("s2ac")){
            textoPlantaAlta.setCompoundDrawablesWithIntrinsicBounds(null,null,imagenCruz,null);
        }if(state.getSensorMovimiento2().equals("s2de")){
            textoPlantaAlta.setCompoundDrawablesWithIntrinsicBounds(null,null,imagenTilde,null);
        }

        if(state.getSensorMagnetico1().equals("m1ac")){
            textoPuertaPrincipal.setCompoundDrawablesWithIntrinsicBounds(null,null,imagenCruz,null);
        }if(state.getSensorMagnetico1().equals("m1de")){
            textoPuertaPrincipal.setCompoundDrawablesWithIntrinsicBounds(null,null,imagenTilde,null);
        }

        if(state.getSensorMagnetico2().equals("m2ac")){
            textoPuertaTrasera.setCompoundDrawablesWithIntrinsicBounds(null,null,imagenCruz,null);
        }if(state.getSensorMagnetico2().equals("m2de")){
            textoPuertaTrasera.setCompoundDrawablesWithIntrinsicBounds(null,null,imagenTilde,null);
        }

        if(state.getSensorMagnetico3().equals("m3ac")){
            textoPuertaCuarto.setCompoundDrawablesWithIntrinsicBounds(null,null,imagenCruz,null);
        }if(state.getSensorMagnetico3().equals("m3de")){
            textoPuertaCuarto.setCompoundDrawablesWithIntrinsicBounds(null,null,imagenTilde,null);
        }

        if(state.getMetodoAlarma()==0){
            radioButtonPuertasSensores.setChecked(true);
        }else{
            radioButtonPuertas.setChecked(true);
        }

        if(state.isAlarmaActivaSensor() || state.isAlarmaActivaMagnetico()){
            radioButtonPuertas.setClickable(false);
            radioButtonPuertasSensores.setClickable(false);
        }else{
            radioButtonPuertas.setClickable(true);
            radioButtonPuertasSensores.setClickable(true);
        }
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

            case R.id.boton0Alarma: {
                setPoniendoClave("0");
                break;
            }

            case R.id.boton00Alarma: {
                setPoniendoClave("00");
                break;
            }

            case R.id.boton1Alarma: {
                setPoniendoClave("1");
                break;
            }

            case R.id.boton2Alarma: {
                setPoniendoClave("2");
                break;
            }

            case R.id.boton3Alarma: {
                setPoniendoClave("3");
                break;
            }

            case R.id.boton4Alarma: {
                setPoniendoClave("4");
                break;
            }

            case R.id.boton5Alarma: {
                setPoniendoClave("5");
                break;
            }

            case R.id.boton6Alarma: {
                setPoniendoClave("6");
                break;
            }

            case R.id.boton7Alarma: {
                setPoniendoClave("7");
                break;
            }

            case R.id.boton8Alarma: {
                setPoniendoClave("8");
                break;
            }

            case R.id.boton9Alarma: {
                setPoniendoClave("9");
                break;
            }

            case R.id.botonIrPantallaPrincipalAlarma: {
                Intent mainActivity= new Intent(view.getContext(),MainActivity.class);
                subscription.dispose();
                startActivity(mainActivity);
                break;
            }

            case R.id.botonDeleteAlarma:{
                System.out.println("toque delete");
                break;
            }

            case R.id.botonActivarAlarma:{
                String data="";
                if(state.getMetodoAlarma()==0){
                    data="alac\r\n";
                }else{
                    data="maac\r\n";
                }
                byte[] buffer=data.getBytes();
                try {
                    System.out.println("envie " + data);
                    state.getSocket().getOutputStream().write(buffer);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                textoAlarmaActiva.setText("ACTIVANDO ALARMA");
                textoAlarmaActiva.setVisibility(View.VISIBLE);
                botonActivarAlarma.setVisibility(View.INVISIBLE);
                radioButtonPuertas.setClickable(false);
                radioButtonPuertasSensores.setClickable(false);
                break;
            }


        }
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radioButtonPuertas:
                if (checked)
                    System.out.println("toque puertas");
                    state.setMetodoAlarma(1);
                    break;
            case R.id.radioButtonPuertasSensores:
                if (checked)
                    System.out.println("toque puertas y sensores");
                    state.setMetodoAlarma(0);
                    break;
        }
    }

    public void analizarObserver(String i){
        switch (i){
            case "s1ac":{
                textoPlantaBaja.setCompoundDrawablesWithIntrinsicBounds(null,null,imagenCruz,null);
                state.setSensorMovimiento1("s1ac");
                state.setSensorMovimiento1("s1ac");
                if(state.isAlarmaActivaSensor()&& state.isTiempoParaSonarActivo()==false){
                    state.setTiempoParaSonarActivo(true);
                    state.playSonido("tiempoDesactivar");
                }
                break;
            }


            case "s1de":{
                textoPlantaBaja.setCompoundDrawablesWithIntrinsicBounds(null,null,imagenTilde,null);
                state.setSensorMovimiento1("s1de");
                break;
            }

            case "s2ac":{
                textoPlantaAlta.setCompoundDrawablesWithIntrinsicBounds(null,null,imagenCruz,null);
                state.setSensorMovimiento2("s2ac");
                if(state.isAlarmaActivaSensor()&& state.isTiempoParaSonarActivo()==false){
                    state.setTiempoParaSonarActivo(true);
                    state.playSonido("tiempoDesactivar");
                }
                break;
            }


            case "s2de":{
                textoPlantaAlta.setCompoundDrawablesWithIntrinsicBounds(null,null,imagenTilde,null);
                state.setSensorMovimiento2("s2de");
                break;
            }

            case "m1ac":{
                System.out.println("m1ac "+ state.isTiempoParaSonarActivo());
                if(state.isAlarmaActivaSensor()&& state.isTiempoParaSonarActivo()==false){
                    state.setTiempoParaSonarActivo(true);
                    state.playSonido("tiempoDesactivar");
                }
                textoPuertaPrincipal.setCompoundDrawablesWithIntrinsicBounds(null,null,imagenCruz,null);
                state.setSensorMagnetico1("m1ac");


                break;
            }


            case "m1de":{
                textoPuertaPrincipal.setCompoundDrawablesWithIntrinsicBounds(null,null,imagenTilde,null);
                state.setSensorMagnetico1("m1de");

                break;
            }

            case "m2ac":{
                textoPuertaTrasera.setCompoundDrawablesWithIntrinsicBounds(null,null,imagenCruz,null);
                state.setSensorMagnetico2("m2ac");
                if(state.isAlarmaActivaSensor()&& state.isTiempoParaSonarActivo()==false){
                    state.setTiempoParaSonarActivo(true);
                    state.playSonido("tiempoDesactivar");
                }
                break;
            }


            case "m2de":{
                textoPuertaTrasera.setCompoundDrawablesWithIntrinsicBounds(null,null,imagenTilde,null);
                state.setSensorMagnetico2("m2de");
                break;
            }

            case "m3ac":{
                textoPuertaCuarto.setCompoundDrawablesWithIntrinsicBounds(null,null,imagenCruz,null);
                //System.out.println("asdasdasd");
                state.setSensorMagnetico3("m3ac");
                if(state.isAlarmaActivaSensor()&& state.isTiempoParaSonarActivo()==false){
                    state.setTiempoParaSonarActivo(true);
                    state.playSonido("tiempoDesactivar");
                }
                break;
            }


            case "m3de":{
                textoPuertaCuarto.setCompoundDrawablesWithIntrinsicBounds(null,null,imagenTilde,null);
                state.setSensorMagnetico3("m3de");
                break;
            }

            case "alac":{
                state.setAlarmaActivaSensor(true);
                state.setAlarmaActivaMagnetico(true);
                textoAlarmaActiva.setText("ALARMA ACTIVA");
            }

            case "maac":{
                state.setAlarmaActivaMagnetico(true);
                textoAlarmaActiva.setText("ALARMA ACTIVA");
            }
        }
    }

    private void setPoniendoClave(String numero){
        if(state.isAlarmaActivaSensor() || state.isAlarmaActivaMagnetico()){
            if(numero=="00"){
                contadorNumerosAgregados++;
                contadorNumerosAgregados++;
            }else{
                contadorNumerosAgregados++;
            }

            claveArmandose+=numero;
            textoNumerosPulsadosAlarma.setText(claveArmandose);
            if(contadorNumerosAgregados==4){
                contadorNumerosAgregados=0;
                for(int i=0;i<state.getListaClaves().size();i++){
                    System.out.println(claveArmandose + " - " + state.getListaClaves().get(i));
                    if(state.getListaClaves().get(i).equals(claveArmandose)){
                        System.out.println("desactivo alarma");
                        textoAlarmaActiva.setVisibility(View.INVISIBLE);
                        botonActivarAlarma.setVisibility(View.VISIBLE);
                        if(state.isAlarmaActivaMagnetico()){
                            state.setAlarmaActivaMagnetico(false);
                        }

                        if(state.isAlarmaActivaSensor()){
                            state.setAlarmaActivaSensor(false);
                            state.setAlarmaActivaMagnetico(false);
                        }

                        textoNumerosPulsadosAlarma.setText("");
                        radioButtonPuertas.setClickable(true);
                        radioButtonPuertasSensores.setClickable(true);
                        state.setSirenaSonando(false);
                        String data="";
                        if(state.getMetodoAlarma()==0){
                            data="alde\r\n";
                        }else{
                            data="made\r\n";
                        }
                        byte[] buffer=data.getBytes();
                        try {
                            System.out.println("envie " + data);
                            state.getSocket().getOutputStream().write(buffer);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }
                claveArmandose="";
            }
        }
    }
}

