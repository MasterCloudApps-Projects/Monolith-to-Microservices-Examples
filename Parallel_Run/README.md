# Parallel Run

En los patrones anteriormente estudiados `Strangler Fig` y `Branch By Abstraction`, teníamos la posibilidad de que coexistiera la versión antigua y nueva de la funcionalidad, pero sólamente una de ellas se activava en un momento concreto.

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
docker run --network host --name postgresMono -e POSTGRES_PASSWORD=postgres -d postgres
```
```
docker run --network host --name postgresMicro -e POSTGRES_PASSWORD=postgres -d postgres
```




## **Ejemplo 2. Github Scientist**

https://github.com/rawls238/Scientist4J


## **Ejemplo 3. Canary Releasing**
Lanzar una versión Canary para un subconjunto de usuarios, por si se produce algún problema sólo un pequeño grupo de usuarios se verán afectados.
Llamaríamos a ambas implementaciones.

Nginx as `Load Balancer`.

Hemos configurado un nginx como `Load Balancer` que nos permite balancear la carga utilizando pesos.
Facilitamos diferentes archivos de configuración que se irían aplicando según viéramos que la versión "Canary" con nuestro microservicio fuera funcionando:
- nginx_0_100
- nginx_10_90
- nginx_20_80
- ...
- nginx_80_20
- nginx_90_10
- nginx_100_0


```
> docker-compose -f Ejemplo_3/3_docker-compose.yml up 
```

Hagamos varias peticiones:

```
curl payment.service/inventory
```

