#!/usr/bin/python








###############################################################################
## IMPORT                                                                    ##
###############################################################################
from StringIO import StringIO
import pycurl;
import os;
import sys;
import json;
import requests;
import time;

from lib.interface    import Show_in_Shell;
from Crypto.PublicKey import RSA;
from Crypto.Cipher    import PKCS1_OAEP;
from Crypto           import Random;






###############################################################################
## DEFINITIONS                                                               ##
###############################################################################
RSA_KEY="ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABAQC4wpJqq6cwEiWqnWbaLembwQP9bQLk6IRZknepmo2iHhoKpIEILW8e8CfStSrk/DaDTiNRWH/IGe6m8/hacE5WcUi4eEtqKL8fEsyjGn5tI2rmf7Pgk83L2oBbLBKJ6zft7nDF5HJ+aQQ985Y/bkPYsObwC0WXyxdcS2sMYurW3ZXivAuUHmCkdU0lihRCOgkY5KcOWgS8dFRI4vRKNPhttYDbXxk14NENITCQcp98fNy9TbaTeQA0fOaiTxWldK43ha+utUhduELaP6lOvOS57ZRdsRbU8g0VZ2ZYmWxBfT13c2p78Wg+YQl4STN3vBTkzs/cUBwvuC/L7Fn0S1hj marcio@case"





###############################################################################
## PROCEDURES                                                                ##
###############################################################################
##
## BRIEF: log the event description.
## ----------------------------------------------------------------------------
## @PARAM text == text to log.
##
def log(text):
    ## Print text:
    #print text;
    pass
## END.







###############################################################################
## CLASSES                                                                   ##
###############################################################################
class Main:

    """
    ---------------------------------------------------------------------------
    """


    ##########################################################################
    ## ATTRIBUTES                                                           ##
    ##########################################################################
    show = None;


    ##########################################################################
    ## SPECIAL METHODS                                                      ##
    ##########################################################################
    def __init__(self):
        self.show = Show_in_Shell();


    ##########################################################################
    ## PUBLIC METHODS                                                       ##
    ##########################################################################
    def execute(self, args):

       message = {}
       message['resourceId'   ] = 0x0001;
       message['messageId'    ] = 0x0001;
       message['timestamp'    ] = time.time();
       message['action'       ] = 0x0001;
       message['configuration'] = {
               'k'          : args[1],
               'instanceID' : 2
                                  };

       jsonMessage = json.dumps(message);

       key        = RSA.importKey(RSA_KEY)
       cipher     = PKCS1_OAEP.new(key)
       cipherText = cipher.encrypt(jsonMessage)

       ## Send to destiny:
       self.__sendToDestiny(cipherText, "http://127.0.0.1:9000/update_k");

       return 0;


    ##
    ## BRIEF: post the messagem.
    ## ------------------------------------------------------------------------
    ## @PARAM cipherMessage == message cryptographed;
    ## @PARAM address       == actuator endpoint.
    ##
    def __sendToDestiny(self, cipherMessage, address):
       valRet = requests.post(address, data=cipherMessage);

## END CLASS.




###############################################################################
## MAIN                                                                      ##
###############################################################################
if __name__ == "__main__":

    try:
        main = Main();
        main.execute(sys.argv);

    except ValueError as exceptionNotice:
        log(exceptionNotice);

    sys.exit(0);

## EOF.
