# Parallel Run

En los patrones anteriormente estudiados `Strangler Fig` y `Branch By Abstraction`, teníamos la posibilidad de que coexistiera la versión antigua y nueva de la funcionalidad, pero sólamente una de ellas se activaba en un momento concreto.

Este patrón, `Parallel Run` en lugar de llamar a la implementación antigua o nueva, llamamos a ambas, lo que nos permite comparar los resultados para asegurarnos de que sean equivalentes.

![alt text](3.30_parallel_run.png)

Utiliza la técnica de `Dark Launching`, implementar una nueva funcionalidad pero que sea invisible para los usuarios. `Parallel Run` es una forma de implementar esta técnica, ya que la nueva funcionalidad es invisible para el usuario.

## **Ejemplo 1. Usando Spies**

### **Paso 1**
Partimos de nuestra aplicación monolítica que loguea notificaciones al usuario.
```
> docker-compose -f Ejemplo_1/1_docker-compose.yml up 

> docker-compose -f Ejemplo_1/1_docker-compose-proxy.yml up -d
```

Probamos que todo funciona correctamente:

```
curl -v -H "Content-Type: application/json" -d '{"shipTo":"Juablaz","total":320}' payment.service/payroll
```

### **Paso 2**
En este paso, tenemos que sacar una versión 2 del monolito, que registre en BBDD la notificación al usuario.
También, debemos desarrollar nuestro microservicio, con una implementación fake, que no envíe la notificación pero registre que dicha notificación se ha enviado puesto que ambas implementaciones van a convivir y no queremos que se dupliquen las notificaciones.

![alt text](3.31_parallel_run.png)

```
> docker-compose -f Ejemplo_1/2_docker-compose.yml up 
```

Podemos probar nuestra nueva implementación del monolito v2:

```
curl -v -H "Content-Type: application/json" -d '{"shipTo":"Juablaz","total":320}' localhost:8082/payroll
```

Con todo desplegado, vamos a migrar las peticiones a la nueva implementación.

```
> docker-compose -f Ejemplo_1/2_docker-compose-proxy.yml up -d
```

Con esto desplegado, periódicamente se realizaría una comparación de los resultados generados por el monolito y el microservicio en nuestro microservicio batch.
Podemos ejecutarlo de forma manual:
```
curl -v  http://localhost:8082/notification/compare
```

Devolvera ``true`` or ``false`` en caso de tener las BBDD equitativas.

### **Paso 3**

Una vez hayamos visto que todo funciona sacaríamos una versión final.

```
> docker-compose -f Ejemplo_1/3_docker-compose.yml up -d
```

```
curl -v -H "Content-Type: application/json" -d '{"shipTo":"Juablaz","total":320}' localhost:8084/payroll
```

```
> docker-compose -f Ejemplo_1/3_docker-compose-proxy.yml up -d
```

```
curl -v -H "Content-Type: application/json" -d '{"shipTo":"Juablaz","total":320}' payment.service/payroll
```

## **Ejemplo 2. Github Scientist**

https://github.com/rawls238/Scientist4J

Lanzamos un ejemplo de esta libreria comparadora, equitativo al Diferencia(falta por subir un ejemplo) el cual hace una llamada a la funcion monolitica y otra a la nueva, creandote unas metricas comparativas que ayudan a ver si te sirve o no la funcionalidad implementada.

Esta actualmente en una clase TEST la cual testea las dos llamadas para traerte 1 notificacion

### **Paso 1**
Partimos de nuestra aplicación monolítica que loguea notificaciones al usuario.
```
> docker-compose -f Ejemplo_1/1_docker-compose.yml up 

> docker-compose -f Ejemplo_1/1_docker-compose-proxy.yml up -d
```

Probamos que todo funciona correctamente:

```
curl -v -H "Content-Type: application/json" -d '{"shipTo":"Juablaz","total":320}' payment.service/payroll
```
### **Paso 3**

Una vez hayamos visto que todo funciona sacaríamos una versión final.

```
> docker-compose -f Ejemplo_1/3_docker-compose.yml up -d
```

```
curl -v -H "Content-Type: application/json" -d '{"shipTo":"Juablaz","total":320}' localhost:8084/payroll
```

```
> docker-compose -f Ejemplo_1/3_docker-compose-proxy.yml up -d
```

```
curl -v -H "Content-Type: application/json" -d '{"shipTo":"Juablaz","total":320}' payment.service/payroll
```

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