package com.trigyn.jws.webstarter.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.trigyn.jws.webstarter.vo.GenericUserNotification;



@Repository
public interface INotificationRepository extends JpaRepository<GenericUserNotification, String> {

}