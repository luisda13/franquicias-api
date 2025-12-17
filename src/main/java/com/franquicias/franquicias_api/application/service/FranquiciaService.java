package com.franquicias.franquicias_api.application.service;

import com.franquicias.franquicias_api.application.port.in.IFranquiciaManagement;
import com.franquicias.franquicias_api.application.port.out.IFranquiciaRepository;
import com.franquicias.franquicias_api.domain.Franquicia;
import com.franquicias.franquicias_api.domain.Producto;
import com.franquicias.franquicias_api.domain.Sucursal;
// Ajusta este import a tu paquete de excepciones exacto
import com.franquicias.franquicias_api.domain.exception.RecursoDuplicadoException;
import com.franquicias.franquicias_api.domain.exception.RecursoNoEncontradoException; // ¡Asegúrate de crear esta clase!

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import org.springframework.stereotype.Service;

@Service // Marca esto como un componente de Spring
@RequiredArgsConstructor // Inyección de dependencia por constructor (para el repositorio)
public class FranquiciaService implements IFranquiciaManagement {

    // Dependencia del Puerto de Salida (solo la interfaz)
    private final IFranquiciaRepository franquiciaRepository;

    /**
     * Criterio 2: Crear Franquicia con validación de existencia y datos.
     */
    @Override
    public Mono<Franquicia> crearFranquicia(Franquicia franquicia) {
        // Validación 1: Verificar que la entidad no sea nula y tenga nombre
        if (franquicia == null || franquicia.getNombre() == null || franquicia.getNombre().trim().isEmpty()) {
            return Mono.error(new IllegalArgumentException("El nombre de la franquicia es obligatorio."));
        }

        // Validación 2: Verificar si ya existe una franquicia con ese nombre.
        return franquiciaRepository.findByNombre(franquicia.getNombre())
                .flatMap(existente ->
                        // Si encuentra una, lanza una excepción de conflicto (409)
                        Mono.error(new RecursoDuplicadoException("La franquicia '" + existente.getNombre() + "' ya existe."))
                )
                .switchIfEmpty(
                        // Si no existe (Mono.empty()), procede a guardar
                        franquiciaRepository.save(franquicia)
                )
                .cast(Franquicia.class);
    }

    /**
     * Busca todas las franquicias.
     * Mapeado a: GET /franquicias
     */
    @Override
    public Flux<Franquicia> findAll() {
        return franquiciaRepository.findAll();
    }

    /**
     * Busca una franquicia por su ID con manejo de error 404.
     * Mapeado a: GET /franquicias/{id}
     */
    @Override
    public Mono<Franquicia> findById(String id) {
        return franquiciaRepository.findById(id)
                // Si el Mono está vacío, lanza la excepción (se mapeará a 404)
                .switchIfEmpty(Mono.error(new RecursoNoEncontradoException("Franquicia", id)));
    }

    /**
     * Busca una franquicia por su nombre con manejo de error 404.
     * Mapeado a: GET /franquicias/nombre/{nombre}
     */
    @Override
    public Mono<Franquicia> findByNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return Mono.error(new IllegalArgumentException("El nombre de la franquicia es obligatorio para la búsqueda."));
        }
        return franquiciaRepository.findByNombre(nombre)
                // Si el Mono está vacío, lanza la excepción (se mapeará a 404)
                .switchIfEmpty(Mono.error(new RecursoNoEncontradoException("Franquicia", nombre)));
    }


    /**
     * Criterio 3: Agrega una nueva sucursal a una franquicia existente.
     * Mapeado a: POST /franquicias/{franquiciaId}/sucursales
     */
    @Override
    public Mono<Franquicia> addSucursal(String franquiciaId, Sucursal sucursal) {
        // Validación de entradas
        if (franquiciaId == null || franquiciaId.trim().isEmpty()) {
            return Mono.error(new IllegalArgumentException("El ID de la franquicia es obligatorio."));
        }
        if (sucursal == null || sucursal.getNombre() == null || sucursal.getNombre().trim().isEmpty()) {
            return Mono.error(new IllegalArgumentException("El nombre de la sucursal es obligatorio."));
        }

        // Flujo: 1. Buscar -> 2. Modificar (Dominio) -> 3. Guardar
        return franquiciaRepository.findById(franquiciaId)
                // 1. Si no existe, lanza 404
                .switchIfEmpty(Mono.error(new RecursoNoEncontradoException("Franquicia", franquiciaId)))

                // 2. Modificar la entidad de Dominio
                .map(franquicia -> {
                    // **IMPORTANTE**: Necesitas asegurar que el metodo agregarSucursal existe en tu clase Franquicia
                    franquicia.agregarSucursal(sucursal);
                    return franquicia;
                })

                // 3. Guardar el documento modificado
                .flatMap(franquiciaRepository::save);
    }

    @Override
    public Mono<Franquicia> addProducto(String franquiciaId, String sucursalNombre, Producto producto) {
        // 1. Validaciones básicas de entrada
        if (franquiciaId == null || sucursalNombre == null || producto == null) {
            return Mono.error(new IllegalArgumentException("Todos los campos (ID, Nombre de Sucursal y Producto) son obligatorios."));
        }
        if (producto.getNombre() == null || producto.getNombre().trim().isEmpty()) {
            return Mono.error(new IllegalArgumentException("El nombre del producto es obligatorio."));
        }

        // 2. Flujo Reactivo: Buscar -> Validar/Modificar -> Guardar
        return franquiciaRepository.findById(franquiciaId)
                // Lanza 404 si la franquicia no existe
                .switchIfEmpty(Mono.error(new RecursoNoEncontradoException("Franquicia", franquiciaId)))
                .map(franquicia -> {

                    // Buscar la sucursal (usando el nuevo metodo de dominio)
                    Sucursal sucursal = franquicia.buscarSucursalPorNombre(sucursalNombre);

                    // Lanza 404 si la sucursal no existe dentro de la franquicia
                    if (sucursal == null) {
                        throw new RecursoNoEncontradoException("Sucursal", sucursalNombre + " en la Franquicia " + franquiciaId);
                    }

                    // Validación de unicidad de Producto
                    if (sucursal.getProductos().stream().anyMatch(p -> p.getNombre().equalsIgnoreCase(producto.getNombre()))) {
                        throw new RecursoDuplicadoException("El producto '" + producto.getNombre() + "' ya existe en la sucursal '" + sucursalNombre + "'.");
                    }

                    // Agregar el producto a la sucursal
                    sucursal.getProductos().add(producto);

                    return franquicia;
                })
                .flatMap(franquiciaRepository::save); // 3. Guardar el dato modificado
    }
}