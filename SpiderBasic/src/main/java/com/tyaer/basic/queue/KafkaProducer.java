package com.tyaer.basic.queue;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.util.Properties;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by yizuotian on 16-4-21.
 */
public class KafkaProducer extends Thread {
    private static final Logger log = Logger.getLogger(KafkaProducer.class);  
    private static final String KAFKA_ADDRESS="";
	private static int messageNo = 1;
	private static Lock lock = new ReentrantLock();
	private int threadNo;
	private final Producer<Integer, String> producer;
	private final String topic = "";
	private final Properties props = new Properties();

	private KafkaProducer(int threadNo) {
		this.threadNo = threadNo;
		props.setProperty("serializer.class","kafka.serializer.StringEncoder");
//      props.setProperty("key.serializer.class", "com.toroot.bean.VehicleSerializer");
//		props.put("serializer.class", "com.toroot.bean.VehicleDecoder");
		props.put("metadata.broker.list", "192.168.1.51:9092");
		producer = new Producer<Integer, String>(
				new ProducerConfig(props));
	}
	
	private static class LazyHolder {
		private static final KafkaProducer INSTANCE=new KafkaProducer(); 
		static{
			System.out.println("a");
		}
	}
	
	public static final KafkaProducer getInstance(){
		return LazyHolder.INSTANCE;
	}
	
	private KafkaProducer( ) {
		props.setProperty("serializer.class","kafka.serializer.StringEncoder");
//      props.setProperty("key.serializer.class", "com.toroot.bean.VehicleSerializer");
//		props.put("serializer.class", "com.toroot.bean.VehicleDecoder");
		props.put("metadata.broker.list", KAFKA_ADDRESS);
		producer = new Producer<Integer, String>(
				new ProducerConfig(props));
	}

	/**
	 * 发送xml数据到kafka
	 * @param topic
	 * @param xml
	 */
	public void sendXmltoKaf(String topic,String xml){
		producer.send(new KeyedMessage<Integer, String>(topic,xml));
	}

	public static void main(String[] args) {
    	PropertyConfigurator.configure(System.getProperty("user.dir")+"/configure/log4j.properties");
//		System.out.println(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
		for (int i = 0; i < 10; i++) {
			KafkaProducer producerThread = new KafkaProducer(i);
			producerThread.start();
//			break;
		}
	}
}