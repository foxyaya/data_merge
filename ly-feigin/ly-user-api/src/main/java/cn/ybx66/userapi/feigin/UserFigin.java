package cn.ybx66.userapi.feigin;

import cn.ybx66.conmmon.vo.ResultMessageDTO;
import cn.ybx66.userapi.config.FeignConfiguration;
import cn.ybx66.userapi.pojo.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * @author Fox
 * @version 1.0
 * @date 2021/2/5 9:46
 * @description 用户feigin接口
 */
@FeignClient(name = "user",configuration = FeignConfiguration.class)
@RequestMapping("/user")
public interface UserFigin {
    /**
     * 获取用户全部信息
     * @param username
     * @param password
     * @return
     */
    @GetMapping("/getUser/{username}/{password}")
    ResultMessageDTO getUser(@PathVariable("username") String username, @PathVariable("password") String password);
    /**
     * 通过用户名查询用户信息
     */
    @GetMapping("/getUser/{username}")
    ResultMessageDTO getUser(@PathVariable("username") String userName);

    @PutMapping("/update")
    public ResultMessageDTO update(@RequestBody User user);

    /**
     * 通过id获取用户信息
     * @param id
     * @return
     */
    @PostMapping("/getUserInfo")
    public ResultMessageDTO getUserInfo(@RequestBody String id);
}
