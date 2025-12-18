package com.franquicias.franquicias_api.application.port.in;

import com.franquicias.franquicias_api.application.dto.ProductoMaxStockDto;
import com.franquicias.franquicias_api.domain.Franquicia;
import com.franquicias.franquicias_api.domain.Producto;
import com.franquicias.franquicias_api.domain.Sucursal;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

// Este es el Puerto de Entrada unificado para la gestión de Franquicias
public interface IFranquiciaManagement {

    // Criterio 2: Crear una nueva franquicia
    Mono<Franquicia> crearFranquicia(Franquicia franquicia);

    //Búsqueda: Buscar todas las franquicias (solicitado en tu plan de pulido)
    Flux<Franquicia> findAll();

    // la validación: Buscar por nombre
    Mono<Franquicia> findByNombre(String nombre);

    // Criterio 3: Añadir una sucursal (Usada por IAddSucursalUseCase en el plan anterior)
    Mono<Franquicia> addSucursal(String franquiciaId, Sucursal sucursal);

    //Buscar por ID (necesario para el Criterio 3 y futuras actualizaciones)
    Mono<Franquicia> findById(String id);

    // Criterio 4: Exponer endpoint para agregar un nuevo producto a la sucursal
    Mono<Franquicia> addProducto(String franquiciaId, String sucursalNombre, Producto producto);

    // Criterio 5.1: Eliminar un producto de una sucursal específica
    Mono<String> deleteProducto(String franquiciaId, String sucursalNombre, String productoNombre);

    // Criterio 5.2: Eliminar un producto de todas las sucursales de la franquicia
    Mono<String> deleteProductoFromAllSucursales(String franquiciaId, String productoNombre);

    /**
     * Criterio 6: Modifica el stock de un producto específico en una sucursal específica.
     * @param nuevoStock El nuevo valor de stock.
     */
    Mono<Franquicia> updateStock(String franquiciaId, String sucursalNombre, String productoNombre, int nuevoStock);

    /**
     * Criterio 7: Obtiene el producto con máximo stock por cada sucursal de una franquicia.
     * @param franquiciaId ID de la franquicia.
     * @return Flux de DTOs con el producto max por sucursal.
     */
    Flux<ProductoMaxStockDto> obtenerProductosMaxStockPorSucursal(String franquiciaId);

    /**
     * Extra 1: Actualiza el nombre de una franquicia.
     */
    Mono<Franquicia> updateNombreFranquicia(String id, String nuevoNombre);

    /**
     * Extra 2: Actualiza el nombre de una sucursal en una franquicia.
     */
    Mono<Franquicia> updateNombreSucursal(String franquiciaId, String nombreActual, String nuevoNombre);

    /**
     * Extra 3: Actualiza el nombre de un producto en una sucursal.
     */
    Mono<Franquicia> updateNombreProducto(String franquiciaId, String sucursalNombre, String nombreActual, String nuevoNombre);
}
