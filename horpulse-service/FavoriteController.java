package com.hotpulse.controller.favorite;

import com.hotpulse.common.Result;
import com.hotpulse.dto.favorite.FavoriteCreateDTO;
import com.hotpulse.service.favorite.FavoriteService;
import com.hotpulse.vo.favorite.FavoriteVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/favorite")
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;

    @GetMapping
    public Result<List<FavoriteVO>> list(@RequestParam Long userId) {
        return Result.success(favoriteService.listByUser(userId)
                .stream().map(FavoriteVO::from).collect(Collectors.toList()));
    }

    @PostMapping
    public Result<?> create(@RequestBody FavoriteCreateDTO favorite) {
        favoriteService.add(favorite.getUserId(), favorite.getNewsId(), favorite.getType());
        return Result.success("created");
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        favoriteService.delete(id);
        return Result.success("deleted");
    }
}
