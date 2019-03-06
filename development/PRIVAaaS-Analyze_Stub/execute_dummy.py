#!/usr/bin/env python








###############################################################################
## IMPORT                                                                    ##
###############################################################################
import os;
import sys;
import json;
import subprocess;
import time;
import mysql.connector;

from confluent_kafka import Consumer, Producer
from jsonschema      import Draft4Validator;
from json            import loads;
from mysql.connector import errorcode;









###############################################################################
## DEFINES                                                                   ##
###############################################################################
KAFKA_AUTO_OFFSET_RESET = 'earliest';
KAFKA_AUTO_COMMIT       = True;
KAFKA_ADDRESS           = "10.0.0.88:9093";
KAFKA_TOPIC_SEND        = "topic-privaaas-planning";
KAFKA_TOPIC_RECV         = "topic-monitor";
KAFKA_GROUPID           = "privaaas";

DB_USER = 'privaaas';
DB_PASS = '123mudar';
DB_HOST = '10.0.0.90'
DB_NAME = 'knowledge';







###############################################################################
## CLASSES                                                                   ##
###############################################################################
class Queue_Listen:


    """
    Class Queue_Listen: listen and consume the queue messages.
    ---------------------------------------------------------------------------
    """


    ###########################################################################
    ## ATTIBUTES                                                             ##
    ###########################################################################
    consumer   = None;
    producer   = None;
    execute    = True;
    __lastDate = '0001-01-01 00:00:00.000000';
    __cnx      = None;
    __date     = '';
    __count    = 0;


    ###########################################################################
    ## SPECIAL METHODS                                                       ##
    ###########################################################################
    ##
    ## BRIEF: initialize the object.
    ## ------------------------------------------------------------------------
    ##
    def __init__(self):
        print "---------------------------------------------------------------"
        print "TMA Analyze Stub                                               "
        print "---------------------------------------------------------------"

        self.consumer = self.__kafka_consumer();
        self.__lastDate = time.strptime(self.__lastDate,"%Y-%m-%d %H:%M:%S.%f");


    ###########################################################################
    ## PUBLIC METHODS                                                        ##
    ###########################################################################
    ##
    ## BRIEF: consume the queue.
    ## ------------------------------------------------------------------------
    ##
    def run(self):

        while True:
            self.__watch_kafka();


    ###########################################################################
    ## PRIVATE METHODS                                                       ##
    ###########################################################################
    ##
    ## BRIEF: consumer messages from kafka queue.
    ## ------------------------------------------------------------------------
    ##
    def __kafka_consumer(self):

        self.consumer = Consumer(
            {
                 "api.version.request" : True,
                 "enable.auto.commit"  : True,
                 "group.id"            : KAFKA_GROUPID,
                 "bootstrap.servers"   : KAFKA_ADDRESS,
                 "default.topic.config": {"auto.offset.reset": "earliest"}
             }
        );
        self.consumer.subscribe([KAFKA_TOPIC_RECV]);
        return self.consumer;


    ##
    ## BRIEF:
    ## ------------------------------------------------------------------------
    ##
    def __watch_kafka(self):
        while self.execute:
           message = self.consumer.poll();

           ## Get message data:
           msgContent = message.value();

           if msgContent != '' and msgContent != "Broker: No more messages":
               msgContent = json.loads(msgContent);

               ## Message Received:
               print "Message Receveid From Knowledge: " + str(msgContent);

               k = msgContent["data"][0]["observations"][0]["value"];
               self.__analyze_score(k);

        self.__consumer.close();



    ##
    ## BRIEF:
    ## ------------------------------------------------------------------------
    ##
    def __watch_database(self):

       valuesList = {};
       for resourceId in range(1,7):

           if self.__date == '':
               query  = "SELECT * FROM Data WHERE descriptionId=" + str(resourceId);
               query += " ORDER BY valueTime DESC LIMIT 1";
           else:
               query  = "SELECT * FROM Data WHERE descriptionId=" + str(resourceId);
               query += " AND valueTime > " + "'" + str(self.__date) + "'";
               query += " ORDER BY valueTime DESC LIMIT 1";

           values = self.__select_database(query);

           try:
                valuesList[values[1]] = values[4];
           except:
                pass;

       if values != '' and (values[3] != self.__date):
           self.__date = values[3];
       
       if valuesList.has_key(1): 
           if self.__count == 0:
               self.__count = 1;
               return 0;

           self.__analyze_score(valuesList[1]);

       return 0;


    ##
    ## BRIEF:
    ## ------------------------------------------------------------------------
    ##
    def __analyze_score(self, k):
        message = {"probeId": "8","configuration": {"k": k}};

        jsonMessage = json.dumps(message);

        self.__send_message(jsonMessage);



    ##
    ## BRIEF: send message to actuator.
    ## ------------------------------------------------------------------------
    ##
    def __send_message(self, message):

        producer = Producer({'bootstrap.servers': KAFKA_ADDRESS})

        ## Trigger any available delivery report callbacks from previous produ-
        ## ce() calls.
        producer.poll(0)

        ## Message Received:
        print "Sending message to Planning: " + str(message);

        ## Asynchronously produce a message, the delivery report callback  will
        ## be triggered from poll() above, or flush() below, when the message 
        ## has been successfully delivered or failed permanently.
        producer.produce(KAFKA_TOPIC_SEND, 
                         message, 
                         callback=self.__delivery_report);

        # Wait for any outstanding messages to be delivered and delivery report
        # callbacks to be triggered.
        producer.flush()


    ##
    ## BRIEF: send callback.
    ## ------------------------------------------------------------------------
    ##
    def __delivery_report(self, err, message):

        ## Called once for each message produced to indicate delivery result. 
        ## Triggered by poll() or flush().
        if err is not None:
             print('Message delivery failed: {}'.format(err));
        else:
            pass;
            #print('Message delivered to {} [{}]'.format(message.topic(), 
            #       message.partition()));


    ##
    ## BRIEF: connect to mysql.
    ## ------------------------------------------------------------------------
    ##
    def __connect_database(self):

        try:
            self.__cnx = mysql.connector.connect(user=DB_USER,
                                                 password=DB_PASS,
                                                 host=DB_HOST,
                                                 database=DB_NAME);

        except mysql.connector.Error as err:

            if err.errno == errorcode.ER_ACCESS_DENIED_ERROR:
               print("Something is wrong with your user name or password");
            elif err.errno == errorcode.ER_BAD_DB_ERROR:
               print("Database does not exist");
            else:
               print(err);

            sys.exit(-1);


    ##
    ## BRIEF: select in databas.
    ## ------------------------------------------------------------------------
    ## @PARAM query == query to perform in database.
    ##
    def __select_database(self, query):
        self.__connect_database();
        ## Get cursor:
        cursor = self.__cnx.cursor();

        ## Execute the query:
        cursor.execute(query);
        
        buffer = '';
        for record in cursor:
            buffer = record;

        cursor.close();
        self.__disconnect_database();
        return buffer;


    ## 
    ## BRIEF: disconnect from database.
    ## -----------------------------------------------------------------------
    ##
    def __disconnect_database(self):
        self.__cnx.close();
## End Class.








###############################################################################
## MAIN                                                                      ##
###############################################################################
if __name__ == '__main__':
    try:
        queue = Queue_Listen();
        queue.run();

    except KeyboardInterrupt:
        pass;

    sys.exit(0);
## EOF.

