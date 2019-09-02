package com.wzz.video.service.Impl;

import com.wzz.video.mapper.BgmMapper;
import com.wzz.video.pojo.Bgm;
import com.wzz.video.service.BgmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

import java.util.List;

@Service
public class BgmServiceImpl implements BgmService {

    @Autowired
    private BgmMapper bgmMapper;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<Bgm> queryBgmList() {
        return bgmMapper.selectAll();
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Bgm queryBgmById(String bgmId) {
        Example bgmExample = new Example(Bgm.class);
        Criteria criteria = bgmExample.createCriteria();
        criteria.andEqualTo("id" ,bgmId);
        return bgmMapper.selectOneByExample(bgmExample);
    }
}