
#! /bin/sh
# installed service name
INSTALL_SERVICE_NAME=VOIPAdapter
# jar full name
JAR_SERVICE_NAME=voip-adapter.jar

cd  /u01/$INSTALL_SERVICE_NAME/bin/
java -Xmx6g -cp $JAR_SERVICE_NAME com.iplustek.work.Main
