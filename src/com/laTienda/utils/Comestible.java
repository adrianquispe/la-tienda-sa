package com.laTienda.utils;

import java.time.LocalDate;

public interface Comestible {
    void setExpirationDate(int dia, int mes, int anio);
    LocalDate getExpirationDate();
    void setCaloriesOfItem(Integer calories);
    Integer getCaloriesOfItem();
}
