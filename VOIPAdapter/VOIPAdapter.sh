
#! /bin/sh
# installed service name
INSTALL_SERVICE_NAME=@INSTALL_SERVICE_NAME@
# jar full name
JAR_SERVICE_NAME=@JAR_SERVICE_NAME@

cd  /u01/$INSTALL_SERVICE_NAME/bin/
java -Xmx6g -cp $JAR_SERVICE_NAME com.iplustek.work.Main
