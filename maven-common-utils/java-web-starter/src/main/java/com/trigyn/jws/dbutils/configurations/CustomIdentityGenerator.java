package com.trigyn.jws.dbutils.configurations;

import java.io.Serializable;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentityGenerator;

public class CustomIdentityGenerator extends IdentityGenerator {

	@Override
	public Serializable generate(SharedSessionContractImplementor session, Object obj) throws HibernateException {
	    if (obj == null) throw new HibernateException(new NullPointerException()) ;
	    Serializable id = session.getEntityPersister(null, obj).getClassMetadata().getIdentifier(obj, session);
	    
	    if (id == null) {
	        return super.generate(session, obj);
	    } else {
	        return id;

	    }
	}
}
