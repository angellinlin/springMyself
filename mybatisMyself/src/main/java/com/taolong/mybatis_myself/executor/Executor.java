package com.taolong.mybatis_myself.executor;

import java.sql.SQLException;
import java.util.List;
import java.util.Set;

import com.taolong.mybatis_myself.config.MappedStatement;

/**
 * @author zhouguilong6
 * sqlsession操作数据库的任务都是由executor完成
 */
public interface Executor {

	<E> Set<E> query(MappedStatement ms, Object parameter) throws SQLException;
}
