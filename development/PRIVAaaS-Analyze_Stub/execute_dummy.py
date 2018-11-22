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
KAFKA_TOPIC_SEND        = "topic-privaaas-planning";
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
        print "TMA Analyze Stub                                               "
        print "---------------------------------------------------------------"


    ###########################################################################
    ## PUBLIC METHODS                                                        ##
    ###########################################################################
    ##
    ## BRIEF: consume the queue.
    ## ------------------------------------------------------------------------
    ##
    def run(self):
        message = {"event":"teste"};

        jsonMessage = json.dumps(message);

        self.__send_message(jsonMessage);


    ###########################################################################
    ## PRIVATE METHODS                                                       ##
    ###########################################################################
    ##
    ## BRIEF: send message to actuator.
    ## ------------------------------------------------------------------------
    ## @PARAM message == message to send.
    ##
    def __send_message(self, message):
        producer = Producer({'bootstrap.servers': KAFKA_ADDRESS})

        ## Trigger any available delivery report callbacks from previous produ-
        ## ce() calls.
        producer.poll(0)

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
            print('Message delivered to {} [{}]'.format(message.topic(), 
                   message.partition()));
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

