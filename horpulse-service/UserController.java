package com.hotpulse.controller.user;

import com.hotpulse.common.Result;
import com.hotpulse.dto.user.LoginRequestDTO;
import com.hotpulse.service.user.UserService;
import com.hotpulse.vo.user.LoginVO;
import com.hotpulse.vo.user.UserVO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public Result<?> register(@RequestBody LoginRequestDTO req) {
        try {
            userService.register(req.getUsername(), req.getPassword());
            return Result.success("register success");
        } catch (Exception e) {
            return Result.fail(400, e.getMessage());
        }
    }

    @PostMapping("/login")
    public Result<LoginVO> login(@RequestBody LoginRequestDTO req) {
        try {
            var loginResult = userService.login(req.getUsername(), req.getPassword());
            LoginVO vo = new LoginVO();
            vo.setToken((String) loginResult.get("token"));
            vo.setUserId((Long) loginResult.get("userId"));
            vo.setUsername((String) loginResult.get("username"));
            vo.setAvatar((String) loginResult.get("avatar"));
            return Result.success(vo);
        } catch (Exception e) {
            return Result.fail(401, e.getMessage());
        }
    }

    @GetMapping("/info")
    public Result<UserVO> info(Authentication auth) {
        Long userId = (Long) auth.getPrincipal();
        return Result.success(UserVO.from(userService.getById(userId)));
    }

    @PutMapping("/avatar")
    public Result<?> updateAvatar(Authentication auth, @RequestBody Map<String, String> body) {
        Long userId = (Long) auth.getPrincipal();
        String avatar = body.get("avatar");
        if (avatar == null || avatar.isBlank()) {
            return Result.fail(400, "avatar is required");
        }
        userService.updateAvatar(userId, avatar);
        return Result.success("avatar updated");
    }

    @PutMapping("/username")
    public Result<?> updateUsername(Authentication auth, @RequestBody Map<String, String> body) {
        Long userId = (Long) auth.getPrincipal();
        String username = body.get("username");
        if (username == null || username.isBlank()) {
            return Result.fail(400, "username is required");
        }
        try {
            userService.updateUsername(userId, username);
            return Result.success("username updated");
        } catch (Exception e) {
            return Result.fail(400, e.getMessage());
        }
    }

    @PutMapping("/password")
    public Result<?> updatePassword(Authentication auth, @RequestBody Map<String, String> body) {
        Long userId = (Long) auth.getPrincipal();
        String oldPassword = body.get("oldPassword");
        String newPassword = body.get("newPassword");
        if (oldPassword == null || newPassword == null || newPassword.isBlank()) {
            return Result.fail(400, "oldPassword and newPassword are required");
        }
        try {
            userService.updatePassword(userId, oldPassword, newPassword);
            return Result.success("password updated");
        } catch (Exception e) {
            return Result.fail(400, e.getMessage());
        }
    }
}
