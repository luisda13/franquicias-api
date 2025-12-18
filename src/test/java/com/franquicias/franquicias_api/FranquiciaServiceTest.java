// src/test/java/com/franquicias/franquicias_api/FranquiciaServiceTest.java

package com.franquicias.franquicias_api;

import com.franquicias.franquicias_api.application.port.out.IFranquiciaRepository;
import com.franquicias.franquicias_api.application.service.FranquiciaService;
import com.franquicias.franquicias_api.domain.Franquicia;
import com.franquicias.franquicias_api.domain.exception.RecursoDuplicadoException;
import com.franquicias.franquicias_api.domain.exception.RecursoNoEncontradoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class FranquiciaServiceTest {

    @InjectMocks
    private FranquiciaService franquiciaService;

    @Mock
    private IFranquiciaRepository franquiciaRepository;

    // --- Objetos de Dominio para Pruebas ---
    private Franquicia franquiciaExistente;
    private Franquicia franquiciaNueva;
    private final String ID_EXISTENTE = "id_existente_123";
    private final String NOMBRE_EXISTENTE = "FranquiciaPrueba";
    private final String NOMBRE_NUEVO = "FranquiciaNueva";

    @BeforeEach
    void setUp() {
        // Objeto que simula una franquicia ya guardada en la DB
        franquiciaExistente = new Franquicia();
        franquiciaExistente.setId(ID_EXISTENTE);
        franquiciaExistente.setNombre(NOMBRE_EXISTENTE);

        // Objeto que simula una franquicia lista para ser guardada
        franquiciaNueva = new Franquicia();
        franquiciaNueva.setNombre(NOMBRE_NUEVO);
    }

    // ----------------------------------------------------------------------
    // 2. Pruebas de Creación (Criterio 2)
    // ----------------------------------------------------------------------

    @Test
    void crearFranquicia_Exito() {
        // Simular: 1. No existe el nombre. 2. Se guarda con éxito.
        when(franquiciaRepository.findByNombre(NOMBRE_NUEVO)).thenReturn(Mono.empty());
        when(franquiciaRepository.save(any(Franquicia.class))).thenReturn(Mono.just(franquiciaNueva));

        StepVerifier.create(franquiciaService.crearFranquicia(franquiciaNueva))
                .assertNext(franquicia -> {
                    assertEquals(NOMBRE_NUEVO, franquicia.getNombre());
                })
                .verifyComplete();

        // Verificar que el repositorio fue llamado correctamente
        verify(franquiciaRepository, times(1)).findByNombre(NOMBRE_NUEVO);
        verify(franquiciaRepository, times(1)).save(franquiciaNueva);
    }

    @Test
    void crearFranquicia_FallaNombreDuplicado() {
        // Simular: El nombre ya existe en la DB.
        when(franquiciaRepository.findByNombre(NOMBRE_EXISTENTE)).thenReturn(Mono.just(franquiciaExistente));

        Franquicia duplicada = new Franquicia();
        duplicada.setNombre(NOMBRE_EXISTENTE); // Intentamos guardar el nombre que ya existe

        StepVerifier.create(franquiciaService.crearFranquicia(duplicada))
                .verifyError(RecursoDuplicadoException.class); // Esperamos la excepción de conflicto (409)

        // Verificar que NO se llamó a save()
        verify(franquiciaRepository, never()).save(any());
    }

    // ----------------------------------------------------------------------
    // 3. Pruebas de Búsqueda por ID (find by ID)
    // ----------------------------------------------------------------------

    @Test
    void findById_Exito() {
        // Simular: El repositorio devuelve el objeto
        when(franquiciaRepository.findById(ID_EXISTENTE)).thenReturn(Mono.just(franquiciaExistente));

        StepVerifier.create(franquiciaService.findById(ID_EXISTENTE))
                .assertNext(franquicia -> {
                    assertEquals(ID_EXISTENTE, franquicia.getId());
                    assertEquals(NOMBRE_EXISTENTE, franquicia.getNombre());
                })
                .verifyComplete();
    }

    @Test
    void findById_FallaNoEncontrado() {
        // Simular: El repositorio devuelve Mono.empty()
        when(franquiciaRepository.findById(anyString())).thenReturn(Mono.empty());

        StepVerifier.create(franquiciaService.findById("id_inexistente"))
                .verifyError(RecursoNoEncontradoException.class); // Esperamos la excepción de no encontrado (404)
    }

    // ----------------------------------------------------------------------
    // 4. Pruebas de Búsqueda por Nombre (find by Nombre)
    // ----------------------------------------------------------------------

    @Test
    void findByNombre_Exito() {
        // Simular: El repositorio devuelve el objeto
        when(franquiciaRepository.findByNombre(NOMBRE_EXISTENTE)).thenReturn(Mono.just(franquiciaExistente));

        StepVerifier.create(franquiciaService.findByNombre(NOMBRE_EXISTENTE))
                .assertNext(franquicia -> {
                    assertEquals(NOMBRE_EXISTENTE, franquicia.getNombre());
                })
                .verifyComplete();
    }

    @Test
    void findByNombre_FallaNoEncontrado() {
        // Simular: El repositorio devuelve Mono.empty()
        when(franquiciaRepository.findByNombre(anyString())).thenReturn(Mono.empty());

        StepVerifier.create(franquiciaService.findByNombre("NombreInexistente"))
                .verifyError(RecursoNoEncontradoException.class); // Esperamos la excepción de no encontrado (404)
    }

    @Test
    void crearFranquicia_FallaNombreVacio() {
        // 1. Caso de prueba: Objeto nulo
        StepVerifier.create(franquiciaService.crearFranquicia(null))
                .verifyError(IllegalArgumentException.class);

        // 2. Caso de prueba: Nombre vacío
        Franquicia franquiciaVacia = new Franquicia();
        franquiciaVacia.setNombre("");

        StepVerifier.create(franquiciaService.crearFranquicia(franquiciaVacia))
                .verifyError(IllegalArgumentException.class);

        // Asegurarse que el repositorio no fue tocado
        verify(franquiciaRepository, never()).findByNombre(any());
        verify(franquiciaRepository, never()).save(any());
    }
}