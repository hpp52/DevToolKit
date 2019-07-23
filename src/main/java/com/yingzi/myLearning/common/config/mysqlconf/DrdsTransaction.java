package com.yingzi.myLearning.common.config.mysqlconf;

import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DrdsTransaction extends DataSourceTransactionManager{
	
	private static final long serialVersionUID = 4477801124104404102L;

	public DrdsTransaction(DataSource dataSource) {
		suspend(dataSource);
	}
	@Override
	protected void prepareTransactionalConnection(Connection con, TransactionDefinition definition)
			throws SQLException {

		con.setAutoCommit(false);
		try (Statement stmt = con.createStatement()){
			/**
			 * 事务级别参考官网：
			 * https://help.aliyun.com/document_detail/71230.html?spm=a2c4g.11186623.4.1.21c9792c3lkuQq
			 */
			 stmt.execute("SET drds_transaction_policy = '2PC'");
			 con.commit();
		} catch (Exception e) {
			con.rollback();
		}finally{
			con.close();
		}
	}
}
