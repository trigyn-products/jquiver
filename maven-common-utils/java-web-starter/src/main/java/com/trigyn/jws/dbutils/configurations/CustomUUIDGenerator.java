package com.trigyn.jws.dbutils.configurations;

import java.io.Serializable;
import java.lang.reflect.Member;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.generator.EventType;
import org.hibernate.generator.GeneratorCreationContext;
import org.hibernate.id.uuid.UuidGenerator;

public class CustomUUIDGenerator { // extends UuidGenerator {

//	private static final long serialVersionUID = 1988680110179279725L;
//
//	public CustomUUIDGenerator(org.hibernate.annotations.UuidGenerator config, Member member,
//			GeneratorCreationContext creationContext) {
//		super(config, member, creationContext);
//	}
//
//	private String entityName;
//
////	@Override
////	public void configure(Type type, Properties params, ServiceRegistry serviceRegistry) {
////		entityName = params.getProperty(ENTITY_NAME);
////		super.configure(type, params, serviceRegistry);
////	}
//
//	@Override
//	public Serializable generate(SharedSessionContractImplementor session, Object owner, Object object, EventType eventType) {
//		Serializable id = (Serializable) session.getEntityPersister(entityName, object).getIdentifier(object, session);
//
//		if (id == null) {
//			return (Serializable) super.generate(session, owner, id, eventType);
//		} else {
//			return id;
//		}
//	}
}