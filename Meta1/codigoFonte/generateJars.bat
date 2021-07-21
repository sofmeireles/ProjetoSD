@echo off
javac -cp . *.java

echo Main-Class: RMIServer > manifest.txt
jar cmf manifest.txt rmiserver.jar *.class

echo Main-Class: MulticastServer > manifest.txt
jar cmf manifest.txt server.jar *.class 

echo Main-Class: MulticastClient > manifest.txt
jar cmf manifest.txt terminal.jar *.class 

echo Main-Class: AdminConsole > manifest.txt
jar cmf manifest.txt console.jar *.class

del manifest.txt

echo Jars created!