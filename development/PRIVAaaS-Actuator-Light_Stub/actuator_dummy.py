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
import socket;

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

PROBE_HOST = '127.0.0.1'     # Endereco IP do Servidor.
PROBE_PORT = 6000            # Porta que o Servidor.





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
    tcp = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    dest = (PROBE_HOST, PROBE_PORT);

    tcp.connect(dest);
 
    ## Message:
    print "Sending message to PRIVAaaS: " + str(message);

    tcp.send (str(message['k']));
    tcp.close();

    return 0;
## End.








##
## BRIEF: Receive messages: 
## ----------------------------------------------------------------------------
##
@app.route('/', methods=['POST'])
def process_message():
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

        ## Message Received:
        print "Message Receveid From Execute " + str(message);

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
    app.run(host=BIND, port=PORT);

## EOF.
