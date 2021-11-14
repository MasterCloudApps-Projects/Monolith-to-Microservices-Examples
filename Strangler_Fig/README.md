# **Strangler Fig**
<div align="center">

[![en](https://img.shields.io/badge/lang-en-red.svg)](https://github.com/MasterCloudApps-Projects/Monolith-to-Microservices-Examples/tree/master/Strangler_Fig/README.md)
[![es](https://img.shields.io/badge/lang-es-yellow.svg)](https://github.com/MasterCloudApps-Projects/Monolith-to-Microservices-Examples/tree/master/Strangler_Fig/README.es.md)
</div>

The `Strangler Fig` pattern consists of incrementally and gradually migrating specific functionalities located within the monolith to independent microservices.

The pattern is divided into 3 steps:
1. Monolithic application. Requests and functionalities are answered within the monolith.
2. Implementation of the functionality in a new microservice.
3. With your new implementation ready, we migrate the requests from the monolith to the microservice.

<div align="center">

![alt text](3.1_strangler_fig_pattern.png)
</div>

Let's apply the pattern in different examples with the three steps explained above.

<br>

## **Example 1. Extraction of independent functionality**.
____________________________________________________________
<div align="center">

[![Video](https://img.youtube.com/vi/FRCl4wJHx-M/0.jpg)](https://www.youtube.com/watch?v=FRCl4wJHx-M)
</div>

In order to perform the migration of requests and hot deployments, we need to set up a reverse proxy. The host of our application is going to be: `payment.service`. 

To do this, we must add to:
- Linux: `/etc/hosts`
- Windows: `C:/Windows/System32/drivers/etc/hosts`

The following line: `127.0.0.1 payment.service`

We start from a monolith that contains all the application logic. The need arises to extract a separate functionality, in this case ``Inventory`` to a new microservice.

Below is an image of the initial and final state of the application after applying the pattern.

<div align="center">

![alt text](3.2_strangler_fig_pattern.png)
</div>

### **Step 1**
We have our monolithic application. Requests and functionalities are answered within it.
```
docker-compose -f Example_1/1_docker-compose-monolith.yml up  
```
```
docker-compose -f Example_1/1_docker-compose-proxy.yml up -d
```
[Note 1](#note1)

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
curl payment.service/inventory
```

### **Step 2**
We must implement the functionality in a new microservice.
```
docker-compose -f Example_1/2_docker-compose-ms.yml up 
```

The requests are still coming to our monolith, but we can test our microservice by calling it directly:
```
curl localhost:8081/inventory
```

We see that the responses come with the `[MS]` tag that we added in the data initializer.

### **Step 3**
With your new implementation ready, we proceed to redirect calls from the monolith to the new microservice.

```
docker-compose -f  Example_1/3_docker-compose-proxy.yml up -d
```

The new proxy configuration is:
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

Let's try making requests:
```
curl payment.service/inventory
```

From this point on, the response will have a ``[MS]`` prefix that we have added to the example data that was automatically added to the microservice.

In case of any problems you can always rollback and redirect the requests back to the monolith.

```
docker-compose -f  Example_1/1_docker-compose-proxy.yml up -d
```

<br>

## **Example 2. Extracting internal functionality**
____________________________________________________________
<div align="center">

[![Video](https://img.youtube.com/vi/hp2e3b-gTMg/0.jpg)](https://www.youtube.com/watch?v=hp2e3b-gTMg)
</div>

If we wish is to apply the pattern on `Payroll` that uses internal functionality in the `User Notifications` monolith, we must expose that internal functionality to the outside via an endpoint.

<div align="center">

![alt text](3.3_strangler_fig_pattern.png)
</div>

How does this fit into our 3 steps:

1. In case we do not have a proxy, we must add one that allows us to route requests. 
2. With the proxy active, we perform the extraction to our microservice. This could be done in several steps:
    - Creation of the empty microservice, without functionality returning `501 Not Implemented`. It is recommended to take it to production to familiarize ourselves with the deployment process.
    - Implementation of the microservice functionality.
3. We move the requests from the monolith to the microservice progressively. If there is an error we can redirect the requests back to the monolith.

<div align="center">

![alt text](3.10_strangler_fig_pattern.png)
</div>

### **Step 1**
We have our monolithic application, requests and functionalities are answered within it.

```
docker-compose -f Example_2/1_docker-compose-monolith.yml up 
```
```
docker-compose -f Example_2/1_docker-compose-proxy.yml up -d
```

We can test our monolith:
```
curl -v -H "Content-Type: application/json" -d '{"shipTo":"Juablaz","total":120}' payment.service/payroll
```

Logs into the notification:
```
Payroll 3 shipped to Juablaz of 120.0
```

### **Step 2**
We must implement the functionality in a new microservice that will communicate with the monolith. Therefore, the monolith must expose an endpoint for the microservice to connect through it `/notification`.
We launch a version of the monolith (`v2`) and our new microservice.

```
docker-compose -f Example_2/2_docker-compose.yml up 
```

We can test our microservice:

```
curl -v -H "Content-Type: application/json" -d '{"shipTo":"Juablaz", "total":220}' localhost:8081/payroll
```

The notification is logged on the new monolith (`v2`), so the communication is successful:
```
Payroll 3 shipped to Juablaz of 220.0
```

The requests through the `payment.service` proxy are still arriving to the old monolith, but we have tested the correct functioning of the new monolith and the microservice.


### **Step 3**
With the new implementation ready, we redirect the requests to the `Payroll` functionality monolith.

```
docker-compose -f Example_2/3_docker-compose-proxy.yml up -d
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
curl -v -H "Content-Type: application/json" -d '{"shipTo":"Juablaz","total":320}' payment.service/payroll
```

Notification is logged in version 2 of the `2_strangler_fig_monolith` monolith:
```
Payroll 3 shipped to Juablaz of 320.0
```

At this point we can consider removing version 1 of the monolith.
What happens if we have had a problem with the new version?
We can quickly load the old proxy configuration:

```
docker-compose -f Example_2/1_docker-compose-proxy.yml up -d
```

This way, the requests go back to the old monolith.

<br>

## **Example 3. Message interception**
____________________________________________________________
<div align="center">

[![Video](https://img.youtube.com/vi/6ArWIM2jJlI/0.jpg)](https://www.youtube.com/watch?v=6ArWIM2jJlI)
</div>

In this example we have not added a proxy to redirect the requests since the pattern is not based on intercepting HTTP requests, but on intercepting and redirecting messages from the messaging queue. We have implemented the example using Kafka.

### Step 1
We have a monolith that receives messages through a queue. 
For this, we have also created a message producer `strangler_fig_producer` and configured a queuing system based on Kafka.
It consists of two topics: `invoicing-v1-topic` and `payroll-v1-topic`.

<div align="center">

![alt text](3.16_strangler_fig_pattern.png)
</div>

```
docker-compose -f Example_3/1_docker-compose-kafka-queue.yml up -d
```
```
docker-compose -f Example_3/1_docker-compose-monolith.yml up --build
```
```
docker-compose -f Example_3/1_docker-compose-producer.yml up -d 
```

Let's do a test through a request:
```
curl -v -H "Content-Type: application/json" -d '{"shipTo":"Juablaz", "total":220}' localhost:9090/messages/send-payroll
```

We can see how it logs into our monolith: 
```
Payroll 3 shipped to Juablaz of 220.0
```

We have three possible cases:
- a) We can change the monolith code.
- b) We cannot change the monolith code.
- c) We cannot change the data source.

## **a) We can change the code of the monolith**.
<div align="center">

[![Video](https://img.youtube.com/vi/ZOlTZme9_D4/0.jpg)](https://www.youtube.com/watch?v=ZOlTZme9_D4)
</div>

### **Step 2**

<div align="center">

![alt text](3.18_strangler_fig_pattern.png)
</div>

We need to modify the monolith code to ignore `Payroll` requests. It will no longer have the `payroll-v1-topic` configured from which it was receiving messages. Also, we need to expose the `Notification` endpoint in the monolith to be able to send notifications from the microservice. Therefore, we need a ``v2`` version of the monolith.

The complication arises when following the pattern and trying to migrate requests from the monolith to the microservice. In this example, we have no requests and we cannot migrate them through the use of a proxy, so we need to update the data source.
- To do this we need to create new topics that we write to from our `Producer` and connect to from the `Monolith-v2`. We cannot continue writing in the same topic that was used in version 1. In this case we are changing the data source and it is possible that depending on the situation we cannot change it.

We will then have our `v1` monolith reading data from:
- invoicing-v1-topic
- payroll-v1-topic

And our `v2` monolith reading data from:
- invoicing-v2-topic
- payroll-v2-topic (just the ms)

In the migration we will switch from writing in the `v1` topics to the `v2` topics.

[Note 2](#note2)

Let's run the example following the pattern, first the implementation and then migrating the messages from the queue:

```
docker-compose -f  Example_3/2_a_docker-compose.yml up --build
```

We can test our new monolith implementation:
```
curl -v localhost:8082/invoicing
```

### **Step 3**
We are going to migrate the messages to new topics where to write. We will change our data source to `invoicing-v2-topic` and `payroll-v2-topic`.
```
docker-compose -f Example_3/3_a_docker-compose-producer.yml up -d --build
```

Let's test that it works correctly:
```
curl -v -H "Content-Type: application/json" -d '{"shipTo":"Juablaz", "total":220}' localhost:9090/messages/send-payroll
```

Logs into our `v2` monolith:
```
Payroll 3 shipped to Juablaz of 220.0
```

We can confirm this by a request to the microservice:
```
curl localhost:8081/payroll/3
```

In case of error we can change the data generation to the old topic:
```
docker-compose -f  Example_3/1_docker-compose-producer.yml up -d
```

## **b) We can NOT change the monolith code**.
<div align="center">

[![Video](https://img.youtube.com/vi/tXzw0gCGwgE/0.jpg)](https://www.youtube.com/watch?v=tXzw0gCGwgE)
</div>

### **Step 2**

![alt text](3.17_strangler_fig_pattern.png)

In this case we cannot touch the monolith. We need that only `Invoicing` messages arrive to the monolith because we cannot remove the processing of those arriving to `Payroll`. Also, we cannot log notifications from the microservice, since we would have to expose an endpoint as we have done in the previous example. 
We will log the creation of Payroll in the microservice itself to simplify the example.

Our flow would be as follows:
- A POST request arrives to `strangler-fig-producer`.
- It generates a message to the Kafka queue to the two possible topics `invoicing-all-msg-topic`, `payroll-all-msg-topic`
- We have a content-based routing microservice `strangler-fig-cbr` that consumes and redirects the topics:
    - `payroll-v1-topic` - Monolith
    - `payroll-v2-topic` - Payroll
- The `payroll-v1-topic` topic would remain unused since we are going to redirect the messages to `v2-topic`.

To apply this to the pattern, as we have explained in the previous example, we need to create new topics to which we write from the `producer` and to which we connect from the `cbr`. We cannot continue writing in the same topic that was used in version 1. In this case we are changing the source of information and it is possible that depending on the situation we cannot change it.

We launch a version exactly **same** as the previous version of the monolith, **changing the topics to which it subscribes**.

```
docker-compose -f Example_3/2_b_docker-compose.yml up --build
```

We can test our new microservice implementation and cbr:
```
curl -v localhost:8081/payroll
```

At this point, requests are still coming in to the old topic, `payroll-v1-topic` and `invoicing-v1-topic`.


### **Step 3**
We are going to migrate the `requests`. In this case, it is to migrate the messages to new topics where to write, update our data source:

```
docker-compose -f Example_3/3_b_docker-compose-producer.yml up -d
```

Let's test that it works correctly:
```
curl -v -H "Content-Type: application/json" -d '{"shipTo":"Juablaz", "total":220}' localhost:9090/messages/send-payroll
```

Logs into our microservice (Remember that the request is not made from the microservice to the monolith to log in since we cannot change the monolith code):
```
Payroll 3 shipped to Juablaz of 220.0
```

In case of error, we can change the data writing to the old monolith:
```
docker-compose -f Example_3/1_docker-compose-producer.yml up -d
```

##  **c) We can NOT change the data source**.
<div align="center">

[![Video](https://img.youtube.com/vi/6hWz_6aTFlo/0.jpg)](https://www.youtube.com/watch?v=6hWz_6aTFlo)
</div>

### **Step 1.1**

After having performed the previous examples, a question arises during the application of this pattern: What happens if we cannot change the data source?

For this, we start from an extended version of the monolith, which has a `FF4J` flag like the ones used in the [Branch by Abstraction](https://github.com/MasterCloudApps-Projects/Monolith-to-Microservices-Examples/tree/master/Branch_By_Abstraction/README.es.md) pattern.

```
docker stop example_3_step_1_strangler_fig_monolith
```
```
docker-compose -f Example_3/1_c_docker-compose-monolith.yml up --build
```

Let's do a test through a request:
```
curl -v -H "Content-Type: application/json" -d '{"shipTo":"Juablaz","total":220}' localhost:9090/messages/send-payroll
```

We can see how it logs into our monolith: 
```
Payroll 3 shipped to Juablaz of 220.0
```

### **Step 2**

Let's run the microservice and disable payroll consumption on the monolith:

```
docker-compose -f  Example_3/2_c_docker-compose-ms.yml up --build
```

We can test our implementation of the microservice:
```
curl -v localhost:8081/payroll
```

If we enter `http://localhost:8080/ff4j-web-console` and change the flag to disabled, it will stop consuming the monolith and will only be done through the microservice.

This step we could modify the monolith code to extend it and add a `NotificationController` that allows to log notifications through the monolith, we have decided not to modify it to simplify the example.


### **Step 3**
In this last step, we would remove the flag and the old implementation, replacing the previous version of the monolith.

```
docker stop example_3_step_1_c_strangler_fig_monolith
```
```
docker-compose -f Example_3/3_c_docker-compose-monolith.yml up --build
```

Let's do a test through a request:
```
curl -v -H "Content-Type: application/json" -d '{"shipTo":"Juablaz","total":220}' localhost:9090/messages/send-payroll
```

We can see how it logs into our microservice: 
```
Payroll 3 shipped to Juablaz of 220.0
```
<br>

# Links of interest:
https://github.com/javieraviles/split-the-monolith

https://www.it-swarm-es.com/es/nginx/docker-nginx-proxy-como-enrutar-el-trafico-un-contenedor-diferente-utilizando-la-ruta-y-no-el-nombre-de-host/828289465/

https://refactorizando.com/kafka-spring-boot-parte-uno/

https://github.com/flipkart-incubator/kafka-filtering#:~:text=Kafka%20doesn't%20support%20filtering,deserialized%20%26%20make%20such%20a%20decision.

https://blog.cloudera.com/scalability-of-kafka-messaging-using-consumer-groups/

https://stackoverflow.com/questions/57952538/consuming-from-single-kafka-partition-by-multiple-consumers


# Commands of interest:
Delete all containers:
docker rm -f $(docker ps -a -q)

Delete all volumes:
docker volume rm -f $(docker volume ls -q)

Delete all images:
docker rmi -f $(docker images -a -q)

<br>

# Notes 

<a id="note1"></a>
### Note 1:
   
``--build``: build images before starting containers.

``-d, --detach``: separate mode: run containers in background, print new container names.

``--force-recreate``: Recreate the containers even if their configuration and image have not changed.
                        and image have not changed.


------

<a id="note2"></a>
### Note 2:
NOTE: 
We have configured our kafka to automatically create topics if it does not find them, `KAFKA_AUTO_CREATE_TOPICS_ENABLE`, if this setting is not enabled it would be necessary to connect to the docker container and run a command. 

It would do:

```
docker exec -it $(docker ps -aqf "name=example_3_kafka_1") bin/kafka-topics.sh --create --zookeeper zookeeper:2181 --replication-factor 1 --partitions 1 --topic payroll-v2-topic
```
```
docker exec -it $(docker ps -aqf "name=example_3_kafka_1") bin/kafka-topics.sh --create --zookeeper zookeeper:2181 --replication-factor 1 --partitions 1 --topic invoicing-v2-topic
```
