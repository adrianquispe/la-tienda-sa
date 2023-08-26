package com.laTienda.carrito;

public class ItemEnVenta {
    private String idItemSale;
    private Integer quantitySale;
    private Float itemBasePrice;
    private Float itemDiscount;

    public ItemEnVenta(String idItemSale, Integer quantitySale) {
        this.idItemSale = idItemSale;
        this.quantitySale = quantitySale;
    }
    public String getIdItemSale() {
        return idItemSale;
    }
    public void setIdItemSale(String idItemSale) {
        this.idItemSale = idItemSale;
    }
    public Integer getQuantitySale() {
        return quantitySale;
    }
    public void setQuantitySale(Integer quantitySale) {
        this.quantitySale = quantitySale;
    }
    public Float getItemBasePrice() {
        return itemBasePrice;
    }
    public void setItemBasePrice(Float itemBasePrice) {
        this.itemBasePrice = itemBasePrice;
    }
    public Float getItemDiscount() {
        return itemDiscount;
    }
    public void setItemDiscount(Float itemDiscount) {
        this.itemDiscount = itemDiscount;
    }
    //warning, only use this method when those attributes have set values
    public Float getFinalPrice(){
        return this.itemBasePrice * (1+this.itemDiscount);
    }
}
