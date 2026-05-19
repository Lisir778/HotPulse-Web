package com.hotpulse.service.hotrank;

import com.hotpulse.entity.hotrank.HotRank;

import java.util.List;

public interface HotRankService {
    List<HotRank> list(String platform);
    void save(HotRank hotRank);
}
