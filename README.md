

#Download Apache kafka:

wget https://www.apache.org/dyn/closer.cgi?path=/kafka/0.10.1.1/kafka-0.10.1.1-src.tgz

Unzip the downloaded folder and copy it to /usr/local/bin/kafka

# scripts

/site-docs:  contains an archive of all documentaions that you can find online
/libs: contains all the dependencies kafka has to run( ex: zookeeper ...).
/config: contains all the files you need to configure the differnet kafka components.
/bin:  contains all the programs kafka needs to run up in different capacities.

# command line

## Run zookeeper: 
You can modify the host port on zookepper.properties  
```shell
sudo bin/zookeeper-server-start.sh  config/zookeeper.properties
```
to ensure that your zookeeper server is running use the telnet command:  

```shell
telnet localhost zookeeper_port
```

## Start kafka broker

```shell
sudo bin/kafka-server-start.sh config/server.properties
```

## Create a new topic
```shell
sudo bin/kafka-topics.sh --create --topic my_topic --zookeeper localhost:2182 --replication-factor 1 --partitions 1
```

To ensure that your topic is created verify that the folder my_topic-0 is created on the /tmp/kafka-logs  

  ll /tmp/kafka-logs

## get list of topics:

  sudo bin/kafka-topics.sh --list --zookeeper localhost:2182

## Console producer : write your message and type enter  

  sudo bin/kafka-console-producer.sh --broker-list localhost:9092 --topic my_topic


## Console Consumer
  sudo bin/kafka-console-consumer.sh --zookeeper localhost:2182 --topic my_topic --from-beginning

# Demo. Fault-tolerance and Resiliency in Apache Kafka

## create a topic with replication factory and partitions

  sudo bin/kafka-topics.sh --create --topic my_topic --zookeeper localhost:2182 --replication-factor rep_f	ctor --partitions partition_number 

## topic info

  sudo bin/kafka-topics.sh --describe --topic my_topic --zookeeper localhost:2182 
