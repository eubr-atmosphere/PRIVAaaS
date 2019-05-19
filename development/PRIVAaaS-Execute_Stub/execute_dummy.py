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
import pycurl;

from StringIO         import StringIO;
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
KAFKA_ADDRESS           = "kafka-0.kafka-hs.default.svc.cluster.local:9093";
KAFKA_TOPIC_RECV        = "topic-execute";
KAFKA_GROUPID           = "privaaas";

DB_USER = 'root';
DB_PASS = 'passtobereplaced';
DB_HOST = '10.98.170.151'
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
           print "Waiting message..."
           message = self.consumer.poll();

           ## Get message data:
           msgContent = message.value();

           ## Message Received:
           print "Message Received From Planning: " + str(msgContent);

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

       print "Getting the cipher keys...";

       ## Create message to send:
       query = "select address,pubKey from Actuator where actuatorId=1";

       ## Select:
       valRet = self.__select_database(query);

       ## Get actuator addres and rsa public key to encrypt de message. Follow
       ## the protocol.
       address = valRet[0];
       rsaKey  = valRet[1];

       key        = RSA.importKey(rsaKey)
       cipher     = PKCS1_OAEP.new(key)
       cipherText = cipher.encrypt(jsonMessage)

       ## Message:
       print "Sending message to Actuator...";

       ## Send to destiny:
       self.__sendToDestiny(address, cipherText);
       return 0;


    ##
    ## BRIEF: post the messagem.
    ## ------------------------------------------------------------------------
    ## @PARAM address       == actuator endpoint.
    ## @PARAM cipherMessage == message cryptographed;
    ##
    def __sendToDestiny(self, address, cipherMessage):
       curlRequest = pycurl.Curl();
       buffer = StringIO()

       curlRequest.setopt(curlRequest.URL, "127.0.0.1:9000/actuator");
       curlRequest.setopt(pycurl.HTTPHEADER, ['Accept: application/json']);
       curlRequest.setopt(pycurl.POST, 1);
       curlRequest.setopt(pycurl.POSTFIELDS, cipherMessage);
       curlRequest.setopt(pycurl.WRITEFUNCTION, buffer.write);
       curlRequest.perform();
       curlRequest.close();

       bodyAnswer = buffer.getvalue();
       buffer.close();

       return bodyAnswer;



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
