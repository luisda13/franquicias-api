package com.franquicias.franquicias_api.infrastructure.api;

import com.franquicias.franquicias_api.application.port.in.ICrearFranquiciaUseCase;
import com.franquicias.franquicias_api.domain.Franquicia;
import reactor.core.publisher.Mono;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/franquicias")
public class FranquiciaController {

    //Inyectamos el Puerto de Entrada
    private final ICrearFranquiciaUseCase crearFranquiciaUseCase;

    public FranquiciaController(ICrearFranquiciaUseCase crearFranquiciaUseCase) {
        this.crearFranquiciaUseCase = crearFranquiciaUseCase;
    }

    /**
     * Expone endpoint para agregar una nueva franquicia.
     * Metodo: POST /franquicias
     * Cuerpo: JSON de la Franquicia
     * Respuesta: La Franquicia guardada y status 201 Created
     */
    @PostMapping(
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.CREATED) // Devuelve 201 Created al crear un recurso
    public Mono<Franquicia> crearFranquicia(@RequestBody Franquicia franquicia) {

        // Llama al Use Case (que es el Service) y devuelve el Mono<Franquicia> resultante.
        // Spring WebFlux se encarga de esperar a que el Mono se complete
        // y serializar la respuesta JSON.
        return crearFranquiciaUseCase.crearFranquicia(franquicia);
    }
}