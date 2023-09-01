-- 11.15 Version script is created renaming 11.14, to resolve some backward compatibilty issues. Hence 11.14 is not to be used in fututre.

ALTER TABLE jq_user
 ADD COLUMN IF NOT EXISTS one_time_password VARCHAR(10)
 AFTER last_password_updated_date;
 
 ALTER TABLE jq_user
 ADD COLUMN IF NOT EXISTS otp_requested_time timestamp  NOT NULL DEFAULT current_timestamp()
 AFTER one_time_password;