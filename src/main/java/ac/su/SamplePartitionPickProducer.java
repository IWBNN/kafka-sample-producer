package ac.su;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class SamplePartitionPickProducer {
    private final static Logger logger = LoggerFactory.getLogger(SamplePartitionPickProducer.class);
    private final static String TOPIC_NAME = "first_topic";
    private final static String BOOTSTRAP_SERVERS = "kbroker001:9092,kbroker002:9092,kbroker003:9092";
    public static void main(String[] args) {
        // 1) producer ����
        KafkaProducer<String, String> producer = getKafkaProducer();
        // 2) �޽��� ���ڵ� ������ Partition ����� ����
        int partitionNo;
        for (int i = 0; i < 3; i++) {
            partitionNo = i;
            for (int j = 0; j < 5; j++) {
                ProducerRecord<String, String> record = new ProducerRecord<>(
                        TOPIC_NAME, partitionNo, // Record �� ������ Ư�� ��Ƽ�� Ÿ���� ������ ���
                        "RecordKey_" + j,        // ���ڵ� Ű�� �ٲ� ���� ������ ���� ��Ƽ�� ������
                        "partition picked not by key j, but by partitionNo param i: " + i
                );
                producer.send(record);
                logger.info("[Record check] partition: {} key: {}", record.partition(), record.key());
            }
        }

        producer.flush();
        producer.close();
    }

    static KafkaProducer<String, String> getKafkaProducer() {
        Properties configs = new Properties();
        configs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        configs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        configs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        return new KafkaProducer<>(configs);
    }
}