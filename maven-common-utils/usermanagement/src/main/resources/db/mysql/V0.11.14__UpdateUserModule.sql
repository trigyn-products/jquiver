ALTER TABLE jq_user
 ADD one_time_password VARCHAR(10)
 AFTER last_password_updated_date;
 
 ALTER TABLE jq_user
 ADD otp_requested_time timestamp  NOT NULL DEFAULT current_timestamp()
 AFTER one_time_password;