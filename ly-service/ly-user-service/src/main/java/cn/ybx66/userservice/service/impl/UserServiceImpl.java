package cn.ybx66.userservice.service.impl;

import cn.ybx66.conmmon.enums.ExceptionEnums;
import cn.ybx66.conmmon.exception.LyException;
import cn.ybx66.conmmon.pojo.PageResDTO;
import cn.ybx66.conmmon.utils.BeanUtil;
import cn.ybx66.conmmon.utils.Md5Utils;
import cn.ybx66.conmmon.vo.ResultDescDTO;
import cn.ybx66.conmmon.vo.ResultMessageDTO;
import cn.ybx66.userapi.pojo.Role;
import cn.ybx66.userapi.pojo.User;
import cn.ybx66.userservice.dto.UserReqDTO;
import cn.ybx66.userservice.dto.UserResDTO;
import cn.ybx66.userservice.dto.userDTO;
import cn.ybx66.userservice.mapper.PermissionsMapper;
import cn.ybx66.userservice.mapper.RoleMapper;
import cn.ybx66.userservice.mapper.UserMapper;
import cn.ybx66.userservice.service.RolesService;
import cn.ybx66.userservice.service.UserService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;


import java.util.Date;
import java.util.List;

import static cn.ybx66.conmmon.utils.Md5Utils.getMd5Code;

/**
 * @author Fox
 * @version 1.0
 * @date 2021/2/5 13:46
 * @description 用户业务处理  后续controller只负责业务逻辑 4-27
 */
@Service
public class UserServiceImpl implements UserService {

    private final static String SALT = "jh520";

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RolesService rolesService;
    @Autowired
    private PermissionsMapper permissionsMapper;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private StringRedisTemplate redisTemplate;

    private static final String YOUR_PHONE_KEY = "sms:phone:";
    /**
     * 存放数据
     * @param userDto
     * @return String
     */
    @Override
    public String saveUser(userDTO userDto) {
        User user = new User();
        BeanUtils.copyProperties(userDto,user);
        //获取redis中缓存的验证码
        String s = redisTemplate.opsForValue().get(YOUR_PHONE_KEY + userDto.getPhone());
        if (s==null || !s.equals(userDto.getMessage())) return "-2";
        User checkUser = this.getUser(user.getUsername());
        if (checkUser!=null){
           return null;
        }
        User userPhoen = new User();
        userPhoen.setPhone(userDto.getPhone());
        List<User> select = userMapper.select(userPhoen);
        if (select==null || select.size()==0){ }else { return "-1"; }
        //三重MD5+salt
        user.setPassword(getMd5Code(Md5Utils.getMd5CodeAndSalt(user.getPassword())));
        //设置盐 暂时不需要 保留
        user.setSalt("123456");
        //装配属性
        try {
             BeanUtil.autoSetAttrOnInsert(user);
        }catch (Exception e){
            throw new LyException(ExceptionEnums.ERROR);
        }
        //获取角色集合
        Role role = new Role();
        try {
            role.setRoleName("user");//初始化定位user
            role.setUserId(user.getId());
            BeanUtil.autoSetAttrOnInsert(role);
        }catch (Exception e){
            throw new LyException(ExceptionEnums.ERROR);
        }
        //角色新增
        rolesService.insertRoles(role);
        //用户新增
        Integer status= userMapper.insert(user);
        //移除验证码信息
        redisTemplate.delete(YOUR_PHONE_KEY + userDto.getPhone());
        return status.toString();
    }

    @Override
    public int update(User user) {
        User checkUser = this.getUser(user.getUsername());
        if (checkUser==null){
            return 0;
        }
        user.setId(checkUser.getId());
        user.setPassword(getMd5Code(Md5Utils.getMd5CodeAndSalt(user.getPassword())));
        if (StringUtils.isBlank(user.getPhone())) user.setPhone(checkUser.getPhone());
        if (StringUtils.isBlank(user.getHeadImage())) user.setHeadImage(checkUser.getHeadImage());
        if (user.getFlag()==null ) user.setFlag(checkUser.getFlag());
        if (StringUtils.isBlank(user.getSign())) user.setSign(checkUser.getSign());
        if (StringUtils.isBlank(user.getSalt())) user.setSalt(checkUser.getSalt());

        int status = userMapper.updateByPrimaryKey(user);
        //更新角色信息
        //获取角色集合
        List<Role> roles = user.getRoles();
        if (roles!=null){
            //新增角色信息
            for (int i = 0; i < roles.size(); i++) {
                rolesService.updateRoles(roles.get(i));
            }
        }
        return status;
    }

    @Override
    public int updateById(User user) {
        //限制 1.用户名唯一 手机号码唯一 方法：数据库设置唯一键
        User user1 = userMapper.selectByPrimaryKey(user.getId());
        if (user1==null) return 0;

        User userPhoen = new User();
        userPhoen.setPhone(user.getPhone());
        List<User> select = userMapper.select(userPhoen);
        if (select==null || select.size()==0){ }else { return -2; }
        user.setPassword(getMd5Code(Md5Utils.getMd5CodeAndSalt(user.getPassword())));
        user.setLastTime(new Date());
        if (user.getFlag()==null)user.setFlag(user1.getFlag());
        if (user.getSalt()==null)user.setSalt(user1.getSalt());
        return userMapper.updateByPrimaryKey(user);
    }

    /**
     * 查询用户是否存在
     * @param userName
     * @return
     */
    @Override
    public User getUser(String userName) {
        User user = new User();
        user.setUsername(userName);
        user.setFlag(1);
        List<User> userList = userMapper.select(user);
        if (userList==null || userList.size()==0){
            return null;
        }
        return getUserInfoALL(userList.get(0));
    }

    /**
     * Login登陆
     * @param userName
     * @param password
     * @return
     */
    @Override
    public User getUser(String userName, String password) {
        User checkUser = this.getUser(userName);
        if (checkUser!=null){
            User user = new User();
            user.setId("-1");
            return user;
        }
        User user = new User();
        user.setUsername(userName);
        List<User> userList = userMapper.selectByExample(user);
        //md5+盐 加密对比
        String code = getMd5Code(password);
        if (!code.equals(getMd5Code(userList.get(0).getPassword()))){
            user.setId("-2");
            return user;
        }
        User userInfo = userList.get(0);
        return getUserInfoALL(userInfo);
    }

    /**
     * 获取未被限制所有用户全部信息 admin
     * @return
     */
    @Override
    public ResultMessageDTO getUserAll() {
        User user = new User();
        user.setFlag(1);
        List<User> select = userMapper.select(user);
        return new ResultMessageDTO(200,ResultDescDTO.OK,select);
    }

    @Override
    public ResultMessageDTO getUserInfo(String userId) {
        User user = userMapper.selectByPrimaryKey(userId);
        if (user==null || user.getFlag()==0){
            return new ResultMessageDTO(400,ResultDescDTO.FAIL,"用户不存在或被限制");
        }
        UserResDTO userResDTO = new UserResDTO();
        BeanUtils.copyProperties(user,userResDTO);
        Role role = new Role();
        role.setFlag(1);
        role.setUserId(userId);
        List<Role> select = roleMapper.select(role);
        userResDTO.setRoles(select);
        return new ResultMessageDTO(200,ResultDescDTO.OK,userResDTO);
    }

    @Override
    public ResultMessageDTO deleteUser(String userId) {
        User user = userMapper.selectByPrimaryKey(userId);
        if (user==null || user.getFlag()==0){
            return new ResultMessageDTO(400,ResultDescDTO.FAIL,"用户不存在或被限制");
        }
        user.setFlag(0);
        userMapper.updateByPrimaryKey(user);
        return new ResultMessageDTO(200,ResultDescDTO.OK,user);
    }

    @Override
    public ResultMessageDTO getUserByUserOrPhone(UserReqDTO userReqDTO) {
        //初始化可用
//        if (user.getUsername()==null && user.getPhone()==null){
//            return new ResultMessageDTO(400,ResultDescDTO.FAIL,"查询参数为空");
//        }
        List<User> select = null;
        int sum =0;
        try{
            Example example = new Example(User.class);
            Example.Criteria criteria = example.createCriteria();
            if (userReqDTO.getPhone()!=null && !userReqDTO.getPhone().equals("")){
                criteria.andEqualTo("phone",userReqDTO.getPhone());
            }
            if (userReqDTO.getUsername()!=null && !userReqDTO.getUsername().equals("")){
                criteria.andEqualTo("username",userReqDTO.getUsername());
            }
            criteria.andEqualTo("flag",1);
            //分页
            Page<User> users = PageHelper.startPage(userReqDTO.getPage(), userReqDTO.getLimit());
            select = userMapper.selectByExample(example);
            //解析分页数据 此处需要注意的是getTotal() 方法 不是PageInfo的子类 获取到的只能是数组的长度
            PageInfo<User> userPageInfo = new PageInfo<>(users.getResult());
            sum = (int)userPageInfo.getTotal();
        }catch (Exception e){
            e.printStackTrace();
            return new ResultMessageDTO(400,ResultDescDTO.FAIL,"异常");
        }

        return new ResultMessageDTO(200,ResultDescDTO.OK,new PageResDTO(sum,select));
    }


    /**
     * 查询出用户的角色及其权限
     * @param user
     * @return
     */
    public User getUserInfoALL(User user){
        //通过用户id获取角色
        Role role = new Role();
        role.setUserId(user.getId());
        List<Role> roleList = roleMapper.select(role);
//        for(Role setRole:roleList){
//            //为用户添加权限
//            Permissions permissions = new Permissions();
//            permissions.setRoleId(setRole.getId());
//            List<Permissions> permissionsList =  permissionsMapper.select(permissions);
//            if (permissionsList==null||permissionsList.size()==0){
//                return null;
//            }
//            setRole.setPermissions(permissionsList);
//        }
        user.setRoles(roleList);
        return user;
    }


}
