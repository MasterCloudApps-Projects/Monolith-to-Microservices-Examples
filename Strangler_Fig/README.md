# Strangler Fig

Vamos a proceder a la realización y explicación del patrón Strangler Fig, que consiste en ir migrando de forma incremental y gradual las funcionalidades específicas situadas dentro del monolito a microservicios independientes.

> Todos los proyectos tienen configurado un swagger para poder realizar peticiones:
`http://localhost:${PORT}/swagger-ui/`

El patrón se divide en la aplicación de 3 pasos:
1. Aplicación monolítica, las peticiones y funcionalidades se responden dentro del mismo.
2. Implementación de la funcionalidad en un nuevo microservicio.
3. Con su nueva implementación lista, migramos las peticiones del monolito al microservicio. Se mantiene la funcionalidad en el monolito por si hay que hacer rollback y redireccionamos las llamadas de la funcionalidad extraída al nuevo microservicio.
![alt text](3.1_strangler_fig_pattern.png)

Vamos a aplicar el patrón en diferentes ejemplos con los tres pasos explicados anteriormente.

## **Ejemplo 1. Extracción de funcionalidad independiente**
Para este ejemplo vamos a utilizar un ejemplo con HTTP Proxy (ng-inx) + docker-compose.

En primer lugar debemos configurar el host de nuestra aplicación: `payment.service`. 

Para ello debemos añadir a:

- Linux: `/etc/hosts`
- Windows: `C:/Windows/System32/drivers/etc/hosts`

La siguiente línea: `127.0.0.1 payment.service`

Partimos de un monolito con toda la lógica de la aplicación. Surge la necesidad de extraer una funcionalidad independiente, en este caso ``Inventory`` a un microservicio nuevo.

A continuación, se muestra una imagen del estado inicial y final de la aplicación tras aplicar el patrón.

![alt text](3.2_strangler_fig_pattern.png)

### **Paso 1**
Tenemos nuestra aplicación monolítica, las peticiones y funcionalidades se responden dentro del mismo.
```
> docker-compose -f Ejemplo_1/1_docker-compose.yml up 
```

Podemos probar nuestro monolito a través del proxy:
```
> curl http:\\payment.service/inventory
```

Detenemos el paso 1:
```
> docker-compose -f Ejemplo_1/1_docker-compose.yml down
```
### **Paso 2**
Debemos implementar la funcionalidad en un nuevo microservicio.
El monolito se queda sin cambios, con la misma implementación.
```
> docker-compose -f  Ejemplo_1/2_docker-compose.yml up
```

Las peticiones seguirían llegando a nuestro monolito.
```
> curl http:\\payment.service/inventory
```

Podemos probar ahora nuestro microservicio:
```
> curl http:\\localhost:8081/inventory
```

Detengamos el paso 2:
```
> docker-compose -f Ejemplo_1/2_docker-compose.yml down
```

En este punto, conviven ambas implementaciones de la misma funcionalidad.

### **Paso 3**
Con su nueva implementación lista, debe poder redireccionar las llamadas desde el monolito al nuevo microservicio.

El monolito se queda inmutable, para poder hacer un rollback en caso de ser necesario.
```
> docker-compose -f  Ejemplo_1/3_docker-compose.yml up
```

Nuestro microservicio se queda igual, con la implementación anterior.
```
> curl http:\\localhost:8081/inventory
```

> docker-compose -f  Ejemplo_1/3_docker-compose.yml down

Esquema seguido durante la aplicación del patrón:

![alt text](3.1_strangler_fig_pattern.png)



# Ejemplo 2. Extracción de funcionalidad interna.
Si deseamos aplicar el patrón sobre Payroll, que utiliza una funcionalidad interna en el monolito (User notification), debemos exponer la funcionalidad a través de una API.

![alt text](3.3_strangler_fig_pattern.png)

### **Paso 1**
Tenemos nuestra aplicación monolítica, las peticiones y funcionalidades se responden dentro del mismo.
```
> docker-compose -f Ejemplo_2/1_docker-compose.yml up 
```
Podemos probar nuestro monolito:
`http://localhost:8080/swagger-ui/`

| verb | url                                 |
|------|-------------------------------------|
| POST | http:\\localhost:8080/payroll |

Mandamos en el body:

```
{
  "shipTo": "Juablaz",
  "total": 520
}
```

Se loguea la notificación:
```
Payroll shipped to Juablaz of 520.0
```

Paramos el ejemplo:
```
> docker-compose -f Ejemplo_2/1_docker-compose.yml down
```

### **Paso 2**
Debemos implementar la funcionalidad en un nuevo microservicio que comunicará con el monolito. Por tanto, el monolito debe implementar una API para exponer el servicio de notificaciones de usuario.

```
> docker-compose -f Ejemplo_2/2_docker-compose.yml up
```
Podemos probar nuestro microservicio:
`http://localhost:8081/swagger-ui/`

| verb | url                                 |
|------|-------------------------------------|
| POST | http:\\localhost:8081/payroll |

Mandamos en el body:

```
{
  "shipTo": "Juablaz",
  "total": 520
}
```

Se loguea la notificación en el monolito:
```
Payroll shipped to Juablaz of 520.0
```

Detengamos el paso 2:

```
> docker-compose -f  Ejemplo_2/2_docker-compose.yml down
```

En este punto, conviven ambas implementaciones de la misma funcionalidad, en este caso de `Payroll`.

### **Paso 3**
Con su nueva implementación lista, podemos quitar la implementación antigua del monolito y utilizar solamente el microservicio para la funcionalidad de `Payroll`.

```
> docker-compose -f  Ejemplo_2/3_docker-compose.yml up


> curl http:\\localhost:8080/payroll
Retorna 404 ERROR.
```

Nuestro microservicio se queda igual, con la implementación anterior.
| verb | url                                 |
|------|-------------------------------------|
| POST | http:\\localhost:8081/payroll |

Mandamos en el body:

```
{
  "shipTo": "Juablaz",
  "total": 520
}
```

Se loguea la notificación en el monolito:
```
Payroll shipped to Juablaz of 520.0
```

> docker-compose -f  Ejemplo_2/3_docker-compose.yml down


# Ejemplo 3. Uso de HTTP Proxy.
https://dzone.com/articles/how-to-nginx-reverse-proxy-with-docker-compose
Ejemplo con HTTP Proxy(ng-inx) + docker-compose.
Con el ejemplo de payroll, se sugiere el uso de un proxy en 3 etapas.
1. Añadir un proxy que permita que todas las peticiones vayan al monolito.
2. Con el proxy activo, realizamos la extracción de nuestro microservicio. Sugiere el uso de múltiples pasos:
    - Implementar el microservicio vacío, sin funcionalidad retornando 501 Not Implemented. Sugiere incluso ponerlo en producción para poder probarlo.
    - Añadir la funcionalidad al microservicio
3. Mover las peticiones del monolito al microservicio. Si hay un error podemos redirigir las peticiones de nuevo al monolito.

![alt text](3.10_strangler_fig_pattern.png)

Configuración de host:
`C:/Windows/System32/drivers/etc/hosts`


> docker-compose -f  Ejemplo_3/1_docker-compose.yml up


TODO Fix que funcione.

# Ejemplo 5. Interceptación de mensajes.
Tenemos un monolito que recibe mensajes a través de una cola.


<h3>Continuará...</h3>

# Ejemplo 3. Extracción de User Notification.
Si deseamos extraer la funcionalidad de User Notification, al llamarse desde diferentes partes del monolito no podemos redirigir las llamadas fuera del sistema. Para ello deberemos utilizar al patrón Branch By Abstraction.

# COMANDOS ÚTILES:

> docker stop $(docker ps -a -q)

> docker rm $(docker ps -a -q)

# Enlaces de interes:

> https://github.com/javieraviles/split-the-monolith
> https://www.it-swarm-es.com/es/nginx/docker-nginx-proxy-como-enrutar-el-trafico-un-contenedor-diferente-utilizando-la-ruta-y-no-el-nombre-de-host/828289465/