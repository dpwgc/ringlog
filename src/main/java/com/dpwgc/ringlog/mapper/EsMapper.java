package com.dpwgc.ringlog.mapper;

import com.dpwgc.ringlog.dao.LogMsg;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface EsMapper extends ElasticsearchRepository<LogMsg,String> {
}

