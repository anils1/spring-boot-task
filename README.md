# spring-boot-task

# Technologies/Tools I have used to solve this challenge:

	1- Java 17
	2- MySQL Database
	3- Spring boot 3
	4- Swagger for API documentation
	5- Kafka for messaging(to implement queue logic)
	6- Using lombok to generate getter/setter/tostring/constructor

# Below are the steps to configure/start Kafka in your pc:

 download kafka from google and extract in your local machine. find here https://archive.apache.org/dist/kafka/3.4.0/kafka-3.4.0-src.tgz 

 # step 1: start zookeeper(default port:2181)

		to start zookeeper, run below command in terminal/cmd 
		cmd : <kafka_folder>/bin/zookeeper-server-start.sh config/zookeeper.properties

 # step 2: start kafka server(default port:9092)

 		to start kafka server, run below command in terminal/mac
		cmd : kafka_folder/bin/kafka-server-start.sh config/server.properties

 # step 3:  create topic, partition, replication factor on kafka broker

		To create above all these, rub below command in terminal/cmd (I am creating my topic name is 'product-topic')
		cmd : bin/kafka-topics.sh --bootstrap-server localhost:9092 --create --topic product-topic --partitions 3 --replication-factor 1

After above steps, you can start spring boot application(required table will be created into database and will be working);

# Swagger API url: (My application is running on 8080)
http://localhost:8080/swagger-ui/index.html

# Prerequisite : 
	1-configure/start kafka
	2-change DB credentials accordingly
	3-start application to create DB tables


# Note: If you want me to do make runnable and testing all the API, then let me know
