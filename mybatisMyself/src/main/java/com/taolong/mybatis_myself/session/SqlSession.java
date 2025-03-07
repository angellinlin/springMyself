package com.taolong.mybatis_myself.session;

import java.util.List;
import java.util.Set;

public interface SqlSession {

	<T> T selectOne(String statement,Object parameter);
	
	<E> Set<E> selectList(String statement, Object parameter);
	
	<T> T getMapper(Class<T> type);
}
