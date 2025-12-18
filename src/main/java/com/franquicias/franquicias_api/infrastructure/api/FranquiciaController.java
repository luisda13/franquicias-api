package com.franquicias.franquicias_api.infrastructure.api;

import com.franquicias.franquicias_api.application.dto.ProductoMaxStockDto;
import com.franquicias.franquicias_api.application.port.in.IFranquiciaManagement;
import com.franquicias.franquicias_api.domain.Franquicia;
import com.franquicias.franquicias_api.domain.Producto;
import com.franquicias.franquicias_api.domain.Sucursal;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/franquicias")
public class FranquiciaController {

    //Declaración de la dependencia
    private final IFranquiciaManagement franquiciaManagement;

    //Constructor para inyección de dependencia
    public FranquiciaController(IFranquiciaManagement franquiciaManagement) {
        this.franquiciaManagement = franquiciaManagement;
    }

    //Criterio 2: Exponer endpoint para crear una nueva franquicia (POST)
    @PostMapping(
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.CREATED) // Devuelve 201 Created al crear un recurso
    public Mono<Franquicia> crearFranquicia(@RequestBody Franquicia franquicia) {
        return franquiciaManagement.crearFranquicia(franquicia);
    }

    //Exponer GET para obtener todas las franquicias
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<Franquicia> getAllFranquicias() {
        return franquiciaManagement.findAll();
    }

    //Exponer GET para buscar una franquicia por su ID
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Franquicia> getFranquiciaById(@PathVariable String id) {
        // El servicio debe manejar el error 404 si no lo encuentra.
        return franquiciaManagement.findById(id);
    }

    //Exponer GET para buscar una franquicia por su nombre
    @GetMapping(value = "/nombre/{nombre}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Franquicia> getFranquiciaByNombre(@PathVariable String nombre) {
        // El servicio debe manejar el error 404 si no lo encuentra.
        return franquiciaManagement.findByNombre(nombre);
    }

    //Criterio 3: Exponer endpoint para agregar una nueva sucursal (POST)
    @PostMapping(
            value = "/{franquiciaId}/sucursales",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.OK) // Se usa 200 OK porque estamos modificando un recurso existente.
    public Mono<Franquicia> addSucursal(
            @PathVariable String franquiciaId,
            @RequestBody Sucursal sucursal) {

        return franquiciaManagement.addSucursal(franquiciaId, sucursal);
    }

    /**
     * Criterio 4: Exponer endpoint para agregar un nuevo producto a la sucursal.
     * Metodo: POST /franquicias/{franquiciaId}/sucursales/{sucursalNombre}/productos
     */
    @PostMapping(
            value = "/{franquiciaId}/sucursales/{sucursalNombre}/productos",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.OK) // Modificación de un recurso existente
    public Mono<Franquicia> addProducto(
            @PathVariable String franquiciaId,
            @PathVariable String sucursalNombre,
            @RequestBody Producto producto) {

        return franquiciaManagement.addProducto(franquiciaId, sucursalNombre, producto);
    }

    /**
     * Criterio 5.1: Eliminar un producto de una sucursal específica.
     * Metodo: DELETE /franquicias/{franquiciaId}/sucursales/{sucursalNombre}/productos/{productoNombre}
     */
    @DeleteMapping(value = "/{franquiciaId}/sucursales/{sucursalNombre}/productos/{productoNombre}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<String> deleteProducto(
            @PathVariable String franquiciaId,
            @PathVariable String sucursalNombre,
            @PathVariable String productoNombre) {

        return franquiciaManagement.deleteProducto(franquiciaId, sucursalNombre, productoNombre);
    }

    /**
     * Criterio 5.2: Eliminar un producto de todas las sucursales de la franquicia.
     * Metodo: DELETE /franquicias/{franquiciaId}/productos/{productoNombre}
     */
    @DeleteMapping(value = "/{franquiciaId}/productos/{productoNombre}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<String> deleteProductoFromAllSucursales(
            @PathVariable String franquiciaId,
            @PathVariable String productoNombre) {

        return franquiciaManagement.deleteProductoFromAllSucursales(franquiciaId, productoNombre);
    }

    /**
     * Criterio 6: Modificar la cantidad de un producto.
     * Metodo: PUT /franquicias/{franquiciaId}/sucursales/{sucursalNombre}/productos/{productoNombre}/stock
     * Cuerpo: {"stock": 150}
     */
    @PutMapping(
            value = "/{franquiciaId}/sucursales/{sucursalNombre}/productos/{productoNombre}/stock",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    public Mono<Franquicia> updateStock(
            @PathVariable String franquiciaId,
            @PathVariable String sucursalNombre,
            @PathVariable String productoNombre,
            @RequestBody Map<String, Integer> requestBody) {

        // Extraemos el stock del mapa JSON. Asumimos que el JSON es {"stock": 150}
        Integer nuevoStock = requestBody.get("stock");

        if (nuevoStock == null) {
            return Mono.error(new IllegalArgumentException("El campo 'stock' es obligatorio en el cuerpo de la petición."));
        }

        return franquiciaManagement.updateStock(franquiciaId, sucursalNombre, productoNombre, nuevoStock);
    }

    @GetMapping("/{id}/productos-max-stock")
    public Flux<ProductoMaxStockDto> obtenerProductosMaxStock(@PathVariable String id) {
        return franquiciaManagement.obtenerProductosMaxStockPorSucursal(id);
    }

    /**
     * Extra 1: Actualizar Nombre de Franquicia.
     * Metodo: PUT /franquicias/{id}
     */
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Mono<Franquicia> updateNombreFranquicia(@PathVariable String id, @RequestBody Map<String, String> requestBody) {
        String nuevoNombre = requestBody.get("nombre");

        if (nuevoNombre == null || nuevoNombre.trim().isEmpty()) {
            return Mono.error(new IllegalArgumentException("El campo 'nombre' es obligatorio en el cuerpo de la petición."));
        }

        return franquiciaManagement.updateNombreFranquicia(id, nuevoNombre);
    }

    /**
     * Extra 2: Actualizar Nombre de Sucursal.
     * Metodo: PUT /franquicias/{id}/sucursales/{nombreActual}
     */
    @PutMapping(value = "/{franquiciaId}/sucursales/{nombreActual}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Mono<Franquicia> updateNombreSucursal(
            @PathVariable String franquiciaId,
            @PathVariable String nombreActual,
            @RequestBody Map<String, String> requestBody) {

        String nuevoNombre = requestBody.get("nombre");

        if (nuevoNombre == null || nuevoNombre.trim().isEmpty()) {
            return Mono.error(new IllegalArgumentException("El campo 'nombre' es obligatorio en el cuerpo de la petición."));
        }

        return franquiciaManagement.updateNombreSucursal(franquiciaId, nombreActual, nuevoNombre);
    }

    /**
     * Extra 3: Actualizar Nombre de Producto.
     * Metodo: PUT /franquicias/{id}/sucursales/{nombre}/productos/{productoActual}
     */
    @PutMapping(value = "/{franquiciaId}/sucursales/{sucursalNombre}/productos/{nombreActual}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Mono<Franquicia> updateNombreProducto(
            @PathVariable String franquiciaId,
            @PathVariable String sucursalNombre,
            @PathVariable String nombreActual,
            @RequestBody Map<String, String> requestBody) {

        String nuevoNombre = requestBody.get("nombre");

        if (nuevoNombre == null || nuevoNombre.trim().isEmpty()) {
            return Mono.error(new IllegalArgumentException("El campo 'nombre' es obligatorio en el cuerpo de la petición."));
        }

        return franquiciaManagement.updateNombreProducto(franquiciaId, sucursalNombre, nombreActual, nuevoNombre);
    }
}