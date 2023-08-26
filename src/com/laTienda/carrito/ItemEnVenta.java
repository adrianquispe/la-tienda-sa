package com.laTienda.carrito;

public class ItemEnVenta {
    private String idItemSale;
    private Integer quantitySale;
    private Float itemBasePrice;
    private Float itemDiscount;
    private Boolean itemImported;

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
    public Boolean getItemImported() { return itemImported; }
    public void setItemImported(Boolean itemImported) { this.itemImported = itemImported; }

    //warning, only use these methods when those attributes have set values
    public Float getPricePerUnit(){
        if (this.itemImported)
            return itemBasePrice*(1F-itemDiscount);
        else
            return itemBasePrice*(1.1F-itemDiscount);
    }
    public Double getTotalOfPurchase(){ return this.getPricePerUnit().doubleValue() * quantitySale; }
}
