package com.wzz.video.mapper;

import com.wzz.video.pojo.SearchRecords;
import com.wzz.video.utils.MyMapper;

import java.util.List;

public interface SearchRecordsMapper extends MyMapper<SearchRecords> {

    public List<String> getHotWords();

}