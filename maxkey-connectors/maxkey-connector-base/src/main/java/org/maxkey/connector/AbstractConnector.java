package org.maxkey.connector;

import java.util.Properties;

import org.springframework.jdbc.core.JdbcTemplate;


public abstract class   AbstractConnector<T>{
	
	
	public static class CONNECTOR_TYPE{
		public static int USERINFO_TYPE=1;
		public static int ORG_TYPE=2;
		public static int GROUP_TYPE=3;
		public static int PASSWORD_TYPE=4;
	}
	
	protected Properties properties;
	
	protected JdbcTemplate jdbcTemplate;
	
	public boolean create(T entity) throws Exception{
		return true;
	}
	
	public boolean update(T entity) throws Exception{
		return true;
	}
	
	public boolean delete(T entity) throws Exception{
		return true;
	}

	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}


	
	
}
