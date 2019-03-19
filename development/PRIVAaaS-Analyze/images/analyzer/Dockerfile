FROM java:8



VOLUME /tmp
ADD target/analyzer.jar analyzer.jar
RUN bash -c 'touch /analyzer.jar'


####################################################################################
## RUN                                                                            ##
####################################################################################
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/analyzer.jar"]
