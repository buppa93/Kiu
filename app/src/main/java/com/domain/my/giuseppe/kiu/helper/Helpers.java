//package universit.ivasco92.kiu;
package com.domain.my.giuseppe.kiu.helper;

/**
 * Created by ivasco92 on 20/06/16.
 */
public class Helpers {
    private String name;
    private String surname;
    private String where;
    private String date;
    private String ora;
    private Double tariffa;
    private float feedback;

    public Helpers (String name, String surname, String where, String date, String ora, Double tariffa, float feedback){
        this.name=name;
        this.surname=surname;
        this.where=where;
        this.date=date;
        this.ora=ora;
        this.tariffa=tariffa;
        this.feedback=feedback;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWhere() {
        return where;
    }

    public void setWhere(String where) {
        this.where = where;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getOra() {
        return ora;
    }

    public void setOra(String ora) {
        this.ora = ora;
    }

    public Double getTariffa() {
        return tariffa;
    }

    public void setTariffa(Double tariffa) {
        this.tariffa = tariffa;
    }

    public float getFeedback() {
        return feedback;
    }

    public void setFeedback(float feedback) {
        this.feedback = feedback;
    }
}
