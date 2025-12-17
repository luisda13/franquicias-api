package com.franquicias.franquicias_api.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.ArrayList;

@Data                    // Genera getters, setters, toString, equals, hashCode
@NoArgsConstructor       // Constructor vacío
@AllArgsConstructor      // Constructor con todos los campos
public class Sucursal {
    private String nombre;
    private List<Producto> productos = new ArrayList<>();

    // --- Lógica de Dominio ---

    // Metodo para añadir un producto a la sucursal
    public void agregarProducto(Producto producto) {
        if (producto != null) {
            this.productos.add(producto);
        }
    }

    // Metodo para buscar un producto por nombre
    public Producto buscarProductoPorNombre(String nombre) {
        return this.productos.stream()
                .filter(p -> p.getNombre().equalsIgnoreCase(nombre))
                .findFirst()
                .orElse(null);
    }
}