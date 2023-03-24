ALTER TABLE jq_dashboard_role_association DROP FOREIGN KEY jq_dashboard_role_association_ibfk_2;

ALTER TABLE jq_dashlet_role_association DROP FOREIGN KEY jq_dashlet_role_association_ibfk_2;

ALTER TABLE jq_dynamic_rest_role_association DROP FOREIGN KEY jq_dynamic_rest_role_association_ibfk_2;

DROP TABLE `jq_user_role`;

ALTER TABLE `jq_dashboard_role_association`  
  ADD FOREIGN KEY (`role_id`) REFERENCES `jq_role`(`role_id`);
  
ALTER TABLE `jq_dynamic_rest_role_association`  
  ADD FOREIGN KEY (`role_id`) REFERENCES `jq_role`(`role_id`); 
  
  SET FOREIGN_KEY_CHECKS=0;
  
  ALTER TABLE `jq_dashlet_role_association`  
  ADD FOREIGN KEY (`role_id`) REFERENCES `jq_role`(`role_id`);
  
  SET FOREIGN_KEY_CHECKS=1;  