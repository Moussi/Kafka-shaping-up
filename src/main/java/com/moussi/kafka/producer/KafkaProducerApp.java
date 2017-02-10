package com.moussi.kafka.producer;

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

    public static final String BOOTSTRAP_SERVERS = "bootstrap.servers";
    public static final String KEY_SERIALIZER = "key.serializer";
    public static final String VALUE_SERIALIZER = "value.serializer";

    public static void main(String[] args) {
        //Create a dictionary for the required/optional Producer config settings
        Properties props = new Properties();
        props.put(BOOTSTRAP_SERVERS, "localhost:9092, localhost:9093");
        props.put(KEY_SERIALIZER, "org.apache.kafka.common.serialization.StringSerializer");
        props.put(VALUE_SERIALIZER, "org.apache.kafka.common.serialization.StringSerializer");

        final KafkaProducer<String, String> myProducer = new KafkaProducer<String, String>(props);

        IntStream.range(0, 150).forEach(nbr -> {
            String message = "MyMessage " + Integer.toString(nbr);
            LOGGER.debug("Sending message {}", message);
            ProducerRecord<String, String>
                producerRecord =
                new ProducerRecord<>("my_topic", Integer.toString(nbr), message);
            myProducer.send(producerRecord);
        });
    }
}
