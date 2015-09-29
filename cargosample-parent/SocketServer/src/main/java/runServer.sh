#!/bin/sh
totalNum=0

while true ;
do
   result1=`curl -w %{http_code}"\n" http://192.168.4.37:8080/web1/index.jsp|grep 200`
   
   if [ ! -z $result1 ] && [ $result1 -eq 200 ];
   then
        let totalNum=$totalNum+1
	break;
   fi
done

while true ;
do
   result2=`curl -w %{http_code}"\n" http://192.168.4.37:8080/web2/index.jsp|grep 200`
   if [ ! -z $result2 ] && [ $result2 -eq 200 ];
   then
        let totalNum=$totalNum+1
	break;
   fi
done

while true ;
do
   result3=`curl -w %{http_code}"\n" http://192.168.4.37:8080/web3/index.jsp|grep 200`
   if [ ! -z $result3 ] && [ $result3 -eq 200 ];
   then
        let totalNum=$totalNum+1
	 break;
   fi
done

while true ;
do
   result4=`curl -w %{http_code}"\n" http://192.168.4.37:18080/web1/index.jsp|grep 200`
   if [ ! -z $result4 ] && [ $result4 -eq 200 ];
   then
        let totalNum=$totalNum+1
	 break;
   fi
done

while true ;
do
   result5=`curl -w %{http_code}"\n" http://192.168.4.37:18080/web2/index.jsp|grep 200`
   if [ ! -z $result5 ] && [ $result5 -eq 200 ];
   then
        let totalNum=$totalNum+1
	 break;
   fi
done

while true ;
do
   result6=`curl -w %{http_code}"\n" http://192.168.4.37:18080/web4/index.jsp|grep 200`
   if [ ! -z $result6 ] && [ $result6 -eq 200 ];
   then
        let totalNum=$totalNum+1
	 break;
   fi
done

while true ;
do
   result7=`curl -w "\n"%{http_code}"\n" http://192.168.4.37:18080/SocketClient/index.jsp|grep 200`
   if [ ! -z $result7 ] && [ $result7 -eq 200 ];
   then
        let totalNum=$totalNum+1
	 break;
   fi
done

while true ;
do
   result8=`curl -w %{http_code}"\n" http://192.168.4.37:28080/web1/index.jsp|grep 200`
   if [ ! -z $result8 ] && [ $result8 -eq 200 ];
   then
        let totalNum=$totalNum+1
	 break;
   fi
done

while true ;
do
   result9=`curl -w %{http_code}"\n" http://192.168.4.37:28080/web5/index.jsp|grep 200`
   if [ ! -z $result9 ] && [ $result9 -eq 200 ];
   then
        let totalNum=$totalNum+1
	 break;
   fi
done
	
echo "all webapp is started"
cd /root/.jenkins/workspace/final_version/cargosample-parent/SocketServer
mvn clean cobertura:coberturae

