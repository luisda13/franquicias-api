package com.franquicias.franquicias_api.domain;

import com.franquicias.franquicias_api.domain.exception.RecursoDuplicadoException;
import com.franquicias.franquicias_api.domain.exception.RecursoNoEncontradoException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;
import java.util.ArrayList;

// Mapeo a la colección de MongoDB
@Document(collection = "franquicias")
@Data                    // ← Genera getters, setters, toString, equals, hashCode
@NoArgsConstructor       // ← Constructor vacío (necesario para MongoDB/Jackson)
@AllArgsConstructor      // ← Constructor con todos los campos (útil)
public class Franquicia {

    @Id
    private String id;

    private String nombre;

    // Lista de sucursales que pertenecen a esta franquicia.
    private List<Sucursal> sucursales = new ArrayList<>();

    // Constructor personalizado para crear una nueva franquicia (sin ID)
    // Este constructor adicional es útil para crear objetos sin ID
    public Franquicia(String nombre, List<Sucursal> sucursales) {
        this.nombre = nombre;
        this.sucursales = sucursales != null ? sucursales : new ArrayList<>();
    }

    // Metodo para añadir una sucursal con validación.
    public void agregarSucursal(Sucursal sucursal) {
        // Validacion de unicidad de Sucursal (Logica de Dominio)
        if (this.sucursales.stream().anyMatch(s -> s.getNombre().equalsIgnoreCase(sucursal.getNombre()))) {
            throw new RecursoDuplicadoException("Ya existe una sucursal con el nombre '" + sucursal.getNombre() + "' en esta franquicia.");
        }
        if (this.sucursales == null) {
            this.sucursales = new ArrayList<>();
        }
        this.sucursales.add(sucursal);
    }

    // Necesario para Criterio 4
    public Sucursal buscarSucursalPorNombre(String nombreSucursal) {
        return this.sucursales.stream()
                .filter(s -> s.getNombre().equalsIgnoreCase(nombreSucursal))
                .findFirst()
                .orElse(null);
    }

    // Lógica para eliminar de una única sucursal
    public void eliminarProductoDeSucursal(String nombreSucursal, String nombreProducto) {

        // Usamos el metodo existente para buscar la sucursal.
        Sucursal sucursal = buscarSucursalPorNombre(nombreSucursal);

        if (sucursal == null) {
            // La sucursal no existe en la franquicia
            throw new RecursoNoEncontradoException("Sucursal", nombreSucursal + " en la Franquicia " + this.getNombre());
        }

        // Intentar eliminar el producto. removeIf devuelve true si se elimina algo.
        boolean productoEliminado = sucursal.getProductos().removeIf(
                p -> p.getNombre().equalsIgnoreCase(nombreProducto)
        );

        if (!productoEliminado) {
            // El producto no existe en esa sucursal
            throw new RecursoNoEncontradoException("Producto", nombreProducto + " en la sucursal " + nombreSucursal);
        }
    }

    // Lógica para eliminar de múltiples sucursales
    public void eliminarProductoDeTodasLasSucursales(String nombreProducto) {
        int productosEliminados = 0;

        // Iteramos sobre todas las sucursales para eliminar el producto
        for (Sucursal sucursal : this.sucursales) {
            boolean eliminado = sucursal.getProductos().removeIf(
                    p -> p.getNombre().equalsIgnoreCase(nombreProducto)
            );
            if (eliminado) {
                productosEliminados++;
            }
        }

        if (productosEliminados == 0) {
            // El producto no existía en NINGUNA sucursal de esta franquicia
            throw new RecursoNoEncontradoException("Producto", nombreProducto + " en ninguna sucursal de la Franquicia " + this.getNombre());
        }
    }

    /**
     * Coordina la actualización del stock de un producto dentro de una sucursal.
     * Lanza 404 si la sucursal o el producto no existen.
     */
    public void modificarCantidadProducto(String nombreSucursal, String nombreProducto, int nuevoStock) {

        //Validar existencia de Sucursal (Lanza 404 si no existe)
        Sucursal sucursal = buscarSucursalPorNombre(nombreSucursal);

        if (sucursal == null) {
            throw new RecursoNoEncontradoException("Sucursal", nombreSucursal + " en la Franquicia " + this.getNombre());
        }

        //Delegar la actualización a la Sucursal y validar el Producto (Lanza 404 si no existe)
        boolean productoActualizado = sucursal.actualizarCantidadProducto(nombreProducto, nuevoStock);

        if (!productoActualizado) {
            throw new RecursoNoEncontradoException("Producto", nombreProducto + " en la sucursal " + nombreSucursal);
        }
    }

    // Extra 1: Actualizar Nombre de Franquicia
    public void actualizarNombre(String nuevoNombre) {
        if (nuevoNombre == null || nuevoNombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nuevo nombre de la franquicia no puede ser vacío.");
        }
        this.nombre = nuevoNombre;
    }

    // Extra 2: Actualizar Nombre de Sucursal
    public void actualizarNombreSucursal(String nombreSucursalActual, String nuevoNombre) {
        if (nombreSucursalActual == null || nuevoNombre == null || nuevoNombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre actual y el nuevo nombre de la sucursal son obligatorios.");
        }

        // Verificar si el nuevo nombre ya está en uso en esta franquicia (409)
        if (this.sucursales.stream().anyMatch(s -> s.getNombre().equalsIgnoreCase(nuevoNombre))) {
            throw new RecursoDuplicadoException("Ya existe una sucursal con el nombre: " + nuevoNombre + " en esta franquicia.");
        }

        //Encontrar la sucursal actual (404)
        Sucursal sucursal = this.sucursales.stream()
                .filter(s -> s.getNombre().equalsIgnoreCase(nombreSucursalActual))
                .findFirst()
                .orElseThrow(() -> new RecursoNoEncontradoException("Sucursal", nombreSucursalActual));

        //Aplicar el cambio
        sucursal.setNombre(nuevoNombre);
    }

    // Extra 3: Actualizar Nombre de Producto
    public void actualizarNombreProducto(String nombreSucursal, String nombreProductoActual, String nuevoNombreProducto) {
        if (nombreSucursal == null || nombreProductoActual == null || nuevoNombreProducto == null || nuevoNombreProducto.trim().isEmpty()) {
            throw new IllegalArgumentException("Los nombres de Sucursal, Producto actual y Producto nuevo son obligatorios.");
        }

        //Encontrar la sucursal (404)
        Sucursal sucursal = this.sucursales.stream()
                .filter(s -> s.getNombre().equalsIgnoreCase(nombreSucursal))
                .findFirst()
                .orElseThrow(() -> new RecursoNoEncontradoException("Sucursal", nombreSucursal));

        //Verificar si el nuevo nombre de producto ya existe en esta sucursal (409)
        if (sucursal.getProductos().stream().anyMatch(p -> p.getNombre().equalsIgnoreCase(nuevoNombreProducto))) {
            throw new RecursoDuplicadoException("Ya existe un producto con el nombre: " + nuevoNombreProducto + " en la sucursal " + nombreSucursal);
        }

        //Encontrar el producto actual (404)
        Producto producto = sucursal.getProductos().stream()
                .filter(p -> p.getNombre().equalsIgnoreCase(nombreProductoActual))
                .findFirst()
                .orElseThrow(() -> new RecursoNoEncontradoException("Producto", nombreProductoActual));

        //Aplicar el cambio
        producto.setNombre(nuevoNombreProducto);
    }
}