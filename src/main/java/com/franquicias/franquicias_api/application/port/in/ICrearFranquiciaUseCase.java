package com.franquicias.franquicias_api.application.port.in;

import com.franquicias.franquicias_api.domain.Franquicia;
import reactor.core.publisher.Mono;

// Puerto de Entrada: Define cómo se crea una franquicia (lo implementará la Application/Service)
public interface ICrearFranquiciaUseCase {

    /**
     * Lógica principal para crear una nueva franquicia.
     * @param franquicia La entidad Franquicia a crear.
     * @return Mono<Franquicia> La franquicia creada.
     */
    Mono<Franquicia> crearFranquicia(Franquicia franquicia);
}