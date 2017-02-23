package com.moussi.kafka.consumer;

import com.moussi.kafka.commons.KafkaUtils;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by amoussi on 21/02/17.
 */
public class KafkaConsumerAssignModeApp {

    public static void main(String[] args) {
        //Create a dictionary for the required/optional Producer config settings
        Properties props = new Properties();
        props.put(KafkaUtils.BOOTSTRAP_SERVERS, "localhost:9092, localhost:9094");
        props.put(KafkaUtils.KEY_DESERIALIZER, KafkaUtils.KAFKA_STRING_DESERIALIZER);
        props.put(KafkaUtils.VALUE_DESERIALIZER, KafkaUtils.KAFKA_STRING_DESERIALIZER);
        props.put(KafkaUtils.CONSUMER_GROUP_ID, "test");


        KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>(props);

        List<TopicPartition> topicPartitions = new ArrayList();
        TopicPartition topicPartition1 = new TopicPartition("myreptopic", 0);
        TopicPartition topicPartition2 = new TopicPartition("myreptopic", 2);
        TopicPartition topicPartition3 = new TopicPartition("myreptopic2", 1);

        topicPartitions.add(topicPartition1);
        topicPartitions.add(topicPartition2);
        topicPartitions.add(topicPartition3);

        kafkaConsumer.assign(topicPartitions);

        try {
            while (true) {
                ConsumerRecords<String, String> consumerRecords = kafkaConsumer.poll(10);
                consumerRecords.forEach(consumerRecord ->
                    printConsumerRecordInfo(consumerRecord)
                );
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            kafkaConsumer.close();
        }

    }

    private static void printConsumerRecordInfo(ConsumerRecord consumerRecord) {
        System.out.println(String.format(
            "Topic: %s , Partition : %s , Offset : %s , Key : %s , Value : %s",
            consumerRecord.topic(),
            consumerRecord.partition(),
            consumerRecord.offset(),
            consumerRecord.key(),
            consumerRecord.value()));
    }

}
