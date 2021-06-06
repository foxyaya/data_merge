package cn.ybx66.permission.controller;

import java.util.concurrent.Callable;

import cn.ybx66.userapi.pojo.UserDto;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
public class AsyncRequestController {

	@GetMapping("/async")
	@RequiresRoles("admin")
	public Callable<UserDto> doAsync(){
		return ()->{
			Thread.sleep(5000);
			return (UserDto)SecurityUtils.getSubject().getPrincipal();
		};
	}
}
