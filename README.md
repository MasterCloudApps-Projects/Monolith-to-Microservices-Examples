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