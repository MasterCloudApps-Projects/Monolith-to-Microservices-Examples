# Decorating Collaborator

Vamos a proceder a la realización y explicación del patrón ``Decorating Collaborator``. Este patrón se basa en la aplicación de un proxy para una vez llegue la respuesta del monolito hacer una operación en un nuevo microservicio. Este microservicio podrá hacer uso o no de información que debe exponer el monolito.


## **Ejemplo 1. Nueva funcionalidad**

En esta ocasión hemos planteado un nuevo enunciado.

![alt text](3.33_decorating_collaborator.png)

### **Paso 1**

Tenemos nuestra aplicación monolítica, las peticiones y funcionalidades se responden dentro del mismo.

```
> docker-compose -f Ejemplo_1/1_docker-compose-monolith.yml up 

> docker-compose -f Ejemplo_1/1_docker-compose-proxy.yml up -d

```

Podemos probar nuestro monolito:
```
> curl -v -H "Content-Type: application/json" -d '{"userName":"Juablaz","prize":250, "description":"Monitor"}' payment.service/order
```


### **Paso 2**
Debemos implementar la funcionalidad en un nuevo microservicio que basado en una respuesta correcta de la creación de `Order` deberá añadir puntos a un usuario en el microservicio de ``Loyalty``.

Lanzamos una versión del microservicio y un ``Gateway`` realizado con spring cloud ``Gateway``.

```
> docker-compose -f Ejemplo_1/2_docker-compose.yml up
```

Podemos probar nuestro microservicio:

```
> curl -v -H "Content-Type: application/json" -d '' localhost:8081/loyalty/Juablaz
```


Podemos probar nuestro ``Gateway``:

```
> curl -v -H "Content-Type: application/json" -d '{"userName":"Juablaz","prize":250, "description":"Monitor"}' localhost:8082/order

> curl localhost:8082/loyalty/Juablaz
```

Se crea el usuario en el microservicio nuevo y se le añaden 10 puntos:
```
{"id":3,"userName":"Juablaz","points":10.0}
```

### **Paso 3**

```
> docker-compose -f Ejemplo_1/3_docker-compose-proxy.yml up -d
```

> curl -v -H "Content-Type: application/json" -d '{"userName":"Juablaz","prize":250, "description":"Monitor"}' payment.service/order

> curl payment.service/loyalty/Juablaz

