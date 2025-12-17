package com.franquicias.franquicias_api.infrastructure.data;

import com.franquicias.franquicias_api.application.port.out.IFranquiciaRepository;
import com.franquicias.franquicias_api.domain.Franquicia;
import reactor.core.publisher.Mono;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

// Spring Data Repository: Maneja la conexión directa a MongoDB de forma reactiva
interface ISpringDataFranquiciaRepository extends ReactiveMongoRepository<Franquicia, String> {

}

@Component
@Repository
public class MongoFranquiciaRepositoryAdapter implements IFranquiciaRepository {

    // Inyectamos el Repositorio de Spring Data
    private final ISpringDataFranquiciaRepository springRepository;

    public MongoFranquiciaRepositoryAdapter(ISpringDataFranquiciaRepository springRepository) {
        this.springRepository = springRepository;
    }

    @Override
    public Mono<Franquicia> save(Franquicia franquicia) {
        return springRepository.save(franquicia);
    }
}