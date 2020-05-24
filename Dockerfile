FROM openjdk:14
ARG JAR_FILE
EXPOSE 8080

WORKDIR /opt/rukou

#dependencies
COPY target/dependency/* ./

#actual code
COPY ${JAR_FILE} rukou-edge.jar

CMD java -cp "*" io.rukou.edge.Main