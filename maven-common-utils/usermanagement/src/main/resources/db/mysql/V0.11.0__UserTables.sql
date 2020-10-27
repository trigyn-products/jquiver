SET FOREIGN_KEY_CHECKS=0;

DROP TABLE IF EXISTS jws_role;
CREATE TABLE jws_role (
  role_id varchar(50) NOT NULL,
  role_name varchar(100) NOT NULL,
  role_description varchar(2000) DEFAULT NULL,
  is_active int(2)  NOT NULL DEFAULT 1,
  PRIMARY KEY (role_id),
  UNIQUE KEY role_name (role_name) 
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS jws_master_modules;
CREATE TABLE jws_master_modules (
  module_id varchar(50) NOT NULL,
  module_name varchar(100) DEFAULT NULL,
  is_system_module int(11) NOT NULL,
  auxiliary_data varchar(100) DEFAULT NULL,
  PRIMARY KEY (module_id),
  UNIQUE KEY module_name(module_name) 
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS jws_role_master_modules_association;
CREATE TABLE jws_role_master_modules_association (
  role_module_id varchar(50) NOT NULL,
  role_id varchar(50) NOT NULL,
  module_id varchar(50) NOT NULL,
  is_active int(2)  NOT NULL DEFAULT 0,
  PRIMARY KEY (role_module_id),
  CONSTRAINT fk_jws_role_master_modules_association_1 FOREIGN KEY (role_id) REFERENCES jws_role (role_id),
  CONSTRAINT fk_jws_role_master_modules_association_2 FOREIGN KEY (module_id) REFERENCES jws_master_modules (module_id)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS jws_user;
CREATE TABLE jws_user (
  user_id varchar(50) NOT NULL,
  first_name varchar(100) NOT NULL,
  last_name varchar(100) NOT NULL,
  email varchar(500) NOT NULL,
  password varchar(100) DEFAULT NULL,
  is_active int(2) NOT NULL DEFAULT 1,
  PRIMARY KEY (user_id),
  UNIQUE KEY email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS jws_user_role_association;
CREATE TABLE jws_user_role_association (
  user_role_id varchar(50) NOT NULL,
  role_id varchar(50) NOT NULL,
  user_id varchar(50) NOT NULL,
  updated_date timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  PRIMARY KEY (user_role_id),
  KEY fk_jws_user_role_association_1 (role_id),
  KEY fk_jws_user_role_association_2 (user_id),
  CONSTRAINT fk_jws_user_role_association_1 FOREIGN KEY (role_id) REFERENCES jws_role (role_id),
  CONSTRAINT fk_jws_user_role_association_2 FOREIGN KEY (user_id) REFERENCES jws_user (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


REPLACE INTO jws_role VALUES ('ae6465b3-097f-11eb-9a16-f48e38ab9348','ADMIN','admin role',1 ),('b4a0dda1-097f-11eb-9a16-f48e38ab9348','ANONYMOUS','anonymous role',1 )
,('2ace542e-0c63-11eb-9cf5-f48e38ab9348','AUTHENTICATED','authenticated role',1 );  

REPLACE INTO jws_master_modules (
   module_id
  ,module_name
  ,is_system_module 
  ,auxiliary_data
) VALUES (
   'f2066c95-098c-11eb-9a16-f48e38ab9348'
  ,'Master Generator'
   ,1
   ,null 
),(
   '07067149-098d-11eb-9a16-f48e38ab9348'
  ,'Grid Utils' 
  ,1
   ,null  
),(
   '1b0a2e40-098d-11eb-9a16-f48e38ab9348'
  ,'Templating' 
  ,1
   ,null  
),(
   '8588104b-0ece-11eb-94b2-f48e38ab9348'
  ,'Multilingual' 
  ,1
   ,null  
),(
   '91a81b68-0ece-11eb-94b2-f48e38ab9348'
  ,'TypeAhead Autocomplete' 
  ,1
   ,null  
),(
   '0ef59343-0ecf-11eb-94b2-f48e38ab9348'
  ,'File Upload Config' 
  ,1
   ,null  
),(
   '30a0ff61-0ecf-11eb-94b2-f48e38ab9348'
  ,'Form Builder' 
  ,1
   ,null  
),(
   '47030ee1-0ecf-11eb-94b2-f48e38ab9348'
  ,'REST API Builder' 
  ,1
   ,null  
),(
   'c6cc466a-0ed3-11eb-94b2-f48e38ab9348'
  ,'Site Layout' 
  ,1
   ,null  
),(
   'b0f8646c-0ecf-11eb-94b2-f48e38ab9348'
  ,'Dashboard' 
  ,1
   ,null  
),(
   'cc24f9b6-0ecf-11eb-94b2-f48e38ab9348'
  ,'Notification' 
  ,1
   ,null  
),(
   'dff72449-0ecf-11eb-94b2-f48e38ab9348'
  ,'User Management' 
  ,1
   ,null  
),(
   'e8026c40-0ecf-11eb-94b2-f48e38ab9348'
  ,'Application Configuration' 
  ,1
   ,null  
);

REPLACE INTO jws_user (
   user_id
  ,first_name
  ,last_name
  ,email
  ,password
  ,is_active
) VALUES (
   '111415ae-0980-11eb-9a16-f48e38ab9348' 
  ,'admin' 
  ,'admin' 
  ,'admin@trigyn.com' 
  ,'$2a$10$pBJRjqtsoyALqF3IHX3EuONIKFDwypA00eGT8G9kSudfOeoyc.ycq'  
  ,1
);

REPLACE INTO jws_user_role_association VALUES 
('cbc69d67-0988-11eb-9a16-f48e38ab9348','ae6465b3-097f-11eb-9a16-f48e38ab9348','111415ae-0980-11eb-9a16-f48e38ab9348',now())
,('9e3d2c83-0c63-11eb-9cf5-f48e38ab9348','2ace542e-0c63-11eb-9cf5-f48e38ab9348','111415ae-0980-11eb-9a16-f48e38ab9348',now());

DROP TABLE IF EXISTS jws_confirmation_token;
CREATE TABLE  `jws_confirmation_token` (
  `token_id` varchar(50) NOT NULL,
  `confirmation_token` varchar(50) NOT NULL,
  `created_date` date NOT NULL,
  `user_id` varchar(500) NOT NULL,
  PRIMARY KEY (`user_id`),
  CONSTRAINT `fk_jws_confirmation_token_1` FOREIGN KEY (`user_id`) REFERENCES `jws_user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS jws_reset_password_token;
CREATE TABLE  `jws_reset_password_token` (
  `token_id` varchar(50) NOT NULL,
  `password_reset_url` varchar(255) NOT NULL,
  `password_reset_gen_time` datetime NOT NULL,
  `user_id` varchar(500) NOT NULL,
  `is_reset_url_expired` tinyint(1) NOT NULL DEFAULT 0 COMMENT 'This flag will decide ,Is password reset url expired or not .0 means not expired and 1 means Expired',
  PRIMARY KEY (`token_id`),
  CONSTRAINT `fk_jws_reset_password_token_1` FOREIGN KEY (`user_id`) REFERENCES `jws_user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS jws_authentication_type;
CREATE TABLE  jws_authentication_type (
authentication_id int(11) auto_increment,
authentication_name varchar(50),
authentication_properties JSON  DEFAULT NULL,
PRIMARY KEY (authentication_id)
)ENGINE=InnoDB  DEFAULT CHARSET=utf8;


REPLACE INTO jws_authentication_type (
   authentication_id
  ,authentication_name
  ,authentication_properties
) VALUES (
   1 
  ,'In Memory Authentication' 
  ,null
), (
   2 
  ,'Database Authentication' 
  ,'[{"name":"enableRegistration","type":"boolean","value":"true"},{"name":"enableCaptcha","type":"boolean","value":"true"}]'
),(
   3 
  ,'LDAP Authentication' 
  ,null
),(
   4 
  ,'Oauth' 
  ,null
);

DROP TABLE IF EXISTS jws_entity_role_association;
CREATE TABLE jws_entity_role_association (
  entity_role_id varchar(50) NOT NULL,
  entity_id varchar(50) NOT NULL,
  entity_name varchar(50) NOT NULL,
  module_id varchar(50) NOT NULL,
  role_id varchar(50) NOT NULL,
  last_updated_date timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  last_updated_by varchar(50) NOT NULL,
  is_active int(2) NOT NULL,
  PRIMARY KEY (entity_role_id),
  CONSTRAINT fk_jws_entity_role_association_1 FOREIGN KEY (module_id) REFERENCES jws_master_modules (module_id),
  CONSTRAINT fk_jws_entity_role_association_2 FOREIGN KEY (role_id) REFERENCES jws_role (role_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

SET FOREIGN_KEY_CHECKS=1;