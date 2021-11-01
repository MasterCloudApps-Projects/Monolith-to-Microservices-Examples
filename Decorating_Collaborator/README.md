# **Decorating Collaborator**
<div align="center">

[![en](https://img.shields.io/badge/lang-en-red.svg)](https://github.com/MasterCloudApps-Projects/Monolith-to-Microservices-Examples/tree/master/Decorating_Collaborator/README.md)
[![es](https://img.shields.io/badge/lang-es-yellow.svg)](https://github.com/MasterCloudApps-Projects/Monolith-to-Microservices-Examples/tree/master/Decorating_Collaborator/README.es.md)
</div>

We are going to proceed with the realization and explanation of the `Decorating Collaborator` pattern. This pattern is based on the application of a proxy. When we make a request, once is responded from the monolith, the proxy will perform an operation on a new microservice. This microservice may or may not make use of information that the monolith must expose.

## **Example 1. New functionality**

This time we have developed a new base code.
<div align="center">

![alt text](3.32_decorating_collaborator.png)
</div>

### **Step 1**

We have our monolithic application, requests and functionalities are answered within it.

```
> docker-compose -f Ejemplo_1/1_docker-compose-monolith.yml up --build

> docker-compose -f Ejemplo_1/1_docker-compose-proxy.yml up -d
```

We can test our monolith:
```
> curl -v -H "Content-Type: application/json" -d '{"userName":"Juablaz","prize":250, "description":"Monitor"}' payment.service/order
```


### **Step 2**
We must implement the functionality in a new microservice that based on a correct response from the creation of `Order` should add points to a user in the` Loyalty` microservice.

We launched a version of the microservice and a `Gateway` made with spring cloud` Gateway`.

```
> docker-compose -f Ejemplo_1/2_docker-compose.yml up --build
```

We can test our microservice:
```
> curl -v -H "Content-Type: application/json" -d '' localhost:8081/loyalty/Juablaz
```

We can test our `Gateway`:

```
> curl -v -H "Content-Type: application/json" -d '{"userName":"Juablaz","prize":250, "description":"Monitor"}' localhost:8082/order

> curl localhost:8082/loyalty/Juablaz
```

The user is created in the new microservice and 10 points are added to it:
```
{"id":3,"userName":"Juablaz","points":10.0}
```

### **Step 3**
Once the gateway is tested, let's move the requests from our nginx proxy to our gateway.

```
> docker-compose -f Ejemplo_1/3_docker-compose-proxy.yml up -d
```

```
> curl -v -H "Content-Type: application/json" -d '{"userName":"Juablaz","prize":250, "description":"Monitor"}' payment.service/order

> curl payment.service/loyalty/Juablaz
```

It is even possible that it is necessary to retrieve more information from the monolith, in that case we would have to expose an endpoint in the monolith and make the request from the microservice.

<div align="center">

![alt text](3.33_decorating_collaborator.png)
</div>

This could create an additional load, plus it introduces a circular dependency, it might be better to change the monolith to provide the required information when our request to create an order is completed. However, this would require changing the monolith code or perhaps using another pattern, which we will study next `Change Data Capture`.
