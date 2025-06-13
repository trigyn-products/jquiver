
ALTER TABLE jq_manual_entry MODIFY entry_content longtext CHARACTER SET utf8mb4 
    COLLATE utf8mb4_general_ci;

ALTER TABLE jq_template_master MODIFY template mediumtext CHARACTER SET utf8mb4 
    COLLATE utf8mb4_general_ci;