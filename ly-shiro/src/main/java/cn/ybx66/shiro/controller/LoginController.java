package cn.ybx66.shiro.controller;

import cn.ybx66.conmmon.vo.ResultDescDTO;
import cn.ybx66.conmmon.vo.ResultMessageDTO;
import cn.ybx66.shiro.service.LoginService;
import cn.ybx66.shiro.vo.UsersDTO;
import cn.ybx66.userapi.feigin.UserFigin;
import cn.ybx66.userapi.pojo.Role;
import cn.ybx66.userapi.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

@RestController
@Slf4j
public class LoginController {

    @Autowired
    private UserFigin userFigin;
    @Autowired
    private LoginService loginService;

    @PostMapping("/login")
    public ResultMessageDTO login(@RequestBody User user, HttpServletRequest request, HttpServletResponse response) {

        //用户认证信息
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(
                user.getUsername(),
                user.getPassword()
        );
        try {
            //进行验证，这里可以捕获异常，然后返回对应信息
            subject.login(usernamePasswordToken);

//            subject.checkRole("admin");
//            subject.checkPermissions("query", "add");
        } catch (UnknownAccountException e) {
            log.error("用户名不存在！", e);
            return new ResultMessageDTO(400, ResultDescDTO.OK,"用户名不存在！");
        } catch (AuthenticationException e) {
            log.error("账号或密码错误！", e);
            return new ResultMessageDTO(400, ResultDescDTO.OK,"账号或密码错误！");
        } catch (AuthorizationException e) {
            log.error("没有权限！", e);
            return new ResultMessageDTO(401, ResultDescDTO.OK,"没有权限！");
        }
        //登录成功 更新用户一些属性
        user.setIp(request.getRemoteAddr());
        user.setLastTime(new Date());
        userFigin.update(user);
        //获取用户信息 并返回
        User byName = loginService.getUserByName(user.getUsername());
        UsersDTO usersDTO = new UsersDTO();
        BeanUtils.copyProperties(byName,usersDTO);
        //处理角色信息
        List<Role> roles = byName.getRoles();
        if (roles!=null && roles.size()!=0){
            String[] role = new String[roles.size()];
            for (int i = 0; i < roles.size(); i++) {
                //装配角色信息
                role[i]=roles.get(i).getRoleName();
            }
            usersDTO.setRoles(role);
        }
        return new ResultMessageDTO(200, ResultDescDTO.OK,usersDTO);
    }

//    @RequiresRoles("admin")
//    @GetMapping("/admin")
//    public String admin() {
//        return "admin success!";
//    }
//
//    @RequiresPermissions("query")
//    @GetMapping("/index")
//    public String index() {
//        return "index success!";
//    }
//
//    @RequiresPermissions("add")
//    @GetMapping("/add")
//    public String add() {
//        return "add success!";
//    }

    /**
     * 用户下线 会删掉redis以及cookies信息
     * @return
     */
    @GetMapping("/logout")
    public ResultMessageDTO logout(){
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return new ResultMessageDTO(200,ResultDescDTO.OK,"success");
    }

    /**
     * 获取用户信息
     * @return
     */
    @RequestMapping("/get")
    public ResultMessageDTO get(){
        Object user =  SecurityUtils.getSubject().getPrincipal();
        return new ResultMessageDTO(200,ResultDescDTO.OK,user);
    }

}
