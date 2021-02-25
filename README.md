# USCS/Itaú Project
<p align="center">
<img alt="CaseItau" src="https://github.com/Lucas19932020/Itau_Repository/blob/master/Images/Ita%C3%BA%20image.PNG" width="400" />
</p>

Used technologies in the project:

- SpringBoot
  - Web
  - DevTools
  - Lombok
  - SpringData Cassandra
  - Spring Kafka

- Database
  - Cassandra
 
- Docker
  - Cassandra
  - Kafka
  - Zookeeper

- API Test
  - Postman
  
Solution draw file is inside the repository as:  

```
Desenho da solução.pptx
```

## Steps to install

**1. Clone the repository**

```bash
https://github.com/Lucas19932020/Itau_Repository
```

**2. Install Docker**

Site to download [Docker](https://docs.docker.com/get-docker/).

**3. Install and configure *Cassandra***

  **3.1. Pull the repository from *Cassandra***

  `docker pull datastax/dse-server:5.1.18`

  **3.2. To create a *Cassandra* conteiner, execute the command:**
  
  `docker run -e DS_LICENSE=accept --memory 4g --name cassandra -p 9042:9042 -d datastax/dse-server:5.1.18`

  **3.3. To copy the file *cassandra.yaml* inside the container, execute the command:**
 
  `docker cp <FILE_CASSANDRA> cassandra:/opt/dse/resources/cassandra/conf/`

  Obs.: Replace the ***<FILE_CASSANDRA>*** through the file directory *cassandra.yaml*, which is located in the project repository `"_/uscsitau/src/main/resources/config/cassandra.yaml_"`.

  **3.4. Stop and Start the container *Cassandra***
  
  `docker stop cassandra`

  `docker start cassandra`

  **3.5. To configure and create the tables, run the command:**
  
  `docker exec -it cassandra bash`

  **3.5.1. To log in *Cassandra*, run the command:**
  
  `cqlsh -u cassandra -p cassandra`

  **3.5.2. To create an user**

  `CREATE ROLE root with SUPERUSER = true AND LOGIN = true and PASSWORD = 'root';`

  **3.5.3. Create the Keyspace**

  `CREATE KEYSPACE dbo WITH REPLICATION = {'class': 'SimpleStrategy','replication_factor' : 1};`

  `USE dbo;`

  **3.5.1. Create the tables**

```bash
  CREATE TABLE cliente (
      nome VARCHAR,
      cpf_cnpj VARCHAR PRIMARY KEY,
      tipo_de_cliente VARCHAR,
      endereco VARCHAR,
      renda DOUBLE,
      razao_social VARCHAR,
      incr_estadual VARCHAR,
      num_conta VARCHAR
  );
```
```bash
  CREATE TABLE conta (
      num_conta VARCHAR PRIMARY KEY,
      agencia VARCHAR,
      dac INT,
      saldo DOUBLE
  );
```
```bash
  CREATE TABLE historico (	
      id UUID PRIMARY KEY,
      num_conta VARCHAR,
      tipo_de_transacao VARCHAR,
      data TIMESTAMP,
      status INT
  );
```

**4. Install and configure the *Kafka* and *Zookeeper***

  **4.1. Clone the repository that contains the *Kafka* and the *Zookeeper***

  `git clone https://github.com/confluentinc/cp-docker-images`

  **4.2. After cloned, navigate to the folder cp-docker-images/examples/kafka-single-node and run the command:**
  
  `docker-compose up -d`

  **4.3. To list the Kafka and Zookeeper services, run the command:**
  
  `docker-compose ps`

  **4.4. To create a Topic in Kafka, execute the command:**

  ```bash
    docker-compose exec kafka  \                                                              
    kafka-topics --create --topic foo --partitions 1 --replication-factor 1 --if-not-exists --zookeeper zookeeper:2181
  ```
    
  **4.5. To validate that the Topic was created, run the command:**

  ```bash
  docker-compose exec kafka  \
      kafka-topics --describe --topic foo  --zookeeper zookeeper:2181
  ```

**5. Import the uscsitau project and kafaka projetc into the IDE**

Execute the classes `StartApplication` and `kafakaApplication`.

## Routing
  - To perform all of the following processes, you need to download Postman. <br>
  Download [Postman here](https://www.postman.com/downloads/).
```bash  
  Registering a Client.
  POST    http://localhost:8080/clientes/salvar
 ```
  Register the client's informations as the image below:
  
  <p align="center">
  <img alt="Post Model Example" src="https://github.com/Lucas19932020/Itau_Repository/blob/master/Images/Images%20Case%20Itau%20-%20USCS%20Readme/1.%20Post%20Model%20Example.PNG" width="1000" />
  </p>
  
  Registration Examples:
  
  <p align="center">
  <img alt="Registering a client Example" src="https://github.com/Lucas19932020/Itau_Repository/blob/master/Images/Images%20Case%20Itau%20-%20USCS%20Readme/2%20Registering%20a%20new%20client.PNG" width="400" />
  <img alt="Registering a new client" src="https://github.com/Lucas19932020/Itau_Repository/blob/master/Images/Images%20Case%20Itau%20-%20USCS%20Readme/3%20Registering%20a%20client%20Example.PNG" width="400" />
  </p>
   
   After registering a new client an event log is sent and is registerd on historico table.
  
  <p align="center">
  <img alt="Keyspace log after registering a new client" src="https://github.com/Lucas19932020/Itau_Repository/blob/master/Images/Images%20Case%20Itau%20-%20USCS%20Readme/7.%20Keyspace%20log%20after%20registering%20a%20new%20client.PNG" />
  </p>

```bash  
  Listing all registered client:  
  GET     http://localhost:8080/clientes/lista
``` 
The command above lists all the registered clients.<br>
See the example bellow: 

<p align="center">
  <img alt="List of registers" src="https://github.com/Lucas19932020/Itau_Repository/blob/master/Images/Images%20Case%20Itau%20-%20USCS%20Readme/8.%20List%20of%20registers.PNG" />
  </p>
  
```bash  
  Searching a specific client:  
  GET     http://localhost:8080/clientes/CPF or CNPJ number
``` 
To search for a specific client it is necesessary to use his or her CNPJ or CPF like the image below:

<p align="center">
  <img alt="Seraching a client by CPF or CNPJ" src="https://github.com/Lucas19932020/Itau_Repository/blob/master/Images/Images%20Case%20Itau%20-%20USCS%20Readme/9.%20Seraching%20a%20client%20by%20CPF.PNG" />
  </p>
  
```bash  
  Updating a register  
  PUT     localhost:8080/clientes/atualizar
```  
The informations that can be updated are the client name, income, adress or company adress, state registration and the company name. The image below shows an updating process.

<p align="center">
  <img alt="Updating a client register.PNG" src="https://github.com/Lucas19932020/Itau_Repository/blob/master/Images/Images%20Case%20Itau%20-%20USCS%20Readme/10.%20Updating%20a%20client%20register.PNG" />
  </p>

```bash  
  Delete process  
  DELETE     localhost:8080/clientes/deletar
```  
  The delete process is simple, it is only needed to write the client CPF or CNPJ like this formula:<br>
  {"cpf_cnpj": "client CPF/CNPJ number}, after writing the desired CPF/CNPJ press send.
  <br>
  An example of deleting a client register:
  
<p align="center">
  <img alt="Deleting a client" src="https://github.com/Lucas19932020/Itau_Repository/blob/master/Images/Images%20Case%20Itau%20-%20USCS%20Readme/11.%20Deleting%20a%20client.PNG" />
  </p>
  
```bash  
  Listing all account numbers  
  GET     localhost:8080/conta/credito
```
The URL above lists all the registered account numbers.

<p align="center">
  <img alt="Conta_List" src="https://github.com/Lucas19932020/Itau_Repository/blob/master/Images/Images%20Case%20Itau%20-%20USCS%20Readme/22.%20Conta_Lista.PNG" />
  </p>

```bash  
  Listing a specific account numbers  
  GET    localhost:8080/conta/"acount_number"
```
  To list a specific registered account numbers, insert the desired account number after "/conta":
  
<p align="center">
  <img alt="Conta_id" src="https://github.com/Lucas19932020/Itau_Repository/blob/master/Images/Images%20Case%20Itau%20-%20USCS%20Readme/23.%20Conta_Id.PNG" />
  </p>

```bash  
  Credit process  
  POST     localhost:8080/conta/credito
```
  To credit a value, inform the agency number and the value to be credited:
  <br>
```
  {"num_conta": "Agency number"}
  {"credito": "value"}
  ```
   The image shows more details:
<p align="center">
  <img alt="Credit value" src="https://github.com/Lucas19932020/Itau_Repository/blob/master/Images/Images%20Case%20Itau%20-%20USCS%20Readme/15.%20Credit%20value.PNG" />
  </p>
  
```bash  
  Debit process  
  POST     localhost:8080/conta/debito
```
  The debit process is similar to the credit process, but instead of credit use debit.
  <br>
  ```
  {"num_conta": "Agency number"}
  {"debito": "value"}
```  
The image below shows an example of the debit process:
<p align="center">
  <img alt="Debit value" src="https://github.com/Lucas19932020/Itau_Repository/blob/master/Images/Images%20Case%20Itau%20-%20USCS%20Readme/16.%20Debit.PNG" />
  </p>
  
  <br><br><br>
  
  After each debiting, crediting, deleting and registering process is sent an event describing the process that passes through Spring, Kafka and finally it is stored at the historico table.
  <br>
  The images show all the events that have been made:
  <br><br>
  Spring terminal log:
  <br>
  <p align="center">
  <img alt="Spring Terminal" src="https://github.com/Lucas19932020/Itau_Repository/blob/master/Images/Images%20Case%20Itau%20-%20USCS%20Readme/17.%20Spring%20Event%20Log.PNG" />
  </p>
  <br><br>
  Kafka terminal:
  <br>
  <p align="center">
  <img alt="Kafka Teminal" src="https://github.com/Lucas19932020/Itau_Repository/blob/master/Images/Images%20Case%20Itau%20-%20USCS%20Readme/18.%20Kafka%20Event%20Log%20after%20Credit%20and%20Debit.PNG" />
  </p>
  <br><br>
  Table historico:
  <br>
    <p align="center">
  <img alt="Historico Table" src="https://github.com/Lucas19932020/Itau_Repository/blob/master/Images/Images%20Case%20Itau%20-%20USCS%20Readme/19.%20Keyspace%20log%20after%20debit%20and%20credit.PNG" />
  </p>
  <br><br>
  
```bash  
  Events List 
  GET     localhost:8080/historico/lista
```
  Use the URL above to list all events performed. Example:
    
  <p align="center">
  <img alt="List Table historico" src="https://github.com/Lucas19932020/Itau_Repository/blob/master/Images/Images%20Case%20Itau%20-%20USCS%20Readme/21.%20List%20table%20historico.PNG" />
  </p>
