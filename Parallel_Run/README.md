# Parallel Run

En los patrones anteriormente estudiados `Strangler Fig` y `Branch By Abstraction`, teníamos la posibilidad de que coexistiera la versión antigua y nueva de la funcionalidad, pero sólamente una de ellas se activava en un momento concreto.

Este patrón, `Parallel Run` en lugar de llamar a la implementación antigua o nueva, llamamos a ambas, lo que nos permite comparar los resultados para asegurarnos de que sean equivalentes.

![alt text](3.30_parallel_run.png)

Utiliza la técnica de `Dark Launching`, implementar una nueva funcionalidad pero que sea invisible para los usuarios. `Parallel Run` es una forma de implementar esta técnica, ya que la nueva funcionalidad es invisible para el usuario.

Vamos a partir para los diferentes ejemplos de la solución anterior del patrón `Branch by Abstraction`:

## Enunciado.
Recordemos brevemente el estado de la aplicación:

```
> docker-compose -f Enunciado/docker-compose.yml up 
```

```
curl -v -H "Content-Type: application/json" -d '{"shipTo":"Juablaz","total":320}' localhost:8080/payroll
```

Se loguea en el monolito:

``1_parallel_run_monolith         | 2021-09-24 11:02:32.470  INFO 1 --- [nio-8080-exec-1] e.c.m.p.s.i.UserNotificationServiceImpl  : Payroll 3 shipped to Juablaz of 320.0``

Si entramos en `http://localhost:8080/ff4j-web-console` y cambiamos el flag, se realizará a través del microservicio.

```
curl -v -H "Content-Type: application/json" -d '{"shipTo":"Juablaz","total":320}' localhost:8080/payroll
```

``1_parallel_run_notification_ms  | 2021-09-24 11:02:39.123  INFO 1 --- [nio-8081-exec-1] e.c.m.p.service.UserNotificationService  : Payroll 4 shipped to Juablaz of 320.0``


## **Ejemplo 1. Usando Spies**

![alt text](3.31_parallel_run.png)

Tener una implementación fake en el MS, que no envíe el mail, pero registra "como que se hubiera enviado".

Añadir base de datos que guarda: Destino y contenido, luego comparamos ambos registros, marcamos elementos como "consumidos".


## **Ejemplo 2. Github Scientist**

https://github.com/rawls238/Scientist4J


## **Ejemplo 3. Canary Releasing**
Lanzar una versión Canary para un subconjunto de usuarios, por si se produce algún problema sólo un pequeño grupo de usuarios se verán afectados.
Llamaríamos a ambas implementaciones.

Nginx as `Load Balancer`.

