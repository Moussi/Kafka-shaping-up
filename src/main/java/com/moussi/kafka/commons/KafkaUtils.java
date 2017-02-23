package com.moussi.kafka.commons;

/**
 * Created by amoussi on 23/02/17.
 */
public class KafkaUtils {

    public static final String BOOTSTRAP_SERVERS = "bootstrap.servers";

    /* Serialisation properties */
    public static final String KEY_SERIALIZER = "key.serializer";
    public static final String VALUE_SERIALIZER = "value.serializer";

    public static final String KEY_DESERIALIZER = "key.deserializer";
    public static final String VALUE_DESERIALIZER = "value.deserializer";

    public static final String KAFKA_STRING_SERIALIZER = "org.apache.kafka.common.serialization.StringSerializer";
    public static final String KAFKA_STRING_DESERIALIZER = "org.apache.kafka.common.serialization.StringDeserializer";

    /* Consumer properties */
    public static final String CONSUMER_GROUP_ID = "group.id";
}
