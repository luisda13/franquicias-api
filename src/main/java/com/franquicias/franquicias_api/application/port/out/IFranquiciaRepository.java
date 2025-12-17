package com.franquicias.franquicias_api.application.port.out;

import com.franquicias.franquicias_api.domain.Franquicia;
import reactor.core.publisher.Mono;

// Puerto de Salida: Define cómo se guardarán los datos (lo implementará la infraestructura)
public interface IFranquiciaRepository {

    /**
     * Guarda una nueva franquicia o actualiza una existente.
     * @param franquicia La entidad Franquicia a guardar.
     * @return Mono<Franquicia> La franquicia guardada, envuelta en un Mono reactivo.
     */
    Mono<Franquicia> save(Franquicia franquicia);
}