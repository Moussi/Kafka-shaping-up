package com.moussi.kafka.producer;

import com.moussi.kafka.commons.KafkaUtils;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.stream.IntStream;

/**
 * Created by amoussi on 10/02/17.
 */
public class KafkaProducerApp {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaProducerApp.class);
    public static final String MYREPTOPIC = "myreptopic";
    public static final String MYREPTOPIC_2 = "myreptopic2";

    public static void main(String[] args) {
        //Create a dictionary for the required/optional Producer config settings
        Properties props = new Properties();
        props.put(KafkaUtils.BOOTSTRAP_SERVERS, "localhost:9092, localhost:9094");
        props.put(KafkaUtils.KEY_SERIALIZER, KafkaUtils.KAFKA_STRING_SERIALIZER);
        props.put(KafkaUtils.VALUE_SERIALIZER, KafkaUtils.KAFKA_STRING_SERIALIZER);

        final KafkaProducer<String, String> myProducer = new KafkaProducer<>(props);

        IntStream.range(0, 150).forEach(nbr -> {
            String message = String.format("Topic : %s => My Message from Moussi %s" , MYREPTOPIC, Integer.toString(nbr));
            String message2 = String.format("Topic : %s => My Message from Moussi %s" , MYREPTOPIC_2, Integer.toString(nbr));
            LOGGER.debug("Sending message {}", message);
            ProducerRecord<String, String>
                producerRecord =
                new ProducerRecord<>(MYREPTOPIC, Integer.toString(nbr), message);
            ProducerRecord<String, String>
                producerRecord2 =
                new ProducerRecord<>(MYREPTOPIC_2, Integer.toString(nbr), message2);
            myProducer.send(producerRecord);
            myProducer.send(producerRecord2);
        });
    }
}
