mvn install dockerfile:build
------------------------------------------------------------------------------------------
docker run --name eureka                         -p 8762:8762   springio/demo-eureka
docker run --name config    --link eureka        -p 59001:59001 springio/demo-config
docker run --name rest-repo --link config --link eureka -p 59201:59201 -p 59202:59202 -e ACTIVE_PROFILES=r1 springio/demo-rest-repo

docker run --name stream    --link config --link eureka -p 59301:59301 springio/demo-stream
docker run --name feign     --link config --link eureka --link rest-repo -p 8080:8080   springio/demo-feign

docker run --name zuul      --link config --link eureka --link rest-repo -p 59101:59101 springio/demo-zuul

---------------------------------------rabbit---------------------------------------------