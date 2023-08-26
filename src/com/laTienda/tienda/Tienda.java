package com.laTienda.tienda;

import com.laTienda.carrito.ItemEnVenta;
import com.laTienda.producto.Limpieza;
import com.laTienda.producto.Producto;
import com.laTienda.utils.*;

import java.util.ArrayList;
import java.util.HashMap;

public class Tienda implements AgregarProductoTienda, VentaProductoTienda {
    private static final Integer VENTAITEMMAX = 3;
    private static final Integer UNIDADESMAX = 10;
    private String name;
    private Integer stockMax;
    private Double cashOfStore;
    private HashMap<String, ItemTienda> products;
    private Float cashOfSales;

    public Tienda(String name, Integer stockMax, Double cashOfStore) {
        this.name = name;
        this.stockMax = stockMax;
        this.cashOfStore = cashOfStore;
        this.products = new HashMap<>();
        this.cashOfSales = 0.0F;
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
    public Float getCashOfSales() { return cashOfSales; }
    public void cashASale(Float money){ this.cashOfSales += money; }
    private void adquireMoreStockOf(String idProduct, Integer quantity){
        this.getItem(idProduct).addStockOfItem(quantity);
    }
    public void showItemsStore() {
        System.out.println("Hay "+products.size()+" productos en la tienda." );
        for(ItemTienda items: products.values()){
            System.out.println(items);
        }
    }
    // ------- methods for purchase of stock ---------

    @Override
    public void buyItemWithLogic(ItemTienda item) {
        System.out.println(" --------- ");
        System.out.println("Iniciando compra automatica");
        boolean buyConcreted;
        if(haveCashForPurchase(item)){
            System.out.println(" - Hay saldo suficiente para la compra");
            if(haveEnoughSpace(item)){
                System.out.println(" - Hay espacio suficiente para la compra");
                this.addProduct(item);
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
    public void buyItemDirect(ItemTienda item) {
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
    private void addProduct(ItemTienda newProduct){
        String idOfProduct = newProduct.getIdProduct();
        Boolean itemInStore = this.itemIsInStore(idOfProduct);
        if(itemInStore){
            ItemTienda itemFound = this.products.get(idOfProduct);
            System.out.println(" - El producto se encuentra en la tienda. Agregando stock...");
            itemFound.addStockOfItem(newProduct.getQuantity());
            itemFound.activateItem();
        }

        else{
            System.out.println(" - El producto es nuevo. Agregando a la tienda...");
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
    @Override
    public void saleItemDirect(ItemEnVenta itemSale) {
        products.get(itemSale.getIdItemSale()).removeStockOfItem(itemSale.getQuantitySale());

    }
    public void calculateCostOfSale(ItemEnVenta itemForSale){
        ItemTienda itemOfStore = this.getItem(itemForSale.getIdItemSale());
        Float marginOfEarning = itemOfStore.getMarginOfEarning();
        Float margOfEarnWithRestrictions = this.earningRestrictions(itemOfStore, marginOfEarning);
        Float baseDiscounts = this.getDiscounts(itemOfStore);
        Float discWithRestrictions = this.discountRestrictions(itemOfStore, baseDiscounts);
        Float marginAfterDiscount = itemOfStore.getMarginOfEarning(discWithRestrictions);
        Float subTotal = itemOfStore.getItemPriceOfSale() * itemForSale.getQuantitySale();

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
    public Float earningRestrictions(ItemTienda itemStore, Float margin){
        Float marginWithRest = margin;
        if(itemStore.itemComestible()){
            System.out.println("El item es un comestible, el margen de ganancia max es 20%");
            marginWithRest = Math.max(margin, 0.2F);
        }
        if(itemStore.itemLimpieza()){
            Limpieza prodLimpieza = (Limpieza) itemStore.getStoreItem();
            UsoLimpieza useOfProdLimpieza = prodLimpieza.getUseType();
            System.out.println("El item es de limpieza, el margen de ganancia max es 25%");
            marginWithRest = Math.max(margin, 0.25F);
            if(!(useOfProdLimpieza == UsoLimpieza.ROPA || useOfProdLimpieza == UsoLimpieza.MULTIUSO)){
                System.out.println("El item de limpieza no es ropa ni multiuso, el margen de ganancia min es 10%");
                marginWithRest = Math.min(marginWithRest, 0.1F);
            }
        }
        return marginWithRest;
    }
    public Float discountRestrictions(ItemTienda itemStore, Float discount){
        Float discWithRest = discount;
        if(discount.equals(0.0F)){
            System.out.println("No presenta descuento, no se calcula restricciones");
        } else{
            String idItem = itemStore.getIdProduct();
            GrupoProducto productGroup = GrupoDeProducto.typeOfProduct(idItem);
            switch(productGroup){
                case BEBIDA:
                    System.out.println("El item es bebida, no puede recibir mas de 15% de descuento");
                    discWithRest = Math.min(discount, 0.15F);
                    break;
                case ENVASE:
                    System.out.println("El item es envase, no puede recibir mas de 20% de descuento");
                    discWithRest = Math.min(discount, 0.2F);
                    break;
                case LIMPIEZA:
                    System.out.println("El item es limpieza, no puede recibir mas de 25 de descuento");
                    discWithRest =  Math.min(discount, 0.25F);
                    break;
                case OTRO:
            }
            System.out.println("Se calculo un descuento de "+discWithRest*100+"%.");
        }
        return discWithRest;
    }
    //public Float finalPriceItem()


    @Override
    public String toString() {
        return "Tienda{" +
                "name='" + name + '\'' +
                ", stockMax=" + stockMax +
                ", cashOfStore=" + cashOfStore +
                ", cashOfSales=" + cashOfSales +
                '}';
    }
}
