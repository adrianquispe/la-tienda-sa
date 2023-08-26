package com.laTienda.descuentos;

public interface ItemTieneDescuento {
    static Float fixedDiscount(TipoDescuentoItem discountType){
        switch (discountType){
            case PRONTOVENC:
                return 0.3F;
            case PRECIO_CUIDADO:
                return 0.1F;
            case OFERTA_DEL_DIA:
                return 0.2F;
            default:
                return 0.0F;
        }
    }
    Float totalDiscount();
    Float priceWithDisc();
    void addDiscount(TipoDescuentoItem discount);
    void removeDiscount(TipoDescuentoItem discount);
    Boolean hasDiscount(TipoDescuentoItem discount);
    Boolean hasDiscounts();
}
