#! /bin/sh
# installed service name
INSTALL_SERVICE_NAME=VOIPAdapter
# original name
ORI_SERVICE_NAME=VOIPAdapter
# jar full name
JAR_SERVICE_NAME=voip-adapter.jar

####################################################################################################################
echo "check environment. "

# test /u01 
if [ ! -d "/u01" ] 
then
	echo "/u01 not found. "
	exit 1
fi

# test /log
if [ ! -d "/log" ] 
then
	echo "/log not found. "
	exit 1
fi

# test /opt/daemontools
if [ ! -d "/opt/daemontools" ]
then
	echo "Daemontools has not installed.Please install Daemontools first. "
	exit 1
fi

# test java enviroment 

	echo "Test java enviroment to be done. "

# test HBase, add by wfy, 20131019

	echo "Test Hbase enviroment to be done. "

# test Solr, add by wfy, 20131019

	echo "Test Solr enviroment to be done. "


#test environment-1.0 files, we don't need it, wfy, 20131019
#if [ ! -f "/usr/local/lib/environment-1.0" ]
#then
#	echo "environment-1.0 has not installed.Please install it first. "
#	exit 1
#fi

mydir=$(cd "$(dirname "$0")";pwd)
cd $mydir


####################################################################################################################
### function definition

function AddLineToLdSoConf()
{
	AlreadyExsit="N"
	while read line
	do
		if [ "$line" == "$1" ]
		then
			AlreadyExsit="Y"
		fi
	done < /etc/ld.so.conf

	if [ "$AlreadyExsit" = "N" ]
	then
		echo "$1">>/etc/ld.so.conf
	else
		echo "$1 is already in /etc/ld.so.conf!"
	fi
}


function daemon_install()
{
	if [ $# -lt 2 ] 
	then
		return 0
	fi
	echo "installing $1 daemon......"
	DaemonToolsDir=/srv/$1
	DaemonToolsFile=$DaemonToolsDir/run
	mkdir -p $DaemonToolsDir
	rm -f $DaemonToolsFile
	install -p ./$2/daemon/$2.daemontools $DaemonToolsFile
	rm -rf /service/$1
	ln -s $DaemonToolsDir /service/
	return 1
}


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


####################################################################################################################
### change related variables
#changeVariableValue "./$ORI_SERVICE_NAME-uninstall.sh"
changeVariableValue "./$ORI_SERVICE_NAME/script/check-$ORI_SERVICE_NAME"
changeVariableValue "./$ORI_SERVICE_NAME/script/restart-$ORI_SERVICE_NAME"
changeVariableValue "./$ORI_SERVICE_NAME/script/start-$ORI_SERVICE_NAME"
changeVariableValue "./$ORI_SERVICE_NAME/script/stop-$ORI_SERVICE_NAME"
changeVariableValue "./$ORI_SERVICE_NAME/daemon/$ORI_SERVICE_NAME.daemontools"
changeSchemaPID 'process.GUID' "$INSTALL_SERVICE_NAME" "./$ORI_SERVICE_NAME/etc/$ORI_SERVICE_NAME.properties"
##  changeVariableValue "./$ORI_SERVICE_NAME/etc/$ORI_SERVICE_NAME-log4j.properties"

####################################################################################################################
### remove or creat directory

mkdir -p  /u01/$INSTALL_SERVICE_NAME/bin/
mkdir -p  /u01/$INSTALL_SERVICE_NAME/script/
mkdir -p  /u01/$INSTALL_SERVICE_NAME/lib/
mkdir -p  /u01/$INSTALL_SERVICE_NAME/etc/
mkdir -p  /u01/$INSTALL_SERVICE_NAME/version/
mkdir -p  /u01/common/lib
mkdir -p  /u01/common/etc
mkdir -p  /u01/common/bin


#log
mkdir -p /log/$INSTALL_SERVICE_NAME/


####################################################################################################################
### bin

install -p ./$ORI_SERVICE_NAME/bin/$JAR_SERVICE_NAME /u01/$INSTALL_SERVICE_NAME/bin/
install -p -m 644 ./$ORI_SERVICE_NAME/bin/$ORI_SERVICE_NAME.sh  /u01/$INSTALL_SERVICE_NAME/bin/

####################################################################################################################
### etc

install -p -m 644 ./$ORI_SERVICE_NAME/etc/*.properties /u01/$INSTALL_SERVICE_NAME/etc/
#install -p -m 644 ./$ORI_SERVICE_NAME/etc/*.xml /u01/$INSTALL_SERVICE_NAME/etc/

####################################################################################################################
### script

install -p ./$ORI_SERVICE_NAME/script/start-$ORI_SERVICE_NAME  /u01/$INSTALL_SERVICE_NAME/script/
install -p ./$ORI_SERVICE_NAME/script/check-$ORI_SERVICE_NAME  /u01/$INSTALL_SERVICE_NAME/script/
install -p ./$ORI_SERVICE_NAME/script/stop-$ORI_SERVICE_NAME  /u01/$INSTALL_SERVICE_NAME/script/
install -p ./$ORI_SERVICE_NAME/script/restart-$ORI_SERVICE_NAME  /u01/$INSTALL_SERVICE_NAME/script/


####################################################################################################################
### lib

sudo cp -rf ./$ORI_SERVICE_NAME/lib/* /u01/$INSTALL_SERVICE_NAME/lib/


####################################################################################################################
### common

#cd ./$NAME/common/

#for file in *
#do 

#    if [ -f "/u01/common/lib/${file}" ] || [ -d "/u01/common/lib/${file}" ] 
#    then 
#         echo "ATTENTION:\"/u01/common/lib/${file}\" has been coverd"
#    fi

#    if [ -f $file ]
#	then 
#    	install -p -m 644 $file /u01/common/
#    else
#		cp -rf $file /u01/common/
#    fi

#done

#cd -

####################################################################################################################
### version

install -p -m 644 ./$ORI_SERVICE_NAME/version/$ORI_SERVICE_NAME.ini  /u01/$INSTALL_SERVICE_NAME/version/



####################################################################################################################
### daemontolls install

daemon_install "$INSTALL_SERVICE_NAME" "$ORI_SERVICE_NAME"
if [ $? -ne 1 ]
then
	echo "$INSTALL_SERVICE_NAME daemon installed Unsuccessful !!!!"
	exit 1
fi

### add line to file /etc/ld.so.conf

AddLineToLdSoConf "/u01/$INSTALL_SERVICE_NAME/lib"

AddLineToLdSoConf "/u01/common/lib"


ldconfig

echo "Program has been successful installed"
exit 0
