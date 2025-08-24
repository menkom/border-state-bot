#!/bin/bash

export DATASOURCE_URL="jdbc:mariadb://localhost:3306/border_state_bot_db"
export LOG_IN_JSON="false"
export DATASOURCE_USER="bot_mariadb_user"
export DATASOURCE_PASSWORD="your_password"
export TELEGRAM_TOKEN=1112222:asdfasdfasdfasdf
export TELEGRAM_USERNAME=TelegramUsername

#set -a
#source .bot.env
#set +a

JAR_PATH="./border-state-bot-1.0.7.2.jar"

# get service that run on 8080 port
pid=$(lsof -t -i:8088)

if [ -n "$pid" ]; then
  echo "Current application running with PID $pid"
  kill -9 $pid
  if [ $? -eq 0 ]; then
    echo "Process finished"
  else
    echo "Failed to kill process"
  fi
else
  echo "No process found on port 8088"
fi

#java -Dlogging.config=./bot-logback-spring.xml -jar "$JAR_PATH"
#nohup - not stop service on console close
#> /dev/null 2>&1  - no logging (but logging managed by application works)
# echo $! > border-state-collector.pid - save application PID to file
nohup java -Dlogging.config=./bot-logback-spring.xml -jar "$JAR_PATH" > /dev/null 2>&1 & echo $! > border-state-bot.pid
echo "Application started in background with PID $!"