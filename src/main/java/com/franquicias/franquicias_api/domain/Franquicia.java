package com.franquicias.franquicias_api.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;
import java.util.ArrayList;

// Mapeo a la colección de MongoDB
@Document(collection = "franquicias")
@Data                    // ← Genera getters, setters, toString, equals, hashCode
@NoArgsConstructor       // ← Constructor vacío (necesario para MongoDB/Jackson)
@AllArgsConstructor      // ← Constructor con todos los campos (útil)
public class Franquicia {

    @Id
    private String id;

    private String nombre;

    // Lista de sucursales que pertenecen a esta franquicia.
    private List<Sucursal> sucursales = new ArrayList<>();

    // Constructor personalizado para crear una nueva franquicia (sin ID)
    // Este constructor adicional es útil para crear objetos sin ID
    public Franquicia(String nombre, List<Sucursal> sucursales) {
        this.nombre = nombre;
        this.sucursales = sucursales != null ? sucursales : new ArrayList<>();
    }

    // Metodo para añadir una sucursal con validación.
    public void agregarSucursal(Sucursal sucursal) {
        if (sucursal != null) {
            this.sucursales.add(sucursal);
        }
    }
}