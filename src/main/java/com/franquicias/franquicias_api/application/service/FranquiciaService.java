package com.franquicias.franquicias_api.application.service;

import com.franquicias.franquicias_api.application.port.in.ICrearFranquiciaUseCase;
import com.franquicias.franquicias_api.application.port.out.IFranquiciaRepository;
import com.franquicias.franquicias_api.domain.Franquicia;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import org.springframework.stereotype.Service;

@Service // Marca esto como un componente de Spring
@RequiredArgsConstructor // Inyección de dependencia por constructor (para el repositorio)
public class FranquiciaService implements ICrearFranquiciaUseCase {

    // Dependencia del Puerto de Salida (solo la interfaz)
    private final IFranquiciaRepository franquiciaRepository;

    @Override
    public Mono<Franquicia> crearFranquicia(Franquicia franquicia) {
        // *** Lógica de Negocio (Aquí irían validaciones, reglas, etc.) ***

        // Llamar al Repositorio para guardar
        return franquiciaRepository.save(franquicia);
    }
}