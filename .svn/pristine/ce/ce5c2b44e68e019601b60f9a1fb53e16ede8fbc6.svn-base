#!/bin/bash
#BASE_DIR="/dianyi/log/yagent/yscheduler/tx/${TX_ID}"
# arg: BASE_DIR TX_ID EVENT_TYPE CMD CONTEXT_DIR
BASE_DIR=${1}
TX_ID=${2}
EVENT_TYPE=${3}
TX_DIR=${1}/${2}
SHELL_DIR=${1}/${2}/${EVENT_TYPE}
CMD=${4}
CONTEXT_DIR=${5}

mkdir -p ${SHELL_DIR} 
PID_FILE=${SHELL_DIR}/pid
EXITCODE_FILE=${SHELL_DIR}/exitcode
LOG_FILE=${TX_DIR}/tx.log

log(){
  DATE=`date "+%Y-%m-%d %H:%M:%S"`
  echo "[INFO][${DATE}] $1" >> ${LOG_FILE}
}

if [ -n "$CONTEXT_DIR" ] ;then
  log "cd context directory $CONTEXT_DIR"  
  cd $CONTEXT_DIR
fi
bash -c "${CMD}" >> ${LOG_FILE} 2>&1 &

PID=$!
echo $PID > $PID_FILE

wait $PID

EXITCODE=$?
echo $EXITCODE > $EXITCODE_FILE
log "Shell task exited(exitCode=${EXITCODE})"