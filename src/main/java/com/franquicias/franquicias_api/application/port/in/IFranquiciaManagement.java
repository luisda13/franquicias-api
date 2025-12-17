package com.franquicias.franquicias_api.application.port.in;

import com.franquicias.franquicias_api.domain.Franquicia;
import com.franquicias.franquicias_api.domain.Sucursal;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

// Este es el Puerto de Entrada unificado para la gestión de Franquicias
public interface IFranquiciaManagement {

    // Criterio 2: Crear una nueva franquicia
    Mono<Franquicia> crearFranquicia(Franquicia franquicia);

    // Nueva Búsqueda: Buscar todas las franquicias (solicitado en tu plan de pulido)
    Flux<Franquicia> findAll();

    // Nuevo para la validación: Buscar por nombre
    Mono<Franquicia> findByNombre(String nombre);

    // Criterio 3: Añadir una sucursal (Usada por IAddSucursalUseCase en el plan anterior)
    Mono<Franquicia> addSucursal(String franquiciaId, Sucursal sucursal);

    // Nuevo: Buscar por ID (necesario para el Criterio 3 y futuras actualizaciones)
    Mono<Franquicia> findById(String id);
}
