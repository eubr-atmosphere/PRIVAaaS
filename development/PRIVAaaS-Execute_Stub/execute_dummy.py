#!/usr/bin/env python








###############################################################################
## IMPORT                                                                    ##
###############################################################################
import os;
import sys;
import json;

from kafka        import KafkaConsumer
from kafka.errors import KafkaError;
from jsonschema   import Draft4Validator;
from json         import loads;








###############################################################################
## DEFINES                                                                   ##
###############################################################################
KAFKA_AUTO_OFFSET_RESET = 'earliest';
KAFKA_AUTO_COMMIT       = True;
KAFKA_VALUE_DESERIALIZER= lambda x: loads(x.decode('utf-8'));
KAFKA_ADDR              = "localhost";
KAFKA_PORT              = "9999";
KAFKA_TOPIC             = "PRIVAaaS_Execute"
KAFKA_GROUPID           = "Execute"








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
    __consumer = None;


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
        print "TMA Execute Dummy                                              "
        print "---------------------------------------------------------------"

        self.__consumer = self.__kafka_consumer();
        if not self.__consumer:
            sys.exit(-1);


    ###########################################################################
    ## PUBLIC METHODS                                                        ##
    ###########################################################################
    ##
    ## BRIEF: consume the queue.
    ## ------------------------------------------------------------------------
    ##
    def __run(self):
        for message in self.__consumer:
            ## Send request to PRIVaaS actuator.
            self.__send_msg_to_actuator(message);


    ###########################################################################
    ## PRIVATE METHODS                                                       ##
    ###########################################################################
    ##
    ## BRIEF: consumer messages from kafka queue.
    ## ------------------------------------------------------------------------
    ##
    def __kafka_consumer(self):

        try:
            ## Instence the consumer object:
            return KafkaConsumer(KAFKA_TOPIC,
                              bootstrap_servers=[KAFKA_ADDR +':'+ KAFKA_PORT],
                              auto_offset_reset=KAFKA_AUTO_OFFSET_RESET,
                              enable_auto_commit=KAFKA_AUTO_COMMIT,
                              consumer_timeout_ms=1000,
                              group_id=KAFKA_GROUPID,
                              value_deserializer=KAFKA_VALUE_DESERIALIZER);

        except KafkaError as error:
            print str(error) + ": Kafka is running??";
            return None;


    ##
    ## BRIEF: send message to actuator.
    ## ------------------------------------------------------------------------
    ## @PARAM message == message to send.
    ##
    def __send_msg_to_actuator(self, message):
        print "prepare";

## End Class.








###############################################################################
## MAIN                                                                      ##
###############################################################################
if __name__ == '__main__':
    queue = Queue_Listen();
    queue.run();

## EOF.
