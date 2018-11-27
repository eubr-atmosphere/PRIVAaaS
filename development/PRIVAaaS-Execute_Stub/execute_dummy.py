#!/usr/bin/env python








###############################################################################
## IMPORT                                                                    ##
###############################################################################
import os;
import sys;
import json;
import Crypto;
import ast;
import subprocess;
import time;
import requests;

from Crypto.PublicKey import RSA;
from Crypto.Cipher    import PKCS1_OAEP;
from Crypto           import Random;
from confluent_kafka  import Consumer, Producer
from jsonschema       import Draft4Validator;
from json             import loads;








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
    __count  = 0;


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
         
           ## Get message data:
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
    def __send_message(self, jsonMessage=''):
       try:   
           message = json.loads(jsonMessage);
       except:
           return -1;

       ## Extract apropriate fields:
       actuatorID = message["actuatorId"];
       actionID   = message["actionId"  ];
       resourceID = message["resourceId"];
       value      = message["value"     ];

       query  = "select actionName from Action where actionId="
       query += actionID;
       
       ## TODO: remove and use kubernetes api python!
       bashCommand = 'kubectl exec -ti mysql-0 -- bash -c "mysql -u root --password=\$MYSQL_ROOT_PASSWORD knowledge -s -N -e \\"' + query + '\\"" | tail -n 1'

       ## Get action description:
       actionDescription = subprocess.check_output(bashCommand, shell=True);

       ## Create message to send:
       query  = "select address,pubKey from Actuator where actuatorId=";
       query += actuatorID;

       ## TODO: remove and use kubernetes api python!
       bashCommand = 'kubectl exec -ti mysql-0 -- bash -c "mysql -u root --password=\$MYSQL_ROOT_PASSWORD knowledge -s -N -e \\"' + query + '\\"" | tail -n 1'

       valuesRet = subprocess.check_output(bashCommand, shell=True);
       valuesRet = valuesRet.split();
        
       ## Get actuator addres and rsa public key to encrypt de message. Follow
       ## the protocol.
       address = valuesRet[0];
       rsaKey  = valuesRet[1] + ' ' + valuesRet[2] + ' ' + valuesRet[3];

       message = {}
       message['resourceId'   ] = resourceID;
       message['messageId'    ] = self.__count;
       message['timestamp'    ] = time.time(); 
       message['action'       ] = actionDescription;
       message['configuration'] = {};
       
       message['configuration']['k'] = value;

       self.__count += 1;
 
       jsonMessage = json.dumps(message);

       key        = RSA.importKey(rsaKey)
       cipher     = PKCS1_OAEP.new(key)
       cipherText = cipher.encrypt(jsonMessage)

       ## Send to destiny:
       self.__sendToDestiny(cipherText,address);

       return 0;


    ##
    ## BRIEF:.
    ## ------------------------------------------------------------------------
    ##
    ##
    ##
    def __sendToDestiny(self, cipherMessage, address):
       valRet = requests.post(address, data=cipherMessage);
       print valRet;

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

