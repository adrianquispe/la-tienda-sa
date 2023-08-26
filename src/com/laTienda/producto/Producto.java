package com.laTienda.producto;

import com.laTienda.utils.GrupoDeProducto;
import com.laTienda.utils.GrupoProducto;

public abstract class Producto {
    private String id;
    private String description;
    private Integer quantity;
    private Float customerPrice;
    private Float stockPrice;
    private Boolean isForSale;

    public Producto(String id, String description, Integer quantity, Float customerPrice, Float stockPrice) {
        this.id = id;
        this.description = description;
        this.quantity = quantity;
        this.customerPrice = customerPrice;
        this.stockPrice = stockPrice;
        this.isForSale = true;
    }

    public Boolean isBebida(){ return this instanceof Bebida; }
    public static Boolean isBebida(String idProd){ return GrupoDeProducto.typeOfProduct(idProd) == GrupoProducto.BEBIDA; }
    public Boolean isEnvasado(){ return this instanceof Envasado; }
    public static Boolean isEnvasado(String idProd){ return GrupoDeProducto.typeOfProduct(idProd) == GrupoProducto.ENVASE; }
    public Boolean isLimpieza(){ return this instanceof Limpieza; }
    public static Boolean isLimpieza(String idProd){ return GrupoDeProducto.typeOfProduct(idProd) == GrupoProducto.LIMPIEZA; }
    public static Boolean isOtro(String idProd){ return GrupoDeProducto.typeOfProduct(idProd) == GrupoProducto.OTRO; }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public Integer getQuantity() {
        return quantity;
    }
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
    public Float getCustomerPrice() {
        return customerPrice;
    }
    public void setCustomerPrice(Float customerPrice) {
        this.customerPrice = customerPrice;
    }
    public Float getStockPrice() {
        return stockPrice;
    }
    public void setStockPrice(Float stockPrice) {
        this.stockPrice = stockPrice;
    }
    public Boolean getForSale() {
        return isForSale;
    }
    public void setForSale(Boolean forSale) {
        isForSale = forSale;
    }
    public void outOfStock(){ this.isForSale = false; }
    public void activateProduct() { this.isForSale = true; }
    public void addQuantity(Integer quantityPlus){
        quantity += quantityPlus;
    }
    public void removeQuantity(Integer quantityMinus){
        quantity -= quantityMinus;
    }
    //attention: don't mistake this method with getCustomerPrice
    public Float productStockCost(){ return quantity*stockPrice; }

    @Override
    public String toString() {
        return '[' +
                "Id Producto='" + id + '\'' +
                ", Descripcion='" + description + '\'' +
                ", Cantidad=" + quantity +
                ", Precio al publico=" + customerPrice +
                ", Precio de compra=" + stockPrice +
                ']';
    }
}
