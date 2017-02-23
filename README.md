

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
  
## Adding Partitions to a Topic

Apache Kafka provides us with alter command to change Topic behaviour and add/modify configurations. We will be using alter command to add more partitions to an existing Topic.

Here is the command to increase the partitions count from 2 to 3 for topic 'my-topic' -

```shell
./bin/kafka-topics.sh --alter --zookeeper localhost:2181 --topic my-topic --partitions 3
```

## Delete a Topic 

Stop Kafka server.
Set the property delete.topic.enable to true on broker conf file (/conf/server.properties).

```shell
bin/kafka-topics.sh --delete --zookeeper localhost:2182 --topic my_rep_topic
```
## Test Performance

This script is used to charge test performance
```shell
./bin/kafka-producer-perf-test.sh --topic myreptopic --num-records 100 --record-size 20 --throughput 10 --producer-props bootstrap.servers=localhost:9092,localhost:9094 key.serializer=org.apache.kafka.common.serialization.StringSerializer value.serializer=org.apache.kafka.common.serialization.StringSerializer
```

## Console producer : write your message and type enter  

  sudo bin/kafka-console-producer.sh --broker-list localhost:9092 --topic my_topic


# Demo. Fault-tolerance and Resiliency in Apache Kafka

## create a topic with replication factory and partitions

  sudo bin/kafka-topics.sh --create --topic my_topic --zookeeper localhost:2182 --replication-factor rep_f	ctor --partitions partition_number 

## topic info

  sudo bin/kafka-topics.sh --describe --topic my_topic --zookeeper localhost:2182 

# Consumer
## Console Consumer
  sudo bin/kafka-console-consumer.sh --zookeeper localhost:2182 --topic my_topic --from-beginning
## Subscribe Vs  Assign on consumption process

By calling the **subscribe** method, you are asking for automatic or dynamic partition assignment. 
That is to say that you're enlisting the single consumer instance to eventually pull from every partition within that topic, which can be at least one, but likely many. When adding multiple topics to the list, you're enlisting the consumer instance to pull from every partition within every topic, which is guaranteed to be many. 
This has very important implications which shouldn't be taken lightly for reasons we'll cover shortly. 
Besides subscribing to topics, there's another option: subscribing to individual partitions. 
This is done through the assign method. The **assign** method is only valid for subscribing to a list containing the class topic partition.

## Offsets management

The offset is the critical value that enables consumers to operate independently by representing the last read position the consumer has read from a partition within a topic. 
When you think about the business of consuming messages, you realize just how important the offset is, and more importantly, whether it is accurate. How Kafka manages the consumer offset is one of the more important things to understand, and that's why we're going to spend a bit of time on it right now.  

First, there is some important terminology to learn about the offset. 
There are different categories of offsets, with each representing the various stage they are in. 
When an individual is reading from a partition, it obviously needs to establish what it has and hasn't read. 
This definitive answer is called **the last committed offset**, and it represents the last record that the consumer has confirmed to have processed. We'll get into this confirmation process shortly, but this is the starting point for a consumer within any given partition, depending on the configured offset reset behavior, which we'll also cover later. You will notice we're really looking at it from a partition viewpoint,and that is because each partition is mutually exclusive with regard to consumer offsets. 

So, for any given topic, a consumer may have multiple offsets it's tracking; one for each partition within a topic. 
As the consumer reads records from **the last committed offset**, it tracks its **current position**. 
As we illustrated, this position advances as the consumer advances in the log towards the last record in the partition, which is known as the **log-end-offset**. 


There is a notable difference, however, between the **current position** and the **last committed offset**, and it represents potentially uncommitted offsets. The success of robust and scalable message consumption in Apache Kafka largely depends on your understanding of what creates this gap and what can be done to narrow it. 

Every application has different processing requirements, functional and non-functional. 

It is the job of the application designer and developer to find the appropriate tradeoffs that work.

There are two very important configuration properties that govern the default behavior of the consumer offset. 

These properties are optional because their defaults are sufficient for getting up and running. 

The first is **`enable.auto.commit`**, which is basically giving Kafka the responsibility to manage when current position offsets are upgraded to full committed offsets. This is a fairly blind setting because Kafka isn't going to know under what logical circumstances a record should be considered a committed record.   

The only thing it can do is establish an interval of time between commit actions that faithfully commit based on a frequency. That frequency is established by the **`auto.commit.interval`** property, and by default it is set to 5000 ms or 5 seconds. Now, for high throughput scenarios, 5 seconds is an eternity, and likely sufficient, but let's consider the biggest variable here for a moment, and that is your processing logic. 
When a record is in processing scope, let's say it has a current offset position of four because the last successfully committed record was three. Let's also suppose that for whatever reason, the processing of the current record takes longer than 5000 ms or whatever that interval is set to. 
Faithfully, Kafka is going to commit that record's offset, regardless if it is finished processing or not, because unless if you tell it explicitly when it's done, how is it supposed to know? 

So to recap and summarize for now on offset behavior. Remember, just because something is read doesn't mean it's committed. A lot of things determine this, and it is very subjective depending on the offset management mode you're operating in. The offset management mode is determined by the offset configuration properties. First and foremost is whether you want Kafka to manage your commits for you. The default is true, because it is very convenient from a development standpoint, but as we saw, depending on the situation, it can be operationally inconvenient if there's an issue. It's a lot like garbage collection in modern programming languages. 
It's very convenient until it is inconvenient. The challenge is generally to have some sort of control to govern when it is tolerable to be inconvenient. Fortunately in Kafka, you can adjust the commit frequency to be in line with your particular consumer application. This is the commit interval we discussed earlier. 
Lengthening this interval will provide an upper bound in which you can ensure your record processing will be finished, but it could also create an offset gap in the opposite direction, where the commits are lagging behind your processing positions. As long as there is a gap, there is some risk exposure to failure, and the possible inconsistent state you may be left with to clean up. 
Not to mention the possible duplication of records when reprocessing. Another property we haven't covered yet but will is the strategy to use when a consumer starts reading from a new partition. The default is to start reading from the latest known committed offset. 
In contrast, this could also be set to the earliest. There's also a setting for none, which basically you're asking Kafka to throw an exception to the consumer and let you decide what to do with it. The offset behavior and the issues related to it vary depending on whether you're in a single consumer or a consumer group topology. 

Kafka stores the committed offsets in a special topic called **__consumer_offsets.** 
If you were to issue a describe command to the cluster asking it to show you all of the topics and their partitions, you would notice this consumer offsets topic, and it would have 50 partitions. 

## commitAsynch and commitSynch

To switch to manual mode, you simply set enable.auto.commit property to false. Of course, by doing this, the property for auto commit interval is irrelevant and therefore ignored. 
When you do this, you are taking full control of when you want Kafka to consider a record to be fully processed. 

You would use the **commitSync** method when you want precise control over when to consider a record truly processed. This is common under circumstances where higher consistency and message processing fidelity is required, where you wouldn't want to retrieve and process new records until you're sure the ones you've currently processed are committed. It is suggested that you invoke this method after you have iterated and processed a batch of consumer records in the for loop, not during. I mean, you can invoke it after every single message, but that level of paranoia may not buy you anything extra other than added latency, because the call is, as the name suggests, synchronous, and will block the thread until it receives a response from the cluster. Hopefully the response is a successful confirmation because if it is an exception, there's not much you can do and you'll just have to start the process of recovery.
The good news about commitSync is that it will automatically retry the commit until it succeeds, or again, if it were to receive an unrecoverable error. To control the retry attempt interval, you would work with the retry.back.ms setting, and it's similar to the setting found in the producer configuration as well. The default is 100 ms, so it will retry a lot. 
With this manual offset management mode, you may be trading throughput and performance for control over the consistency. The synchronous blocking nature of the call can add a measure of latency to the overall polling process.

Like the commitSync method, you would use its asynchronous sibling to control when to consider your messages truly processed. 
The difference here is due to the asynchronous nature of the call, you may not know exactly when the commit succeeded or not. Because of this, the **commitAsync** method does not automatically retry when a commit doesn't happen. Retrying without knowing whether the first attempt succeeded of failed can lead to ordering issues and possible duplication of records; however, there is a useful option to pass in, and that is a callback. 
That callback will be triggered upon the commit response from the cluster. With this callback, you can determine the status of the commit and act accordingly. Since this is a non-blocking option, the throughput and overall performance is going to be better because you will not have to wait for a response to continue processing.
