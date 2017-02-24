package com.moussi.kafka.consumer.group;

import com.moussi.kafka.commons.KafkaUtils;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by amoussi on 21/02/17.
 */
public class KafkaConsumerGroupModeApp02 {

    public static void main(String[] args) {
        //Create a dictionary for the required/optional Producer config settings
        Properties props = new Properties();
        props.put(KafkaUtils.BOOTSTRAP_SERVERS, "localhost:9092, localhost:9094");
        props.put(KafkaUtils.KEY_DESERIALIZER, KafkaUtils.KAFKA_STRING_DESERIALIZER);
        props.put(KafkaUtils.VALUE_DESERIALIZER, KafkaUtils.KAFKA_STRING_DESERIALIZER);
        props.put(KafkaUtils.CONSUMER_GROUP_ID, "test-group");


        KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>(props);

        List<String> topics = new ArrayList();
        topics.add("myreptopic");

        kafkaConsumer.subscribe(topics);

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
