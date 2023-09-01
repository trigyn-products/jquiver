package com.trigyn.jws.dbutils.repository;

import java.util.Objects;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.trigyn.jws.dbutils.entities.AdditionalDatasourceRepository;
import com.trigyn.jws.dbutils.service.DataSourceFactory;
import com.trigyn.jws.dbutils.vo.DataSourceVO;

@Repository
public class DBConnection {

	protected DataSource					dataSource						= null;

	protected JdbcTemplate					jdbcTemplate					= null;

	protected NamedParameterJdbcTemplate	namedParameterJdbcTemplate		= null;

	@Autowired
	protected HibernateTemplate				hibernateTemplate				= null;

	@Autowired
	protected SessionFactory				sessionFactory					= null;

	@Autowired
	private AdditionalDatasourceRepository	additionalDatasourceRepository	= null;

	@Autowired
	public DBConnection(DataSource dataSource) {
		this.dataSource = dataSource;
		setJdbcTemplate();
		setNamedParameterJdbcTemplate();
	}

	public DataSource getDataSource() {
		return this.dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public JdbcTemplate getJdbcTemplate() {
		return this.jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public void setJdbcTemplate() {
		this.jdbcTemplate = new JdbcTemplate(this.dataSource);
	}

	public NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {
		return this.namedParameterJdbcTemplate;
	}

	public void setNamedParameterJdbcTemplate(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
	}

	public void setNamedParameterJdbcTemplate() {
		this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(this.dataSource);
	}

	public HibernateTemplate getHibernateTemplate() {
		return this.hibernateTemplate;
	}

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}

	public Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public NamedParameterJdbcTemplate updateNamedParameterJdbcTemplateDataSource(String dataSourceId) {
		NamedParameterJdbcTemplate namedParameterJdbcTemplate = null;
		if (StringUtils.isBlank(dataSourceId) == false) {
			DataSourceVO dataSourceVO = additionalDatasourceRepository.getDataSourceConfiguration(dataSourceId);
			namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(DataSourceFactory.getDataSource(dataSourceVO));
		} else {
			namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
		}
		return namedParameterJdbcTemplate;
	}

	public JdbcTemplate updateJdbcTemplateDataSource(String dataSourceId) {
		JdbcTemplate jdbcTemplate = null;
		if (StringUtils.isBlank(dataSourceId) == false) {
			DataSourceVO dataSourceVO = additionalDatasourceRepository.getDataSourceConfiguration(dataSourceId);
			jdbcTemplate = new JdbcTemplate(DataSourceFactory.getDataSource(dataSourceVO));
		} else {
			jdbcTemplate = new JdbcTemplate(dataSource);
		}
		return jdbcTemplate;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (!(o instanceof DBConnection)) {
			return false;
		}
		DBConnection dBConnection = (DBConnection) o;
		return Objects.equals(dataSource, dBConnection.dataSource) && Objects.equals(jdbcTemplate, dBConnection.jdbcTemplate)
				&& Objects.equals(namedParameterJdbcTemplate, dBConnection.namedParameterJdbcTemplate);
	}

	@Override
	public int hashCode() {
		return Objects.hash(dataSource, jdbcTemplate, namedParameterJdbcTemplate);
	}

}