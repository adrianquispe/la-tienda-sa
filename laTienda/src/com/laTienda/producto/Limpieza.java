package com.laTienda.producto;

import com.laTienda.utils.UsoLimpieza;

public class Limpieza extends Producto {
    private UsoLimpieza useType;

    public Limpieza(String id, String description, Integer quantity, Float customerPrice, Float stockPrice, UsoLimpieza useType) {
        super(id, description, quantity, customerPrice, stockPrice);
        this.useType = useType;
    }

    public UsoLimpieza getUseType() {
        return useType;
    }

    public void setUseType(UsoLimpieza useType) {
        this.useType = useType;
    }
}
