# Branch By Abstraction 

## Estado final:

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


