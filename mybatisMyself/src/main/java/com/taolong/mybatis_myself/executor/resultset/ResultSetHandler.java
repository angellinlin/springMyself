package com.taolong.mybatis_myself.executor.resultset;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

public interface ResultSetHandler {

	<E> Set<E> handleResultSets(ResultSet resultSet) throws SQLException;
}
