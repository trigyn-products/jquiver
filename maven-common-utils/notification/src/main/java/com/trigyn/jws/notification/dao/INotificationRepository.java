package com.trigyn.jws.notification.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.trigyn.jws.notification.entities.GenericUserNotification;

@Repository
public interface INotificationRepository extends JpaRepository<GenericUserNotification, String>{

}
