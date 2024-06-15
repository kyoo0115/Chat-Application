package project.realtimechatapplication.repository.elasticsearch;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import project.realtimechatapplication.entity.elasticsearch.ElasticsearchMessageEntity;

@Repository
public interface ElasticsearchMessageRepository extends ElasticsearchRepository<ElasticsearchMessageEntity, Long> {
    List<ElasticsearchMessageEntity> findByChatRoomIdAndMessageContaining(Long chatRoomId, String message);
}
