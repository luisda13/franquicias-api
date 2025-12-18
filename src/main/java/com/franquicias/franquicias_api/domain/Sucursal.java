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

    /**
     * Busca y actualiza el stock de un producto.
     * @return true si el producto fue encontrado y actualizado, false si no.
     */
    public boolean actualizarCantidadProducto(String nombreProducto, int nuevoStock) {
        if (this.productos == null) return false;

        // Buscar el producto por nombre
        return this.productos.stream()
                .filter(p -> p.getNombre().equalsIgnoreCase(nombreProducto))
                .findFirst()
                .map(producto -> {
                    // Si el producto existe, aplica el cambio de stock
                    producto.setStock(nuevoStock);
                    return true;
                })
                .orElse(false);
    }
}