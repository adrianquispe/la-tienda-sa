import com.laTienda.carrito.ItemEnVenta;
import com.laTienda.producto.Bebida;
import com.laTienda.producto.Envasado;
import com.laTienda.producto.Limpieza;
import com.laTienda.producto.Producto;
import com.laTienda.tienda.*;
import com.laTienda.utils.Envase;
import com.laTienda.utils.GrupoDeProducto;
import com.laTienda.utils.GrupoProducto;
import com.laTienda.utils.UsoLimpieza;

import java.time.temporal.ValueRange;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Tienda currentStore = welcome();
        //System.out.println(currentStore.toString());
        boolean operationSelected = false;
        int operation;
        showOperations();
        do{
            operation = selectOperation();
            switch (operation){
                case 1:
                    buyByOrder(currentStore, true);
                    operationSelected = true;
                    break;
                case 2:
                    buyByConsole(currentStore, true);
                    operationSelected = true;
                    break;
                case 3:
                    System.out.println("TODO");
                    operationSelected = true;
                    break;
                case 4:
                    sellInteractive(currentStore, true);
                    operationSelected = true;
                    break;
                case 5:
                    manageStore(currentStore);
                    operationSelected = true;
                    break;
                case 9:
                    showOperations();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Operacion invalida. (ingrese 9 si necesita ver las opciones)");
            }
            if(operationSelected){
                showOperations();
                operationSelected = false;
            }
        }while(operation != 0);
        System.out.println("Gracias por su visita!");
    }
    public static Tienda welcome(){
        String storeName;
        int stockMaxStore;
        double cashOfStore;
        boolean inputOk = false;
        Scanner sc = new Scanner(System.in);
        System.out.println("Bienvenido. Por favor ingrese los datos de su tienda ");
        do {
            System.out.println("Ingrese nombre de la tienda: ");
            storeName = sc.nextLine();
            System.out.println("Ingrese el espacio de stock maximo que puede almacenar su tienda: ");
            stockMaxStore = sc.nextInt();
            System.out.println("Ingrese la cantidad de dinero que tiene su tienda: (use 'coma' si tiene decimales)");
            cashOfStore = sc.nextDouble();
            System.out.println("Usted ingreso:");
            System.out.println(" - Nombre de tienda: "+storeName);
            System.out.println(" - Espacio maximo de tienda: "+stockMaxStore);
            System.out.printf(" - Dinero de tienda: %.2f\n",cashOfStore);
            System.out.println("Los datos son correctos?: (Y/N) ");//only proceeds when passing a "Y"
            inputOk = inputYesNo();
            if(inputOk)
                System.out.println("Felicidades, ya posee su tienda!");
            else
                System.out.println("Por favor vuelva a ingresar los datos.");
        } while(!inputOk);
        return new Tienda(storeName, stockMaxStore, cashOfStore);
    }
    public static int selectOperation(){
        Scanner scOpt = new Scanner(System.in);
        int option;
        option = scOpt.nextInt();
        return option;
    }
    private static void showOperations(){
        System.out.println(" --- 0 --- 0 --- ");
        System.out.println("Operaciones disponibles:");
        System.out.println(" 1 - Compra de producto por Orden (Obsolete): carga los datos de la orden de compra y el sistema gestiona la transaccion");
        System.out.println(" 2 - Compra de producto interactiva: el sistema lo va guiando mientras hace la compra");
        System.out.println(" 3 - Venta de producto por Orden (-FUERA DE SERVICIO-)");
        System.out.println(" 4 - Venta de producto interactiva: realiza una venta en el momento ingresando id y cantidad");
        System.out.println(" 5 - Gestionar Tienda");
        System.out.println(" 0 - Salir");
        System.out.println("(ATENCION - ELIJA UNA OPCION INGRESANDO NUMERO, CUALQUIER OTRO INGRESO TERMINARA EL PROGRAMA Y PERDERA LOS DATOS)");
        System.out.println("Por favor ingrese una opcion: ");
    }
    public static void buyByOrder(Tienda thisShop, boolean logger){
        System.out.println(" --- 0 --- 0 --- ");
        System.out.println("Por favor ingrese los datos solicitados, el sistema mostrara si la operacion se concreto con exito o hubo algun evento inesperado");
        thisShop.buyItemWithLogic(generateItemByConsole(), logger);
    }
    public static void buyByConsole(Tienda thisShop, boolean logger){
        System.out.println(" --- 0 --- 0 --- ");
        System.out.println("Por favor siga los pasos, e ingrese los datos de ser solicitados ");
        buyInteractive(thisShop, logger);
    }
    private static ItemTienda generateItemByConsole(){
        Producto aProduct = generateProductByConsole();
        ItemTienda anItem;
        boolean isEdible;
        boolean hasDiscounts;
        Integer caloriesItem = null;
        int anioExp = 0;
        int mesExp = 0;
        int diaExp = 0;
        Scanner sc = new Scanner(System.in);
        System.out.println("Datos adicionales (Los valores que ingrese debera confirmarlo consultando la lista de productos)");
        System.out.println("Es comestible? (Y/N)");
        isEdible = inputYesNo();
        if(isEdible){
            System.out.println("Ingrese las calorias del producto: ");
            caloriesItem = sc.nextInt();
            System.out.println("Ingrese anio de vencimiento: (20XX)");
            anioExp = sc.nextInt();
            System.out.println("Ingrese mes de vencimiento: (XX)");
            mesExp = sc.nextInt();
            System.out.println("Ingrese dia de vencimiento: (XX)");
            diaExp = sc.nextInt();
        }
        System.out.println("Tiene o tendra algun descuento? (Y/N)");
        hasDiscounts = inputYesNo();
        if (hasDiscounts && isEdible){
            System.out.println(" - Aviso: la gestion de descuentos se hara fuera de esta operacion (agregado-borrado) - ");
            ItemTiendaComestibleConDescuento itemEdibleDisc = new ItemTiendaComestibleConDescuento(aProduct);
            itemEdibleDisc.setCaloriesOfItem(caloriesItem);
            itemEdibleDisc.setExpirationDate(diaExp, mesExp, anioExp);
            anItem = itemEdibleDisc;
        } else {
            if (isEdible) {
                ItemTiendaComestible itemEdible = new ItemTiendaComestible(aProduct);
                itemEdible.setCaloriesOfItem(caloriesItem);
                itemEdible.setExpirationDate(diaExp, mesExp, anioExp);
                anItem = itemEdible;
            }
            if (hasDiscounts){
                System.out.println(" - Aviso: la gestion de descuentos se hara fuera de esta operacion (agregado-borrado) - ");
                anItem = new ItemTiendaConDescuento(aProduct);
            }
            anItem = new ItemTienda(aProduct);
        }
        return anItem;
    }
    private static Producto generateProductByConsole(){
        //System.out.println(" - Datos basicos del producto -");
        Producto thisProduct = null;
        String idProd;
        String descProd;
        Integer quantityProd;
        Float stockPriceProd;
        Float customerPriceProd;
        GrupoProducto prodGroup;
        boolean creationProdBeforeConfirmationOk = false;
        boolean creationProdOk = false;
        boolean idProdOk = false;
        Scanner sc = new Scanner(System.in);

        do {
            do {
                System.out.println("Ingrese id del item (5 caracteres): [Atencion: ingrese correctamente este campo, de lo contrario el programa se detendra!]");
                idProd = sc.nextLine().toUpperCase().substring(0,5);
                prodGroup = GrupoDeProducto.typeOfProduct(idProd);
                System.out.println(" - Se detecto un producto de tipo: "+prodGroup.toString());
                if(ItemTienda.idValid(idProd))
                    idProdOk = true;
                else
                    System.out.println(" - Id invalido - ");
            }while(!idProdOk);
            System.out.println("Ingrese descripcion del producto: ");//if the product exist this is useless
            descProd = sc.nextLine();
            System.out.println("Ingrese cantidad del producto: ");
            quantityProd = sc.nextInt();
            System.out.println("Ingrese precio de stock: ");
            stockPriceProd = sc.nextFloat();
            System.out.println("Ingrese precio de venta al publico: ");
            customerPriceProd = sc.nextFloat();
            switch(prodGroup){//no time to make this a method :(
                case LIMPIEZA:
                    UsoLimpieza useLimpProd = null;
                    boolean useLimpOk = false;
                    int optionCleaning;
                    do {
                        System.out.println("Ingrese su uso de limpieza: ");
                        System.out.println(" 1. Cocina");
                        System.out.println(" 2. Pisos");
                        System.out.println(" 3. Ropa");
                        System.out.println(" 4. Multiuso");
                        optionCleaning = sc.nextInt();
                        if(optionCleaning <=4 && optionCleaning >= 1){
                            useLimpProd = UsoLimpieza.values()[optionCleaning-1];
                            useLimpOk = true;
                        }
                        else
                            System.out.println(" - Valor ingresado invalido - ");
                    } while(!useLimpOk);
                    thisProduct = new Limpieza(idProd, descProd, quantityProd, customerPriceProd, stockPriceProd, useLimpProd);
                    creationProdBeforeConfirmationOk = true;
                    break;
                case BEBIDA:
                    boolean alcoholicProd;
                    float alcoholLevel;
                    boolean importedProd;
                    System.out.println("Es alcoholica? : (Y/N)");
                    alcoholicProd = inputYesNo();
                    if(alcoholicProd){
                        System.out.println("Ingrese nivel de alcohol en %: (use 'coma' de ser necesario)");
                        alcoholLevel = sc.nextFloat();
                    } else
                        alcoholLevel = 0.0F;
                    System.out.println("Es importada? : (Y/N)");
                    importedProd = inputYesNo();
                    thisProduct = new Bebida(idProd, descProd, quantityProd, customerPriceProd, stockPriceProd, alcoholicProd, alcoholLevel, importedProd);
                    creationProdBeforeConfirmationOk = true;
                    break;
                case ENVASE:
                    Envase packTypeProd = null;
                    boolean importedPackProd;
                    int optionPack;
                    boolean packTypeOk=false;
                    do {
                        System.out.println("Ingrese tipo de empaque: ");
                        System.out.println(" 1. Plastico");
                        System.out.println(" 2. Vidrio");
                        System.out.println(" 3. Lata");
                        optionPack = sc.nextInt();
                        if(optionPack <=3 && optionPack >= 1){
                            packTypeProd = Envase.values()[optionPack-1];
                            packTypeOk = true;
                        }
                        else
                            System.out.println(" - Valor ingresado invalido - ");
                    } while(!packTypeOk);
                    System.out.println("Es importado? : (Y/N)");
                    importedPackProd = inputYesNo();
                    thisProduct = new Envasado(idProd, descProd, quantityProd, customerPriceProd, stockPriceProd, packTypeProd, importedPackProd);
                    creationProdBeforeConfirmationOk = true;
                    break;
                default:
                    System.out.println(" - Si ve este mensaje, algo salio mal, contacte al desarrollador - ");
            }
            if(creationProdBeforeConfirmationOk){
                System.out.println("Por favor confirme si los datos ingresados son correctos (Hay datos que no se pueden mostrar)");
                System.out.println(thisProduct);
                System.out.println("Los datos son correctos? (Y/N)");
                creationProdOk = inputYesNo();
                if (!creationProdOk){
                    System.out.println("Por favor ingrese nuevamente los datos");
                    sc = new Scanner(System.in);
                }
            }
        }while(!creationProdOk);
        return thisProduct;
    }

    private static boolean inputYesNo(){
        Scanner sc = new Scanner(System.in);
        boolean correctAnswer = false;
        String input;
        Boolean answer = null;
        do{
            input = sc.nextLine().substring(0,1).toUpperCase();
            if(input.equals("Y")){
                answer = true;
                correctAnswer = true;
            }
            else if(input.equals("N")){
                answer = false;
                correctAnswer = true;
            } else {
                System.out.println(" - Ingreso incorrecto, responda Y/N: ");
            }

        }while(!correctAnswer);
        return answer;
    }

    private static void buyInteractive(Tienda store, boolean logger){
        System.out.println(" - * - ");
        Scanner sc = new Scanner(System.in);
        String idItem;
        Integer buyQuantity;
        Float costItem;
        ItemTienda currentItem;
        boolean idItemOk = false;
        do {
            System.out.println("Ingrese id del item (5 caracteres): [Atencion: ingrese correctamente este campo, de lo contrario el programa se detendra!]");
            idItem = sc.nextLine().toUpperCase().substring(0,5);
            if(ItemTienda.idValid(idItem))
                idItemOk = true;
            else
                System.out.println(" - Id invalido - ");
        }while(!idItemOk);
        System.out.println("Ingrese la cantidad de compra: ");
        buyQuantity = sc.nextInt();
        System.out.println("Ingrese el precio por unidad del producto: (usar 'coma' de ser necesario)");
        costItem = sc.nextFloat();
        System.out.println(" - Chequeando datos ingresados - ");
        if(store.itemIsInStore(idItem)){
            currentItem = store.getItem(idItem);
            System.out.println("Se encontro item!");
            if(store.haveEnoughSpace(buyQuantity) && store.haveCashForPurchaseItem(buyQuantity, idItem)){
                System.out.println("Hay espacio disponible para la compra!");
                System.out.println("Hay saldo suficiente para la compra!");
                System.out.println("Confirma la compra? (Y/N)");
                if (inputYesNo()){
                    currentItem.addStockOfItem(buyQuantity);
                    store.removeCashFromPurchase(idItem, buyQuantity);
                    System.out.println("Compra realizada! Gracias, vuelva pronto!");
                }else{
                    System.out.println("Vuelva pronto!");
                }
            } else if(!store.haveEnoughSpace(buyQuantity)) {
                System.out.println("No hay espacio en tienda suficiente, por favor libere espacio en tienda o ingrese menor cantidad");
            } else {
                System.out.println("No hay dinero en tienda suficiente, por favor ingrese otro monto o renueve el dinero de la tienda");
            }
        } else {
            System.out.println("Producto no encontrado");
            if(store.haveEnoughSpace(buyQuantity) && store.haveCashForPurchase(costItem*buyQuantity)){
                System.out.println("Hay espacio disponible para la compra!");
                System.out.println("Hay saldo suficiente para la compra!");
                System.out.println("Desea agregar una compra del nuevo producto? (Y/N)");
                if(inputYesNo()){
                    System.out.println("Se le rediccionara a la planilla de carga de nuevo item, una ves realizado se le confirmara la compra...");
                    System.out.println(" - ATENCION: VERIFIQUE QUE LOS DATOS A INGRESAR SEAN LOS MISMOS QUE LOS INGRESADOS PREVIAMENTE -");
                    currentItem = generateItemByConsole();
                    //System.out.println("debug= item creado: "+ currentItem);
                    store.buyItemDirect(currentItem, logger);
                    //System.out.println(" - debug= item en tienda: "+store.getItem(idItem));
                    System.out.println("Compra realizada! Gracias, vuelva pronto!");
                }
                else
                    System.out.println("Vuelva pronto!");
            } else if(!store.haveEnoughSpace(buyQuantity)) {
                //System.out.println(" debug - stock tienda: "+store.getStockMax()+" - stock producto: "+ buyQuantity);
                System.out.println("No hay espacio en tienda suficiente, por favor libere espacio en tienda o ingrese menor cantidad");
            } else {
                System.out.println("No hay dinero en tienda suficiente, por favor ingrese otro monto o renueve el dinero de la tienda");
            }
        }
    }

    private static void sellInteractive(Tienda store, boolean logger){
        System.out.println(" - * - ");
        System.out.println("Bienvenido al gestor de ventas interactivo.");
        System.out.println(" - < Info: designed as a shopping cart > - "); //debug message
        System.out.println("Se le notifica que el limite de cantidad por producto es de "+store.UNIDADESXITEMMAX+
                " unidad/es, y solo puede adquirir hasta "+store.VENTAITEMMAX+" producto/s por sesion.");
        ArrayList<ItemEnVenta> itemSaleList = new ArrayList<>();
        showOptionsSaleCart();
        boolean success = false;
        boolean exitSaleGestor = false;
        do {
            switch (selectOperation()){
                case 1:
                    success = addToCart(store, itemSaleList, true);
                    break;
                case 2:
                    emptyCart(itemSaleList, true);
                    break;
                case 3:
                    displayShopCart(store, itemSaleList);
                    break;
                case 4:
                    continueToPayment(store, itemSaleList, true);
                    exitSaleGestor = true;
                    break;
                case 5:
                    System.out.println("Venta cancelada!");
                    exitSaleGestor = true;
                    break;
                case 9:
                    showOptionsSaleCart();
                    break;
                default:
                    System.out.println("Ingreso invalido (ingrese 9 para ver las opciones)");
            }
            if(!success) showOptionsSaleCart();
        } while (!exitSaleGestor);
    }

    private static void showOptionsSaleCart(){
        System.out.println("Ingrese operacion: ");
        System.out.println(" 1- Agregar item al carrito");
        System.out.println(" 2- Vaciar carrito");
        System.out.println(" 3- Mostrar carrito");
        System.out.println(" 4- Confirmar compra: procede al calculo de precio final");
        System.out.println(" 5- Cancelar compra (ojo - sin confirmacion)");
    }

    private static boolean addToCart(Tienda shop, ArrayList<ItemEnVenta> itemList, boolean logger){
        if(itemList.size()>=shop.VENTAITEMMAX){
            System.out.println("Lo sentimos, alcanzo el limite de items por sesion.");
            return false;
        }
        ItemEnVenta itemRequested = reciveOrderInteractive(shop, true);
        if(itemRequested == null){
            System.out.println("Operacion cancelada!");
        } else {
            itemList.add(itemRequested);
            System.out.println("Item agregado al carrito!");
            return true;
        }
        return false;
    }
    private static void emptyCart(ArrayList<ItemEnVenta> list, boolean logger){
        list.clear();
        if(logger)System.out.println("Lista vaciada!");
    }

    private static ItemEnVenta reciveOrderInteractive(Tienda store, boolean logger){
        String inputId;
        Scanner sc = new Scanner(System.in);
        boolean inputIdOk = false;
        System.out.println("Ingrese id del producto que desee adquirir: ");
        do {
            inputId = sc.nextLine();
            if(store.itemIsInStore(inputId)){
                if(logger)System.out.println("Producto encontrado!");
                inputIdOk = true;
            } else {
                System.out.println("Proucto no encontrado, intentelo nuevamente. (si desea salir cancelar, ingrese 0)");
            }

        } while (!inputIdOk && !inputId.equals("0"));
        if(inputId.equals("0")) return null;
        Integer quantityOfPurchase;
        boolean quantityOk = false;
        System.out.println("Ingrese cantidad de unidades a comprar: ");
        do {
            quantityOfPurchase = sc.nextInt();
            if(ValueRange.of(0, store.UNIDADESXITEMMAX).isValidValue(quantityOfPurchase)){
                quantityOk = true;
            } else {
                System.out.println("Cantidad de items invalido, por favor recuerde que el limite es "+
                        store.UNIDADESXITEMMAX+" por producto");
                System.out.println("Reingrese cantidad: (si desea cancelar, ingrese 0)");
            }
        } while (!quantityOfPurchase.equals(0) && !quantityOk);
        if(quantityOfPurchase.equals(0)) return null;
        if(!store.canBePurchased(inputId, quantityOfPurchase, true)) return null;
        System.out.println("Confirma ingreso de compra de "+quantityOfPurchase+
                " unidad/es del item '"+store.getItem(inputId).getName()+"'?");
        return inputYesNo() ? (new ItemEnVenta(inputId, quantityOfPurchase)) : null;
    }
    private static void displayShopCart(Tienda store, ArrayList<ItemEnVenta> shopList){
        if(shopList.isEmpty())
            System.out.println("Su carrito esta vacio!");
        else {
            System.out.println("Su carrito contiene los siguientes productos: ");
            for(ItemEnVenta item : shopList){
                System.out.println(" - "+item.basicSaleData(store));
            }
        }
    }
    public static void continueToPayment(Tienda shop, ArrayList<ItemEnVenta> itemList, boolean logger){
        if(itemList.isEmpty()){
            System.out.println("Nada que hacer, el carrito esta vacio! (cerrando sesion)");
            return;
        }
        System.out.println("Se procedera con el calculo de precios, aguarde un instante...");
        System.out.println("    * * * * * * ");
        for(ItemEnVenta item : itemList){
            if(logger)System.out.println(" - - - - - ");
            shop.calculateCostOfSale(item, true);
        }
        if(logger)System.out.println(" - - - - - ");
        System.out.println("    * * * * * * ");
        confirmSale(shop, itemList, true);
    }
    private static void confirmSale(Tienda shop, ArrayList<ItemEnVenta> itemList, boolean logger){
        Double totalOfSale = 0.0;
        if(logger)System.out.println("Se procedera al pago de los siguientes items:");
        for(ItemEnVenta item : itemList){
            if(logger)System.out.println(" - "+item.getFinalSaleItemDetail(shop));
            totalOfSale += item.getTotalOfPurchase();
        }
        System.out.println("Total de venta: "+totalOfSale);
        System.out.println("Confirmar venta? (Y/N)");
        if(inputYesNo()){
            shop.saleItemListDirect(itemList);
            System.out.println("Venta exitosa! gracias, vuelva pronto");
        } else {
            System.out.println("Venta cancelada! vuelva pronto");
        }
    }

    private static void manageStore(Tienda store){  //option 5: manage store
        boolean exitManager = false;
        boolean optSelected = false;
        showOptionManageStore();
        do{
            switch (selectOperation()){
                case 1:
                    System.out.println(store);
                    optSelected = true;
                    break;
                case 2:
                    displayStoreItems(store);
                    optSelected = true;
                    break;
                case 3:
                    addDiscountItem(store, true);
                    optSelected = true;
                    break;
                case 4:
                    System.out.println("TODO!");
                    optSelected = true;
                    break;
                case 5:
                    exitManager = true;
                    break;
                case 9:
                    showOptionManageStore();
                default:
                    System.out.println("Operacion invalida. (ingrese 9 si necesita ver las opciones)");
            }
            if(optSelected){
                showOptionManageStore();
                optSelected = false;
            }
        }while(!exitManager);
    }
    private static void showOptionManageStore(){
        //System.out.println(" - - - - - - - - - - ");
        System.out.println("Ingrese opcion: ");
        System.out.println(" 1- Mostrar datos de tienda");
        System.out.println(" 2- Mostrar lista de productos");
        System.out.println(" 3- Agregar descuento a item");
        System.out.println(" 4- Cambiar de tienda (COMMING SOON)");
        System.out.println(" 5- Volver al menu anterior");
    }
    private static void addDiscountItem(Tienda store, boolean logger){
        Scanner sc = new Scanner(System.in);
        String idProd;
        System.out.println("Ingrese id del producto: ");
        idProd = sc.nextLine();
        if(store.itemIsInStore(idProd)){
            store.addDiscountToItem(idProd, true);
        } else {
            System.out.println("El item no esta en la tienda o es incorrecto.");
        }
    }
    private static void displayStoreItems(Tienda store){
        System.out.println("Productos en la tienda: ");
        System.out.println(store.getAllItems());
    }
}
