package cn.javak.service;

import cn.javak.mapper.RoleMapper;
import cn.javak.mapper.UserMapper;
import cn.javak.mapper.UserRoleMapper;
import cn.javak.pojo.User;
import cn.javak.pojo.UserRoleKey;
import cn.javak.utils.AddressConfig;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author: theTian
 * @date: 2020/5/27 23:23
 */
@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserRoleMapper userRoleMapper;
    //登录 by username
    public User login(User user){
        User selUser = userMapper.selectByUsername(user.getUsername());
        return selUser;
    }

    //登录 by userId
    public User login(Integer userId){
        User selUser = userMapper.selectByPrimaryKey(userId);
        return selUser;
    }

    //注册
    public User signup(User user) {
        String curUsername = user.getUsername();
        if (null != userMapper.selectByUsername(curUsername)){
            return null;
        }
        //添加用户
        Date date = new Date();
        user.setBirthday(date);
        user.setRegisterTime(date);
        user.setGender(1);
        user.setNickName("用户_"+user.getUsername());
        //设置初始的20个精美头像
        int randomName = RandomUtils.nextInt(0, 21);
        user.setHeadPic(AddressConfig.HEAD_PIC_ADDRESS+ randomName +".png");
        userMapper.insertSelective(user);
        User selUser = userMapper.selectByUsername(curUsername);
        //初始化角色 普通用户
        userRoleMapper.insert(new UserRoleKey(selUser.getUserId(), 1));
        return selUser;
    }

    public void update(User user) {
        userMapper.updateByPrimaryKeySelective(user);
    }

    public User selEmail(String email) {
        return userMapper.selectByEmail(email);
    }

    public User selectByNN(String nickName) {
        return userMapper.selectByNN(nickName);
    }

    public User selectByUserId(Integer userId) {
        return userMapper.selectByPrimaryKey(userId);
    }
}
