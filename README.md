# Monolith-to-Microservices-Examples


> Todos los proyectos tienen configurado un swagger para poder realizar peticiones:
`localhost:${PORT}/swagger-ui/`


Patrones a estudiar:
Patrones de código:

- Strangler Fig









- Branch by Abstraction

- Parallel Run: Probar el microservicio a la vez que el monolito, comparar la diferencia.

- Decorating Collaborator: Añadir nueva funcionalidad.


Los patrones suelen basarse en ponerse en medio de la red:
- RabbitMQ
- Diferencia
- Proxy


- Change Data Capture
Utilizar Debezium, eventos con Kafka o con Redis.


Patrones de BBDD: (más complejos)
- Database as a Service, con debezium
- Change Data Ownership

Para la realización de cada ejemplo utilizaremos docker a través de múltiples docker-compose para cada ejemplo.


# COMANDOS ÚTILES:

> docker stop $(docker ps -a -q)

> docker rm $(docker ps -a -q)






REU: 27/09/2021
Explicar en el propio README no sólo la ejecución, parte de implementación, por ejemplo, conf de nginx, interfaz que hemos añadido, etc etc.


Hacer un ejemplito con parallen run y Diferencia:
- https://lordofthejars.github.io/diferencia-docs-site/diferencia/0.6.0/index.html
- https://www.infoq.com/articles/tap-compare-diferencia/


Para decorating collaborator: usar spring gateway (problema el uso reactiva funcional ¿?¿? (no))

para reload en caliente de nginx:
- Consul
- change dynamically ngins (debería estar la config en un volumen fuera y que se coja de ese volumen, luego hacer un reload, ahora no funciona porque se coge al generar la imagen)


PARA LA DEMO:
- Mostrar los pasos (quizá dividir en varios docker compose) 
    - Monolito + nginx
    - ms y actualizo nginx

- Quizá tener un cliente de test que haga peeticiones periódicas para demostrar no caída de servicio

- Enseñar también rollback en caso de error.
