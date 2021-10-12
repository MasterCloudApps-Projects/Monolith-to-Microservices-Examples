# Strangler Fig

The ``Strangler Fig`` pattern consists of an incremental and gradual migration of the specific functionalities located within the monolith to independent microservices.

The pattern is divided into 3 steps:
1. Monolithic application, requests and functionalities are responded within it.
2. Implementation of the functionality in a new microservice.
3. With the new implementation ready, we can migrate requests from the monolith to the microservice.

<div align="center">

![alt text](3.1_strangler_fig_pattern.png)
</div>

We are going to apply the pattern in different examples with the three steps explained above.

<br>

## **Ejemplo 1. Separated funcionality extraction**
____________________________________________________________

In order to perform the migration of requests and hot deployments, we must configure a reverse proxy. The host of our application will be: `payment.service`.

To do this, we must add:
- Linux: `/etc/hosts`
- Windows: `C:/Windows/System32/drivers/etc/hosts`

The following line: `127.0.0.1 payment.service`

We start from a monolith that contains all the logic of the application. The need arises to extract an independent functionality, in this case ``Inventory`` to a new microservice.

Below there is an image of the initial and final state of the application after applying the pattern.

<div align="center">

![alt text](3.2_strangler_fig_pattern.png)
</div>

### **Step 1**
We have our monolithic application, requests and functionalities are answered within it.

```
> docker-compose -f Ejemplo_1/1_docker-compose-monolith.yml up 

> docker-compose -f Ejemplo_1/1_docker-compose-proxy.yml up -d
```

Our proxy is configured to direct all requests to the existing monolith.

```
server {
  listen 80;
  server_name payment.service;

  location ~ ^/ {
    proxy_pass http://1-strangler-fig-monolith:8080;
  }
}
```

We can test our monolith through a request to:
```
> curl payment.service/inventory
```

### **Step 2**
We must implement the functionality in a new microservice.
```
> docker-compose -f Ejemplo_1/2_docker-compose-ms.yml up
```

Requests keep coming to our monolith, but we can test our microservice by calling it directly:
```
> curl localhost:8081/inventory
```

We see that the responses come with the tag ``[MS]`` that we have added in the data initializer.

### **Step 3**
With your new implementation ready, we proceed to redirect calls from the monolith to the new microservice.

```
> docker-compose -f  Ejemplo_1/3_docker-compose-proxy.yml up -d
```

The new proxy settings are:
```
server {
  listen 80;
  server_name payment.service;

  location ~ ^/(?!(inventory)) {
    proxy_pass http://1-strangler-fig-monolith:8080;
  }

  location /inventory {
    proxy_pass http://2-strangler-fig-inventory-ms:8081;
  }
}
```

Let's try to make a request:
```
> curl payment.service/inventory
```

Now the response will have a prefix ``[MS]`` that we have added to the sample data automatically registered in the microservice.

In case of any problem, you can always do a rollback and redirect the requests back to the monolith.

```
> docker-compose -f  Ejemplo_1/1_docker-compose-proxy.yml up -d
```

<br>

## **Example 2. Internal functionality extraction**
____________________________________________________________

If we want to apply the pattern on ``Payroll``, which uses an internal functionality in the ``User notification`` monolith, we must expose said internal functionality to the outside through an endpoint.


<div align="center">

![alt text](3.3_strangler_fig_pattern.png)
</div>

¿How does this fit into our 3 steps?:

1. Our monolith, in case of not having a proxy, we must add one that allows directing requests.
2. With the proxy active, we perform the extraction of our microservice. It could be done in several steps:
    - Creation of the empty microservice, without functionality returning ``501 Not Implemented ``. It is recommended to take it to production to familiarize ourselves with the deployment process.
    - Implementation of the microservice functionality.
3. We move requests from the monolith to the microservice progressively. If there is an error we can redirect the requests back to the monolith.

<div align="center">

![alt text](3.10_strangler_fig_pattern.png)
</div>

### **Step 1**
We have our monolithic application, requests and functionalities are answered within it.

```
> docker-compose -f Ejemplo_2/1_docker-compose-monolith.yml up 

> docker-compose -f Ejemplo_2/1_docker-compose-proxy.yml up -d
```

We can test our monolith:
```
> curl -v -H "Content-Type: application/json" -d '{"shipTo":"Juablaz","total":120}' payment.service/payroll
```

Log in to the notification:
```
Payroll 3 shipped to Juablaz of 120.0
```

### **Step 2**
We must implement the functionality in a new microservice that will communicate with the monolith. Therefore, the monolith must expose an endpoint for the microservice to connect through it ``/notification``.
We released a version of the monolith (``v2``) and our new microservice.

```
> docker-compose -f Ejemplo_2/2_docker-compose.yml up
```

We can test our microservice:
```
> curl -v -H "Content-Type: application/json" -d '{"shipTo":"Juablaz","total":220}' localhost:8081/payroll
```

The notification is logged in the new monolith (``v2``), therefore the communication is correct:
```
Payroll 3 shipped to Juablaz of 220.0
```

Requests through the proxy ``payment.service`` continue to reach the previous monolith, but we have tested the correct operation of the new monolith and the microservice.

### **Step 3**
With the new implementation ready, we redirected requests to the `Payroll` functionality monolith.

```
> docker-compose -f  Ejemplo_2/3_docker-compose-proxy.yml up -d
```

The new configuration is:
```
server {
  listen 80;
  server_name payment.service;

  location ~ ^/(?!(payroll)) {
    proxy_pass http://2-strangler-fig-monolith:8082;
  }

  location /payroll {
    proxy_pass http://2-strangler-fig-payroll-ms:8081;
  }
}
```

We can test our application:
```
> curl -v -H "Content-Type: application/json" -d '{"shipTo":"Juablaz","total":320}' payment.service/payroll
```

The notification is logged in the monolith ``v2``:
```
Payroll 3 shipped to Juablaz of 320.0
```

At this point we can consider removing version 1 of the monolith:

```
> docker-compose -f  Ejemplo_2/1_docker-compose_monolith.yml down
```

And what happens if we have had a problem in the new version?
We can quickly load the old proxy settings:

```
> docker-compose -f Ejemplo_2/1_docker-compose-proxy.yml up -d
```

In this way, the petitions go back to the old monolith.

<br>
