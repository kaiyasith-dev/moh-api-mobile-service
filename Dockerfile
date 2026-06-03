
FROM eclipse-temurin:17-jdk-alpine

# 2) Set timezone (Alpine needs tzdata to install/copy zoneinfo)
ENV TZ=Asia/Vientiane
RUN apk add --no-cache tzdata \
 && cp /usr/share/zoneinfo/$TZ /etc/localtime \
 && echo $TZ > /etc/timezone \
 && apk del tzdata

# 3) Alpine uses musl; keep nsswitch simple (mdns4_* are glibc-specific)
RUN printf "hosts: files dns\n" > /etc/nsswitch.conf

# 4) App layout
ENV APP_HOME=/usr/apps
WORKDIR $APP_HOME

ENV PORT=8002
EXPOSE $PORT

# Put a deterministic name on the jar to avoid subshells in CMD
COPY target/*.jar app.jar

# 5) Start the app
ENTRYPOINT ["java","-jar","/usr/apps/app.jar"]


#->build project<-#
# mvn clean dependency:tree compile package

#apb.services.uat
#moh-api-mobile-servic

# docker build --tag apb.registry-img.com/api/v2/pbuat/moh-api-mobile-service:v0.0.1 .
# docker push apb.registry-img.com/api/v2/pbuat/moh-api-mobile-service:v0.0.1
