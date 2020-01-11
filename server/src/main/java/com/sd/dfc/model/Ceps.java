package com.sd.dfc.model;

//classe armazerá o conjunto de ceps que uma transportadora abrange, de cepinicio até cepfim.
public class Ceps {

    public Ceps(){}

    public Ceps(long id, long cepInicio, long cepFim){
        this.id = id;
        this.cepInicio = cepInicio;
        this.cepFim = cepFim;
    }

    private long id;
    private long cepInicio;
    private long cepFim;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCepInicio() {
        return cepInicio;
    }

    public void setCepInicio(long cepInicio) {
        this.cepInicio = cepInicio;
    }

    public long getCepFim() {
        return cepFim;
    }

    public void setCepFim(long cepFim) {
        this.cepFim = cepFim;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
