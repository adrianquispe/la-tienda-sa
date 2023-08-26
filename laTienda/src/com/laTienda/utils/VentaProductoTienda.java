package com.laTienda.utils;

import com.laTienda.carrito.ItemEnVenta;

import java.util.ArrayList;

public interface VentaProductoTienda {
    /**
     * sale with logic applied internally
     * use with orders of sale or similar
     * currently: TODO
     * @param itemSale
     */
    void saleItemWithLogic(ArrayList<ItemEnVenta> itemSaleList);

    /**
     * unsafe method, must be used
     * after applying some logic.
     * use when there is currently
     * a sale in progress
     * @param itemSale
     */
    void saleItemDirect(ItemEnVenta itemSale);
}
