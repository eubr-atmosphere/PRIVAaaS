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
import mysql.connector;

from mysql.connector  import errorcode;
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
KAFKA_ADDRESS           = "10.0.0.88:9093";
KAFKA_TOPIC_RECV        = "topic-privaaas-execute";
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
    consumer = None;
    producer = None;
    execute  = True;
    __count  = 0;
    __cnx    = None;


    ###########################################################################
    ## SPECIAL METHODS                                                       ##
    ###########################################################################
    ##
    ## BRIEF: initialize the object.
    ## ------------------------------------------------------------------------
    ##
    def __init__(self):
        print "---------------------------------------------------------------"
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

           ## Message Received:
           print "Message Receveid From Planning: " + str(msgContent);

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
       actuatorID = message["actuatorId"   ];
       actionID   = message["actionId"     ];
       resourceID = message["resourceId"   ];
       value      = message["configuration"];

       query  = "SELECT actionName FROM Action where actionId="
       query += actionID;
       
       ## Select:
       valRet = self.__select_database(query);

       actionDescription = valRet[0];

       ## Create message to send:
       query  = "select address,pubKey from Actuator where actuatorId=";
       query += actuatorID;

       ## Select:
       valRet = self.__select_database(query);

       ## Get actuator addres and rsa public key to encrypt de message. Follow
       ## the protocol.
       address = valRet[0];
       rsaKey  = valRet[1];

       message = {}
       message['resourceId'   ] = resourceID;
       message['messageId'    ] = self.__count;
       message['timestamp'    ] = time.time(); 
       message['action'       ] = actionDescription;
       message['configuration'] = value;

       self.__count += 1;
 
       jsonMessage = json.dumps(message);

       key        = RSA.importKey(rsaKey)
       cipher     = PKCS1_OAEP.new(key)
       cipherText = cipher.encrypt(jsonMessage)

       ## Message:
       print "Sending message to Actuator: " + str(message);

       ## Send to destiny:
       self.__sendToDestiny(cipherText,address);

       return 0;


    ##
    ## BRIEF: post the messagem.
    ## ------------------------------------------------------------------------
    ## @PARAM cipherMessage == message cryptographed;
    ## @PARAM address       == actuator endpoint.
    ##
    def __sendToDestiny(self, cipherMessage, address):
       valRet = requests.post(address, data=cipherMessage);


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
        queue.execute=False;
        queue.consumer.unsubscribe([KAFKA_TOPIC_RECV]);
        queue.consumer.close()

    sys.exit(0);
## EOF.
