package ac.su;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;
import org.apache.kafka.common.InvalidRecordException;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.utils.Utils;

import java.util.List;
import java.util.Map;


public class CustomPartitioner implements Partitioner {

    @Override
    public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
        if (keyBytes == null) {
            throw new InvalidRecordException("Need message key");
        }
        // ��Ƽ�� �Ҵ��� key �ؽ̿� �ñ��� �ʰ�, Ư�� �޽��� ������ Ư�� partition �� �Ҵ�
        if (((String)key).startsWith("partitionZero")) // ��� ó���� 0�� ��Ƽ��
            return 0;
        if (((String)key).contains("step") || ((String)key).contains("finish")) // ���� �۾� ���� ����� 1�� ��Ƽ��
            return 1;
        if (((String)value).contains("customer") || ((String)value).contains("user")) // ���� ������ ó�� ����� 2�� ��Ƽ��
            return 2;

        // Ư�� ���ǿ� ������ ���� ��� murmur2 �ؽ� �� mod ������ ���� ���� �й�
        List<PartitionInfo> partitions = cluster.partitionsForTopic(topic);
        int numPartitions = partitions.size();
        return Utils.toPositive(Utils.murmur2(keyBytes)) % numPartitions;
    }

    @Override
    public void configure(Map<String, ?> configs) {}

    @Override
    public void close() {}
}
