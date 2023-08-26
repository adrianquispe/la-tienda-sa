package com.laTienda.producto;

public class Bebida extends Producto {
    private Boolean isAlcoholic;
    private Float alcoholGrade;
    private Boolean isImported;

    public Bebida(String id, String description, Integer quantity, Float customerPrice, Float stockPrice, Boolean isAlcoholic, Float alcoholGrade, Boolean isImported) {
        super(id, description, quantity, customerPrice, stockPrice);
        this.isAlcoholic = isAlcoholic;
        this.alcoholGrade = alcoholGrade;
        this.isImported = isImported;
    }

    public Boolean getAlcoholic() {
        return isAlcoholic;
    }

    public void setAlcoholic(Boolean alcoholic) {
        isAlcoholic = alcoholic;
    }

    public Float getAlcoholGrade() {
        return alcoholGrade;
    }

    public void setAlcoholGrade(Float alcoholGrade) {
        this.alcoholGrade = alcoholGrade;
    }

    public Boolean getImported() {
        return isImported;
    }

    public void setImported(Boolean imported) {
        isImported = imported;
    }

}
