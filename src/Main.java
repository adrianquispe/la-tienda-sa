import com.laTienda.producto.Bebida;
import com.laTienda.producto.Envasado;
import com.laTienda.producto.Limpieza;
import com.laTienda.producto.Producto;
import com.laTienda.tienda.*;
import com.laTienda.utils.Envase;
import com.laTienda.utils.GrupoDeProducto;
import com.laTienda.utils.GrupoProducto;
import com.laTienda.utils.UsoLimpieza;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Tienda currentStore = welcome();
        //System.out.println(currentStore.toString());
        int operation;
        do{
            operation = selectOperation();
            switch (operation){
                case 1:
                    buyByOrder(currentStore);
                    break;
                case 2:
                    System.out.println("TODO");
                    break;
                case 3:
                    break;
                case 4:
                    break;
                case 5:
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Operacion invalida.");
            }
        }while(operation != 0);
        System.out.println("Gracias por su visita!");
    }
    public static Tienda welcome(){
        String storeName;
        int stockMaxStore;
        double cashOfStore;
        boolean imputOk = false;
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
            imputOk = imputYesNo();
            if(imputOk)
                System.out.println("Felicidades, ya posee su tienda!");
            else
                System.out.println("Por favor vuelva a ingresar los datos.");
        } while(!imputOk);
        return new Tienda(storeName, stockMaxStore, cashOfStore);
    }
    public static int selectOperation(){
        Scanner scOpt = new Scanner(System.in);
        int option;
        System.out.println(" --- 0 --- 0 --- ");
        System.out.println("Operaciones disponibles:");
        System.out.println(" 1 - Compra de producto por Orden: carga los datos de la orden de compra y el sistema gestiona la transaccion");
        System.out.println(" 2 - Compra de producto interactiva: el sistema lo va guiando mientras hace la compra");
        System.out.println(" 3 - Venta de producto por Orden (-FUERA DE SERVICIO-)");
        System.out.println(" 4 - Venta de producto interactiva: realiza una venta en el momento ingresando id y cantidad");
        System.out.println(" 5 - Funcion proxima a implementar (-COMING SOON-)");
        System.out.println(" 0 - Salir");
        System.out.println("(ATENCION - ELIJA UNA OPCION INGRESANDO NUMERO, CUALQUIER OTRO INGRESO TERMINARA EL PROGRAMA Y PERDERA LOS DATOS)");
        System.out.println("Por favor ingrese una opcion: ");
        option = scOpt.nextInt();
        return option;
    }
    public static void buyByOrder(Tienda thisShop){
        System.out.println(" --- 0 --- 0 --- ");
        System.out.println("Por favor ingrese los datos solicitados, el sistema mostrara si la operacion se concreto con exito o hubo algun evento inesperado");
        thisShop.buyItemWithLogic(generateItemByConsole());
    }
    private static ItemTienda generateItemByConsole(){
        Producto aProduct = generateProductByConsole();
        ItemTienda anItem;
        Boolean isEdible;
        Boolean hasDiscounts;
        Integer caloriesItem = null;
        int anioExp = 0;
        int mesExp = 0;
        int diaExp = 0;
        Scanner sc = new Scanner(System.in);
        System.out.println("Datos adicionales (Los valores que ingrese debera confirmarlo consultando la lista de productos)");
        System.out.println("Es comestible? (Y/N)");
        isEdible = imputYesNo();
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
        hasDiscounts = imputYesNo();
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
        System.out.println(" - Datos basicos del producto -");
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
                if(Producto.isOtro(idProd))
                    System.out.println(" - Id invalido - ");
                else
                    idProdOk = true;
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
                    Boolean alcoholicProd;
                    Float alcoholLevel;
                    Boolean importedProd;
                    System.out.println("Es alcoholica? : (Y/N)");
                    alcoholicProd = imputYesNo();
                    if(alcoholicProd){
                        System.out.println("Ingrese nivel de alcohol en %: (use 'coma' de ser necesario)");
                        alcoholLevel = sc.nextFloat();
                    } else
                        alcoholLevel = 0.0F;
                    System.out.println("Es importada? : (Y/N)");
                    importedProd = imputYesNo();
                    thisProduct = new Bebida(idProd, descProd, quantityProd, customerPriceProd, stockPriceProd, alcoholicProd, alcoholLevel, importedProd);
                    creationProdBeforeConfirmationOk = true;
                    break;
                case ENVASE:
                    Envase packTypeProd = null;
                    Boolean importedPackProd;
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
                    importedPackProd = imputYesNo();
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
                creationProdOk = imputYesNo();
                if (!creationProdOk){
                    System.out.println("Por favor ingrese nuevamente los datos");
                    sc = new Scanner(System.in);
                }
            }
        }while(!creationProdOk);
        return thisProduct;
    }

    private static boolean imputYesNo(){
        Scanner sc = new Scanner(System.in);
        boolean correctAnswer = false;
        String imput;
        boolean answer = false;
        do{
            imput = sc.nextLine().substring(0,1).toUpperCase();
            if(imput.equals("Y")){
                answer = true;
                correctAnswer = true;
            }
            else if(imput.equals("N")){
                answer = false;
                correctAnswer = true;
            } else {
                System.out.println(" - Ingreso incorrecto, responda Y/N: ");
            }

        }while(!correctAnswer);
        return answer;
    }
}
