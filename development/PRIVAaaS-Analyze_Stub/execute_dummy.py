#!/usr/bin/env python








###############################################################################
## IMPORT                                                                    ##
###############################################################################
import os;
import sys;
import json;
import subprocess;
import time;

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
    consumer   = None;
    producer   = None;
    execute    = True;
    __lastDate = '0001-01-01 00:00:00.000000';


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
            self.__watch_database();
            time.sleep(5);


    ###########################################################################
    ## PRIVATE METHODS                                                       ##
    ###########################################################################
    def __watch_database(self):

       v = [0, 0.0, 0.0, 0.0, 0.0, 0.0];
       d = ['','', '', '', '', ''];

       count  = 0;

       for resourceId in range(27, 33):
           query  = "SELECT * FROM Data WHERE descriptionId=" + str(resourceId);
           query += " ORDER BY valueTime DESC LIMIT 1";

           ## TODO: remove and use kubernetes api python!
           bashCommand = 'kubectl exec -ti mysql-0 -- bash -c "mysql -u root --password=\$MYSQL_ROOT_PASSWORD knowledge -s -N -e \\"' + query + '\\"" | tail -n 1'

           ## Get action description:
           try:
               valRet = subprocess.check_output(bashCommand, shell=True);
               valRet = valRet.split();

               v[count] = valRet[5];
               d[count] = time.strptime(valRet[3]+' '+valRet[4], "%Y-%m-%d %H:%M:%S.%f");

           except:
               pass;

           count += 1;

       ## Check if exist dataloss:  
       if d[0] == d[1] == d[2] == d[3] == d[4] == d[5]:

           ## If the sample is new analyze the score:
           if d[0] > self.__lastDate: 
               self.__analyze_score(v);

               ## Set new date:
               self.__lastDate = d[0];
           else:
               print "New samples not found...";

       else:
           print "Data loss...";

       return 0;


    ##
    ## BRIEF:
    ## ------------------------------------------------------------------------
    ##
    def __analyze_score(self, values):
     
        k = values[0];

        message = {"probeId": "8","configuration": {"k": values[0]}};

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

