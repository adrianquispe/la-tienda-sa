package com.laTienda.producto;

import com.laTienda.utils.Envase;

public class Envasado extends Producto {
    private Envase bottleType;
    private Boolean isImported;

    public Envasado(String id, String description, Integer quantity, Float customerPrice, Float stockPrice, Envase bottleType, Boolean isImported) {
        super(id, description, quantity, customerPrice, stockPrice);
        this.bottleType = bottleType;
        this.isImported = isImported;
    }

    public Envase getBottleType() {
        return bottleType;
    }

    public void setBottleType(Envase bottleType) {
        this.bottleType = bottleType;
    }

    public Boolean getImported() {
        return isImported;
    }

    public void setImported(Boolean imported) {
        isImported = imported;
    }
}
