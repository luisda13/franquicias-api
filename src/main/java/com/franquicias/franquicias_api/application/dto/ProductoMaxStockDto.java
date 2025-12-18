package com.franquicias.franquicias_api.application.dto;

import lombok.Data;

@Data
public class ProductoMaxStockDto {
    private String productoNombre;
    private int stock;
    private String sucursalNombre;
}