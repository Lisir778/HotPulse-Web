package com.hotpulse.service.hotrank.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hotpulse.entity.hotrank.HotRank;
import com.hotpulse.mapper.hotrank.HotRankMapper;
import com.hotpulse.service.hotrank.HotRankService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HotRankServiceImpl implements HotRankService {

    private final HotRankMapper hotRankMapper;

    @Override
    public List<HotRank> list(String platform) {
        LambdaQueryWrapper<HotRank> wrapper = new LambdaQueryWrapper<HotRank>()
                .orderByAsc(HotRank::getRankNum);
        if (platform != null && !platform.isBlank()) {
            wrapper.eq(HotRank::getPlatform, platform);
        }
        return hotRankMapper.selectList(wrapper);
    }

    @Override
    public void save(HotRank hotRank) {
        hotRankMapper.insert(hotRank);
    }
}
