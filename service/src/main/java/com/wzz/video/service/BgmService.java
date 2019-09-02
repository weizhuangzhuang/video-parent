package com.wzz.video.service;

import com.wzz.video.pojo.Bgm;

import java.util.List;

public interface BgmService {

    public List<Bgm> queryBgmList();

    public Bgm queryBgmById(String bgmId);

}
