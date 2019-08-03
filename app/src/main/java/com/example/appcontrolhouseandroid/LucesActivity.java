package com.example.appcontrolhouseandroid;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;


import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.io.IOException;
import java.util.Calendar;

import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;


public class LucesActivity extends AppCompatActivity implements View.OnClickListener {

    //Variables para funcionamiento de la app
    private GlobalState state;
    private PublishSubject<String> publishSubject;
    private Disposable subscription;

    //Variables para UI
    private ImageButton botonIrPantallaPrincipalLuces;
    Switch switchPrincipalesModoAutomatico;
    Switch lucesPrincipalesPrendidas;
    ConstraintLayout layoutPrincipalNoAutomatico;
    private Button botonPrincipalSemanaDe;
    private Button botonPrincipalSemanaA;
    TextView textoPrincipalSemanaDe;
    TextView textoPrincipalSemanaA;
    private Button botonPrincipalFindeDe;
    private Button botonPrincipalFindeA;
    TextView textoPrincipalFindeDe;
    TextView textoPrincipalFindeA;

    Switch switchPatioModoAutomatico;
    Switch lucesPatioPrendidas;
    ConstraintLayout layoutPatioNoAutomatico;
    private Button botonPatioSemanaDe;
    private Button botonPatioSemanaA;
    TextView textoPatioSemanaDe;
    TextView textoPatioSemanaA;
    private Button botonPatioFindeDe;
    private Button botonPatioFindeA;
    TextView textoPatioFindeDe;
    TextView textoPatioFindeA;
    private Button botonAlerta;



    //variables metodos internos
    private static final String CERO = "0";
    private static final String DOS_PUNTOS = ":";
    //Calendario para obtener fecha & hora
    public final Calendar c = Calendar.getInstance();

    //Variables para obtener la hora hora
    final int hora = c.get(Calendar.HOUR_OF_DAY);
    final int minuto = c.get(Calendar.MINUTE);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_luces);

        botonIrPantallaPrincipalLuces = (ImageButton) findViewById(R.id.botonIrPantallaPrincipalLuces);

        botonPrincipalSemanaDe= (Button) findViewById(R.id.botonPrincipalSemanaDe);
        botonPrincipalSemanaA= (Button) findViewById(R.id.botonPrincipalSemanaA);
        textoPrincipalSemanaDe= (TextView) findViewById(R.id.textoPrincipalSemanaDe);
        textoPrincipalSemanaA= (TextView) findViewById(R.id.textoPrincipalSemanaA);

        botonPrincipalFindeDe= (Button) findViewById(R.id.botonPrincipalFindeDe);
        botonPrincipalFindeA= (Button) findViewById(R.id.botonPrincipalFindeA);
        textoPrincipalFindeDe= (TextView) findViewById(R.id.textoPrincipalFindeDe);
        textoPrincipalFindeA= (TextView) findViewById(R.id.textoPrincipalFindeA);

        botonPatioSemanaDe= (Button) findViewById(R.id.botonPatioSemanaDe);
        botonPatioSemanaA= (Button) findViewById(R.id.botonPatioSemanaA);
        textoPatioSemanaDe= (TextView) findViewById(R.id.textoPatioSemanaDe);
        textoPatioSemanaA= (TextView) findViewById(R.id.textoPatioSemanaA);

        botonPatioFindeDe= (Button) findViewById(R.id.botonPatioFindeDe);
        botonPatioFindeA= (Button) findViewById(R.id.botonPatioFindeA);
        textoPatioFindeDe= (TextView) findViewById(R.id.textoPatioFindeDe);
        textoPatioFindeA= (TextView) findViewById(R.id.textoPatioFindeA);



        switchPrincipalesModoAutomatico = (Switch) findViewById(R.id.switchPrincipalesModoAutomatico);
        lucesPrincipalesPrendidas= (Switch) findViewById(R.id.lucesPrincipalPrendidas);
        layoutPrincipalNoAutomatico= (ConstraintLayout) findViewById(R.id.layoutPrincipalNoAutomatico);

        switchPatioModoAutomatico = (Switch) findViewById(R.id.switchPatioModoAutomatico);
        lucesPatioPrendidas= (Switch) findViewById(R.id.lucesPatioPrendidas);
        layoutPatioNoAutomatico= (ConstraintLayout) findViewById(R.id.layoutPatioNoAutomatico);
        botonAlerta= (Button) findViewById(R.id.botonAlertaLucesActivity);



    }

    @Override
    protected void onStart() {

        // In KITKAT (4.4) and next releases, hide the virtual buttons
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            hideVirtualButtons();
        }


        super.onStart();
        state = ((GlobalState) getApplicationContext());
        publishSubject=state.getPublish();

        subscription = publishSubject.subscribe(i -> {
            //System.out.println("recibo en Mainctivity OnStart " + i);
            analizarObserver(i);
        });

        if(state.isLucesPrincipalesAutomatico()){
            switchPrincipalesModoAutomatico.setChecked(true);
            lucesPrincipalesPrendidas.setVisibility(View.INVISIBLE);
            layoutPrincipalNoAutomatico.setVisibility(View.VISIBLE);
        }else{
            switchPrincipalesModoAutomatico.setChecked(false);
            lucesPrincipalesPrendidas.setVisibility(View.VISIBLE);
            layoutPrincipalNoAutomatico.setVisibility(View.INVISIBLE);
        }

        switchPrincipalesModoAutomatico.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
                if (bChecked) {
                    //textView.setText(switchOn);
                    lucesPrincipalesPrendidas.setVisibility(View.INVISIBLE);
                    state.setLucesPrincipalesAutomatico(true);
                    layoutPrincipalNoAutomatico.setVisibility(View.VISIBLE);
                } else {
                    lucesPrincipalesPrendidas.setVisibility(View.VISIBLE);
                    layoutPrincipalNoAutomatico.setVisibility(View.INVISIBLE);
                    state.setLucesPrincipalesAutomatico(false);
                    if(state.isLucesPrincipalesCortexPrendido()){
                        lucesPrincipalesPrendidas.setChecked(true);
                    }else{
                        lucesPrincipalesPrendidas.setChecked(false);
                    }

                }
            }
        });

        lucesPrincipalesPrendidas.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            String data="";
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
                if (bChecked) {
                    System.out.println("prender luces principales");
                    //textView.setText(switchOn);
                    state.setLucesPrincipalesPrendido(true);
                    state.setLucesPrincipalesAppPrendido(true);
                    data="l1pr\r\n";
                    byte[] buffer=data.getBytes();
                    try {
                        state.getSocket().getOutputStream().write(buffer);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    state.setLucesPrincipalesPrendido(false);
                    state.setLucesPrincipalesAppPrendido(false);
                    data="l1ap\r\n";
                    byte[] buffer=data.getBytes();
                    try {
                        state.getSocket().getOutputStream().write(buffer);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    System.out.println("apagar luces principales");
                }
            }
        });

        if(state.isLucesPatioAutomatico()){
            switchPatioModoAutomatico.setChecked(true);
            lucesPatioPrendidas.setVisibility(View.INVISIBLE);
            layoutPatioNoAutomatico.setVisibility(View.VISIBLE);
        }else{
            switchPatioModoAutomatico.setChecked(false);
            lucesPatioPrendidas.setVisibility(View.VISIBLE);
            layoutPatioNoAutomatico.setVisibility(View.INVISIBLE);
        }

        switchPatioModoAutomatico.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
                if (bChecked) {
                    System.out.println("estoy en si");
                    //textView.setText(switchOn);
                    lucesPatioPrendidas.setVisibility(View.INVISIBLE);
                    state.setLucesPatioAutomatico(true);
                    state.setLucesPatioAppPrendido(true);
                    layoutPatioNoAutomatico.setVisibility(View.VISIBLE);
                } else {
                    System.out.println("estoy en no");
                    lucesPatioPrendidas.setVisibility(View.VISIBLE);
                    layoutPatioNoAutomatico.setVisibility(View.INVISIBLE);
                    state.setLucesPatioAutomatico(false);
                    state.setLucesPatioAppPrendido(false);
                    if(state.isLucesPatioCortexPrendido()){
                        lucesPatioPrendidas.setChecked(true);
                    }else{
                        lucesPatioPrendidas.setChecked(false);
                    }
                }
            }
        });

        lucesPatioPrendidas.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            String data="";
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
                if (bChecked) {
                    System.out.println("prender luces patio ");
                    state.setLucesPatioPrendido(true);
                    //textView.setText(switchOn);
                    data="l2pr\r\n";
                    byte[] buffer=data.getBytes();
                    try {
                        state.getSocket().getOutputStream().write(buffer);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    System.out.println("apagar luces patio");
                    state.setLucesPatioPrendido(false);
                    data="l2ap\r\n";
                    byte[] buffer=data.getBytes();
                    try {
                        state.getSocket().getOutputStream().write(buffer);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        textoPrincipalSemanaDe.setText(state.getHoraPrincipalDeSemana() + DOS_PUNTOS + state.getMinutoPrincipalDeSemana() );
        textoPrincipalSemanaA.setText(state.getHoraPrincipalASemana() + DOS_PUNTOS + state.getMinutoPrincipalASemana() );
        textoPrincipalFindeDe.setText(state.getHoraPrincipalDeFinde() + DOS_PUNTOS + state.getMinutoPrincipalDeFinde() );
        textoPrincipalFindeA.setText(state.getHoraPrincipalAFinde() + DOS_PUNTOS + state.getMinutoPrincipalAFinde() );

        textoPatioSemanaDe.setText(state.getHoraPatioDeSemana() + DOS_PUNTOS + state.getMinutoPatioDeSemana() );
        textoPatioSemanaA.setText(state.getHoraPatioASemana() + DOS_PUNTOS + state.getMinutoPatioASemana() );
        textoPatioFindeDe.setText(state.getHoraPatioDeFinde() + DOS_PUNTOS + state.getMinutoPatioDeFinde() );
        textoPatioFindeA.setText(state.getHoraPatioAFinde() + DOS_PUNTOS + state.getMinutoPatioAFinde() );

        if(state.isLucesPatioPrendido()){
            lucesPatioPrendidas.setChecked(true);
        }

        if(!state.isLucesPatioPrendido()){
            lucesPatioPrendidas.setChecked(false);
        }

        if(state.isLucesPrincipalesPrendido()){
            lucesPrincipalesPrendidas.setChecked(true);
        }

        if(!state.isLucesPrincipalesPrendido()){
            lucesPrincipalesPrendidas.setChecked(false);
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
            case R.id.botonIrPantallaPrincipalLuces: {
                Intent main = new Intent(view.getContext(), MainActivity.class);
                subscription.dispose();
                startActivity(main);
                break;
            }

            case R.id.botonPrincipalSemanaDe: {
                TimePickerDialog recogerHora = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        //Formateo el hora obtenido: antepone el 0 si son menores de 10
                        String horaFormateada =  (hourOfDay < 10)? String.valueOf(CERO + hourOfDay) : String.valueOf(hourOfDay);
                        //Formateo el minuto obtenido: antepone el 0 si son menores de 10
                        String minutoFormateado = (minute < 10)? String.valueOf(CERO + minute):String.valueOf(minute);
                        //Obtengo el valor a.m. o p.m., dependiendo de la selección del usuario
                        String AM_PM;
                        if(hourOfDay < 12) {
                            AM_PM = "a.m.";
                        } else {
                            AM_PM = "p.m.";
                        }
                        //Muestro la hora con el formato deseado
                        //etHora.setText(horaFormateada + DOS_PUNTOS + minutoFormateado + " " + AM_PM);
                        //System.out.println(horaFormateada + DOS_PUNTOS + minutoFormateado + " " + AM_PM);
                        textoPrincipalSemanaDe.setText(horaFormateada + DOS_PUNTOS + minutoFormateado );
                        state.setHoraPrincipalDeSemana(horaFormateada);
                        state.setMinutoPrincipalDeSemana(minutoFormateado);

                    }
                    //Estos valores deben ir en ese orden
                    //Al colocar en false se muestra en formato 12 horas y true en formato 24 horas
                    //Pero el sistema devuelve la hora en formato 24 horas
                }, hora, minuto, false);

                recogerHora.show();
                break;
            }

            case R.id.botonPrincipalSemanaA: {
                TimePickerDialog recogerHora = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        //Formateo el hora obtenido: antepone el 0 si son menores de 10
                        String horaFormateada =  (hourOfDay < 10)? String.valueOf(CERO + hourOfDay) : String.valueOf(hourOfDay);
                        //Formateo el minuto obtenido: antepone el 0 si son menores de 10
                        String minutoFormateado = (minute < 10)? String.valueOf(CERO + minute):String.valueOf(minute);
                        //Obtengo el valor a.m. o p.m., dependiendo de la selección del usuario
                        String AM_PM;
                        if(hourOfDay < 12) {
                            AM_PM = "a.m.";
                        } else {
                            AM_PM = "p.m.";
                        }
                        //Muestro la hora con el formato deseado
                        //etHora.setText(horaFormateada + DOS_PUNTOS + minutoFormateado + " " + AM_PM);
                        //System.out.println(horaFormateada + DOS_PUNTOS + minutoFormateado + " " + AM_PM);
                        textoPrincipalSemanaA.setText(horaFormateada + DOS_PUNTOS + minutoFormateado);
                        state.setHoraPrincipalASemana(horaFormateada);
                        state.setMinutoPrincipalASemana(minutoFormateado);

                    }
                    //Estos valores deben ir en ese orden
                    //Al colocar en false se muestra en formato 12 horas y true en formato 24 horas
                    //Pero el sistema devuelve la hora en formato 24 horas
                }, hora, minuto, false);

                recogerHora.show();
                break;
            }

            case R.id.botonPrincipalFindeDe: {
                TimePickerDialog recogerHora = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        //Formateo el hora obtenido: antepone el 0 si son menores de 10
                        String horaFormateada =  (hourOfDay < 10)? String.valueOf(CERO + hourOfDay) : String.valueOf(hourOfDay);
                        //Formateo el minuto obtenido: antepone el 0 si son menores de 10
                        String minutoFormateado = (minute < 10)? String.valueOf(CERO + minute):String.valueOf(minute);
                        //Obtengo el valor a.m. o p.m., dependiendo de la selección del usuario
                        String AM_PM;
                        if(hourOfDay < 12) {
                            AM_PM = "a.m.";
                        } else {
                            AM_PM = "p.m.";
                        }
                        //Muestro la hora con el formato deseado
                        //etHora.setText(horaFormateada + DOS_PUNTOS + minutoFormateado + " " + AM_PM);
                        //System.out.println(horaFormateada + DOS_PUNTOS + minutoFormateado + " " + AM_PM);
                        textoPrincipalFindeDe.setText(horaFormateada + DOS_PUNTOS + minutoFormateado);
                        state.setHoraPrincipalDeFinde(horaFormateada);
                        state.setMinutoPrincipalDeFinde(minutoFormateado);
                    }
                    //Estos valores deben ir en ese orden
                    //Al colocar en false se muestra en formato 12 horas y true en formato 24 horas
                    //Pero el sistema devuelve la hora en formato 24 horas
                }, hora, minuto, false);

                recogerHora.show();
                break;
            }

            case R.id.botonPrincipalFindeA: {
                TimePickerDialog recogerHora = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        //Formateo el hora obtenido: antepone el 0 si son menores de 10
                        String horaFormateada =  (hourOfDay < 10)? String.valueOf(CERO + hourOfDay) : String.valueOf(hourOfDay);
                        //Formateo el minuto obtenido: antepone el 0 si son menores de 10
                        String minutoFormateado = (minute < 10)? String.valueOf(CERO + minute):String.valueOf(minute);
                        //Obtengo el valor a.m. o p.m., dependiendo de la selección del usuario
                        String AM_PM;
                        if(hourOfDay < 12) {
                            AM_PM = "a.m.";
                        } else {
                            AM_PM = "p.m.";
                        }
                        //Muestro la hora con el formato deseado
                        //etHora.setText(horaFormateada + DOS_PUNTOS + minutoFormateado + " " + AM_PM);
                        //System.out.println(horaFormateada + DOS_PUNTOS + minutoFormateado + " " + AM_PM);
                        textoPrincipalFindeA.setText(horaFormateada + DOS_PUNTOS + minutoFormateado);
                        state.setHoraPrincipalAFinde(horaFormateada);
                        state.setMinutoPrincipalAFinde(minutoFormateado);
                    }
                    //Estos valores deben ir en ese orden
                    //Al colocar en false se muestra en formato 12 horas y true en formato 24 horas
                    //Pero el sistema devuelve la hora en formato 24 horas
                }, hora, minuto, false);

                recogerHora.show();
                break;
            }

            case R.id.botonPatioSemanaDe: {
                TimePickerDialog recogerHora = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        //Formateo el hora obtenido: antepone el 0 si son menores de 10
                        String horaFormateada =  (hourOfDay < 10)? String.valueOf(CERO + hourOfDay) : String.valueOf(hourOfDay);
                        //Formateo el minuto obtenido: antepone el 0 si son menores de 10
                        String minutoFormateado = (minute < 10)? String.valueOf(CERO + minute):String.valueOf(minute);
                        //Obtengo el valor a.m. o p.m., dependiendo de la selección del usuario
                        String AM_PM;
                        if(hourOfDay < 12) {
                            AM_PM = "a.m.";
                        } else {
                            AM_PM = "p.m.";
                        }
                        //Muestro la hora con el formato deseado
                        //etHora.setText(horaFormateada + DOS_PUNTOS + minutoFormateado + " " + AM_PM);
                        //System.out.println(horaFormateada + DOS_PUNTOS + minutoFormateado + " " + AM_PM);
                        textoPatioSemanaDe.setText(horaFormateada + DOS_PUNTOS + minutoFormateado );
                        state.setHoraPatioDeSemana(horaFormateada);
                        state.setMinutoPatioDeSemana(minutoFormateado);

                    }
                    //Estos valores deben ir en ese orden
                    //Al colocar en false se muestra en formato 12 horas y true en formato 24 horas
                    //Pero el sistema devuelve la hora en formato 24 horas
                }, hora, minuto, false);

                recogerHora.show();
                break;
            }

            case R.id.botonPatioSemanaA: {
                TimePickerDialog recogerHora = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        //Formateo el hora obtenido: antepone el 0 si son menores de 10
                        String horaFormateada =  (hourOfDay < 10)? String.valueOf(CERO + hourOfDay) : String.valueOf(hourOfDay);
                        //Formateo el minuto obtenido: antepone el 0 si son menores de 10
                        String minutoFormateado = (minute < 10)? String.valueOf(CERO + minute):String.valueOf(minute);
                        //Obtengo el valor a.m. o p.m., dependiendo de la selección del usuario
                        String AM_PM;
                        if(hourOfDay < 12) {
                            AM_PM = "a.m.";
                        } else {
                            AM_PM = "p.m.";
                        }
                        //Muestro la hora con el formato deseado
                        //etHora.setText(horaFormateada + DOS_PUNTOS + minutoFormateado + " " + AM_PM);
                        //System.out.println(horaFormateada + DOS_PUNTOS + minutoFormateado + " " + AM_PM);
                        textoPatioSemanaA.setText(horaFormateada + DOS_PUNTOS + minutoFormateado);
                        state.setHoraPatioASemana(horaFormateada);
                        state.setMinutoPatioASemana(minutoFormateado);

                    }
                    //Estos valores deben ir en ese orden
                    //Al colocar en false se muestra en formato 12 horas y true en formato 24 horas
                    //Pero el sistema devuelve la hora en formato 24 horas
                }, hora, minuto, false);

                recogerHora.show();
                break;
            }

            case R.id.botonPatioFindeDe: {
                TimePickerDialog recogerHora = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        //Formateo el hora obtenido: antepone el 0 si son menores de 10
                        String horaFormateada =  (hourOfDay < 10)? String.valueOf(CERO + hourOfDay) : String.valueOf(hourOfDay);
                        //Formateo el minuto obtenido: antepone el 0 si son menores de 10
                        String minutoFormateado = (minute < 10)? String.valueOf(CERO + minute):String.valueOf(minute);
                        //Obtengo el valor a.m. o p.m., dependiendo de la selección del usuario
                        String AM_PM;
                        if(hourOfDay < 12) {
                            AM_PM = "a.m.";
                        } else {
                            AM_PM = "p.m.";
                        }
                        //Muestro la hora con el formato deseado
                        //etHora.setText(horaFormateada + DOS_PUNTOS + minutoFormateado + " " + AM_PM);
                        //System.out.println(horaFormateada + DOS_PUNTOS + minutoFormateado + " " + AM_PM);
                        textoPatioFindeDe.setText(horaFormateada + DOS_PUNTOS + minutoFormateado);
                        state.setHoraPatioDeFinde(horaFormateada);
                        state.setMinutoPatioDeFinde(minutoFormateado);

                    }
                    //Estos valores deben ir en ese orden
                    //Al colocar en false se muestra en formato 12 horas y true en formato 24 horas
                    //Pero el sistema devuelve la hora en formato 24 horas
                }, hora, minuto, false);

                recogerHora.show();
                break;
            }

            case R.id.botonPatioFindeA: {
                TimePickerDialog recogerHora = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        //Formateo el hora obtenido: antepone el 0 si son menores de 10
                        String horaFormateada =  (hourOfDay < 10)? String.valueOf(CERO + hourOfDay) : String.valueOf(hourOfDay);
                        //Formateo el minuto obtenido: antepone el 0 si son menores de 10
                        String minutoFormateado = (minute < 10)? String.valueOf(CERO + minute):String.valueOf(minute);
                        //Obtengo el valor a.m. o p.m., dependiendo de la selección del usuario
                        String AM_PM;
                        if(hourOfDay < 12) {
                            AM_PM = "a.m.";
                        } else {
                            AM_PM = "p.m.";
                        }
                        //Muestro la hora con el formato deseado
                        //etHora.setText(horaFormateada + DOS_PUNTOS + minutoFormateado + " " + AM_PM);
                        //System.out.println(horaFormateada + DOS_PUNTOS + minutoFormateado + " " + AM_PM);
                        textoPatioFindeA.setText(horaFormateada + DOS_PUNTOS + minutoFormateado);
                        state.setHoraPatioAFinde(horaFormateada);
                        state.setMinutoPatioAFinde(minutoFormateado);
                    }
                    //Estos valores deben ir en ese orden
                    //Al colocar en false se muestra en formato 12 horas y true en formato 24 horas
                    //Pero el sistema devuelve la hora en formato 24 horas
                }, hora, minuto, false);

                recogerHora.show();
                break;
            }

            case R.id.botonAlertaLucesActivity:{
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
