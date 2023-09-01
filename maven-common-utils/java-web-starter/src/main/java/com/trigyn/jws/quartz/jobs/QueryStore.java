package com.trigyn.jws.quartz.jobs;

public final class QueryStore {

	public static final String JPA_QUERY_TO_GET_MAIL_SCHEDULE_BY_GROUP_ID = "SELECT ms FROM MailSchedule AS ms WHERE ms.mailScheduleGroupId = :mailScheduleGroupId";

}
