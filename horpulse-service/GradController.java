package com.hotpulse.controller.grad;

import com.hotpulse.common.Result;
import com.hotpulse.dto.grad.RecommendQueryDTO;
import com.hotpulse.service.grad.GradRecommendService;
import com.hotpulse.vo.grad.RecommendVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/grad")
@RequiredArgsConstructor
public class GradController {

    private final GradRecommendService gradRecommendService;

    @PostMapping("/recommend")
    public Result<List<RecommendVO>> recommend(@RequestBody RecommendQueryDTO query) {
        return Result.success(gradRecommendService.recommend(query));
    }
}
