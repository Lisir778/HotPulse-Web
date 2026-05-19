package com.hotpulse.controller.hotrank;

import com.hotpulse.common.Result;
import com.hotpulse.dto.hotrank.HotRankCreateDTO;
import com.hotpulse.dto.hotrank.HotRankQueryDTO;
import com.hotpulse.entity.hotrank.HotRank;
import com.hotpulse.service.hotrank.HotRankService;
import com.hotpulse.vo.hotrank.HotRankVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/hotrank")
@RequiredArgsConstructor
public class HotRankController {

    private final HotRankService hotRankService;

    @GetMapping
    public Result<List<HotRankVO>> list(@RequestParam(required = false) String platform) {
        HotRankQueryDTO queryDTO = new HotRankQueryDTO();
        queryDTO.setPlatform(platform);
        return Result.success(hotRankService.list(queryDTO.getPlatform())
                .stream().map(HotRankVO::from).collect(Collectors.toList()));
    }

    @PostMapping
    public Result<?> create(@RequestBody HotRankCreateDTO dto) {
        HotRank hotRank = new HotRank();
        hotRank.setNewsId(dto.getNewsId());
        hotRank.setPlatform(dto.getPlatform());
        hotRank.setRankNum(dto.getRankNum());
        hotRank.setHeatValue(dto.getHeatValue());
        hotRank.setUpdatedAt(dto.getUpdatedAt());
        hotRankService.save(hotRank);
        return Result.success("created");
    }
}
