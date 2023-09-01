REPLACE INTO jq_encryption_algorithms_lookup (encryption_algo_id,encryption_algo_name) 
VALUES 
(4,'AES/CBC');

UPDATE jq_encryption_algorithms_lookup SET encryption_algo_name='AES/ECB' WHERE encryption_algo_id=2;