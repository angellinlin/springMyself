package com.taolong.mybatis_myself.session;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.taolong.mybatis_myself.config.Configuration;
import com.taolong.mybatis_myself.config.MappedStatement;
import com.taolong.mybatis_myself.executor.Executor;
import com.taolong.mybatis_myself.executor.SimpleExecutor;

public class DefaultSqlSession implements SqlSession {

	private Configuration configuration;
	
	private Executor executor;
	
	
	public DefaultSqlSession(Configuration configuration) {
		super();
		this.configuration = configuration;
		executor = new SimpleExecutor(configuration);
	}

	@Override
	public <T> T selectOne(String statement,Object parameter) {
		Set<Object> selectList = this.selectList(statement, parameter);
		if (selectList == null || selectList.isEmpty()) return null;
		return (T)selectList.iterator().next();//返回第一个
	}

	@Override
	public <E> Set<E> selectList(String statement, Object parameter)  {
		MappedStatement ms = configuration.getMappedStatement(statement);
		ArrayList<Object> list = new ArrayList<>();
		try {
			return executor.query(ms, parameter);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public <T> T getMapper(Class<T> type) {
		return configuration.getMapper(type, this);
	}

}
