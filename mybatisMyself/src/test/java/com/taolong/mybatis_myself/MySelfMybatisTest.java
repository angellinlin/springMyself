package com.taolong.mybatis_myself;

import org.junit.Test;

import com.taolong.mybatis_myself.bean.TUser;
import com.taolong.mybatis_myself.mapper.TUserMapper;
import com.taolong.mybatis_myself.session.SqlSession;
import com.taolong.mybatis_myself.session.SqlSessionFactory;

import java.util.List;
import java.util.Set;

public class MySelfMybatisTest {

    /**
     * 测试MyBatis的基本功能。
     */
	@Test
	public void testMybatis() {
		SqlSessionFactory factory = new SqlSessionFactory();
		SqlSession session = factory.openSession();
		TUserMapper userMapper = session.getMapper(TUserMapper.class);
		TUser user = userMapper.selectByPrimaryKey(129);
		System.out.println(user);
	}
}
