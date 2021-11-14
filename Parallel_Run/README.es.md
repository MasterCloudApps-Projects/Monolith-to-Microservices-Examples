# **Parallel Run**

<div align="center">

[![en](https://img.shields.io/badge/lang-en-red.svg)](https://github.com/MasterCloudApps-Projects/Monolith-to-Microservices-Examples/tree/master/Parallel_Run/README.md)
[![es](https://img.shields.io/badge/lang-es-yellow.svg)](https://github.com/MasterCloudApps-Projects/Monolith-to-Microservices-Examples/tree/master/Parallel_Run/README.es.md)
</div>

En los patrones anteriormente estudiados `Strangler Fig` y `Branch By Abstraction`, teníamos la posibilidad de que coexistiera la versión antigua y nueva de la funcionalidad, pero sólamente una de ellas se activaba en un momento concreto.

Este patrón, `Parallel Run` en lugar de llamar a la implementación antigua o nueva, llamamos a ambas, lo que nos permite comparar los resultados para asegurarnos de que sean equivalentes.
<div align="center">

![alt text](3.30_parallel_run.png)
</div>

Utiliza la técnica de `Dark Launching`, implementar una nueva funcionalidad pero que sea invisible para los usuarios. `Parallel Run` es una forma de implementar esta técnica, ya que la nueva funcionalidad es invisible para el usuario.

## **Ejemplo 1. Usando Spies**
____________________________________________________________
### **Paso 1**
Partimos de nuestra aplicación monolítica que loguea notificaciones al usuario.
```
> docker-compose -f Ejemplo_1/1_docker-compose.yml up 

> docker-compose -f Ejemplo_1/1_docker-compose-proxy.yml up -d
```

Probamos que todo funciona correctamente:

```
> curl -v -H "Content-Type: application/json" -d '{"shipTo":"Juablaz","total":320}' payment.service/payroll
```

### **Paso 2**
En este paso, tenemos que sacar una versión 2 del monolito, que registre en BBDD la notificación al usuario.
También, debemos desarrollar nuestro microservicio, con una implementación modificada, que no envíe realmente la notificación pero registre que la registre como que se hubiera enviado (``Spy``). Ambas implementaciones van a convivir y no queremos que se dupliquen las notificaciones.
<div align="center">

![alt text](3.31_parallel_run.png)
</div>

```
> docker-compose -f Ejemplo_1/2_docker-compose.yml up 
```

Podemos probar nuestra nueva implementación del monolito `v2`:

```
> curl -v -H "Content-Type: application/json" -d '{"shipTo":"Juablaz","total":320}' localhost:8082/payroll
```

Con todo desplegado, vamos a migrar las peticiones a la nueva implementación.

```
> docker-compose -f Ejemplo_1/2_docker-compose-proxy.yml up -d
```

En este momento, se registra en la BBDD desde el microservicio y desde el monolito el envío de notificaciones. Tenemos un microservicio con un batch que periódicamente realiza una comparación de los resultados generados.
Este microservicio, devuelve ``true`` or ``false`` en caso de tener las BBDD equitativas.

Hemos habilitado una opción para que podamos ejecutarlo de forma manual:

```
> curl -v  http://localhost:8083/notification/comparation
```

Devuelve ``true`` y loguea:
```
> 2_parallel_run_batch_service      | 2021-10-11 09:49:25.334  INFO 1 --- [nio-8083-exec-2] e.c.m.p.service.UserNotificationService  : ConsumitionsCount: 6 for mono size: 6 and micro size: 6
```

### **Paso 3**

Una vez hayamos visto que la nueva implementación en el microservicio genera los mismos resultados que el monolito, podemos sacar una versión final.

```
> docker-compose -f Ejemplo_1/3_docker-compose.yml up --build
```

Probamos que funcione correctamente:
```
> curl -v -H "Content-Type: application/json" -d '{"shipTo":"Juablaz","total":320}' localhost:8084/payroll
```

Migramos las peticiones a la versión final:
```
> docker-compose -f Ejemplo_1/3_docker-compose-proxy.yml up -d
```

```
> curl -v -H "Content-Type: application/json" -d '{"shipTo":"Juablaz","total":320}' payment.service/payroll
```

<br>

## **Ejemplo 2. Github Scientist**
____________________________________________________________
Existen librerías que permiten comparar resultados de forma muy sencilla. En nuestro caso, vamos a proceder a realizar un ejemplo con:
https://github.com/rawls238/Scientist4J

El ejemplo anterior, la comparación se realizaba en background, a través de nuestro batch, sin embargo, puede ser necesario que implementemos esa lógica en tiempo real. Por contra, esta solución añade latencias y puede no ser eficiente utilizarla en nuestra solución.

También podemos hacer una comparación en background, dejando el hilo principal de la aplicación procesando el flujo de las notificaciones del monolito y realizar una comparación con la librería de forma asíncrona en otro hilo, publicando la respuesta en una BBDD.

Para aplicar este ejemplo, debemos tomar parte de la lógica utilizada en el anterior ejemplo del patrón y tener una versión Spy de nuestro microservicio, para evitar duplicar las notificaciones.

Vamos a realizar un ejemplo de comparación en tiempo real de resultados a través de la librería presentada anteriormente:

### **Paso 1**
Partimos de nuestra aplicación monolítica que loguea notificaciones al usuario.
```
> docker-compose -f Ejemplo_2/1_docker-compose.yml up 

> docker-compose -f Ejemplo_2/1_docker-compose-proxy.yml up -d
```

Probamos que todo funciona correctamente:

```
> curl -v -H "Content-Type: application/json" -d '{"shipTo":"Juablaz","total":320}' payment.service/payroll
```

### **Paso 2**
La librería tiene un uso sencillo, en caso de querer comparar dos operaciones síncronas:

```java
Experiment<Integer> e = new Experiment("foo");
e.run(this::controlFunction, this::candidateFunction);
```

En caso de ser asíncronas:

```java
Experiment<Integer> e = new Experiment("foo");
e.runAsync(this::controlFunction, this::candidateFunction);
```

En nuestro caso, debemos hacer una pequeña modificación al código del monolito y del microservicio, puesto que nuestras operaciones son `void`. Vamos a retornar un ``String`` almacenado previamente para este ejemplo en un ``ConcurrentMap``. Esto nos permite comparar las llamadas newCode/oldCode de lo que nos ha creado la nueva implementacion con la que ya habia:
```java
public Boolean scientistExperiment(Long id) {
        DropwizardMetricsProvider metricRegistry = new DropwizardMetricsProvider();
        Experiment<String> experiment = new Experiment<>("notify", metricRegistry);

        Callable<String> oldCodePath = () -> userNotificationService.getNotify(id);
        Callable<String> newCodePath = () -> userNotificationServiceMS.getNotify(id);
        try {
            experiment.run(oldCodePath, newCodePath);
        } catch (Exception e) {
            System.out.println(e);
        }

        MetricName metricName = MetricName.build("scientist.notify.mismatch");
        Counter result = metricRegistry.getRegistry().getCounters().get(metricName);

        log.info("The ScientistExperiment result with compare oldCode/newCode is " + result.getCount() + " mismatch");
        return result.getCount() == 0;
    }
```

```
> docker-compose -f Ejemplo_2/2_docker-compose.yml up 
```

```
> curl -v -H "Content-Type: application/json" -d '{"shipTo":"Juablaz","total":320}' localhost:8082/payroll
```


Actualizamos a utilizar el proxy:

```
> docker-compose -f Ejemplo_2/2_docker-compose-proxy.yml up -d
```

```
> curl -v -H "Content-Type: application/json" -d '{"shipTo":"Juablaz","total":320}' payment.service/payroll
```


### **Paso 3**

Una vez hayamos visto que la nueva implementación en el microservicio genera los mismos resultados que el monolito, podemos sacar una versión final.

```
> docker-compose -f Ejemplo_2/3_docker-compose.yml up
```

```
> curl -v -H "Content-Type: application/json" -d '{"shipTo":"Juablaz","total":320}' localhost:8084/payroll
```

Migramos las peticiones a la versión final:
```
> docker-compose -f Ejemplo_2/3_docker-compose-proxy.yml up -d
```

```
> curl -v -H "Content-Type: application/json" -d '{"shipTo":"Juablaz","total":320}' payment.service/payroll
```

<br>

## **Ejemplo 3. Diferencia**
____________________________________________________________
Este ejemplo es algo diferente. Realmente `Diferencia` esta montado sobre un proxy que actuaria en nuestro caso como comparador externo. 

### **Paso 1**
Partimos de nuestra aplicación monolítica que loguea notificaciones al usuario.
```
> docker-compose -f Ejemplo_3/1_docker-compose.yml up 

> docker-compose -f Ejemplo_3/1_docker-compose-proxy.yml up -d
```

Probamos que todo funciona correctamente:

```
> curl -v -H "Content-Type: application/json" -d '{"shipTo":"Juablaz","total":320}' payment.service/payroll
```

### **Paso 2**
Primero, tendriamos que inicializar los dos contenedores con sus respectivas proxys:
<div align="center">

![alt text](diferencia_simple.png)
</div>


En el esquema anterior, se puede ver que una petición se multidifunde a dos instancias del Servicio A, siendo una ``V1`` y otra ``V2``. Ambas devuelven una respuesta y luego es comparada por Diferencia. Finalmente, observe que no se devuelve ninguna respuesta sino el resultado (en forma de Código de Estado HTTP).

Dado que Diferencia no devuelve una respuesta real, sino una comparación, significa efectivamente que es necesario utilizar el proxy de Diferencia con fines de prueba (para comprobar la compatibilidad con versiones anteriores) y no como un proxy de producción como Envoy. Esto significa que Diferencia podría utilizarse para realizar pruebas concretas de compatibilidad con versiones anteriores o para utilizar la técnica de Traffic Shadowing/Mirroring.

Para ello, ponemos a correr independientemente tanto nuestro monolito como nuestra nueva implementacion: 

```
> docker-compose -f Ejemplo_3/2_docker-compose.yml up --build
```
En nuestro caso nos queda en dos endpoint distintos:

```
Monolith: payment.service;
MicroService: payment-ms.service;
```

Y finalmente ponemos el `Diferencia` apuntando a los dos endpoint anteriores, quedara el mismo como un proxy. Realizaremos una peticion al `Diferencia`, el cual replicara hacia los dos servicios esa request. Entonces, cuando se devuelva la respuesta de cada instancia, `Diferencia` comprobará si ambas respuestas son similares, y si es el caso, entonces las dos implementaciones podrían considerarse compatibles y la implementación de la nueva versión está libre de regresión.

```
docker run --rm -ti -p 8080:8080 -p 8081:8081 -p 8082:8082 lordofthejars/diferencia:0.6.0 start -c localhost:8083 -p localhost:8084
```

TODO: Las URLs no salen fuera del docker `Diferencia` He rpibado mil maneras.... vamos no consigo conectarlo de dentro afuera ideas? Documentacion.....

https://lordofthejars.github.io/diferencia-docs-site/diferencia/0.6.0/index.html

### **Paso 3**
Una vez hayamos visto que la nueva implementación en el microservicio genera los mismos resultados que el monolito, podemos sacar una versión final.

```
> docker-compose -f Ejemplo_2/3_docker-compose.yml up -d
```

```
> curl -v -H "Content-Type: application/json" -d '{"shipTo":"Juablaz","total":320}' localhost:8084/payroll
```

Migramos las peticiones a la versión final:
```
> docker-compose -f Ejemplo_2/3_docker-compose-proxy.yml up -d
```

```
> curl -v -H "Content-Type: application/json" -d '{"shipTo":"Juablaz","total":320}' payment.service/payroll
```

<br>

## **Ejemplo 4. Canary Releasing**
____________________________________________________________
### **Paso 1**
Lanzar una versión Canary para un subconjunto de usuarios, por si se produce algún problema sólo un pequeño grupo de usuarios se verán afectados.

Hemos configurado un nginx como `Load Balancer` que nos permite balancear la carga utilizando pesos.

```
> docker-compose -f Ejemplo_4/1_docker-compose.yml up --build

> docker-compose -f Ejemplo_4/1_docker-compose-proxy.yml up -d
```

La configuración por defecto es la siguiente:
```
upstream loadbalancer {
  server 1-parallel-run-monolith:8080 weight=10;
}
server {
  listen 8080;
  server_name payment.service;

  location / {
    proxy_pass http://loadbalancer;
  }
}
```

Toda la carga de peticiones irán a nuestro monolito.

Nuestra aplicación es accesible a través de:

```
> curl payment.service/payroll
```

Todas las peticiones van al monolito.


### **Paso 2**
Lanzamos una nueva versión de la aplicación.

```
> docker-compose -f Ejemplo_4/2_docker-compose.yml up --build
```

Podemos probarla utilizando peticiones directas al monolito v2 y al microservicio

```
> curl -v -H "Content-Type: application/json" -d '{"shipTo":"Juablaz","total":320}' localhost:8082/payroll
```

### **Paso 3**
Vamos a migrar poco a poco las peticiones.

Pero antes, vamos a ejecutar nuestro generador de peticiones:
```
> cd ..\Request Generator\
```

```
> node request_generator.js http://payment.service/payroll 1000 payroll.json
```

Tiempo en segundos que estaremos lanzando peticiones, el json que manda y a qué petición.

```
upstream loadbalancer {
  server 1-parallel-run-monolith:8080 weight=9;
  server 2-parallel-run-monolith:8080 weight=1;
}
server {
  listen 80;
  server_name payment.service;
  location / {
    proxy_pass http://loadbalancer;
  }
}
```

Facilitamos diferentes archivos de configuración que se irían aplicando según viéramos que la versión "Canary" con nuestro microservicio fuera funcionando:
- nginx_0_100
- nginx_10_90
- nginx_20_80
- ...
- nginx_80_20
- nginx_90_10
- nginx_100_0

Arrancamos una nueva configuración del nginx:

```
> docker-compose -f Ejemplo_4/3_docker-compose-proxy.yml up -d
```

Podemos realiar varias peticiones para verificar de forma aproximada los pesos:

```
curl payment.service/inventory
```

En caso de cualquier problema siempre se puede hacer un rollback y redirigir de nuevo las peticiones al monolito inicial.
```
> docker-compose -f  Ejemplo_4/1_docker-compose-proxy.yml up -d
```

