#!/bin/bash
#set lang, so output file should be utf8
export LANG=zh_CN.UTF-8

# arg: BASE_DIR TX_ID EVENT_TYPE
BASE_DIR=${1}
TX_ID=${2}
EVENT_TYPE=${3}
TX_DIR=${1}/${2}
SHELL_DIR=${1}/${2}/${EVENT_TYPE}

mkdir -p ${SHELL_DIR} 
PID_FILE=${SHELL_DIR}/pid

PID=`cat $PID_FILE 2>/dev/null`

running()
{
    ps -p $1 >/dev/null 2>/dev/null || return 1
    return 0
}

if running $PID
  then
   exit 1
else
  exit 0
fi