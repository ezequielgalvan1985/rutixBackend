FROM eclipse-temurin:21.0.3_9-jdk

EXPOSE 8096
#definir directorio raiz del contenedor
WORKDIR /root

#Copiar y pegar archivos dentro del contenedor
COPY ./pom.xml /root
COPY ./.mvn /root/.mvn
COPY ./mvnw /root/

#Copia codigo fuente
COPY ./src /root/src

#Descargar las dependencias
RUN ./mvnw dependency:go-offline

RUN ./mvnw clean install -DskipTests
ENV JAVA_TOOL_OPTIONS="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005"

ENTRYPOINT ["java", "-jar", "/root/target/rutix-0.0.1-SNAPSHOT.jar"]
