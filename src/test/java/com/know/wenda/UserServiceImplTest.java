package com.know.wenda;

import com.know.wenda.base.KnownoWendaApplicationTests;
import com.know.wenda.dao.UserDAO;
import com.know.wenda.domain.UserDO;
import com.know.wenda.service.IUserService;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Random;

/**
 * UserServiceImplTest
 *
 * @author hlb
 */
public class UserServiceImplTest extends KnownoWendaApplicationTests {

    @Autowired
    private IUserService userService;
    @Autowired
    private UserDAO userDAO;

    @Test
    public void addUser(){
        Random random = new Random();
        UserDO userDO = new UserDO();
        userDO.setHeadUrl(String.format("http://images.nowcode.com/head/%dt.png",random.nextInt(1000)));
        userDO.setName(String.format("USER%d",1));
        userDO.setPassword("123456");
        userDO.setSalt("666");
        int result = userDAO.insert(userDO);
        Assert.assertEquals(1,result);

    }

    @Test
    public void selectUser(){
        UserDO userDO = userDAO.get(1);
        Assert.assertNotNull(userDO);
    }

    @Test
    public void updatePassword(){
        UserDO userDO = new UserDO();
        userDO.setPassword("admin123");
        userDO.setId(5);
        userDAO.updatePassword(userDO);
    }



}