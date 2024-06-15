package project.realtimechatapplication.repository.elasticsearch;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import project.realtimechatapplication.entity.elasticsearch.ElasticsearchChatRoomEntity;

import java.util.List;

@Repository
public interface ElasticsearchChatRoomRepository extends ElasticsearchRepository<ElasticsearchChatRoomEntity, Long> {
    List<ElasticsearchChatRoomEntity> findByNameContaining(String name);
}
