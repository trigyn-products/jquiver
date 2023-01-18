SET FOREIGN_KEY_CHECKS=0;

REPLACE INTO jq_file_upload(file_upload_id, file_bin_id, file_path, original_file_name, physical_file_name, updated_by, last_update_ts, file_association_id) VALUES
('0cad6a17-9f5f-4d25-b983-b8b691a144d0', 'default', '/images', 'ADC.png', 'd750854d-92fd-4486-a9f8-13949b05fa16', 'admin@jquiver.com', NOW(), 'default'), 
('1f4e7749-3365-400a-98de-2716a1de01fc', 'default', '/images', 'Akram-Bhasha.jfif', '7b505847-580a-46af-9368-db7c1f46d0ec', 'admin@jquiver.com', NOW(), 'default'),
('4efc83a2-4ef9-40bd-a60b-a5d639d1bea5', 'default', '/images', 'Aman-Prasad.png', '293dc7a1-39ff-4192-bf80-9b06b8d1dffc', 'admin@jquiver.com', NOW(), 'default'), 
('6b268691-ddfc-4d3f-9abd-bbc88bbfe974', 'default', '/images', 'Satish-Pandey.jpg', '6a42bf98-8a8d-4be6-897a-559b256dfe28', 'admin@jquiver.com', NOW(), 'default'), 
('756de33e-b86a-11eb-9690-f48e38ab8cd7', 'default', '/images', 'Abhay-Desai.png', '33283eb3-e224-40e8-aa6c-9ba48cebaaba', 'admin@jquiver.com', NOW(), 'default'),
('bc03c67f-a5a0-4063-8ef4-c50ed8f076f3', 'default', '/images', 'Amit-Jadhav.jpg', 'b7b9c9fd-3bbc-46d3-bf59-d5e343f2e332', 'admin@jquiver.com', NOW(), 'default'), 
('c91b58b7-0a29-45c1-960d-3648821d4547', 'default', '/images', 'Satyawan-Sawant.jpg', '9eb15762-540b-42b1-948b-29242ffcb20e', 'admin@jquiver.com', NOW(), 'default'), 
('e3dd0b7c-242a-47b3-9a36-c23ef378b10f', 'default', '/images', 'Ravi-Gowda.png', '6b7d7cb1-8b51-4626-8135-6181fcb88fe5', 'admin@jquiver.com', NOW(), 'default'),
('fa498f47-ba98-4f07-bb61-efc2508b21f4', 'default', '/images', 'Mini-Pillai.jpg', '960c885c-b4d9-4ea5-a58d-4cbd95ce82f6', 'admin@jquiver.com', NOW(), 'default');

SET FOREIGN_KEY_CHECKS=1;