#############################################################################################
###                            Copy and Build Application                                 ###
#############################################################################################
FROM maven:3.6.0-jdk-11-slim AS build
COPY finance-manager-server/src /home/finance-manager/finance-manager-server/src
COPY finance-manager-server/pom.xml /home/finance-manager/finance-manager-server
COPY pom.xml /home/finance-manager
WORKDIR /home/finance-manager
RUN mvn -f /home/finance-manager/finance-manager-server/pom.xml clean install -Pprod

#############################################################################################
###                         Copy the jar and set the Entrypoint                           ###
#############################################################################################
FROM openjdk:11-jdk-slim
COPY --from=build /home/finance-manager/finance-manager-server/target/finance-manager.jar /usr/local/lib/app.jar
ENTRYPOINT ["java","-jar","-Dspring.profiles.active=prod","/usr/local/lib/app.jar"]