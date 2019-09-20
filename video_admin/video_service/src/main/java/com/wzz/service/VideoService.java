package com.wzz.service;

import com.wzz.pojo.Bgm;
import com.wzz.utils.PagedResult;

public interface VideoService {

    /**
     * 添加bgm
     * @param bgm
     */
    public void addBgm(Bgm bgm);

    /**
     * 删除bgm
     * @param id
     */
    public void deleteBgm(String id);

    /**
     * 分页查询bgm列表
     * @param page
     * @param pageSize
     * @return
     */
    public PagedResult queryBgmList(Integer page , Integer pageSize);

    /**
     * 分页查询举报列表
     * @param page
     * @param pageSize
     * @return
     */
    PagedResult queryReportList(Integer page, Integer pageSize);

    /**
     * 更改视频状态
     * @param videoId
     * @param status
     */
    void updateVideoStatus(String videoId, Integer status);
}
