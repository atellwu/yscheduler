create_time=$1
heartbeat_create_time=$2
host=$3
user=$4
passwd=$5
database=$6
echo "create_time: $create_time"
echo "heartbeat_create_time: $heartbeat_create_time"
mysql -u$user -p$passwd -h$host --database=$database -N -e"
DELETE FROM attempt WHERE create_time < \"$create_time\" ; select 'DELETED ATTEMPT',row_count();
DELETE FROM task_instance WHERE create_time < \"$create_time\";select 'DELETED TASK_INSTANCE',row_count();
DELETE FROM workflow_instance WHERE create_time < \"$create_time\";select 'DELETED WORKFLOW_INSTANCE',row_count();
DELETE FROM attempt WHERE instance_id IN (SELECT id FROM task_instance WHERE task_id IN (SELECT id FROM task WHERE NAME LIKE '_heartbeat_%') AND create_time < \"$heartbeat_create_time\");select 'DELETE HEARTBEAT_TASK_ATTEMPT',row_count();
DELETE FROM task_instance WHERE task_id IN (SELECT id FROM task WHERE NAME LIKE '_heartbeat_%') AND create_time < \"$heartbeat_create_time\";select 'DELETED HEARTBEAT_TASK_INSTANCE',row_count();
" -s