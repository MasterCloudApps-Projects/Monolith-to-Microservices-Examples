# Branch By Abstraction 

Vamos a proceder a la realización y explicación del patrón ``Branch By Abstraction``, que se basa en permitir que dos implementaciones del mismo código coexistan en la misma versión, sin romper la funcionalidad.

Nos situamos en el caso de que necesitamos migrar un código interio del monolito el cuál recibe peticiones internas de otros servicios también internos del mismo. Se aplica en 5 pasos:
1. Crear una abstracción para reemplazar la funcionalidad.
2. Cambiar los clientes de la funcionalidad existente para utilizar la nueva abstracción.
3. Crear una nueva implementación de la abstracción que realice la petición a nuestro nuevo microservicio.
4. Cambiar la abstracción para usar nuestra nueva implementación.
5. Limpiar la abstracción y eliminar la implementación anterior.
6. (Opcional): Borrar la interfaz.

## Ejemplo 1. Extracción de una funcionalidad dependiente.

### **Paso 1**
Tenemos nuestra aplicación monolítica, las peticiones y funcionalidades se responden dentro del mismo.
```
> docker-compose -f Ejemplo_1/1_docker-compose.yml up 
```

Podemos probar nuestro monolito:
```
> curl localhost:8080/inventory
```

Detenemos el paso 1:
```
> docker-compose -f Ejemplo_1/1_docker-compose.yml down
```

### **Paso 2**
Vamos a aplicar el patrón para extraer la funcionalidad de `UserNotification` con los pasos explicados anteriormente.

1. Creamos la interfaz `UserNotificationService`.
2. Adaptamos la implementación de `UserNotificationService` (que pasa a llamarse `UserNotificationServiceImpl`) existente para utilizar la interfaz.
3. Creamos una nueva implementación de la interfaz, `UserNotificationServiceMSImpl`.
4. Introducimos `ff4j` que nos permite cambiar el uso de una u otra implementación en tiempo de ejecución.

En este paso, vamos a llegar hasta el [``4``], nuestra aplicación se queda con el siguiente estado en el que podemos cambiar la implementación activa:

![alt text](3.25_branch_by_abstraction.png)

Vamos a desplegar el ejemplo:
```
> docker-compose -f Ejemplo_2/2_docker-compose.yml up 
```

Hacemos una petición:
```
curl -v -H "Content-Type: application/json" -d '{"shipTo":"Juablaz","total":320}' localhost:8080/payroll
```

Se loguea en el monolito:

``1_parallel_run_monolith         | 2021-09-24 11:02:32.470  INFO 1 --- [nio-8080-exec-1] e.c.m.p.s.i.UserNotificationServiceImpl  : Payroll 3 shipped to Juablaz of 320.0``

Si entramos en `http://localhost:8080/ff4j-web-console` y cambiamos el flag a habilitado, se realizará a través del microservicio.

Repetimos la petición:

```
curl -v -H "Content-Type: application/json" -d '{"shipTo":"Juablaz","total":320}' localhost:8080/payroll
```

``2_parallel_run_notification_ms  | 2021-09-24 11:02:39.123  INFO 1 --- [nio-8081-exec-1] e.c.m.p.service.UserNotificationService  : Payroll 4 shipped to Juablaz of 320.0``

Detenemos el paso 2:
```
> docker-compose -f Ejemplo_2/2_docker-compose.yml down
```


### **Paso 3**
5. Eliminaríamos el flag y la implementación antigua.
![alt text](3.27_branch_by_abstraction.png)

6. (Opcional): Borrar la interfaz.
![alt text](3.28_branch_by_abstraction.png)

Vamos a desplegar el ejemplo:
```
> docker-compose -f Ejemplo_1/3_docker-compose.yml up 
```

Hacemos una petición:
```
curl -v -H "Content-Type: application/json" -d '{"shipTo":"Juablaz","total":320}' localhost:8080/payroll
```

``3_branch_by_abstraction_notification_ms  | 2021-09-24 14:38:13.520  INFO 1 --- [nio-8081-exec-8] e.c.m.b.service.UserNotificationService  : Payroll 4 shipped to Juablaz of 320.0``