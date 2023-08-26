package com.laTienda.tienda;

import com.laTienda.producto.Producto;
import com.laTienda.descuentos.ItemTieneDescuento;
import com.laTienda.descuentos.TipoDescuentoItem;

import java.util.HashSet;


public class ItemTiendaConDescuento extends ItemTienda implements ItemTieneDescuento {
    HashSet<TipoDescuentoItem> discountList;
    public ItemTiendaConDescuento(Producto storeItem) {
        super(storeItem);
        this.discountList = new HashSet<>();
    }


    @Override
    public Float totalDiscount() {
        Float discountAcum = 0.0F;
        for(TipoDescuentoItem discountType : discountList){
            discountAcum += ItemTieneDescuento.fixedDiscount(discountType);
        }
        return discountAcum;
    }

    @Override
    public Float priceWithDisc() {
        return this.getStoreItem().getCustomerPrice() * (1 - this.totalDiscount());
    }

    @Override
    public void addDiscount(TipoDescuentoItem discount) {
        this.discountList.add(discount);
    }

    @Override
    public void removeDiscount(TipoDescuentoItem discount) {
        this.discountList.remove(discount);
    }

    @Override
    public Boolean hasDiscount(TipoDescuentoItem discount) {
        return this.discountList.contains(discount);
    }

    @Override
    public Boolean hasDiscounts() {
        return !this.discountList.isEmpty();
    }

    @Override
    public Float getItemPriceOfSale() {
        return this.priceWithDisc();
    }
    //devuelve el margen con el descuento ya aplicado
    @Override
    public Float getMarginOfEarning() {
        float a = this.getItemPriceOfSale() / getItemPriceOfStock() * 100;
        Integer b = Math.round(a);
        return (b.floatValue() / 100) -1;
    }
}
