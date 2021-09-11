# Strangler Fig

Vamos a proceder a la realización y explicación del patrón Strangler Fig, que consiste en ir migrando de forma incremental y gradual las funcionalidades específicas situadas dentro del monolito a microservicios independientes.

> Todos los proyectos tienen configurado un swagger para poder realizar peticiones:
`http://localhost:${PORT}/swagger-ui/`

El patrón se divide en la aplicación de 3 pasos:
1. Aplicación monolítica, las peticiones y funcionalidades se responden dentro del mismo.
2. Implementación de la funcionalidad en un nuevo microservicio.
3. Con su nueva implementación lista, quitamos la implementación antigua, dejando la funcionalidad restante en el interior del monolito y redireccionamos las llamadas de la funcionalidad extraída al nuevo microservicio.
![alt text](3.1_strangler_fig_pattern.png)

Vamos a ir explicando diferentes casos mediante ejemplos.

## **Ejemplo 1. Extracción de funcionalidad independiente**
Vamos a aplicar el patrón en el siguiente ejemplo con los tres pasos explicados anteriormente. Partimos de un monolito en el puerto `8080` con toda la lógica de la aplicación. Surge la necesidad de extraer la funcionalidad ``Inventory`` a un microservicio independiente nuevo localizado en el puerto `8081`.

A continuación, se muestra una imagen del estado inicial y final de la aplicación tras aplicar el patrón.

![alt text](3.2_strangler_fig_pattern.png)

### **Paso 1**
Tenemos nuestra aplicación monolítica, las peticiones y funcionalidades se responden dentro del mismo.
```
> docker build -t 1_strangler_fig_monolith:v1 Ejemplo_1/1_strangler_fig_monolith/

> docker run --name 1_strangler_fig_monolith -p 8080:8080 1_strangler_fig_monolith:v1
```
Podemos probar nuestro monolito:
```
> curl http:\\localhost:8080/inventory

> docker stop 1_strangler_fig_monolith 
```

### **Paso 2**
Debemos implementar la funcionalidad en un nuevo microservicio.
El monolito se queda sin cambios, con la misma implementación.
```
> docker build -t 2_strangler_fig_monolith:v1 Ejemplo_1/2_strangler_fig_monolith/
> docker run --name 2_strangler_fig_monolith -p 8080:8080 2_strangler_fig_monolith:v1

> curl http:\\localhost:8080/inventory
```

Probemos ahora nuestro microservicio:
```
> docker build -t 2_strangler_fig_inventory_ms:v1 Ejemplo_1/2_strangler_fig_inventory_ms/
> docker run --name 2_strangler_fig_inventory_ms -p 8081:8081 2_strangler_fig_inventory_ms:v1

> curl http:\\localhost:8081/inventory
```
Detengamos el paso 2:

```
> docker stop 2_strangler_fig_monolith 
> docker stop 2_strangler_fig_inventory_ms 
```

En este punto, conviven ambas implementaciones de la misma funcionalidad.

### **Paso 3**
Con su nueva implementación lista, debe poder redireccionar las llamadas desde el monolito al nuevo microservicio.

El monolito se queda sin la funcionalidad de inventory.
```
> docker build -t 3_strangler_fig_monolith:v1 Ejemplo_1/3_strangler_fig_monolith/
> docker run --name 3_strangler_fig_monolith -p 8080:8080 3_strangler_fig_monolith:v1
> curl http:\\localhost:8080/inventory
Retorna 404 ERROR.
```

Nuestro microservicio se queda igual, con la implementación anterior.
```
> docker build -t 3_strangler_fig_inventory_ms:v1 Ejemplo_1/3_strangler_fig_inventory_ms/
> docker run --name 3_strangler_fig_inventory_ms -p 8081:8081 3_strangler_fig_inventory_ms:v1

> curl http:\\localhost:8081/inventory

> docker stop 3_strangler_fig_monolith 
> docker stop 3_strangler_fig_inventory_ms 
```

Esquema seguido durante la aplicación del patrón:

![alt text](3.1_strangler_fig_pattern.png)



# Ejemplo 2. Extracción de funcionalidad interna.
Si deseamos aplicar el patrón sobre Payroll, que utiliza una funcionalidad interna en el monolito (User notification), debemos exponer la funcionalidad a través de una API.
![alt text](3.3_strangler_fig_pattern.png)


# Ejemplo 3. Extracción de User Notification.
Si deseamos extraer la funcionalidad de User Notification, al llamarse desde diferentes partes del monolito no podemos redirigir las llamadas fuera del sistema. Para ello deberemos utilizar al patrón Branch By Abstraction.


# Ejemplo 4. Uso de HTTP Proxy.
https://dzone.com/articles/how-to-nginx-reverse-proxy-with-docker-compose

Ejemplo con HTTP Proxy(ng-inx) + docker-compose.
Con el ejemplo de payroll, se sugiere el uso de un proxy en 3 etapas.
1. Añadir un proxy que permita que todas las peticiones vayan al monolito.
2. Con el proxy activo, realizamos la extracción de nuestro microservicio. Sugiere el uso de múltiples pasos:
    - Implementar el microservicio vacío, sin funcionalidad retornando 501 Not Implemented. Sugiere incluso ponerlo en producción para poder probarlo.
    - Añadir la funcionalidad al microservicio
3. Mover las peticiones del monolito al microservicio. Si hay un error podemos redirigir las peticiones de nuevo al monolito.

![alt text](3.10_strangler_fig_pattern.png)


# Ejemplo 5. Interceptación de mensajes.
Tenemos un monolito que recibe mensajes a través de una cola.


<h3>Continuará...</h3>