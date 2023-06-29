# Technical challenge

## About
Technical challenge where users can create and manage orders.

## How to download and run the project
### Cloning the project
    git clone https://github.com/RenanRPDR/manage-orders.git

### Abrindo o projeto e baixando as dependencias
- Open the project in IntelliJ IDEA and use the Maven menu on the right to download the dependencies
  - Manage orders -> Lifecycle -> clean and install
- If you prefer the terminal, use the commands below to download the dependencies
- ```mvn clean ```
- ```mvn install ```


### Download, install and create database
- Download it from the official website, click here: [Docker](https://www.docker.com/products/docker-desktop/)
  
- Download the postgres image
  - ```docker pull postgres```
- Creating your database
  - ```docker run --name ManageOrdersDB -e POSTGRES_PASSWORD=1234 -e POSTGRES_DB=ordermanagement-db -p 5432:5432 -d postgres ```

### Download, install and configure Insomnia
- Download it from the official website, click here: [Insomnia](https://insomnia.rest/download)
- Import the collection of requests from the "Order Management - requests.json" file into the main project folder

### Technologies and tools
- Java17+, Spring Boot and JPA
- Docker and Postgres
- Git and GitFlow
- IntelliJ IDEA, DBeaver and Insomnia