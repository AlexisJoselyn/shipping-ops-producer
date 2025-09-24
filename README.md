# 📦 Shipping Orchestrator Producer 🚚

Este microservicio forma parte del **bootcamp de Java en NTT Data**.

Este servicio se encarga de **producir eventos de envíos (shipments)** y publicarlos en Kafka.

---

## ⚙️ Tecnologías principales

* **Java 17**
* **Spring Boot**
* **Apache Kafka** (Producer)
* **Avro** para los esquemas de mensajes
* **Docker** para la ejecución de los servicios

---

## 🚀 Levantar el proyecto

### 1. Clonar repositorios

Es importante clonar **ambos repositorios** (producer y consumer) en la **misma carpeta raíz**, porque con Docker se espera que estén juntos.

```bash
mkdir microservices-ntt
cd microservices-ntt

# Clonar ambos repos
git clone https://github.com/AlexisJoselyn/shipping-ops-producer
git clone https://github.com/AlexisJoselyn/dispatch-orchestrator-consumer
```

### 2. Configuración de Kafka y Docker

Este microservicio usa Kafka, así que necesitas levantar la infraestructura de mensajes (Zookeeper + Kafka).

Revisar `docker-compose.yml` que se encarga de levantar Kafka.

Ejemplo:

```bash
docker-compose up -d
```

### 3. Levantar el Producer

```bash
cd dispatch-orchestrator-producer
./mvnw spring-boot:run
```

El servicio arranca en el puerto **8087** por defecto.

Puedes comprobarlo en: [http://localhost:8087/actuator/health](http://localhost:8087/actuator/health)

### 4. Comunicación con el Consumer

* El **Producer** envía eventos al tópico de Kafka.
* El **Consumer** [en el otro repo](https://github.com/AlexisJoselyn/dispatch-orchestrator-consumer) escucha esos eventos y guarda/transforma la información.

Por eso, para que todo funcione, **Producer y Consumer deben estar corriendo al mismo tiempo**.

---

## 📚 Lo que aprendí aquí

* A crear un microservicio **producer** con Spring Boot.
* A integrar **Kafka** y publicar mensajes usando **Avro**.
* A configurar Docker para levantar toda la infraestructura.
* A pensar en la comunicación asíncrona entre servicios.

---

## 📝 Notas para mí

* Puerto del servicio: **8087**
* Depende de Kafka (levantar con Docker antes de correrlo).
* Debe estar en la misma carpeta que el **consumer** para que `docker-compose` funcione bien.

---

✨ *Este proyecto forma parte de mi camino aprendiendo backend en el Bootcamp de NTT Data.*

