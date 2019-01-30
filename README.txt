mvn install dockerfile:build
------------------------------------------------------------------------------------------
docker run --name eureka                         -p 8762:8762   springio/demo-eureka
docker run --name config    --link eureka        -p 59001:59001 springio/demo-config
docker run --name rest-repo --link config --link eureka -p 59201:59201 -p 59202:59202 -e ACTIVE_PROFILES=r1 springio/demo-rest-repo

docker run --name stream    --link config --link eureka -p 59301:59301 springio/demo-stream
docker run --name feign     --link config --link eureka --link rest-repo -p 8080:8080   springio/demo-feign

docker run --name zuul      --link config --link eureka --link rest-repo -p 59101:59101 springio/demo-zuul

---------------------------------------rabbit---------------------------------------------
docker run -d --hostname localhost --name rabbit -e RABBITMQ_DEFAULT_USER=admin -e RABBITMQ_DEFAULT_PASS=admin -p 15672:15672 -p 5672:5672 -p 25672:25672 -p 61613:61613 -p 1883:1883 rabbitmq:management

---------------------------------------stop all and remove all----------------------------
docker stop $(docker ps -a -q)
docker rm $(docker ps -a -q)

------------------------------------------------------------------------------------------