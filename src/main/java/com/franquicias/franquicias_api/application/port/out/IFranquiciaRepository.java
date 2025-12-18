package com.franquicias.franquicias_api.application.port.out;

import com.franquicias.franquicias_api.domain.Franquicia;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;

// Puerto de Salida: Define cómo se guardarán los datos (lo implementará la infraestructura)
public interface IFranquiciaRepository {

    /**
     * Guarda una nueva franquicia o actualiza una existente.
     * @param franquicia La entidad Franquicia a guardar.
     * @return Mono<Franquicia> La franquicia guardada, envuelta en un Mono reactivo.
     */
    Mono<Franquicia> save(Franquicia franquicia);
    Mono<Franquicia> findById(String id);

    // Nuevo: Buscar una franquicia por su nombre exacto
    Mono<Franquicia> findByNombre(String nombre);

    // Nuevo: Buscar todas las franquicias (Flux para una lista reactiva)
    Flux<Franquicia> findAll();
}