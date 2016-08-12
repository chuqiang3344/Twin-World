package com.tyaer.basic.queue;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;


public class KafkaSource {
    static Producer<String, String> producer = null;

    static {

        Properties props = new Properties();
        props.put("bootstrap.servers", "192.168.2.11:9092");
        props.put("acks", "all");
        props.put("retries", 0);
        props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        props.put("buffer.memory", 33554432);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        producer = new KafkaProducer<String, String>(props);

    }

    public static void sendNews(String news) {
        ProducerRecord<String, String> data = new ProducerRecord<String, String>("collectionInfo", news);
        Future<RecordMetadata> send = producer.send(data);
        try {
            send.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public static void close() {
        producer.close();
    }

    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            KafkaSource.sendNews(
                    "1217 -- 2016-07-13 10:42:25 -- 中 ?? -- 中国 --  火热七月，红歌嘹亮 ??2011 ?7 ?1日是伟大的中国共产党成立90周年。实践证明，没有中国共产党就没有新中国，就没有中国特色社会主义 ? 坚持中国特色社会主义道路，推进社会主义现代化，实现中华民族伟大复兴，必须毫不动摇地坚持中国共产党的领导。庆祝中国建 ?90周年...");
        }
        System.out.println("finish");
    }
}
