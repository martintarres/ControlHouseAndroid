package com.example.appcontrolhouseandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

import java.io.IOException;
import java.util.ArrayList;

import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;

public class SetupActivity extends AppCompatActivity implements View.OnClickListener, OnItemSelectedListener, AdapterView.OnItemClickListener {

    //Variables para funcionamiento de la app
    private GlobalState state;
    private PublishSubject<String> publishSubject;
    private Disposable subscription;


    //Variables para UI
    private Spinner spinnerTiempoAlarma;
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
    private ImageButton botonAgregarClaves;
    private ImageButton botonDelete;
    private ImageButton botonIrPantallaPrincipalConfig;
    private ImageButton botonBorrarLista;
    private TextView textoIngresePasswordDesbloqueo;
    private TextView textoMostrarPasswordDesbloqueoConfiguracion;
    private ArrayList<Persona> listaAdaptadaListView;
    private AdaptadorListView adaptador;
    private ListView lv1;
    private boolean estoyAgregandoClave=false;
    private boolean deboDesbloquear=false;
    private int contadorNumerosAgregados=0;
    private String claveArmandose="";
    private String claveParaDesbloqueo="";
    private int numeroListaBorrar=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        spinnerTiempoAlarma = (Spinner) findViewById(R.id.spinnerTiempoAlarma);
        boton0 = (Button) findViewById(R.id.boton0);
        boton00 = (Button) findViewById(R.id.boton00);
        boton1 = (Button) findViewById(R.id.boton1);
        boton2 = (Button) findViewById(R.id.boton2);
        boton3 = (Button) findViewById(R.id.boton3);
        boton4 = (Button) findViewById(R.id.boton4);
        boton5 = (Button) findViewById(R.id.boton5);
        boton6 = (Button) findViewById(R.id.boton6);
        boton7 = (Button) findViewById(R.id.boton7);
        boton8 = (Button) findViewById(R.id.boton8);
        boton9 = (Button) findViewById(R.id.boton9);
        botonDelete = (ImageButton) findViewById(R.id.botonDelete);
        botonIrPantallaPrincipalConfig = (ImageButton) findViewById(R.id.botonIrPantallaPrincipalConfig);
        botonBorrarLista= (ImageButton) findViewById(R.id.botonBorrarLista);
        botonAgregarClaves = (ImageButton) findViewById(R.id.botonAgregarClaves);
        spinnerTiempoAlarma.setOnItemSelectedListener( this);

        textoIngresePasswordDesbloqueo = (TextView) findViewById(R.id.textoIngresePasswordDesbloqueo);
        textoMostrarPasswordDesbloqueoConfiguracion = (TextView) findViewById(R.id.textoMostrarPasswordDesbloqueoConfiguracion);

        listaAdaptadaListView=new ArrayList<Persona>();
        adaptador = new AdaptadorListView(this);
        lv1 = (ListView)findViewById(R.id.list1);
        lv1.setAdapter(adaptador);

        String datosSpiner[] = new String[]{"3 SEGUNDOS", "5 SEGUNDOS", "8 SEGUNDOS", "10 SEGUNDOS", "15 SEGUNDOS"};
        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, datosSpiner);
        spinnerTiempoAlarma.setAdapter(adapterSpinner);


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

        subscription = publishSubject.subscribe(i -> System.out.println("recibo en SetupActivity OnStart" + i));

        for(int i=0;i<state.getListaClaves().size();i++){
            listaAdaptadaListView.add(new Persona(state.getListaClaves().get(i),  Integer.toString(i)));
        }
        lv1.setAdapter(adaptador);
        lv1.setOnItemClickListener(this);
        spinnerTiempoAlarma.setSelection(state.getTiempoYaSeteadoAlarma());

        if (!state.getListaClaves().isEmpty()) {
            textoIngresePasswordDesbloqueo.setVisibility(View.VISIBLE);
            textoMostrarPasswordDesbloqueoConfiguracion.setVisibility(View.VISIBLE);
            botonAgregarClaves.setVisibility(View.INVISIBLE);
            lv1.setVisibility(View.INVISIBLE);
            deboDesbloquear=true;
        }

        if(state.getListaClaves().isEmpty()){
            textoMostrarPasswordDesbloqueoConfiguracion.setVisibility(View.VISIBLE);
            botonAgregarClaves.setVisibility(View.VISIBLE);
            lv1.setVisibility(View.VISIBLE);
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

            case R.id.boton0: {
                if(estoyAgregandoClave){
                    setPoniendoClave("0");
                }
                if(deboDesbloquear){
                    setPoniendoDesbloqueo("0");
                }
                break;
            }

            case R.id.boton00: {
                if(estoyAgregandoClave){
                    setPoniendoClave("00");
                }
                if(deboDesbloquear){
                    setPoniendoDesbloqueo("00");
                }
                break;
            }

            case R.id.boton1: {
                if(estoyAgregandoClave){
                    setPoniendoClave("1");
                }
                if(deboDesbloquear){
                    setPoniendoDesbloqueo("1");
                }
                break;
            }

            case R.id.boton2: {
                if(estoyAgregandoClave){
                    setPoniendoClave("2");
                }
                if(deboDesbloquear){
                    setPoniendoDesbloqueo("2");
                }
                break;
            }

            case R.id.boton3: {
                if(estoyAgregandoClave){
                    setPoniendoClave("3");
                }
                if(deboDesbloquear){
                    setPoniendoDesbloqueo("3");
                }
                break;
            }

            case R.id.boton4: {
                if(estoyAgregandoClave){
                    setPoniendoClave("4");
                }
                if(deboDesbloquear){
                    setPoniendoDesbloqueo("4");
                }
                break;
            }

            case R.id.boton5: {
                if(estoyAgregandoClave){
                    setPoniendoClave("5");
                }
                if(deboDesbloquear){
                    setPoniendoDesbloqueo("5");
                }
                break;
            }

            case R.id.boton6: {
                if(estoyAgregandoClave){
                    setPoniendoClave("6");
                }
                if(deboDesbloquear){
                    setPoniendoDesbloqueo("6");
                }
                break;
            }

            case R.id.boton7: {
                if(estoyAgregandoClave){
                    setPoniendoClave("7");
                }
                if(deboDesbloquear){
                    setPoniendoDesbloqueo("7");
                }
                break;
            }

            case R.id.boton8: {
                if(estoyAgregandoClave){
                    setPoniendoClave("8");
                }
                if(deboDesbloquear){
                    setPoniendoDesbloqueo("8");
                }
                break;
            }

            case R.id.boton9: {
                if(estoyAgregandoClave){
                    setPoniendoClave("9");
                }
                if(deboDesbloquear){
                    setPoniendoDesbloqueo("9");
                }
                break;
            }

            case R.id.botonIrPantallaPrincipalConfig: {
                Intent mainActivity= new Intent(view.getContext(),MainActivity.class);
                System.out.println("mando andr");
                String data="outc\r\n";
                byte[] buffer=data.getBytes();
                try {
                    state.getSocket().getOutputStream().write(buffer);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                subscription.dispose();
                startActivity(mainActivity);
                break;
            }

            case R.id.botonDelete:{
                System.out.println("toque delete");
                break;
            }

            case R.id.botonAgregarClaves: {
                estoyAgregandoClave=true;
                textoMostrarPasswordDesbloqueoConfiguracion.setVisibility(View.VISIBLE);
                botonAgregarClaves.setPressed(true);
                break;
            }

            case R.id.botonBorrarLista:{
                state.getListaClaves().remove(numeroListaBorrar);
                botonBorrarLista.setVisibility(View.INVISIBLE);
                adaptador.clear();
                for(int i=0;i<state.getListaClaves().size();i++){
                    listaAdaptadaListView.add(new Persona(state.getListaClaves().get(i),  Integer.toString(i)));
                }
                adaptador.notifyDataSetChanged();
                break;
            }

        }
    }

    void setPoniendoClave(String numero) {
        if(numero=="00"){
            contadorNumerosAgregados++;
            contadorNumerosAgregados++;
        }else{
            contadorNumerosAgregados++;
        }

        claveArmandose+=numero;
        textoMostrarPasswordDesbloqueoConfiguracion.setText(claveArmandose);
        if(contadorNumerosAgregados==4){
            contadorNumerosAgregados=0;
            state.getListaClaves().add(claveArmandose);
            estoyAgregandoClave=false;
            listaAdaptadaListView.add(new Persona(claveArmandose,  Integer.toString(state.getListaClaves().size())));
            lv1.setAdapter(adaptador);
            claveArmandose="";
            textoMostrarPasswordDesbloqueoConfiguracion.setVisibility(View.INVISIBLE);
            textoMostrarPasswordDesbloqueoConfiguracion.setText("");

        }
    }

    void setPoniendoDesbloqueo(String numero){
        if(numero=="00"){
            contadorNumerosAgregados++;
            contadorNumerosAgregados++;
        }else{
            contadorNumerosAgregados++;
        }

        claveParaDesbloqueo+=numero;
        textoMostrarPasswordDesbloqueoConfiguracion.setText(claveParaDesbloqueo);
        if(contadorNumerosAgregados==4){
            textoMostrarPasswordDesbloqueoConfiguracion.setText("");
            contadorNumerosAgregados=0;
            for(int i=0;i<state.getListaClaves().size();i++){
                if(state.getListaClaves().get(i).equals(claveParaDesbloqueo)){
                    botonAgregarClaves.setVisibility(View.VISIBLE);
                    lv1.setVisibility(View.VISIBLE);
                    textoIngresePasswordDesbloqueo.setVisibility(View.INVISIBLE);
                    deboDesbloquear=false;
                }
            }
            claveParaDesbloqueo="";
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String data="";
        switch (i){
            case 0:
                System.out.println("3");
                data="tim1\r\n";
                //data="andr\r\n";
                break;
            case 1:
                System.out.println("5");
                data="tim2\r\n";
                //data="andr\r\n";
                break;
            case 2:
                System.out.println("8");
                data="tim3\r\n";
                //data="andr\r\n";
                break;
            case 3:
                System.out.println("10");
                data="tim4\r\n";
                //data="andr\r\n";
                break;
            case 4:
                System.out.println("15");
                data="tim5\r\n";
                //data="andr\r\n";
                break;
        }

        state.setTiempoYaSeteadoAlarma(i);
        byte[] buffer=data.getBytes();
        try {
            state.getSocket().getOutputStream().write(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        System.out.println("La nada misma");
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        System.out.println("seleccione: " + i);
        lv1.setItemChecked(i,true);
        view.setBackgroundColor(Color.WHITE);
        numeroListaBorrar=i;
        botonBorrarLista.setVisibility(View.VISIBLE);
    }


    class AdaptadorListView extends ArrayAdapter<Persona> {

        AppCompatActivity appCompatActivity;

        AdaptadorListView(AppCompatActivity context) {
            super(context, R.layout.persona, listaAdaptadaListView);
            appCompatActivity = context;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = appCompatActivity.getLayoutInflater();
            View item = inflater.inflate(R.layout.persona, null);

            TextView textView1 = (TextView)item.findViewById(R.id.textoClave);
            textView1.setText(listaAdaptadaListView.get(position).getNombre());
            textView1.setTextSize(20);
            //TextView textView2 = (TextView)item.findViewById(R.id.textoOrden);
            //textView2.setText(listaAdaptadaListView.get(position).getOrden());
            //textView2.setTextSize(20);
            return(item);
        }
    }

}
