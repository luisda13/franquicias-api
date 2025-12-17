package com.franquicias.franquicias_api.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data                    // Genera getters, setters, toString, equals, hashCode
@NoArgsConstructor       // Constructor vacío (necesario para frameworks)
@AllArgsConstructor      // Constructor con todos los campos
public class Producto {
    private String nombre;
    private int stock;

    // --- Lógica de Dominio ---

    /**
     * Lógica para modificar el stock de forma segura.
     * @param cambio La cantidad a sumar (positiva) o restar (negativa).
     */
    public void modificarStock(int cambio) {
        this.stock += cambio;
    }
}