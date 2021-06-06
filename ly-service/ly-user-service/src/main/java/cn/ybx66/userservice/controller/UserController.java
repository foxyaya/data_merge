package cn.ybx66.userservice.controller;

import cn.ybx66.conmmon.enums.ExceptionEnums;
import cn.ybx66.conmmon.exception.LyException;
import cn.ybx66.conmmon.vo.ResultDescDTO;
import cn.ybx66.conmmon.vo.ResultMessageDTO;
import cn.ybx66.userapi.pojo.User;
import cn.ybx66.userservice.dto.RoleDTO;
import cn.ybx66.userservice.dto.UserReqDTO;
import cn.ybx66.userservice.dto.userDTO;
import cn.ybx66.userservice.service.RolesService;
import cn.ybx66.userservice.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Fox
 * @version 1.0
 * 用户身份：admin    user
 * @date 2020/2/20 22:55
 */
@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RolesService rolesService;

    /**
     * 用户新增
     * @param user
     * @return 用户id
     */
    @PostMapping("/register")
    public ResultMessageDTO insert(@RequestBody userDTO user, HttpServletRequest request){
        if (user==null){
            throw new LyException(ExceptionEnums.USER_CANNOT_BE_NULL);
        }
        //获取注册ip
        user.setIp(request.getRemoteAddr());
        String s = userService.saveUser(user);
        if (StringUtils.isBlank(s)) return new ResultMessageDTO(400, ResultDescDTO.FAIL,"用户名已存在！");
        if (s.equals("-1")) return new ResultMessageDTO(400, ResultDescDTO.FAIL,"手机号码已存在！");
        if (s.equals("-2")) return new ResultMessageDTO(400, ResultDescDTO.FAIL,"验证码不正确，请检查！");
        return new ResultMessageDTO(201, ResultDescDTO.OK,"创建成功！");
    }

    /**
     * Feigin获取用户信息
     */
    @GetMapping("/getUser/{username}")
    public ResultMessageDTO getUser(@PathVariable("username") String username){
        User userInfo = userService.getUser(username);
        if (userInfo==null){
            return new ResultMessageDTO(400, ResultDescDTO.FAIL,"账户被限制，请联系管理员！");
        }
        return new ResultMessageDTO(200, ResultDescDTO.OK,userInfo);
    }
    /**
     * 用户更新
     * @param user
     * @return 用户id
     */
    @PutMapping("/updateById")
    public ResultMessageDTO updateById(@RequestBody User user,HttpServletRequest request){
        if (user==null){
            throw new LyException(ExceptionEnums.USER_CANNOT_BE_NULL);
        }
        user.setIp(request.getRemoteAddr());
        int update = userService.updateById(user);
        if (update==-2) return new ResultMessageDTO(400, ResultDescDTO.OK,"更新失败，请检查手机号码或用户名是否错误！");
        if (update==0){
            return new ResultMessageDTO(400, ResultDescDTO.OK,"更新失败，请检查手机号码或用户名是否错误！");
        }
        return new ResultMessageDTO(200, ResultDescDTO.OK,"更新成功！");
    }


    /**
     * 用户更新
     * @param user
     * @return 用户id
     */
    @PutMapping("/update")
    public ResultMessageDTO update(@RequestBody User user){
        if (user==null){
            throw new LyException(ExceptionEnums.USER_CANNOT_BE_NULL);
        }
        int update = userService.update(user);
        if (update==0){
            return new ResultMessageDTO(200, ResultDescDTO.OK,"更新失败！");
        }
        return new ResultMessageDTO(200, ResultDescDTO.OK,"更新成功！");
    }

    /**
     *
     * @param username
     * @param password
     * @return
     */
    @GetMapping("/getUser/{username}/{password}")
    public ResultMessageDTO getUser(@PathVariable("username") String username,@PathVariable("password") String password){
        User user = userService.getUser(username, password);
        if (user==null) return new ResultMessageDTO(400, ResultDescDTO.FAIL,"用户不存在");
        if (user.getId().equals("-1")){
            throw new LyException(ExceptionEnums.USER_BE_NULL);
        }else if (user.getId().equals("-2")){
            throw new LyException(ExceptionEnums.PASSWORD_ERROR);
        }
        return new ResultMessageDTO(200, ResultDescDTO.OK,user);
    }

    /**
     * 测试方法
     * @return
     */
    @GetMapping("/get")
    @RequiresRoles("admin")
    public ResponseEntity<String> getUser(){
        return ResponseEntity.status(HttpStatus.OK).body("success");
    }

    /**
     * 获取用户信息 管理员
     * @param user
     * @return
     */
    @PostMapping("/getUserByUserOrPhone")
    @RequiresRoles("admin")
    public ResultMessageDTO getUserByUserOrPhone(@RequestBody UserReqDTO userReqDTO){
        return userService.getUserByUserOrPhone(userReqDTO);
    }

    /**
     * 获取用户信息
     * @param id
     * @return
     */
    @PostMapping("/getUserInfo")
    public ResultMessageDTO getUserInfo(@RequestBody String id){
        return userService.getUserInfo(id);
    }

    /**
     * 删除用户
     * @param userId
     * @return
     */
    @DeleteMapping("/deleteUser")
    public ResultMessageDTO deleteUser(@RequestBody String userId){
        return userService.deleteUser(userId);
    }

    /**
     * 更新用户角色
     * @param roleDTO
     * @return
     */
    @PutMapping("/updateRolesByUserId")
    public ResultMessageDTO updateRolesByuserId(@RequestBody RoleDTO roleDTO){
        return rolesService.updateRolesByuserId(roleDTO);
    }

}
