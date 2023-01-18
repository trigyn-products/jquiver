package com.trigyn.jws.dynarest.dao;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.trigyn.jws.dbutils.repository.DBConnection;
import com.trigyn.jws.dynarest.entities.MailHistory;

@Repository
public class SendMailDAO extends DBConnection {

	@Autowired
	public SendMailDAO(DataSource dataSource) {
		super(dataSource);
	}

	public void saveFailedMails(MailHistory mailHistory) {
		getCurrentSession().saveOrUpdate(mailHistory);

	}

}
