package com.franquicias.franquicias_api.infrastructure.api;

import com.franquicias.franquicias_api.application.port.in.IFranquiciaManagement;
import com.franquicias.franquicias_api.domain.Franquicia;
import com.franquicias.franquicias_api.domain.Sucursal;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
}