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
KAFKA_ADDRESS           = "10.0.0.88:9093";
KAFKA_TOPIC_RECV        = "topic-privaaas-planning";
KAFKA_TOPIC_SEND        = "topic-privaaas-execute";
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
    __try    = 0;


    ###########################################################################
    ## SPECIAL METHODS                                                       ##
    ###########################################################################
    ##
    ## BRIEF: initialize the object.
    ## ------------------------------------------------------------------------
    ##
    def __init__(self):
        print "---------------------------------------------------------------"
        print "TMA Planning Stub                                              "
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

           ## Get the message value:
           msgContent = message.value();

           if msgContent != '' and msgContent != "Broker: No more messages":

               ## Message Received:
               print "Message Received From Analyzer: " + str(msgContent);

               ## Getting the planning:
               msgPlanning = self.__get_planning(msgContent);

        self.__consumer.close();


    ###########################################################################
    ## PRIVATE METHODS                                                       ##
    ###########################################################################
    ##
    ## BRIEF: getting the planning.
    ## ------------------------------------------------------------------------
    ## @PARAM message == message to getting planning.
    ## 
    def __get_planning(self, jsonMessage):


        messageReceived = json.loads(jsonMessage);

        if self.__try == 0:
            newK = str(int(messageReceived["configuration"]["k"]) + 1);
            self.__try = 1;
        else:
            newK = str(int(messageReceived["configuration"]["k"]));
           

        message = {"actuatorId" : "1",
                   "actionId"   : "1",
                   "resourceId" : "1",
                   "configuration" : {"k": newK}};

        self.__send_message(message);
        return 0;


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
                 "default.topic.config": {"auto.offset.reset": "latest"}
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

        jsonMessage = json.dumps(message);

        producer = Producer({'bootstrap.servers': KAFKA_ADDRESS})

        ## Trigger any available delivery report callbacks from previous produ-
        ## ce() calls.
        producer.poll(0)

        ## Message Received:
        print "Sending message to Execute: " + str(message);

        ## Asynchronously produce a message, the delivery report callback  will
        ## be triggered from poll() above, or flush() below, when the message
        ## has been successfully delivered or failed permanently.
        producer.produce(KAFKA_TOPIC_SEND, 
                         jsonMessage, 
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
            #print('Message delivered to {} [{}]'.format(message.topic(), 
            #       message.partition()));
            pass;
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

