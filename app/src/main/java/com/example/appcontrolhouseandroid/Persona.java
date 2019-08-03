package com.example.appcontrolhouseandroid;

public class Persona {
    private String nombre;
    private String orden;


    public Persona(String nombre,String orden) {
        this.nombre=nombre;
        this.orden=orden;
    }

    public String getNombre() {
        return nombre;
    }

    public String getOrden(){
        return orden;
    }

}