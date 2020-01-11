package com.sd.dfc.model;

public class Transportadora {

    private long id;
    private String nome;
    private Ceps abrangencia;
    private double peso;

    public Transportadora(){}

    public Transportadora(long id, String nome, Ceps abrangencia, double peso){
        this.id = id;
        this.nome = nome;
        this.abrangencia = abrangencia;
        this.peso = peso;
    }

    public long getId(){
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNome(){
        return this.nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Ceps getAbrangencia(){
        return this.abrangencia;
    }

    public void setAbrangencia(Ceps abrangencia) {
        this.abrangencia = abrangencia;
    }

    public double getPeso(){
        return this.peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
