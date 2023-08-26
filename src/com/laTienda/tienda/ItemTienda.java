package com.laTienda.tienda;

import com.laTienda.producto.Producto;

public class ItemTienda {
    private Producto storeItem;

    public ItemTienda(Producto storeItem) {
        this.storeItem = storeItem;
    }

    public Producto getStoreItem() {
        return storeItem;
    }

    public void setStoreItem(Producto storeItem) {
        this.storeItem = storeItem;
    }
    public Float priceForSale(){
        return storeItem.getCustomerPrice();
    }
    public String getIdProduct(){
        return storeItem.getId();
    }
    public Float itemStockPrice(){ return this.storeItem.productStockCost(); }
    public Boolean itemIsForSale(){ return storeItem.getForSale(); }
    public void itemOutOfStock(){ storeItem.outOfStock(); }
    public void addStockOfItem(Integer quantity){ storeItem.addQuantity(quantity); }
    public void removeStockOfItem(Integer quantity){ storeItem.removeQuantity(quantity); }
    public Integer getQuantity(){ return storeItem.getQuantity(); }
    public Float getItemPriceOfSale(){ return storeItem.getCustomerPrice(); }
    public Float getItemPriceOfStock(){ return storeItem.getStockPrice(); }
    public Float getMarginOfEarning(){
        float a = getItemPriceOfSale() / getItemPriceOfStock() * 100;
        Integer b = Math.round(a);
        return (b.floatValue() / 100) - 1;
    }
    //devuelve el margen aplicando descuento por parametro
    public Float getMarginOfEarning(Float discount){
        float a = (getItemPriceOfSale() * (1-discount)) / getItemPriceOfStock() * 100;
        Integer b = Math.round(a);
        return (b.floatValue() / 100) - 1;
    }
    public Boolean itemConDescuento(){ return this instanceof ItemTiendaConDescuento; }
    public Boolean itemComestible(){ return this instanceof ItemTiendaComestible; }
    public Boolean itemComestConDesc(){ return this.itemComestible() && this.itemConDescuento();}
    public Boolean itemBebida(){ return storeItem.isBebida(); }
    public Boolean itemEnvasado(){ return storeItem.isEnvasado(); }
    public Boolean itemLimpieza(){ return storeItem.isLimpieza(); }
}
