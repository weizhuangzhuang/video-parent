package com.wzz.service.impl;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wzz.enums.BGMOperatorTypeEnum;
import com.wzz.mapper.BgmMapper;
import com.wzz.mapper.UsersReportMapper;
import com.wzz.mapper.UsersReportMapperCustom;
import com.wzz.mapper.VideosMapper;
import com.wzz.pojo.*;
import com.wzz.pojo.vo.Reports;
import com.wzz.service.VideoService;
import com.wzz.util.ZKCurator;
import com.wzz.utils.JsonUtils;
import com.wzz.utils.PagedResult;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class VideoServiceImpl implements VideoService {

    @Autowired
    private BgmMapper bgmMapper;

    @Autowired
    private VideosMapper videosMapper;

    @Autowired
    private UsersReportMapperCustom usersReportMapperCustom;

    @Autowired
    private Sid sid;

    @Autowired
    private ZKCurator zkCurator;

    @Override
    public void addBgm(Bgm bgm) {
        String bgmId = sid.nextShort();
        bgm.setId(bgmId);
        bgmMapper.insert(bgm);
        //添加节点
        Map<String , String> map = new HashMap<>();
        map.put("operType" , BGMOperatorTypeEnum.ADD.type);
        map.put("path" , bgm.getPath());
        zkCurator.sendBgmOperator(bgmId , JsonUtils.objectToJson(map));
    }

    @Override
    public void deleteBgm(String id) {
        Bgm bgm = bgmMapper.selectByPrimaryKey(id);
        bgmMapper.deleteByPrimaryKey(id);
        //删除节点
        Map<String , String> map = new HashMap<>();
        map.put("operType" , BGMOperatorTypeEnum.DELETE.type);
        map.put("path" , bgm.getPath());
        zkCurator.sendBgmOperator(id , JsonUtils.objectToJson(map));
    }

    @Override
    public PagedResult queryBgmList(Integer page, Integer pageSize) {
        PageHelper.startPage(page , pageSize);
        List<Bgm> bgmList = bgmMapper.selectByExample(new BgmExample());
        PageInfo<Bgm> pageList = new PageInfo<>(bgmList);

        PagedResult pagedResult = new PagedResult();

        pagedResult.setPage(page);
        //总记录数
        pagedResult.setRecords(pageList.getTotal());
        pagedResult.setRows(bgmList);
        //总页数
        pagedResult.setTotal(pageList.getPages());

        return pagedResult;
    }

    @Override
    public PagedResult queryReportList(Integer page, Integer pageSize) {
        PageHelper.startPage(page , pageSize);
        List<Reports> reports = usersReportMapperCustom.selectAllVideoReport();
        PageInfo<Reports> pageList = new PageInfo<>(reports);

        PagedResult pagedResult = new PagedResult();

        pagedResult.setPage(page);
        //总记录数
        pagedResult.setRecords(pageList.getTotal());
        pagedResult.setRows(reports);
        //总页数
        pagedResult.setTotal(pageList.getPages());

        return pagedResult;
    }

    @Override
    public void updateVideoStatus(String videoId, Integer status) {
        Videos video = new Videos();
        video.setId(videoId);
        video.setStatus(status);
        //有值才会更新
        videosMapper.updateByPrimaryKeySelective(video);
    }
}
