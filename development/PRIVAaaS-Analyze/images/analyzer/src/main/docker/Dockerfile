FROM java:8
VOLUME /tmp
ADD analyzer.jar analyzer.jar
RUN bash -c 'touch /analyzer.jar'
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/analyzer.jar"]