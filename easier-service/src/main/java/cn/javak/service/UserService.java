package cn.javak.service;

import cn.javak.mapper.UserMapper;
import cn.javak.mapper.UserRoleMapper;
import cn.javak.pojo.RespBean;
import cn.javak.pojo.User;
import cn.javak.pojo.UserRoleKey;
import cn.javak.token.TokenUtil;
import cn.javak.utils.AddressConfig;
import cn.javak.utils.Constants;
import cn.javak.utils.FTPUtils;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;

/**
 * @author: theTian
 * @date: 2020/5/27 23:23
 */
@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
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

    public RespBean updateAvatar(MultipartFile file) throws IOException {
        Integer userId = TokenUtil.getTokenUserId();
        if (null == userId){
            return RespBean.error("tokenError");
        }
        if (!file.isEmpty()) {
            String fileName = System.currentTimeMillis() + file.getOriginalFilename();
            boolean saveOk = FTPUtils.uploadFile(FTPUtils.loginFTP(), Constants.ADDRESS.AVATAR_ABSOLUTE_DIR, fileName, file.getInputStream());
            if (saveOk){
                String newAvatarAddress = Constants.ADDRESS.AVATAR_NET_ADDRESS + fileName;
                User user = new User();
                user.setUserId(userId);
                user.setHeadPic(newAvatarAddress);
                userMapper.updateByPrimaryKeySelective(user);
                logger.info("用户"+userId+"修改头像成功,新头像为"+newAvatarAddress);
                JSONObject object = new JSONObject();
                object.put("newHeadPic", newAvatarAddress);
                return RespBean.ok("修改头像成功", object);
            }else {
                logger.error("修改头像失败");
                return RespBean.error("修改头像失败");
            }
        }
        return RespBean.error("修改头像失败");
    }
}
