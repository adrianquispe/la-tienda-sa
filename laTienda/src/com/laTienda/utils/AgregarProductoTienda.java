package com.laTienda.utils;


import com.laTienda.tienda.ItemTienda;

public interface AgregarProductoTienda{
    /**
     * it uses/applies logic of purchase.
     * normally used by buying orders
     * @param item
     */
    void buyItemWithLogic(ItemTienda item);

    /**
     * unsafe, buy an item directly
     * logic of purchase should be applied beforehand
     * @param item
     */
    void buyItemDirect(ItemTienda item);
    void addExistingItem(String idItem, Integer quantity);
    Boolean haveCashForPurchase(ItemTienda item);
    Boolean haveCashForPurchase(Float cost);
    Boolean haveEnoughSpace(ItemTienda item);
    Boolean haveEnoughSpace(Integer quantity);

}
