#!/usr/bin/env python








###############################################################################
## IMPORT                                                                    ##
###############################################################################
import os;
import json;
import sys;
import Crypto;
import ast;
import logging.config;
import logging;
import requests;

from jsonschema       import Draft4Validator;
from flask            import Flask;
from flask            import request;
from Crypto.PublicKey import RSA;
from Crypto.Cipher    import PKCS1_OAEP;









###############################################################################
## DEFINES                                                                   ##
###############################################################################
DEBUG="False"

BIND="0.0.0.0"
PORT=9001

PROBE_URL="http://192.168.0.12:9002"






###############################################################################
## PROCEDURES                                                                ##
###############################################################################
app = Flask(__name__)

print "-----------------------------------------------------------------------"
print "Starting Monitor Server Python";
print "-----------------------------------------------------------------------"



##
## BRIEF: Send Json message to Probe: 
## ----------------------------------------------------------------------------
## @PARAM message == message to send.
##
def send_message_to_update_k(message):

    key   =  message.keys()[0];
    value =  message[key];

    ## Send Message
    valRet = requests.post(PROBE_URL + "/update", json={key: value})
    
    print "Sending data to probe, return code: " + str(valRet.status_code);
    return 0;
## End.








##
## BRIEF: Receive messages: 
## ----------------------------------------------------------------------------
##
@app.route('/', methods=['POST'])
def process_message():
    print 'Processing Request ' + str(request);

    if request.method == 'POST':

        try:
            cipherMessage = request.data;
        except:
            return "Error!";

        ## Get the private key value:
        key = RSA.importKey(open('/root/privaaas').read());

        ## Create a new objetc that handle de criptographic key:
        cipher = PKCS1_OAEP.new(key);

        ## Descript the message received:
        jsonMessage = cipher.decrypt(cipherMessage);

        message = json.loads(jsonMessage);

        ## HANDLE ACTIONS:
        if message['action'].strip() == "update k":
            send_message_to_update_k(message['configuration']);
        else:
            print "Action is not recoginized!!!!";
         
    else:
        return "Method GET is not supported!"

    return "ok";
## END.








###############################################################################
## MAIN                                                                      ##
###############################################################################
if __name__ == '__main__':
    app.run(debug=DEBUG, host=BIND, port=PORT);

## EOF.
