package com.laTienda.tienda;

import com.laTienda.producto.Producto;
import com.laTienda.utils.Comestible;

import java.time.LocalDate;

public class ItemTiendaComestibleConDescuento extends ItemTiendaConDescuento implements Comestible {
    private Integer calories;
    private LocalDate expDate;
    public ItemTiendaComestibleConDescuento(Producto storeItem) {
        super(storeItem);
    }
    @Override
    public void setExpirationDate(int dia, int mes, int anio) {
        this.expDate = LocalDate.of(anio, mes, dia);
    }

    public Integer getCalories() {
        return calories;
    }

    public LocalDate getExpDate() {
        return expDate;
    }

    @Override
    public LocalDate getExpirationDate() {
        return getExpDate();
    }

    @Override
    public void setCaloriesOfItem(Integer calories) {
        this.calories = calories;
    }

    @Override
    public Integer getCaloriesOfItem() {
        return getCalories();
    }
}