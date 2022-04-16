package com.dpwgc.ringlog.service;

import com.dpwgc.ringlog.dao.LogMsg;
import com.dpwgc.ringlog.util.ResultUtil;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 日志检索服务
 */
@Service
public class LogService {

    @Resource
    ElasticsearchRestTemplate elasticsearchTemplate;

    /**
     * 检索日志（通配符+时间范围）
     * @param value1 检索条件1
     * @param value2 检索条件2
     * @param value3 检索条件3
     * @param start 时间区间开头
     * @param end 时间区间结尾
     * @return ResultUtil<Object>
     */
    public ResultUtil<Object> search(String value1,String value2,String value3,long start,long end){

        ResultUtil<Object> resultUtil = new ResultUtil<>();

        //根据一个值查询多个字段  并高亮显示  这里的查询是取并集，即多个字段只需要有一个字段满足即可
        //需要查询的字段（通配符查询）

        BoolQueryBuilder boolQueryBuilder= QueryBuilders.boolQuery();

        if (value1.length() > 0) {
            boolQueryBuilder.must(QueryBuilders.boolQuery()
                    .should(QueryBuilders.wildcardQuery("tag",value1))
                    .should(QueryBuilders.wildcardQuery("content",value1))
                    .should(QueryBuilders.wildcardQuery("note",value1))
                    .should(QueryBuilders.wildcardQuery("host",value1))
                    .should(QueryBuilders.wildcardQuery("file",value1)));
        }

        if (value2.length() > 0) {
            boolQueryBuilder.must(QueryBuilders.boolQuery()
                    .should(QueryBuilders.wildcardQuery("tag",value2))
                    .should(QueryBuilders.wildcardQuery("content",value2))
                    .should(QueryBuilders.wildcardQuery("note",value2))
                    .should(QueryBuilders.wildcardQuery("host",value2))
                    .should(QueryBuilders.wildcardQuery("file",value2)));
        }

        if (value3.length() > 0) {
            boolQueryBuilder.must(QueryBuilders.boolQuery()
                    .should(QueryBuilders.wildcardQuery("tag",value3))
                    .should(QueryBuilders.wildcardQuery("content",value3))
                    .should(QueryBuilders.wildcardQuery("note",value3))
                    .should(QueryBuilders.wildcardQuery("host",value3))
                    .should(QueryBuilders.wildcardQuery("file",value3)));
        }

        boolQueryBuilder.must(QueryBuilders.rangeQuery("time").from(start).to(end));

        //构建高亮查询
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(boolQueryBuilder)
                .withHighlightFields(
                        new HighlightBuilder.Field("tag")
                        ,new HighlightBuilder.Field("content")
                        ,new HighlightBuilder.Field("note")
                        ,new HighlightBuilder.Field("host")
                        ,new HighlightBuilder.Field("file"))
                .withHighlightBuilder(new HighlightBuilder().preTags("<span style='color:red'>").postTags("</span>"))
                .build();
        //查询
        SearchHits<LogMsg> search = elasticsearchTemplate.search(searchQuery, LogMsg.class);
        //得到查询返回的内容
        List<SearchHit<LogMsg>> searchHits = search.getSearchHits();
        //设置一个最后需要返回的实体类集合
        List<LogMsg> logs = new ArrayList<>();
        //遍历返回的内容进行处理
        for(SearchHit<LogMsg> searchHit:searchHits){
            //高亮的内容
            Map<String, List<String>> highlightFields = searchHit.getHighlightFields();
            //将高亮的内容填充到content中
            searchHit.getContent().setTag(highlightFields.get("tag")==null ? searchHit.getContent().getTag():highlightFields.get("tag").get(0));
            searchHit.getContent().setContent(highlightFields.get("content")==null ? searchHit.getContent().getContent():highlightFields.get("content").get(0));
            searchHit.getContent().setNote(highlightFields.get("note")==null ? searchHit.getContent().getNote():highlightFields.get("note").get(0));
            searchHit.getContent().setHost(highlightFields.get("host")==null ? searchHit.getContent().getHost():highlightFields.get("host").get(0));
            searchHit.getContent().setFile(highlightFields.get("file")==null ? searchHit.getContent().getFile():highlightFields.get("file").get(0));
            //放到实体类中
            logs.add(searchHit.getContent());
        }

        //返回数据
        resultUtil.setCode(200);
        resultUtil.setMsg("ok");
        resultUtil.setData(logs);
        return resultUtil;
    }
}
