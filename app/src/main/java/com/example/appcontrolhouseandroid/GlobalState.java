package com.example.appcontrolhouseandroid;


import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.subjects.PublishSubject;

public class GlobalState extends Application {


    private BluetoothAdapter bAdapter;
    private static final int REQUEST_ENABLE_BT = 1;
    private BluetoothSocket socket;
    public BluetoothDevice dispositivoRemoto;
    private boolean primeraVez=true;





    private Observable<String> source;
    PublishSubject<String> watcher = PublishSubject.create();
    String palabraRecibida = "";

    //variables globales que guardan el contenido que deberemos utilizar en todo el ciclo de vida
    private ArrayList<String> listaClaves=new ArrayList<String>();
    private int tiempoYaSeteadoAlarma=0;
    private int metodoAlarma=0;
    private boolean alarmaActivaSensor=false;
    private boolean alarmaActivaMagnetico=false;
    private String sensorMovimiento1="";
    private String sensorMovimiento2="";
    private String sensorMagnetico1="";
    private String sensorMagnetico2="";
    private String sensorMagnetico3="";
    private boolean tiempoParaSonarActivo=false;

    private boolean lucesPrincipalesAutomatico=false;
    private boolean lucesPatioAutomatico=false;
    private boolean lucesPatioPrendido=false;
    private boolean lucesPrincipalesPrendido=false;

    private String horaPrincipalDeSemana="00";
    private String horaPrincipalASemana="00";
    private String horaPrincipalDeFinde="00";
    private String horaPrincipalAFinde="00";

    private String horaPatioDeSemana="00";
    private String horaPatioASemana="00";
    private String horaPatioDeFinde="00";
    private String horaPatioAFinde="00";

    private String minutoPrincipalDeSemana="00";
    private String minutoPrincipalASemana="00";
    private String minutoPrincipalDeFinde="00";
    private String minutoPrincipalAFinde="00";

    private String minutoPatioDeSemana="00";
    private String minutoPatioASemana="00";
    private String minutoPatioDeFinde="00";
    private String minutoPatioAFinde="00";


    //variables de control para luces
    private boolean lucesPrincipalesAppPrendido=false;
    private boolean lucesPatioAppPrendido=false;
    private boolean lucesPrincipalesCortexPrendido=false;
    private boolean lucesPatioCortexPrendido=false;


    //variables sonido
    public SoundPool sp;
    public int flujodemusicaActivandoAlarma=0;
    public int flujodemusicaTiempoParaDesactivar=0;
    public int flujodemusicaAlarmaSonando=0;
    public MediaPlayer sonidoActivandoAlarma;
    public MediaPlayer sonidoTiempoParaDesactivar;
    public MediaPlayer sonidoAlarmaSonando;

    private boolean sirenaSonando=false;

    public void constructor() {
        conectarCentral();
        crearHiloObservables();
        crearHiloEstoyConectado();
        crearHiloLuces();

        sp = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        //this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
        flujodemusicaActivandoAlarma= sp.load(this,R.raw.sonidoactivaralarma,1);
        flujodemusicaTiempoParaDesactivar= sp.load(this,R.raw.sonidotiempoparadesactivar,1);
        flujodemusicaAlarmaSonando= sp.load(this,R.raw.sonidoalarmasonando,1);
    }

    public boolean isPrimeraVez() {
        return primeraVez;
    }

    public void setPrimeraVez(boolean primeraVez) {
        this.primeraVez = primeraVez;
    }

    public int getTiempoYaSeteadoAlarma() { return tiempoYaSeteadoAlarma; }

    public void setTiempoYaSeteadoAlarma(int tiempoYaSeteadoAlarma) { this.tiempoYaSeteadoAlarma = tiempoYaSeteadoAlarma; }

    public BluetoothSocket getSocket() {
        return socket;
    }

    public int getMetodoAlarma() { return metodoAlarma; }

    public void setMetodoAlarma(int metodoAlarma) { this.metodoAlarma = metodoAlarma; }

    public boolean isAlarmaActivaSensor() { return alarmaActivaSensor; }

    public void setAlarmaActivaSensor(boolean alarmaActivaSensor) { this.alarmaActivaSensor = alarmaActivaSensor; }

    public boolean isAlarmaActivaMagnetico() { return alarmaActivaMagnetico; }

    public void setAlarmaActivaMagnetico(boolean alarmaActivaMagnetico) { this.alarmaActivaMagnetico = alarmaActivaMagnetico; }

    public String getSensorMovimiento1() { return sensorMovimiento1; }

    public void setSensorMovimiento1(String sensorMovimiento1) { this.sensorMovimiento1 = sensorMovimiento1; }

    public String getSensorMovimiento2() { return sensorMovimiento2; }

    public void setSensorMovimiento2(String sensorMovimiento2) { this.sensorMovimiento2 = sensorMovimiento2; }

    public String getSensorMagnetico1() { return sensorMagnetico1; }

    public void setSensorMagnetico1(String sensorMagnetico1) { this.sensorMagnetico1 = sensorMagnetico1; }

    public String getSensorMagnetico2() { return sensorMagnetico2; }

    public void setSensorMagnetico2(String sensorMagnetico2) { this.sensorMagnetico2 = sensorMagnetico2; }

    public String getSensorMagnetico3() { return sensorMagnetico3; }

    public void setSensorMagnetico3(String sensorMagnetico3) { this.sensorMagnetico3 = sensorMagnetico3; }

    public boolean isLucesPrincipalesAutomatico(){return lucesPrincipalesAutomatico; }

    public boolean isLucesPatioAutomatico(){return lucesPatioAutomatico; }

    public void setLucesPrincipalesAutomatico(boolean lucesPrincipalesAutomatico){this.lucesPrincipalesAutomatico=lucesPrincipalesAutomatico;}

    public void setLucesPatioAutomatico(boolean lucesPatioAutomatico){this.lucesPatioAutomatico=lucesPatioAutomatico;}

    public String getHoraPrincipalDeSemana() {
        return horaPrincipalDeSemana;
    }

    public String getHoraPrincipalASemana() {
        return horaPrincipalASemana;
    }

    public String getHoraPrincipalDeFinde() {
        return horaPrincipalDeFinde;
    }

    public String getHoraPrincipalAFinde() {
        return horaPrincipalAFinde;
    }

    public String getHoraPatioDeSemana() {
        return horaPatioDeSemana;
    }

    public String getHoraPatioASemana() {
        return horaPatioASemana;
    }

    public String getHoraPatioDeFinde() {
        return horaPatioDeFinde;
    }

    public String getHoraPatioAFinde() {
        return horaPatioAFinde;
    }

    public void setHoraPrincipalDeSemana(String horaPrincipalDeSemana) {
        this.horaPrincipalDeSemana = horaPrincipalDeSemana;
    }

    public void setHoraPrincipalASemana(String horaPrincipalASemana) {
        this.horaPrincipalASemana = horaPrincipalASemana;
    }

    public void setHoraPrincipalDeFinde(String horaPrincipalDeFinde) {
        this.horaPrincipalDeFinde = horaPrincipalDeFinde;
    }

    public void setHoraPrincipalAFinde(String horaPrincipalAFinde) {
        this.horaPrincipalAFinde = horaPrincipalAFinde;
    }

    public void setHoraPatioDeSemana(String horaPatioDeSemana) {
        this.horaPatioDeSemana = horaPatioDeSemana;
    }

    public void setHoraPatioASemana(String horaPatioASemana) {
        this.horaPatioASemana = horaPatioASemana;
    }

    public void setHoraPatioDeFinde(String horaPatioDeFinde) {
        this.horaPatioDeFinde = horaPatioDeFinde;
    }

    public void setHoraPatioAFinde(String horaPatioAFinde) {
        this.horaPatioAFinde = horaPatioAFinde;
    }

    public String getMinutoPrincipalDeSemana() {
        return minutoPrincipalDeSemana;
    }

    public String getMinutoPrincipalASemana() {
        return minutoPrincipalASemana;
    }

    public String getMinutoPrincipalDeFinde() {
        return minutoPrincipalDeFinde;
    }

    public String getMinutoPrincipalAFinde() {
        return minutoPrincipalAFinde;
    }

    public String getMinutoPatioDeSemana() {
        return minutoPatioDeSemana;
    }

    public String getMinutoPatioASemana() {
        return minutoPatioASemana;
    }

    public String getMinutoPatioDeFinde() {
        return minutoPatioDeFinde;
    }

    public String getMinutoPatioAFinde() {
        return minutoPatioAFinde;
    }

    public void setMinutoPrincipalDeSemana(String minutoPrincipalDeSemana) {
        this.minutoPrincipalDeSemana = minutoPrincipalDeSemana;
    }

    public void setMinutoPrincipalASemana(String minutoPrincipalASemana) {
        this.minutoPrincipalASemana = minutoPrincipalASemana;
    }

    public void setMinutoPrincipalDeFinde(String minutoPrincipalDeFinde) {
        this.minutoPrincipalDeFinde = minutoPrincipalDeFinde;
    }

    public void setMinutoPrincipalAFinde(String minutoPrincipalAFinde) {
        this.minutoPrincipalAFinde = minutoPrincipalAFinde;
    }

    public void setMinutoPatioDeSemana(String minutoPatioDeSemana) {
        this.minutoPatioDeSemana = minutoPatioDeSemana;
    }

    public void setMinutoPatioASemana(String minutoPatioASemana) {
        this.minutoPatioASemana = minutoPatioASemana;
    }

    public void setMinutoPatioDeFinde(String minutoPatioDeFinde) {
        this.minutoPatioDeFinde = minutoPatioDeFinde;
    }

    public void setMinutoPatioAFinde(String minutoPatioAFinde) {
        this.minutoPatioAFinde = minutoPatioAFinde;
    }


    public boolean isLucesPatioPrendido() {
        return lucesPatioPrendido;
    }

    public boolean isLucesPrincipalesPrendido() {
        return lucesPrincipalesPrendido;
    }



    public void setLucesPatioPrendido(boolean lucesPatioPrendido) {
        this.lucesPatioPrendido = lucesPatioPrendido;
    }

    public void setLucesPrincipalesPrendido(boolean lucesPrincipalesPrendido) {
        this.lucesPrincipalesPrendido = lucesPrincipalesPrendido;
    }


    public boolean isLucesPrincipalesAppPrendido() {
        return lucesPrincipalesAppPrendido;
    }

    public boolean isLucesPatioAppPrendido() {
        return lucesPatioAppPrendido;
    }

    public boolean isLucesPrincipalesCortexPrendido() {
        return lucesPrincipalesCortexPrendido;
    }

    public boolean isLucesPatioCortexPrendido() {
        return lucesPatioCortexPrendido;
    }

    public void setLucesPrincipalesAppPrendido(boolean lucesPrincipalesAppPrendido) {
        this.lucesPrincipalesAppPrendido = lucesPrincipalesAppPrendido;
    }

    public void setLucesPatioAppPrendido(boolean lucesPatioAppPrendido) {
        this.lucesPatioAppPrendido = lucesPatioAppPrendido;
    }

    public void setLucesPrincipalesCortexPrendido(boolean lucesPrincipalesCortexPrendido) {
        this.lucesPrincipalesCortexPrendido = lucesPrincipalesCortexPrendido;
    }

    public void setLucesPatioCortexPrendido(boolean lucesPatioCortexPrendido) {
        this.lucesPatioCortexPrendido = lucesPatioCortexPrendido;
    }


    public boolean isTiempoParaSonarActivo() {
        return tiempoParaSonarActivo;
    }

    public void setTiempoParaSonarActivo(boolean tiempoParaSonarActivo) {
        this.tiempoParaSonarActivo=tiempoParaSonarActivo;
    }

    public void setSirenaSonando(boolean sirenaSonando) {
        this.sirenaSonando = sirenaSonando;
    }

    public Observable<String> getSource() {

        return source;
    }

    public void setListaClaves(ArrayList<String> listaClaves){
        this.listaClaves=listaClaves;
    }

    public ArrayList<String> getListaClaves(){ return listaClaves; }

    private void crearObserver(char a) {
        source = Observable.create(emitter -> {

            if (a != 13) {
                palabraRecibida += a;
            } else {
                System.out.println("recibi: " + palabraRecibida);
                //TODO creo que deberia ver de adivinar las palabras porque algunas llegan con letras de mas
                if(palabraRecibida.contains("s1ac")){
                    emitter.onNext("s1ac");
                }

                if(palabraRecibida.contains("s1de")){
                    emitter.onNext("s1de");
                }

                if(palabraRecibida.contains("s2ac")){
                    emitter.onNext("s2ac");
                }

                if(palabraRecibida.contains("s2de")){
                    emitter.onNext("s2de");
                }

                if(palabraRecibida.contains("m1ac")){
                    emitter.onNext("m1ac");
                }

                if(palabraRecibida.contains("m1de")){
                    emitter.onNext("m1de");
                }

                if(palabraRecibida.contains("m2ac")){
                    emitter.onNext("m2ac");
                }

                if(palabraRecibida.contains("m2de")){
                    emitter.onNext("m2de");
                }

                if(palabraRecibida.contains("m3ac")){
                    emitter.onNext("m3ac");
                }

                if(palabraRecibida.contains("m3de")){
                    emitter.onNext("m3de");
                }

                if(palabraRecibida.contains("alac")){
                    emitter.onNext("alac");
                }

                if(palabraRecibida.contains("alde")){
                    //tiempoParaSonarActivo=false;
                    setTiempoParaSonarActivo(false);
                    stopSonido("alarmaSonando");
                    stopSonido("tiempoDesactivar");
                }

                if(palabraRecibida.contains("made")){
                    //tiempoParaSonarActivo=false;
                    setTiempoParaSonarActivo(false);
                    stopSonido("alarmaSonando");
                    stopSonido("tiempoDesactivar");
                }


                if(palabraRecibida.contains("maac")){
                    emitter.onNext("maac");
                }

                if(palabraRecibida.contains("l1pr")){
                    System.out.println("l1pr");
                    setLucesPrincipalesCortexPrendido(true);
                }

                if(palabraRecibida.contains("l1ap")){
                    System.out.println("l1ap");
                    setLucesPrincipalesCortexPrendido(false);
                }

                if(palabraRecibida.contains("l2pr")){
                    System.out.println("l2pr");
                    setLucesPatioCortexPrendido(true);
                }

                if(palabraRecibida.contains("l2ap")){
                    System.out.println("l2ap");
                    setLucesPatioCortexPrendido(false);
                }

                if(palabraRecibida.contains("sire")){
                    System.out.println("llego sire");
                    if(!sirenaSonando){
                        playSonido("alarmaSonando");
                        stopSonido("tiempoDesactivar");
                        sirenaSonando=true;
                    }

                }

                if(palabraRecibida.contains("acti")){
                    playSonido("activandoAlarma");
                }

                if(palabraRecibida.contains("tdes")){
                    //System.out.println("tiempo para desactivar "+ isTiempoParaSonarActivo() );
                    if(isTiempoParaSonarActivo()==false){
                        //System.out.println("tiempo para desactivar");
                        playSonido("tiempoDesactivar");
                        //tiempoParaSonarActivo=true;
                        setTiempoParaSonarActivo(true);
                    }

                }

                palabraRecibida = "";
            }
        });
        source.observeOn(AndroidSchedulers.mainThread()).subscribe(watcher);

    }

    public PublishSubject<String> getPublish() {
        return watcher;
    }

    private void conectarCentral() {
        bAdapter = BluetoothAdapter.getDefaultAdapter();
        dispositivoRemoto = bAdapter.getRemoteDevice("00:21:13:00:D0:06");
        try {
            socket = dispositivoRemoto.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
            socket.connect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void crearHiloObservables() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        while (socket.getInputStream().available() > 0) {
                            crearObserver((char) socket.getInputStream().read());
                        }
                    } catch (Exception e) {

                    }
                }

            }
        });
        thread.start();
    }

    //Hilo que va a ver si estoy conectado a BT o por algun motivo se desconecto. En dicho caso se vuelve a conectar
    private void crearHiloEstoyConectado() {
        String data = "okok\r\n";
        byte[] buffer = data.getBytes();
        Thread thread= new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){

                    try {
                        socket.getOutputStream().write(buffer);
                    } catch (IOException e) {
                        conectarCentral();
                        //TODO creo que aca voy a tener que mandar algun mensaje para que reinicie todo el observer !!
                        System.out.println("debo volver a conectarme");
                    }

                    try {
                        Thread.currentThread().sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
        thread.start();
    }

    private void crearHiloLuces() {

        Thread thread= new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    if(isLucesPrincipalesAutomatico()){
                        String horaMenor="00";
                        String minutoMenor="00";
                        String horaMayor="23";
                        String minutoMayor="59";
                        Calendar fecha= Calendar.getInstance();
                        System.out.println("dia de la semana " + fecha.get(Calendar.DAY_OF_WEEK));
                        System.out.println("hora " +fecha.get(Calendar.HOUR_OF_DAY));
                        System.out.println("minuto "+fecha.get(Calendar.MINUTE));
                        switch (fecha.get(Calendar.DAY_OF_WEEK)){

                            case 1:{
                                System.out.println("domingo");
                                String data="";
                                if(prenderPrincipalesAutomaticasSemana()) {
                                    setLucesPrincipalesAppPrendido(true);
                                    data="l1pr\r\n";
                                }else {
                                    setLucesPrincipalesAppPrendido(false);
                                    data="l1ap\r\n";
                                }

                                if(isLucesPrincipalesCortexPrendido() != isLucesPrincipalesAppPrendido()){

                                    byte[] buffer=data.getBytes();
                                    try {
                                        System.out.println("envie " + data);
                                        getSocket().getOutputStream().write(buffer);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }

                                break;
                            }

                            case 2:{
                                System.out.println("lunes");
                                String data="";
                                if(prenderPrincipalesAutomaticasSemana()) {
                                    setLucesPrincipalesAppPrendido(true);
                                    data="l1pr\r\n";
                                }else {
                                    setLucesPrincipalesAppPrendido(false);
                                    data="l1ap\r\n";
                                }

                                if(isLucesPrincipalesCortexPrendido() != isLucesPrincipalesAppPrendido()){

                                    byte[] buffer=data.getBytes();
                                    try {
                                        System.out.println("envie " + data);
                                        getSocket().getOutputStream().write(buffer);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                                break;
                            }

                            case 3:{
                                System.out.println("martes");
                                String data="";
                                if(prenderPrincipalesAutomaticasSemana()) {
                                    setLucesPrincipalesAppPrendido(true);
                                    data="l1pr\r\n";
                                }else {
                                    setLucesPrincipalesAppPrendido(false);
                                    data="l1ap\r\n";
                                }

                                if(isLucesPrincipalesCortexPrendido() != isLucesPrincipalesAppPrendido()){

                                    byte[] buffer=data.getBytes();
                                    try {
                                        System.out.println("envie " + data);
                                        getSocket().getOutputStream().write(buffer);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                                break;
                            }

                            case 4:{
                                String data="";
                                if(prenderPrincipalesAutomaticasSemana()) {
                                    setLucesPrincipalesAppPrendido(true);
                                    data="l1pr\r\n";
                                }else {
                                    setLucesPrincipalesAppPrendido(false);
                                    data="l1ap\r\n";
                                }

                                if(isLucesPrincipalesCortexPrendido() != isLucesPrincipalesAppPrendido()){

                                    byte[] buffer=data.getBytes();
                                    try {
                                        System.out.println("envie " + data);
                                        getSocket().getOutputStream().write(buffer);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                                break;
                            }

                            case 5:{
                                System.out.println("jueves");
                                String data="";
                                if(prenderPrincipalesAutomaticasSemana()) {
                                    setLucesPrincipalesAppPrendido(true);
                                    data="l1pr\r\n";
                                }else {
                                    setLucesPrincipalesAppPrendido(false);
                                    data="l1ap\r\n";
                                }

                                if(isLucesPrincipalesCortexPrendido() != isLucesPrincipalesAppPrendido()){

                                    byte[] buffer=data.getBytes();
                                    try {
                                        System.out.println("envie " + data);
                                        getSocket().getOutputStream().write(buffer);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                                break;
                            }

                            case 6:{
                                System.out.println("viernes");
                                String data="";
                                if(prenderPrincipalesAutomaticasFinde()) {
                                    setLucesPrincipalesAppPrendido(true);
                                    data="l1pr\r\n";
                                }else {
                                    setLucesPrincipalesAppPrendido(false);
                                    data="l1ap\r\n";
                                }

                                if(isLucesPrincipalesCortexPrendido() != isLucesPrincipalesAppPrendido()){

                                    byte[] buffer=data.getBytes();
                                    try {
                                        System.out.println("envie " + data);
                                        getSocket().getOutputStream().write(buffer);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                                break;
                            }

                            case 7:{
                                System.out.println("sabado");
                                String data="";
                                if(prenderPrincipalesAutomaticasFinde()) {
                                    setLucesPrincipalesAppPrendido(true);
                                    data="l1pr\r\n";
                                }else {
                                    setLucesPrincipalesAppPrendido(false);
                                    data="l1ap\r\n";
                                }

                                if(isLucesPrincipalesCortexPrendido() != isLucesPrincipalesAppPrendido()){

                                    byte[] buffer=data.getBytes();
                                    try {
                                        System.out.println("envie " + data);
                                        getSocket().getOutputStream().write(buffer);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                                break;
                            }
                        }
                    }

                    if(isLucesPatioAutomatico()){
                        String horaMenor="00";
                        String minutoMenor="00";
                        String horaMayor="23";
                        String minutoMayor="59";
                        Calendar fecha= Calendar.getInstance();
                        System.out.println("dia de la semana " + fecha.get(Calendar.DAY_OF_WEEK));
                        System.out.println("hora " +fecha.get(Calendar.HOUR_OF_DAY));
                        System.out.println("minuto "+fecha.get(Calendar.MINUTE));
                        switch (fecha.get(Calendar.DAY_OF_WEEK)){

                            case 1:{
                                System.out.println("domingo");
                                String data="";
                                if(prenderPatioAutomaticasSemana()) {
                                    setLucesPatioAppPrendido(true);
                                    data="l2pr\r\n";
                                }else {
                                    setLucesPatioAppPrendido(false);
                                    data="l2ap\r\n";
                                }

                                if(isLucesPatioCortexPrendido() != isLucesPatioAppPrendido()){

                                    byte[] buffer=data.getBytes();
                                    try {
                                        System.out.println("envie " + data);
                                        getSocket().getOutputStream().write(buffer);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }

                                break;
                            }

                            case 2:{
                                System.out.println("lunes");
                                String data="";
                                if(prenderPatioAutomaticasSemana()) {
                                    setLucesPatioAppPrendido(true);
                                    data="l2pr\r\n";
                                }else {
                                    setLucesPatioAppPrendido(false);
                                    data="l2ap\r\n";
                                }

                                if(isLucesPatioCortexPrendido() != isLucesPatioAppPrendido()){

                                    byte[] buffer=data.getBytes();
                                    try {
                                        System.out.println("envie " + data);
                                        getSocket().getOutputStream().write(buffer);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                                break;
                            }

                            case 3:{
                                System.out.println("martes");
                                String data="";
                                if(prenderPatioAutomaticasSemana()) {
                                    setLucesPatioAppPrendido(true);
                                    data="l2pr\r\n";
                                }else {
                                    setLucesPatioAppPrendido(false);
                                    data="l2ap\r\n";
                                }

                                if(isLucesPatioCortexPrendido() != isLucesPatioAppPrendido()){

                                    byte[] buffer=data.getBytes();
                                    try {
                                        System.out.println("envie " + data);
                                        getSocket().getOutputStream().write(buffer);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                                break;
                            }

                            case 4:{
                                String data="";
                                if(prenderPatioAutomaticasSemana()) {
                                    setLucesPatioAppPrendido(true);
                                    data="l2pr\r\n";
                                }else {
                                    setLucesPatioAppPrendido(false);
                                    data="l2ap\r\n";
                                }

                                if(isLucesPatioCortexPrendido() != isLucesPatioAppPrendido()){

                                    byte[] buffer=data.getBytes();
                                    try {
                                        System.out.println("envie " + data);
                                        getSocket().getOutputStream().write(buffer);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                                break;
                            }

                            case 5:{
                                System.out.println("jueves");
                                String data="";
                                if(prenderPatioAutomaticasSemana()) {
                                    setLucesPatioAppPrendido(true);
                                    data="l2pr\r\n";
                                }else {
                                    setLucesPatioAppPrendido(false);
                                    data="l2ap\r\n";
                                }

                                if(isLucesPatioCortexPrendido() != isLucesPatioAppPrendido()){

                                    byte[] buffer=data.getBytes();
                                    try {
                                        System.out.println("envie " + data);
                                        getSocket().getOutputStream().write(buffer);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                                break;
                            }

                            case 6:{
                                System.out.println("viernes");
                                String data="";
                                if(prenderPatioAutomaticasFinde()) {
                                    setLucesPatioAppPrendido(true);
                                    data="l2pr\r\n";
                                }else {
                                    setLucesPatioAppPrendido(false);
                                    data="l2ap\r\n";
                                }

                                if(isLucesPatioCortexPrendido() != isLucesPatioAppPrendido()){

                                    byte[] buffer=data.getBytes();
                                    try {
                                        System.out.println("envie " + data);
                                        getSocket().getOutputStream().write(buffer);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                                break;
                            }

                            case 7:{
                                System.out.println("sabado");
                                String data="";
                                if(prenderPatioAutomaticasFinde()) {
                                    setLucesPatioAppPrendido(true);
                                    data="l2pr\r\n";
                                }else {
                                    setLucesPatioAppPrendido(false);
                                    data="l2ap\r\n";
                                }

                                if(isLucesPatioCortexPrendido() != isLucesPatioAppPrendido()){

                                    byte[] buffer=data.getBytes();
                                    try {
                                        System.out.println("envie " + data);
                                        getSocket().getOutputStream().write(buffer);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                                break;
                            }
                        }

                    }
                    try {
                        Thread.currentThread().sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
        thread.start();

    }

    private boolean prenderPrincipalesAutomaticasSemana(){
        String horaMenor="00";
        String minutoMenor="00";
        String horaMayor="23";
        String minutoMayor="59";
        Calendar fecha= Calendar.getInstance();
        System.out.println("dia de la semana " + fecha.get(Calendar.DAY_OF_WEEK));
        System.out.println("hora " +fecha.get(Calendar.HOUR_OF_DAY));
        System.out.println("minuto "+fecha.get(Calendar.MINUTE));

        if(fecha.get(Calendar.HOUR_OF_DAY)>12 &&
                fecha.get(Calendar.HOUR_OF_DAY) > Integer.parseInt(horaPrincipalDeSemana) &&
                fecha.get(Calendar.HOUR_OF_DAY) < Integer.parseInt(horaMayor)

        ){

            System.out.println("prender luces automaticas 1");
            return true;
        }else if(fecha.get(Calendar.HOUR_OF_DAY) == Integer.parseInt(horaMayor) &&
                fecha.get(Calendar.MINUTE) >= Integer.parseInt(minutoMayor)

        ){

            System.out.println("prender luces automaticas 5");
            return true;
        }
        else if(fecha.get(Calendar.HOUR_OF_DAY)<12 &&
                fecha.get(Calendar.HOUR_OF_DAY) < Integer.parseInt(horaPrincipalASemana) &&
                fecha.get(Calendar.HOUR_OF_DAY) > Integer.parseInt(horaMenor) ){

            System.out.println("prender luces principales automaticas 2");
            return true;
        }else if(  (  fecha.get(Calendar.HOUR_OF_DAY) == Integer.parseInt(horaPrincipalDeSemana) &&
                (fecha.get(Calendar.HOUR_OF_DAY) != Integer.parseInt(horaPrincipalASemana)) )
                && fecha.get(Calendar.MINUTE) >= Integer.parseInt(minutoPrincipalDeSemana) ){

            System.out.println("prender luces principales automaticas 3 ");
            return true;
        }else if((fecha.get(Calendar.HOUR_OF_DAY) == Integer.parseInt(horaPrincipalASemana) &&
                (fecha.get(Calendar.HOUR_OF_DAY) != Integer.parseInt(horaPrincipalDeSemana)) )
                &&
                fecha.get(Calendar.MINUTE) < Integer.parseInt(minutoPrincipalASemana)) {

            System.out.println("prender luces principales automaticas 4 ");
            return true;
        }else if((fecha.get(Calendar.HOUR_OF_DAY) == Integer.parseInt(horaPrincipalASemana) &&
                (fecha.get(Calendar.HOUR_OF_DAY) == Integer.parseInt(horaPrincipalDeSemana)) )
                && ((fecha.get(Calendar.MINUTE) >= Integer.parseInt(minutoPrincipalDeSemana)) &&
                (fecha.get(Calendar.MINUTE)<Integer.parseInt(minutoPrincipalASemana)))){

            System.out.println("todo igual nero y prende las luces");
            return true;
        }
        else{

            System.out.println("apagar luces automaticas");
            return false;
        }
    }

    private boolean prenderPrincipalesAutomaticasFinde(){
        String horaMenor="00";
        String minutoMenor="00";
        String horaMayor="23";
        String minutoMayor="59";
        Calendar fecha= Calendar.getInstance();
        System.out.println("dia de la semana " + fecha.get(Calendar.DAY_OF_WEEK));
        System.out.println("hora " +fecha.get(Calendar.HOUR_OF_DAY));
        System.out.println("minuto "+fecha.get(Calendar.MINUTE));
        if(fecha.get(Calendar.HOUR_OF_DAY)>12 &&
                fecha.get(Calendar.HOUR_OF_DAY) > Integer.parseInt(horaPrincipalDeFinde) &&
                fecha.get(Calendar.HOUR_OF_DAY) < Integer.parseInt(horaMayor)

        ){
            System.out.println("prender luces automaticas 1");
            return true;
        }else if(fecha.get(Calendar.HOUR_OF_DAY) == Integer.parseInt(horaMayor) &&
                fecha.get(Calendar.MINUTE) >= Integer.parseInt(minutoMayor)

        ){
            System.out.println("prender luces automaticas 5");
            return true;
        }
        else if(fecha.get(Calendar.HOUR_OF_DAY)<12 &&
                fecha.get(Calendar.HOUR_OF_DAY) < Integer.parseInt(horaPrincipalAFinde) &&
                fecha.get(Calendar.HOUR_OF_DAY) > Integer.parseInt(horaMenor) ){
            System.out.println("prender luces principales automaticas 2");
            return true;
        }else if(  (  fecha.get(Calendar.HOUR_OF_DAY) == Integer.parseInt(horaPrincipalDeFinde) &&
                (fecha.get(Calendar.HOUR_OF_DAY) != Integer.parseInt(horaPrincipalAFinde)) )
                && fecha.get(Calendar.MINUTE) >= Integer.parseInt(minutoPrincipalDeFinde) ){
            System.out.println("prender luces principales automaticas 3 ");
            return true;
        }else if((fecha.get(Calendar.HOUR_OF_DAY) == Integer.parseInt(horaPrincipalAFinde) &&
                (fecha.get(Calendar.HOUR_OF_DAY) != Integer.parseInt(horaPrincipalDeFinde)) )
                &&
                fecha.get(Calendar.MINUTE) < Integer.parseInt(minutoPrincipalAFinde)) {
            System.out.println("prender luces principales automaticas 4 ");
            return true;
        }else if((fecha.get(Calendar.HOUR_OF_DAY) == Integer.parseInt(horaPrincipalAFinde) &&
                (fecha.get(Calendar.HOUR_OF_DAY) == Integer.parseInt(horaPrincipalDeFinde)) )
                && ((fecha.get(Calendar.MINUTE) >= Integer.parseInt(minutoPrincipalDeFinde)) &&
                (fecha.get(Calendar.MINUTE)<Integer.parseInt(minutoPrincipalAFinde)))){
            System.out.println("todo igual nero y prende las luces");
            return true;
        }
        else{
            System.out.println("apagar luces automaticas");
            return false;
        }
    }

    private boolean prenderPatioAutomaticasFinde(){
        String horaMenor="00";
        String minutoMenor="00";
        String horaMayor="23";
        String minutoMayor="59";
        Calendar fecha= Calendar.getInstance();
        System.out.println("dia de la semana " + fecha.get(Calendar.DAY_OF_WEEK));
        System.out.println("hora " +fecha.get(Calendar.HOUR_OF_DAY));
        System.out.println("minuto "+fecha.get(Calendar.MINUTE));
        if(fecha.get(Calendar.HOUR_OF_DAY)>12 &&
                fecha.get(Calendar.HOUR_OF_DAY) > Integer.parseInt(horaPatioDeFinde) &&
                fecha.get(Calendar.HOUR_OF_DAY) < Integer.parseInt(horaMayor)

        ){
            System.out.println("prender luces automaticas 1");
            return true;
        }else if(fecha.get(Calendar.HOUR_OF_DAY) == Integer.parseInt(horaMayor) &&
                fecha.get(Calendar.MINUTE) >= Integer.parseInt(minutoMayor)

        ){
            System.out.println("prender luces automaticas 5");
            return true;
        }
        else if(fecha.get(Calendar.HOUR_OF_DAY)<12 &&
                fecha.get(Calendar.HOUR_OF_DAY) < Integer.parseInt(horaPatioAFinde) &&
                fecha.get(Calendar.HOUR_OF_DAY) > Integer.parseInt(horaMenor) ){
            System.out.println("prender luces principales automaticas 2");
            return true;
        }else if(  (  fecha.get(Calendar.HOUR_OF_DAY) == Integer.parseInt(horaPatioDeFinde) &&
                (fecha.get(Calendar.HOUR_OF_DAY) != Integer.parseInt(horaPatioAFinde)) )
                && fecha.get(Calendar.MINUTE) >= Integer.parseInt(minutoPatioDeFinde) ){
            System.out.println("prender luces principales automaticas 3 ");
            return true;
        }else if((fecha.get(Calendar.HOUR_OF_DAY) == Integer.parseInt(horaPatioAFinde) &&
                (fecha.get(Calendar.HOUR_OF_DAY) != Integer.parseInt(horaPatioDeFinde)) )
                &&
                fecha.get(Calendar.MINUTE) < Integer.parseInt(minutoPatioAFinde)) {
            System.out.println("prender luces principales automaticas 4 ");
            return true;
        }else if((fecha.get(Calendar.HOUR_OF_DAY) == Integer.parseInt(horaPatioAFinde) &&
                (fecha.get(Calendar.HOUR_OF_DAY) == Integer.parseInt(horaPatioDeFinde)) )
                && ((fecha.get(Calendar.MINUTE) >= Integer.parseInt(minutoPatioDeFinde)) &&
                (fecha.get(Calendar.MINUTE)<Integer.parseInt(minutoPatioAFinde)))){
            System.out.println("todo igual nero y prende las luces");
            return true;
        }
        else{
            System.out.println("apagar luces automaticas");
            return false;
        }
    }

    private boolean prenderPatioAutomaticasSemana(){
        String horaMenor="00";
        String minutoMenor="00";
        String horaMayor="23";
        String minutoMayor="59";
        Calendar fecha= Calendar.getInstance();
        System.out.println("dia de la semana " + fecha.get(Calendar.DAY_OF_WEEK));
        System.out.println("hora " +fecha.get(Calendar.HOUR_OF_DAY));
        System.out.println("minuto "+fecha.get(Calendar.MINUTE));

        if(fecha.get(Calendar.HOUR_OF_DAY)>12 &&
                fecha.get(Calendar.HOUR_OF_DAY) > Integer.parseInt(horaPatioDeSemana) &&
                fecha.get(Calendar.HOUR_OF_DAY) < Integer.parseInt(horaMayor)

        ){

            System.out.println("prender luces automaticas 1");
            return true;
        }else if(fecha.get(Calendar.HOUR_OF_DAY) == Integer.parseInt(horaMayor) &&
                fecha.get(Calendar.MINUTE) >= Integer.parseInt(minutoMayor)

        ){

            System.out.println("prender luces automaticas 5");
            return true;
        }
        else if(fecha.get(Calendar.HOUR_OF_DAY)<12 &&
                fecha.get(Calendar.HOUR_OF_DAY) < Integer.parseInt(horaPatioASemana) &&
                fecha.get(Calendar.HOUR_OF_DAY) > Integer.parseInt(horaMenor) ){

            System.out.println("prender luces principales automaticas 2");
            return true;
        }else if(  (  fecha.get(Calendar.HOUR_OF_DAY) == Integer.parseInt(horaPatioDeSemana) &&
                (fecha.get(Calendar.HOUR_OF_DAY) != Integer.parseInt(horaPatioASemana)) )
                && fecha.get(Calendar.MINUTE) >= Integer.parseInt(minutoPatioDeSemana) ){

            System.out.println("prender luces principales automaticas 3 ");
            return true;
        }else if((fecha.get(Calendar.HOUR_OF_DAY) == Integer.parseInt(horaPatioASemana) &&
                (fecha.get(Calendar.HOUR_OF_DAY) != Integer.parseInt(horaPatioDeSemana)) )
                &&
                fecha.get(Calendar.MINUTE) < Integer.parseInt(minutoPatioASemana)) {

            System.out.println("prender luces principales automaticas 4 ");
            return true;
        }else if((fecha.get(Calendar.HOUR_OF_DAY) == Integer.parseInt(horaPatioASemana) &&
                (fecha.get(Calendar.HOUR_OF_DAY) == Integer.parseInt(horaPatioDeSemana)) )
                && ((fecha.get(Calendar.MINUTE) >= Integer.parseInt(minutoPatioDeSemana)) &&
                (fecha.get(Calendar.MINUTE)<Integer.parseInt(minutoPatioASemana)))){

            System.out.println("todo igual nero y prende las luces");
            return true;
        }
        else{

            System.out.println("apagar luces automaticas");
            return false;
        }
    }

    public void playSonido(String sonido){
        sonidoActivandoAlarma= MediaPlayer.create(this,R.raw.sonidoactivaralarma);
        sonidoAlarmaSonando= MediaPlayer.create(this,R.raw.sonidoalarmasonando);
        sonidoTiempoParaDesactivar= MediaPlayer.create(this,R.raw.sonidotiempoparadesactivar);

        switch (sonido){
            case "activandoAlarma":{
                sonidoActivandoAlarma.start();
                break;
            }

            case "alarmaSonando":{
                sonidoAlarmaSonando.start();
                break;
            }

            case "tiempoDesactivar":{
                System.out.println(sonidoTiempoParaDesactivar.isPlaying());
                if(!sonidoTiempoParaDesactivar.isPlaying()){
                    sonidoTiempoParaDesactivar.start();
                }

                break;
            }
        }

    }

    public void stopSonido(String sonido){
        switch (sonido){
            case "activandoAlarma":{
                sonidoActivandoAlarma.stop();
                break;
            }

            case "alarmaSonando":{
                sonidoAlarmaSonando.stop();
                break;
            }

            case "tiempoDesactivar":{
                sonidoTiempoParaDesactivar.stop();
                break;
            }
        }
    }


}
