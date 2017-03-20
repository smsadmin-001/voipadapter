#! /bin/sh
mydir=$(cd "$(dirname "$0")";pwd)
cd $mydir
INSTALL_CONFIG=./*-install.sh

# installed service name
INSTALL_SERVICE_NAME=`cat $INSTALL_CONFIG | grep -v '^[[:space:]]\{0,\}#' | grep INSTALL_SERVICE_NAME | head -1 | awk -F= '{print $2}'`
# original name
ORI_SERVICE_NAME=`cat $INSTALL_CONFIG | grep -v '^[[:space:]]\{0,\}#' | grep ORI_SERVICE_NAME | head -1 | awk -F= '{print $2}'`
# jar full name
JAR_SERVICE_NAME=`cat $INSTALL_CONFIG | grep -v '^[[:space:]]\{0,\}#' | grep JAR_SERVICE_NAME | head -1 | awk -F= '{print $2}'`

#####################################################################
function changeVariableValue()
{
	sed -i "s/INSTALL_SERVICE_NAME=.*/INSTALL_SERVICE_NAME=$INSTALL_SERVICE_NAME/" $1
	sed -i "s/ORI_SERVICE_NAME=.*/ORI_SERVICE_NAME=$ORI_SERVICE_NAME/" $1
	sed -i "s/JAR_SERVICE_NAME=.*/JAR_SERVICE_NAME=$JAR_SERVICE_NAME/" $1
}

function changeSchemaPID()
{
  NAME=$1
  VALUE=$2
  FILEPATH=$3
  sed -i "/<name>$NAME<\/name>/{n;s!\(<value>\)\S\S*\(</value>\)!\1$VALUE\2!}" $FILEPATH
}


##########################################################################
### change related variables
#changeVariableValue "./$ORI_SERVICE_NAME-uninstall.sh"
#changeVariableValue "./$ORI_SERVICE_NAME/script/check-$ORI_SERVICE_NAME"
#changeVariableValue "./$ORI_SERVICE_NAME/script/restart-$ORI_SERVICE_NAME"
#changeVariableValue "./$ORI_SERVICE_NAME/script/start-$ORI_SERVICE_NAME"
#changeVariableValue "./$ORI_SERVICE_NAME/script/stop-$ORI_SERVICE_NAME"
#changeVariableValue "./$ORI_SERVICE_NAME/daemon/$ORI_SERVICE_NAME.daemontools"
#changeVariableValue "./$ORI_SERVICE_NAME/bin/$ORI_SERVICE_NAME.sh"
#changeSchemaPID 'process.GUID' "$INSTALL_SERVICE_NAME" "./$ORI_SERVICE_NAME/etc/$ORI_SERVICE_NAME-schema.xml"
#changeVariableValue "./$ORI_SERVICE_NAME/etc/$ORI_SERVICE_NAME-log4j.properties"


echo "uninstalling $INSTALL_SERVICE_NAME program"

# test /u01/$SERVICE_NAME
if [ ! -d "/u01/$INSTALL_SERVICE_NAME" ] 
then 
    echo "/u01/$INSTALL_SERVICE_NAME not found."
    exit 1
fi

#stop-component
cd /u01/$INSTALL_SERVICE_NAME/script/
sh stop-$ORI_SERVICE_NAME

rm -rf  /u01/$INSTALL_SERVICE_NAME
rm -rf /service/$INSTALL_SERVICE_NAME
rm -rf /srv/$INSTALL_SERVICE_NAME
exit 0
