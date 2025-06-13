package com.trigyn.jws.dbutils.configurations;

import java.io.Serializable;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

public class CustomIdentityGenerator { // implements IdentifierGenerator {

//	@Override
//	public Serializable generate(SharedSessionContractImplementor session, Object obj) throws HibernateException {
//	    if (obj == null) throw new HibernateException(new NullPointerException()) ;
//	    Serializable id = (Serializable) session.getEntityPersister(null, obj).getClassMetadata().getIdentifier(obj, session);
//	    
//	    if (id == null) {
//	        return (Serializable) generate(session, obj, null, null);
//	    } else {
//	        return id;
//
//	    }
//	}
}
