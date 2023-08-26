package com.laTienda.utils;


import com.laTienda.producto.Producto;

public interface GrupoDeProducto {
    static GrupoProducto typeOfProduct(String idProduct){
        switch (idProduct.substring(0,2)){
            case "AB":
                return GrupoProducto.ENVASE;
            case "AC":
                return GrupoProducto.BEBIDA;
            case "AZ":
                return GrupoProducto.LIMPIEZA;
            default:
                return GrupoProducto.OTRO;
        }
    }
}
