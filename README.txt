----------------------------------------build app and docker image---------------------------------------------
mvn install dockerfile:build
---------------------------------------build from outer pom--------------------------------------
mvn clean install -pl demo-api                          -DskipTests
mvn clean install -pl demo-stream dockerfile:build
mvn clean install -pl demo-eureka dockerfile:build
mvn clean install -pl demo-config dockerfile:build
mvn clean install -pl demo-rest-repo dockerfile:build
mvn clean install -pl demo-feign dockerfile:build
mvn clean install -pl demo-zuul dockerfile:build
mvn clean install -pl demo-redis dockerfile:build
mvn clean install -pl demo-webflux dockerfile:build
----------------------------------------start up app individual with link--------------------------------------------------
docker run --name eureka                         -p 8762:8762   springio/demo-eureka
docker run --name config    --link eureka        -p 59001:59001 springio/demo-config
docker run --name rest-repo --link config --link eureka -p 59201:59201 -p 59202:59202 -e ACTIVE_PROFILES=r1 springio/demo-rest-repo

docker run --name stream    --link config --link eureka -p 59301:59301 springio/demo-stream
docker run --name feign     --link config --link eureka --link rest-repo -p 8080:8080   springio/demo-feign

docker run --name zuul      --link config --link eureka --link rest-repo -p 59101:59101 springio/demo-zuul

---------------------------------------rabbit---------------------------------------------
docker run -d --hostname localhost --name rabbit -e RABBITMQ_DEFAULT_USER=admin -e RABBITMQ_DEFAULT_PASS=admin -p 15672:15672 -p 5672:5672 -p 25672:25672 -p 61613:61613 -p 1883:1883 rabbitmq:management
---------------------------------------redis-----------------------------------------------------
docker run --name redis --network spring-cloud-network -p 6379:6379 -d redis redis-server --appendonly yes


---------------------------------------stop all and remove all on win10----------------------------
docker stop $(docker ps -a -q)
docker rm $(docker ps -a -q)
before using testcontainers integration test it should make sure docker compose container env clean
----------------------------------------start up app individual with network--------------------------------------------------
above use --link connect each other
below create network for it

docker network create -d bridge spring-cloud-network

and then

docker run --name eureka    --network  spring-cloud-network -p 8762:8762   springio/demo-eureka
docker run --name config 	--network  spring-cloud-network -p 59001:59001 springio/demo-config
docker run --name rest-repo --network  spring-cloud-network -p 59201:59201 -p 59202:59202 -e ACTIVE_PROFILES=r1 springio/demo-rest-repo
docker run --name stream    --network  spring-cloud-network -p 59301:59301 springio/demo-stream
docker run --name feign     --network  spring-cloud-network -p 8080:8080   springio/demo-feign
docker run --name zuul      --network  spring-cloud-network -p 59101:59101 springio/demo-zuul
----------------------------------------start up all app by docker-compose --------------------------------------------------
docker-compose up
docker-compose -f docker-compose-redis.yml up
---------------------------------------check network---------------------------------------------
docker run -it --rm --name busybox1 --network spring-cloud-network busybox sh
<or docker attach --sig-proxy=false busybox1>
/ #ping config
---------------------------------------login a docker container and trouble shoot---------------------------------
docker exec -it <container name> /bin/bash
docker exec -it  /bin/bash
---------------------------------------debug experience between different module---------------------------------------------
1, make sure local startup each spring cloud app is ok in IDE(eureka)
2, make sure mvn install -pl xxx dockerfiel:build is all ok
3, make sure docker-compose up is ok finally
4, make sure testcontainers refer docker-compose.yml is ok

*before each docker refer startup, clean the env ==> docker stop $(docker ps -a -q) and stop local IDE started items
*@Rule and @ClassRule in junit has been set auto start and stop in @Before @BeforeClass and @After
---------------------------------------debug experience single module---------------------------------------------
How to fix spring boot app auto stop?  ==> check if import spring-boot-starter-web
---------------------------------------github Personal access tokens----------------------------------------------
 736b161b9113c19033244e8078a38288fe415758
 --------------------------------------build a docker image with maven--------------------------------------------
 docker build -f Dockerfile-jenkins -t demo/demo-jenkins:v1 .
 --------------------------------------startup jenkins------------------------------------------------------------
 docker-compose -f docker-compose-jenkins.yml up
