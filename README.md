franquicias-api: Prueba Técnica Backend Developer
Este proyecto es una API RESTful desarrollada con Spring Boot WebFlux (Programación Reactiva) para gestionar franquicias, sucursales y productos, utilizando MongoDB Atlas como sistema de persistencia en la nube.

Criterios Cumplidos y Puntos Extra
La solución cumple con todos los criterios de aceptación principales y abarca la mayoría de los puntos extra solicitados:

    Criterio / Plus,Estado,Descripción
    Arquitectura,✅ Reactiva / Clean,Utiliza Spring WebFlux para reactividad y sigue una estructura de capas limpia.
    Persistencia,✅ MongoDB Atlas,Usa MongoDB como base de datos en la nube.
    Contenerización,✅ Docker,Se incluye un Dockerfile para empaquetar la aplicación.
    Despliegue en Nube,✅ Railway,La solución está desplegada y funcional en la plataforma Railway.
    Tests,⚠️ Parcial,"Incluye Unit Tests, aunque la cobertura para la lógica compleja (Stock) es un trabajo pendiente."

Estructura del Proyecto
El proyecto sigue una estructura simple basada en capas de Spring Boot Reactive:

    src/
    └── main/
        └── java/
            └── com/
                └── franquicias/
                    ├── api/          # Controladores (WebFlux)
                    ├── domain/       # Modelos de Dominio (Entidades de MongoDB)
                    ├── service/      # Lógica de Negocio y Reglas
                    └── repository/   # Acceso a Datos (Spring Data Reactive MongoDB)

Ejecución Local con Docker Compose
Para ejecutar la aplicación localmente (incluyendo una instancia de MongoDB en Docker), asegúrate de tener Docker y Docker Compose instalados. en application.properties

spring.application.name=franquicias-api

    # --- Configuracion de Conexion a MongoDB (Reactive) ---
    
    # Host: Docker Compose
    spring.mongodb.host=mongo
    # Puerto por defecto de MongoDB
    spring.mongodb.port=27017
    # Nombre de la base de datos que se creara
    spring.mongodb.database=franquiciasdb
    
    spring.mongodb.uri=${MONGO_URI}

Levantar la Aplicación
Ejecuta el siguiente comando en la raíz del proyecto para construir la imagen y levantar la aplicación junto con la base de datos de MongoDB:

docker-compose up --build

Verificar
La aplicación estará disponible en: http://localhost:8080

Despliegue y URL de Producción
La solución está actualmente desplegada en Railway, conectada a MongoDB Atlas (el servicio de persistencia en la nube).


Endpoint locales

    All Franquicia Get - http://localhost:8080/franquicias
    Busucar franquicia por ID - Get - http://localhost:8080/franquicias/{diFranquicia}
    Buscar franquicia por nombre - Get - http://localhost:8080/franquicias/nombre/{nombreFranquicia}
    Buscar mayor cantidad producto - Get - http://localhost:8080/franquicias/{diFranquicia}/productos-max-stock
    Crear Franquicia - Post - http://localhost:8080/franquicias
    Body
    {
    "nombre": "Global Burgers Corp3",
    "sucursales": [
    {
    "nombre": "Sucursal Central (Centro)3",
    "productos": [
    {
    "nombre": "Burger Clásica3",
    "stock": 50
    },
    {
    "nombre": "Fries Max3",
    "stock": 120
    }
    ]
    },
    {
    "nombre": "Sucursal Norte (Mall)3",
    "productos": []
    }
    ]
    }
    Crear Sucursal - Post - http://localhost:8080/franquicias/{diFranquicia}/sucursales
    Body
    {
    "nombre": "Sucursal Sur (Aeropuerto)",
    "productos": [
    {
    "nombre": "Ensalada César",
    "stock": 150
    }
    ]
    }
    Crear Producto - Post - http://localhost:8080/franquicias/{diFranquicia}/sucursales/{nombreSucursal}/productos
    Body
    {"nombre": "Sancocho", "stock": 30}
    Eliminar Producto por Sucursal - Delete - http://localhost:8080/franquicias/{diFranquicia}/sucursales/{nombreSucursal}/productos/{nombreProducto}
    Eliminar mismo producto de todas las sucursales - Delete - http://localhost:8080/franquicias/{diFranquicia}/productos/{nombreProducto}
    Actualizar cantidad Producto - Put - http://localhost:8080/franquicias/{diFranquicia}/sucursales/{nombreSucursal}/productos/{nombreProducto}/stock
    Body - {"stock": 500}
    Actualizar Nombre Franquicia - Put - http://localhost:8080/franquicias/{diFranquicia}
    Body - {"nombre": "Global Burgers Corp la original"}
    Actualizar Nombre Secursal - Put - http://localhost:8080/franquicias/{diFranquicia}/sucursales/{nombreSucursal}
    Body - {"nombre": "Sucursal Sur (Aeropuerto)2"}
    Actualizar Nombre Producto - Put - http://localhost:8080/franquicias/{diFranquicia}/sucursales/{sucursalNombre}/productos/productosNombre
    Body - {"nombre": "hamburgejas al vapor"}

Endpoint Web

    All Franquicia Get - https://franquicias-api-production.up.railway.app/franquicias
    Busucar franquicia por ID - Get - https://franquicias-api-production.up.railway.app/franquicias/{diFranquicia}
    Buscar franquicia por nombre - Get - https://franquicias-api-production.up.railway.app/franquicias/nombre/{nombreFranquicia}
    Buscar mayor cantidad producto - Get - https://franquicias-api-production.up.railway.app/franquicias/{diFranquicia}/productos-max-stock
    Crear Franquicia - Post - https://franquicias-api-production.up.railway.app/franquicias
    Body
    {
    "nombre": "Global Burgers Corp3",
    "sucursales": [
    {
    "nombre": "Sucursal Central (Centro)3",
    "productos": [
    {
    "nombre": "Burger Clásica3",
    "stock": 50
    },
    {
    "nombre": "Fries Max3",
    "stock": 120
    }
    ]
    },
    {
    "nombre": "Sucursal Norte (Mall)3",
    "productos": []
    }
    ]
    }
    Crear Sucursal - Post - https://franquicias-api-production.up.railway.app/franquicias/{diFranquicia}/sucursales
    Body
    {
    "nombre": "Sucursal Sur (Aeropuerto)",
    "productos": [
    {
    "nombre": "Ensalada César",
    "stock": 150
    }
    ]
    }
    Crear Producto - Post - https://franquicias-api-production.up.railway.app/franquicias/{diFranquicia}/sucursales/{nombreSucursal}/productos
    Body
    {"nombre": "Sancocho", "stock": 30}
    Eliminar Producto por Sucursal - Delete - https://franquicias-api-production.up.railway.app/franquicias/{diFranquicia}/sucursales/{sucursalNombre}/productos/{nombreProducto}
    Eliminar mismo producto de todas las sucursales - Delete - https://franquicias-api-production.up.railway.app/franquicias/{diFranquicia}/productos/{nombreProducto}
    Actualizar cantidad Producto - Put - https://franquicias-api-production.up.railway.app/franquicias/{diFranquicia}/sucursales/{nombreSucursal}/productos/{nombreProducto}/stock
    Body - {"stock": 500}
    Actualizar Nombre Franquicia - Put - https://franquicias-api-production.up.railway.app/franquicias/{diFranquicia}
    Body - {"nombre": "Global Burgers Corp la original"}
    Actualizar Nombre Secursal - Put - https://franquicias-api-production.up.railway.app/franquicias/{diFranquicia}/sucursales/{nombreScurusal}
    Body - {"nombre": "Sucursal Sur (Aeropuerto)2"}
    Actualizar Nombre Producto - Put - https://franquicias-api-production.up.railway.app/franquicias/{diFranquicia}/sucursales/{nombreScurusal}/productos/{nombreProdcuto}
    Body - {"nombre": "hamburgejas al vapor"}