#!/usr/bin/env python








###############################################################################
## IMPORT                                                                    ##
###############################################################################
import os;
import sys;
import json;

from confluent_kafka import Consumer, Producer
from jsonschema   import Draft4Validator;
from json         import loads;








###############################################################################
## DEFINES                                                                   ##
###############################################################################
KAFKA_AUTO_OFFSET_RESET = 'earliest';
KAFKA_AUTO_COMMIT       = True;
KAFKA_ADDRESS           = "10.0.0.68:9093";
KAFKA_TOPIC_RECV        = "topic-privaaas-execute";
KAFKA_GROUPID           = "privaaas";







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
    consumer = None;
    producer = None;
    execute  = True;


    ###########################################################################
    ## SPECIAL METHODS                                                       ##
    ###########################################################################
    ##
    ## BRIEF: initialize the object.
    ## ------------------------------------------------------------------------
    ##
    def __init__(self):
        print "---------------------------------------------------------------"
        print "INIT THE QUEUE CONSUMER                                        "
        print "TMA Execute Stub                                               "
        print "---------------------------------------------------------------"

        self.consumer = self.__kafka_consumer();
        if not self.consumer:
            sys.exit(-1);


    ###########################################################################
    ## PUBLIC METHODS                                                        ##
     ###########################################################################
    ##
    ## BRIEF: consume the queue.
    ## ------------------------------------------------------------------------
    ##
    def run(self):
        while self.execute:
           message = self.consumer.poll();

           ##
           msgContent = message.value();

           if msgContent != '' and msgContent != "Broker: No more messages":
               self.__send_message(msgContent);

        self.__consumer.close();


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
    ## BRIEF: send message to actuator.
    ## ------------------------------------------------------------------------
    ## @PARAM message == message to send.
    ##
    def __send_message(self, message):
        print message
## End Class.








###############################################################################
## MAIN                                                                      ##
###############################################################################
if __name__ == '__main__':
    try:
        queue = Queue_Listen();
        queue.run();

    except KeyboardInterrupt:
        queue.execute=False;
        queue.consumer.unsubscribe([KAFKA_TOPIC_RECV]);
        queue.consumer.close()

    sys.exit(0);
## EOF.

