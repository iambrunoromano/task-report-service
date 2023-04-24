TRUNCATE TABLE task_execution_report;
INSERT INTO task_execution_report(task_execution_id,task_id,start_date_time,end_date_time,execution_time_seconds,error_message,status,insert_date,update_date) VALUES (13,13,'2023-01-01 00:00:00','2023-01-01 00:01:00',60,'','FAILURE',NOW(),NOW());

TRUNCATE TABLE task_step_execution_report;
INSERT INTO task_step_execution_report(task_step_execution_id,task_execution_id,step_name,status,start_date_time,end_date_time,execution_time_seconds,error_message,insert_date,update_date) VALUES (13,13,'step_1','SUCCESS','2023-01-01 00:00:00','2023-01-01 00:01:00',60,'',NOW(),NOW());