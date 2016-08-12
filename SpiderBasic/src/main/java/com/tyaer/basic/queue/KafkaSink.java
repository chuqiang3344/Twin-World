package com.tyaer.basic.queue;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Arrays;
import java.util.Properties;

public class KafkaSink extends Thread {
    public static void main(String[] args) {
        consume();
    }

    public static void consume() {
        Properties props = new Properties();
        props.put("bootstrap.servers", "192.168.2.11:9092,192.168.2.12:9092,192.168.2.13:9092");
        props.put("zookeeper.connect", "192.168.2.11:2181");
        //group 代表一个消费组
        props.put("group.id", "g4");
        //连接超时
        props.put("zookeeper.session.timeout.ms", "4000");
        props.put("zookeeper.sync.time.ms", "200");
        props.put("auto.commit.interval.ms", "1000");
        props.put(org.apache.kafka.clients.consumer.ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props);

        consumer.subscribe(Arrays.asList("collectionInfo"));
        while (true) {

            ConsumerRecords<String, String> records = consumer.poll(100);

            for (ConsumerRecord<String, String> record : records) {
                System.out.printf("partition=%d,offset = %d, key = %s, value = %s", record.partition(), record.offset(), record.key(), record.value());
                System.out.println();
            }

            consumer.commitSync();
        }
    }
}