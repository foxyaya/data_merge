//package cn.ybx66.item.controller;
//
//import cn.ybx66.conmmon.enums.ExceptionEnums;
//import cn.ybx66.conmmon.exception.LyException;
//import cn.ybx66.cn.ybx66.pojo.User;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
///**
// * @author Fox
// * @version 1.0
// * @date 2020/2/20 22:55
// */
//@RestController
//@RequestMapping("user")
//public class UserController {
//    @Autowired
//    private UserService userService;
//
//    @PostMapping("insert")
//    public ResponseEntity<User> insert(@RequestBody User user){
//        if (user.getPrice()==null){
////            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
//            throw new LyException(ExceptionEnums.PRICE_CANNOT_BE_NULL);
//        }
//        return ResponseEntity.status(HttpStatus.CREATED).body(userService.saveUser(user));
//    }
//}
