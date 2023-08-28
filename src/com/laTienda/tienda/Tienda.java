package com.laTienda.tienda;

import com.laTienda.carrito.ItemEnVenta;
import com.laTienda.producto.Limpieza;
import com.laTienda.producto.Producto;
import com.laTienda.utils.*;

import java.util.ArrayList;
import java.util.HashMap;

public class Tienda implements AgregarProductoTienda, VentaProductoTienda {
    public final Integer VENTAITEMMAX = 3;
    public final Integer UNIDADESXITEMMAX = 10;
    private String name;
    private Integer stockMax;
    private Double cashOfStore;
    private HashMap<String, ItemTienda> products;
    private Double cashOfSales;

    public Tienda(String name, Integer stockMax, Double cashOfStore) {
        this.name = name;
        this.stockMax = stockMax;
        this.cashOfStore = cashOfStore;
        this.products = new HashMap<>();
        this.cashOfSales = 0.0D;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Integer getStockMax() {
        return stockMax;
    }
    public void setStockMax(Integer stockMax) {
        this.stockMax = stockMax;
    }
    public Double getCashOfStore() {
        return cashOfStore;
    }
    public void setCashOfStore(Double cashOfStore) {
        this.cashOfStore = cashOfStore;
    }
    public HashMap<String, ItemTienda> getProducts() { return products; }
    public void addCash(Float moreCash){ cashOfStore += moreCash; }
    public void removeCash(Float useCash){ cashOfStore -= useCash; }
    public ItemTienda getItem(String idItem){ return products.get(idItem); }
    public Producto getProduct(String idProduct){ return this.getItem(idProduct).getStoreItem(); }
    public Double getCashOfSales(){ return cashOfSales; }
    public String getItemName(String id){ return this.getItem(id).getName(); }
    public void cashASale(Double money){ this.cashOfSales += money; }
    private void adquireMoreStockOf(String idProduct, Integer quantity){
        this.getItem(idProduct).addStockOfItem(quantity);
    }
    public void showItemsStore() {
        System.out.println("Hay "+products.size()+" productos en la tienda." );
        for(ItemTienda items: products.values()){
            System.out.println(items);
        }
    }
    public Float showUsedSpaceInPercent(){
        Double var = (this.totalStockQuantity().doubleValue() / stockMax) * 10000;
        Long var2 = Math.round(var);
        return var2.floatValue() / 100;
    }
    // ------- methods for purchase of stock ---------

    @Override
    public void buyItemWithLogic(ItemTienda item, boolean logger) {
        System.out.println(" --------- ");
        System.out.println("Iniciando compra automatica");
        boolean buyConcreted;
        if(haveCashForPurchase(item)){
            System.out.println(" - Hay saldo suficiente para la compra");
            if(haveEnoughSpace(item)){
                System.out.println(" - Hay espacio suficiente para la compra");
                this.addProduct(item , logger);
                System.out.println(" - Actualizando saldo de la tienda...");
                this.removeCash(item.itemStockPrice());
                buyConcreted = true;
            }
            else{
                buyConcreted = false;
                System.out.println(" - No hay espacio suficiente para la compra");
            }

        }
        else{
            buyConcreted = false;
            System.out.println(" - Saldo Insuficiente para la compra");
        }
        if(buyConcreted)
            System.out.println("Compra realizada con exito");
        else
            System.out.println("Compra fallada");
        System.out.println("Fin compra automatica");
        System.out.println(" --------- ");
    }
    @Override
    public void buyItemDirect(ItemTienda item, boolean logger) {
        this.addNewProduct(item);
        this.removeCash(item.itemStockPrice());
    }

    @Override
    public void addExistingItem(String idItem, Integer quantity) {
        this.getProduct(idItem).addQuantity(quantity);
    }

    @Override
    public Boolean haveCashForPurchase(ItemTienda item) {
        return this.getCashOfStore() >= item.itemStockPrice();
    }
    @Override
    public Boolean haveCashForPurchase(Float cost) { return this.getCashOfStore() >= cost; }
    public Boolean haveCashForPurchaseItem(Integer quantity, String item){
        return this.getCashOfStore() >= products.get(item).getItemPriceOfStock() * quantity;
    }
    @Override
    public Boolean haveEnoughSpace(ItemTienda item) {
        //System.out.println("//debug: espacio disponible: "+this.availableStockSpace());
        return item.getStoreItem().getQuantity() <= this.availableStockSpace();
    }
    @Override
    public Boolean haveEnoughSpace(Integer quantity) {
        return quantity <= this.availableStockSpace();
    }
    public Boolean itemIsInStore(String idItem) {
        return products.containsKey(idItem);
    }
    private Integer availableStockSpace(){
        return Integer.max((stockMax - this.totalStockQuantity()), 0);
    }
    private Integer totalStockQuantity(){
        final Integer[] totalQuantity = {0};
        this.getProducts().forEach((id, item) -> totalQuantity[0] += item.getQuantity());
        return totalQuantity[0];
    }
    private void addProduct(ItemTienda newProduct, boolean logger){
        String idOfProduct = newProduct.getIdProduct();
        Boolean itemInStore = this.itemIsInStore(idOfProduct);
        if(itemInStore){
            ItemTienda itemFound = this.products.get(idOfProduct);
            if(logger)System.out.println(" - El producto se encuentra en la tienda. Agregando stock...");
            itemFound.addStockOfItem(newProduct.getQuantity());
            itemFound.activateItem();
        }

        else{
            if(logger)System.out.println(" - El producto es nuevo. Agregando a la tienda...");
            this.addNewProduct(newProduct);
        }
    }
    private void addNewProduct(ItemTienda newProduct){
        this.products.put(newProduct.getIdProduct(), newProduct);
    }
    public void removeCashFromPurchase(String id, Integer quantity){
        removeCash(products.get(id).getItemPriceOfStock() * quantity);
    }
    // --------- methods for sale of items of store ---------

    @Override
    public void saleItemWithLogic(ArrayList<ItemEnVenta> itemSaleList) {
     //TODO
    }

    /**
     * unsafe, make a sale of a product.
     * logic should be applied beforehand
     * @param itemSale
     */
    @Override
    public void saleItemDirect(ItemEnVenta itemSale) {
        products.get(itemSale.getIdItemSale()).removeStockOfItem(itemSale.getQuantitySale());
        this.cashASale(itemSale.getTotalOfPurchase());
    }
    public void calculateCostOfSale(ItemEnVenta itemForSale, boolean logger){
        ItemTienda itemOfStore = this.getItem(itemForSale.getIdItemSale());
        Float marginOfEarning = itemOfStore.getMarginOfEarning();
        Float margOfEarnWithRestrictions = this.earningRestrictions(itemOfStore, marginOfEarning, logger);
        Float baseDiscounts = this.getDiscounts(itemOfStore);
        Float discWithRestrictions = this.discountRestrictions(itemOfStore, baseDiscounts, logger);
        Float marginAfterDiscount = itemOfStore.getMarginOfEarning(discWithRestrictions);
        if(marginAfterDiscount < 0){
            itemForSale.setItemDiscount(0.0F);
            if(logger)System.out.println("Lamentablemente el descuento a aplicar produce perdidas. No podra se aplicado.");
        } else {
            itemForSale.setItemDiscount(discWithRestrictions);
            if(logger)System.out.println("El descuento ("+itemForSale.getItemDiscount()*100+"%) puede ser aplicado!");
        }
        if(marginOfEarning.equals(margOfEarnWithRestrictions)){
            itemForSale.setItemBasePrice(itemOfStore.getItemPriceOfSale());
        } else {
            itemForSale.setItemBasePrice(itemOfStore.getItemPriceOfSale(margOfEarnWithRestrictions));
        }
        if (itemOfStore.itemImported()){
            if(logger)System.out.println("El producto es importado, el precio incrementa un 10%");
            itemForSale.setItemImported(itemOfStore.itemImported());
        }
    }
    public Float getDiscounts(ItemTienda item){ //si tiene descuentos retorna su total o si no tiene 0.0f
        Float discounts = 0.0F;
        if(item.itemConDescuento()){
            ItemTiendaConDescuento itemDisc = (ItemTiendaConDescuento) item;
            if(itemDisc.hasDiscounts()){
                discounts += itemDisc.totalDiscount();
            }
        }
        return discounts;
    }
    public Float earningRestrictions(ItemTienda itemStore, Float margin, boolean logger){
        Float marginWithRest = margin;
        if(itemStore.itemComestible()){
            if(logger)System.out.println("El item es un comestible, el margen de ganancia base max es 20%");
            marginWithRest = Math.max(margin, 0.2F);
        }
        if(itemStore.itemLimpieza()){
            Limpieza prodLimpieza = (Limpieza) itemStore.getStoreItem();
            UsoLimpieza useOfProdLimpieza = prodLimpieza.getUseType();
            if(logger)System.out.println("El item es de limpieza, el margen de ganancia base max es 25%");
            marginWithRest = Math.max(margin, 0.25F);
            if(!(useOfProdLimpieza == UsoLimpieza.ROPA || useOfProdLimpieza == UsoLimpieza.MULTIUSO)){
                if(logger)System.out.println("El item de limpieza no es ropa ni multiuso, el margen de ganancia base min es 10%");
                marginWithRest = Math.min(marginWithRest, 0.1F);
            }
        }
        return marginWithRest;
    }
    public Float discountRestrictions(ItemTienda itemStore, Float discount, boolean logger){
        Float discWithRest = discount;
        if(discount.equals(0.0F)){
            if(logger)System.out.println("No presenta descuento, no se calcula restricciones");
        } else{
            String idItem = itemStore.getIdProduct();
            GrupoProducto productGroup = GrupoDeProducto.typeOfProduct(idItem);
            switch(productGroup){
                case BEBIDA:
                    if(logger)System.out.println("El item es bebida, no puede recibir mas de 15% de descuento");
                    discWithRest = Math.min(discount, 0.15F);
                    break;
                case ENVASE:
                    if(logger)System.out.println("El item es envase, no puede recibir mas de 20% de descuento");
                    discWithRest = Math.min(discount, 0.2F);
                    break;
                case LIMPIEZA:
                    if(logger)System.out.println("El item es limpieza, no puede recibir mas de 25 de descuento");
                    discWithRest =  Math.min(discount, 0.25F);
                    break;
                case OTRO:
            }
            if(logger)System.out.println("Se calculo un descuento de "+discWithRest*100+"%.");
        }
        return discWithRest;
    }
    //public Float finalPriceItem()


    @Override
    public String toString() {
        return " ---------- "+
                " \nTienda: " + name +
                " \n - Tamaño de almacenamiento maximo=" + stockMax +
                " \n - Saldo de tienda: " + cashOfStore +
                " \n - Saldo de ventas: " + cashOfSales +
                " \n ---------- ";
    }
}
