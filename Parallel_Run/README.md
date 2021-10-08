# Parallel Run

En los patrones anteriormente estudiados `Strangler Fig` y `Branch By Abstraction`, teníamos la posibilidad de que coexistiera la versión antigua y nueva de la funcionalidad, pero sólamente una de ellas se activaba en un momento concreto.

Este patrón, `Parallel Run` en lugar de llamar a la implementación antigua o nueva, llamamos a ambas, lo que nos permite comparar los resultados para asegurarnos de que sean equivalentes.

![alt text](3.30_parallel_run.png)

Utiliza la técnica de `Dark Launching`, implementar una nueva funcionalidad pero que sea invisible para los usuarios. `Parallel Run` es una forma de implementar esta técnica, ya que la nueva funcionalidad es invisible para el usuario.


Vamos a partir para los diferentes ejemplos de la solución anterior del patrón `Branch by Abstraction`:

## Enunciado.
Vamos a partir del ejemplo anterior de `Branch By Abstraction` modificando un poco el código para hacer las dos peticiones simultáneas:

```
> docker-compose -f Enunciado/docker-compose.yml up 
```

```
curl -v -H "Content-Type: application/json" -d '{"shipTo":"Juablaz","total":320}' localhost:8080/payroll
```


## **Ejemplo 1. Usando Spies**

![alt text](3.31_parallel_run.png)

Tener una implementación fake en el MS, que no envíe el mail, pero registra "como que se hubiera enviado".

Añadir base de datos que guarda: Destino y contenido, luego comparamos ambos registros, marcamos elementos como "consumidos".

Creamos las BBDD tanto para el monolito como para el Microservicio:
```
docker run --name postgresMono -p 5433:5432 -e POSTGRES_PASSWORD=postgres -d postgres
```
```
docker run --name postgresMicro -p 5434:5432 -e POSTGRES_PASSWORD=postgres -d postgres
```

Una vez que tenemos lanzadas las 2 BBDD, lanzamos primero `1_parallel_run_notification_ms` seguidamente `1_parallel_run_monolith` y el batch el ultimo `1_parallel_run_batch_service`

Una vez esta corriendo todo, si queremos comprobar el batch:
```
curl -v  http://localhost:8082/notification/compare
```
Devolvera true or false en caso de tener las BBDD equitativas, al menos equitativas las llamadas NO Consumidas.

- docker compose por el momento no funciona. PROBAR BIEN EL DEPENDSON

## **Ejemplo 2. Github Scientist**

https://github.com/rawls238/Scientist4J

Lanzamos un ejemplo de esta libreria comparadora, equitativo al Diferencia(falta por subir un ejemplo) el cual hace una llamada a la funcion monolitica y otra a la nueva, creandote unas metricas comparativas que ayudan a ver si te sirve o no la funcionalidad implementada.

Esta actualmente en una clase TEST la cual testea las dos llamadas para traerte 1 notificacion

-Falta implementar Diferencia


## **Ejemplo 3. Canary Releasing**


### **Paso 1**
Lanzar una versión Canary para un subconjunto de usuarios, por si se produce algún problema sólo un pequeño grupo de usuarios se verán afectados.

Hemos configurado un nginx como `Load Balancer` que nos permite balancear la carga utilizando pesos.

```
> docker-compose -f Ejemplo_3/1_docker-compose.yml up 

> docker-compose -f Ejemplo_3/1_docker-compose-proxy.yml up -d
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
> docker-compose -f Ejemplo_3/2_docker-compose.yml up 
```

Podemos probarla utilizando peticiones directas al monolito v2 y al microservicio

```
> curl -v -H "Content-Type: application/json" -d '{"shipTo":"Juablaz","total":320}' localhost:8082/payroll
```

### **Paso 3**
Vamos a migrar poco a poco las peticiones.

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
> docker-compose -f Ejemplo_3/3_docker-compose-proxy.yml up 
```

Podemos realiar varias peticiones para verificar de forma aproximada los pesos:

```
curl payment.service/inventory
```

En caso de cualquier problema siempre se puede hacer un rollback y redirigir de nuevo las peticiones al monolito inicial.
```
> docker-compose -f  Ejemplo_3/1_docker-compose-proxy.yml up
```