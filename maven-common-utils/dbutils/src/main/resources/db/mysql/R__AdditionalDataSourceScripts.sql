
SET FOREIGN_KEY_CHECKS=0;

REPLACE INTO jq_file_upload(file_upload_id, file_path, original_file_name, physical_file_name, updated_by, last_update_ts, file_bin_id, file_association_id) VALUES
('f918e998-84b8-4b99-b81c-fedc774f871d', '/images', 'view.svg', '46b7d510-541c-459d-8d5e-6e570a1c173d', 'admin@jquiver.io', NOW(), 'default', 'default');


SET FOREIGN_KEY_CHECKS=1;