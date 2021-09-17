# Strangler Fig

Vamos a proceder a la realización y explicación del patrón Strangler Fig, que consiste en ir migrando de forma incremental y gradual las funcionalidades específicas situadas dentro del monolito a microservicios independientes.

> Todos los proyectos tienen configurado un swagger para poder realizar peticiones:
`localhost:${PORT}/swagger-ui/`

El patrón se divide en la aplicación de 3 pasos:
1. Aplicación monolítica, las peticiones y funcionalidades se responden dentro del mismo.
2. Implementación de la funcionalidad en un nuevo microservicio.
3. Con su nueva implementación lista, migramos las peticiones del monolito al microservicio. Se mantiene la funcionalidad en el monolito por si hay que hacer rollback y redireccionamos las llamadas de la funcionalidad extraída al nuevo microservicio.

![alt text](3.1_strangler_fig_pattern.png)

Vamos a aplicar el patrón en diferentes ejemplos con los tres pasos explicados anteriormente.

Para la realización de cada ejemplo utilizaremos un HTTP Proxy (ng-inx) y docker.

## **Ejemplo 1. Extracción de funcionalidad independiente**

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
> curl payment.service/inventory
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
> curl payment.service/inventory
```

Podemos probar ahora nuestro microservicio llamándolo directamente y no a través del proxy:
```
> curl localhost:8081/inventory
```

Detengamos el paso 2:
```
> docker-compose -f Ejemplo_1/2_docker-compose.yml down
```

### **Paso 3**
Con su nueva implementación lista, procedemos a redireccionar las llamadas desde el monolito al nuevo microservicio.

En caso de cualquier problema siempre se puede hacer un rollback y redirigir de nuevo las peticiones al monolito.
```
> docker-compose -f  Ejemplo_1/3_docker-compose.yml up
```

Nuestro microservicio se queda igual, con la implementación anterior.
```
> curl payment.service/inventory
```
Ahora la respuesta contará con un prefijo ``[MS]`` que hemos añadido a los datos dados de alta en el microservicio.

> docker-compose -f  Ejemplo_1/3_docker-compose.yml down


# Ejemplo 2. Extracción de funcionalidad interna .
Si deseamos aplicar el patrón sobre ``Payroll``, que utiliza una funcionalidad interna en el monolito ``User notification``, debemos dicha funcionalidad interna al exterior a través de un endpoint.

![alt text](3.3_strangler_fig_pattern.png)

Se recomienda en el caso de no disponer de un proxy añadirlo para el desarrollo de este ejemplo, añadiríamos una capa entre medias de la petición para seleccionar el destino de la misma. 

¿Cómo encaja esto en nuestros 3 pasos?:

1. Añadir un proxy que permita que todas las peticiones vayan al monolito.
2. Con el proxy activo, realizamos la extracción de nuestro microservicio. Sugiere el uso de múltiples pasos:
    - Implementar el microservicio vacío, sin funcionalidad retornando ``501 Not Implemented``. Se recomienda llevarlo a producción para familiarizarnos con el proceso de despliegue.
    - Añadir la funcionalidad al microservicio.
3. Mover las peticiones del monolito al microservicio de forma progresiva. Si hay un error podemos redirigir las peticiones de nuevo al monolito.

![alt text](3.10_strangler_fig_pattern.png)

### **Paso 1**
Tenemos nuestra aplicación monolítica, las peticiones y funcionalidades se responden dentro del mismo.

```
> docker-compose -f Ejemplo_2/1_docker-compose.yml up 
```
Podemos probar nuestro monolito:
```
> curl -v -H "Content-Type: application/json" -d '{"shipTo":"Juablaz","total":120}' payment.service/payroll
```

Se loguea en la notificación:
```
Payroll shipped to Juablaz of 120.0
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

```
> curl -v -H "Content-Type: application/json" -d '{"shipTo":"Juablaz2","total":220}' localhost:8081/payroll
```

Se loguea la notificación en el monolito, por lo tanto la comunicación es correcta:
```
Payroll shipped to Juablaz2 of 220.0
```

Detengamos el paso 2:

```
> docker-compose -f  Ejemplo_2/2_docker-compose.yml down
```


### **Paso 3**
Con la nueva implementación lista, redirigimos las peticiones al monolito de la funcionalidad de `Payroll`.

```
> docker-compose -f  Ejemplo_2/3_docker-compose.yml up
```

Podemos probar nuestra aplicación:
```
> curl -v -H "Content-Type: application/json" -d '{"shipTo":"Juablaz3","total":320}' payment.service/payroll
```

Se loguea la notificación en el monolito:
```
Payroll shipped to Juablaz of 320.0
```

¿Cómo sabemos si ha ido a través del monolito o del microservicio?
Hagamos una petición ``GET`` de ``Payroll``.
```
> curl payment.service/payroll
```
Vemos que en la respuesta aparece el tag ``[MS]`` en los datos retornados, por lo que la respuesta es del microservicio.

```
> docker-compose -f  Ejemplo_2/3_docker-compose.yml down
```

# Ejemplo 3. Interceptación de mensajes.
Tenemos un monolito que recibe mensajes a través de una cola.


<h3>Continuará...</h3>

# Ejemplo 4. Extracción de User Notification.
Nos falta por probar la extracción de la funcionalidad de ``User Notification``, a necesitar interceptar las peticiones dentro del monolito, no podemos redirigir las llamadas fuera del sistema. Para ello deberemos utilizar al patrón ``Branch By Abstraction``.

# COMANDOS ÚTILES:

> docker stop $(docker ps -a -q)

> docker rm $(docker ps -a -q)

# Enlaces de interes:

> https://github.com/javieraviles/split-the-monolith

> https://www.it-swarm-es.com/es/nginx/docker-nginx-proxy-como-enrutar-el-trafico-un-contenedor-diferente-utilizando-la-ruta-y-no-el-nombre-de-host/828289465/