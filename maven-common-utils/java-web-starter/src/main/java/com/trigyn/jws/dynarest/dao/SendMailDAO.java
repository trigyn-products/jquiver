package com.trigyn.jws.dynarest.dao;

import javax.sql.DataSource;

import org.springframework.stereotype.Repository;

import com.trigyn.jws.dbutils.repository.DBConnection;
import com.trigyn.jws.dynarest.entities.MailHistory;

@Repository
public class SendMailDAO extends DBConnection {

	public SendMailDAO(DataSource dataSource) {
		super(dataSource);
		// TODO Auto-generated constructor stub
	}

	public void saveFailedMails(MailHistory mailHistory) {
		getCurrentSession().saveOrUpdate(mailHistory);
		
	}

}
