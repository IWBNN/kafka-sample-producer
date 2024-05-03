package ac.su;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class SampleProducer {
    private final static String BOOTSTRAP_SERVERS = "kbroker001:9092,kbroker002:9092,kbroker003:9092";
    private final static String TOPIC_NAME = "first_topic";
    private final static Logger logger = LoggerFactory.getLogger(SampleProducer.class);

    public static void main(String[] args) {
        // 1. .sh �� �������� ���� �⺻ ������ �Ǵ� '--param' ���� �־��� ���������� programming ������ �Ҵ�
        Properties configs = new Properties();
        configs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        configs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        configs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        // 2. producer ��ü ����
        KafkaProducer<String, String> producer = new KafkaProducer<>(configs);
        // producer ���� �׻� �۾��� ��ġ�� ���� ���� ������ �ڵ忡 ��������� �����ؾ� ��
        // 1. ����� close() ȣ��
        // 2. try - with - resource �������� producer ����
        for (int i = 0; i < 10; i++) {
            // 3. �޽��� ���� �� Record ���·� Topic �� ���ε�
            String msgValue = "this" + i + "msg is from java client ";
            ProducerRecord<String, String> record = new ProducerRecord<>(TOPIC_NAME, msgValue);
            // 4. �޽��� ���� ��� => �޽����� ���� ���� ��ġ�� �̸� ����
            producer.send(record); // ���ø����̼��� �߻���Ű�� �����ʹ� ��ġ�۾��� �����ؼ� ���� ��� �߱�
            // Partitioner ��� ��� �޽����� ������ ���� -> ��ġ �۾� �غ�
            logger.info("[Record Ready] {}", record);
        }
        // 5. Record ��ġ�� Broker�� ����
        producer.flush();
        producer.close();
    }
}
