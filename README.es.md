# Monolith-to-Microservices-Examples

<div align="center">

[![en](https://img.shields.io/badge/lang-en-red.svg)](https://github.com/MasterCloudApps-Projects/Monolith-to-Microservices-Examples/tree/master/README.md)
[![es](https://img.shields.io/badge/lang-es-yellow.svg)](https://github.com/MasterCloudApps-Projects/Monolith-to-Microservices-Examples/tree/master/README.es.md)
</div>



> Todos los proyectos tienen configurado un swagger para poder realizar peticiones:
`localhost:${PORT}/swagger-ui/`


Patrones a estudiar:
Patrones de código:


* [1. Strangler Fig](https://github.com/MasterCloudApps-Projects/Monolith-to-Microservices-Examples/tree/master/Strangler_Fig/README.es.md)
* [2. Branch by Abstraction](https://github.com/MasterCloudApps-Projects/Monolith-to-Microservices-Examples/tree/master/Branch_By_Abstraction/README.es.md)
* [3. Parallel Run](https://github.com/MasterCloudApps-Projects/Monolith-to-Microservices-Examples/tree/master/Parallel_Run/README.es.md)
* [4. Decorating Collaborator](https://github.com/MasterCloudApps-Projects/Monolith-to-Microservices-Examples/tree/master/Decorating_Collaborator/README.es.md)
* [5. Change Data Capture](https://github.com/MasterCloudApps-Projects/Monolith-to-Microservices-Examples/tree/master/Change_Data_Capture/README.es.md)



- 










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


Para decorating collaborator: usar spring gateway (problema el uso reactiva funcional ¿?¿)

para reload en caliente de nginx:
- Consul
- change dynamically nginx (debería estar la config en un volumen fuera y que se coja de ese volumen, luego hacer un reload, ahora no funciona porque se coge al generar la imagen)


PARA LA DEMO:
- Mostrar los pasos (quizá dividir en varios docker compose) 
    - Monolito + nginx
    - ms y actualizo nginx

- Quizá tener un cliente de test que haga peeticiones periódicas para demostrar no caída de servicio

- Enseñar también rollback en caso de error.