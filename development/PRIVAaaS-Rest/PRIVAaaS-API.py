#!/usr/bin/python








###############################################################################
## IMPORT                                                                    ##
###############################################################################
from StringIO import StringIO
import pycurl;
import sys;
import json;

from lib.interface import Show_in_Shell;






###############################################################################
## DEFINITIONS                                                               ##
###############################################################################
WEB_BIND   = "127.0.0.1";
WEB_PORT   = "9000";





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
    print text;
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
    def run(self, arguments):
        del arguments[0];

        valRet = {};

        if   len(arguments[0]) == 0:
            log("Command is not supported!");

        elif arguments[0] == "list"  :
            del arguments[0];
            valRet =  self.__list(arguments);
            self.show.list(valRet);

        elif arguments[0] == "status":
            del arguments[0];
            valRet = self.__status(arguments);
            self.show.status(valRet);

        elif arguments[0] == "create":
            del arguments[0];
            valRet = self.__create(arguments);
            self.show.create(valRet);

        elif arguments[0] == "finish":
            del arguments[0];
            valRet = self.__finish(arguments);
            self.show.finish(valRet);

        else:
            log("Command is not supported!");

        return valRet;


    ##########################################################################
    ## PRIVATE METHODS                                                       ##
    ##########################################################################
    ##
    ## BRIEF: get all PRIVAaaS instances running.
    ## -----------------------------------------------------------------------
    ## @PARAM arguments == arguments array.
    ##
    def __list(self, arguments):
        dataToSend = {};

        valRet = self.__sendCurlPost(dataToSend, "/list");
        valRet = json.loads(valRet);
        return valRet;


    ##
    ## BRIEF: check PRIVAaaS instance status.
    ## -----------------------------------------------------------------------
    ## @PARAM arguments == arguments array.
    ##
    def __status(self, arguments):
        valRet = "{}";

        ## Verify if the arguments has a instanceId:
        if len(arguments) == 1:
 
            ## Verify if the instanceID is in valid format:
            if self.__validInstanceId(arguments[0]) == True:
                dataToSend = {
                    'instanceID': str(arguments[0])
                };

                valRet = self.__sendCurlPost(json.dumps(dataToSend),"/status");
            else:
                self.__usage("InstanceID in incorrect format!");
        else:
            self.__usage("InstanceID not found!");

        return valRet;


    ##
    ## BRIEF: create PRIVAaaS instance.
    ## -----------------------------------------------------------------------
    ## @PARAM arguments == arguments array.
    ##
    def __create(self, arguments):
        valRet = "{}";

        if len(arguments) == 3:
            dataToSend = {
                "rwdata" : arguments[0],
                "policy" : arguments[1],
                "k"      : arguments[2]
            };

            valRet = self.__sendCurlPostCreate(dataToSend);
        else:
            self.__usage("Few arguments!");

        valRet = json.loads(valRet);
        return valRet;


    ##
    ## BRIEF: finish PRIVAaaS instance, get the result.
    ## -----------------------------------------------------------------------
    ## @PARAM arguments == arguments array.
    ##
    def __finish(self, arguments):
        print arguments


    ##
    ## BRIEF:
    ## ------------------------------------------------------------------------
    ##
    ##
    def __usage(self, stringToShow):
        print stringToShow;
        print "..."


    ##
    ## BRIEF:
    ## ------------------------------------------------------------------------
    ##
    ##
    def __sendCurlPostCreate(self, dataToSend):

        curlRequest = pycurl.Curl();
        buffer = StringIO()

        print dataToSend

        curlRequest.setopt(pycurl.HTTPHEADER, ['Accept: multipart/form-data']);
        curlRequest.setopt(curlRequest.URL, WEB_BIND+":"+WEB_PORT+"/create");
        curlRequest.setopt(pycurl.POST, 1);
        curlRequest.setopt(curlRequest.HTTPPOST, [
            ("policy", (curlRequest.FORM_FILE, dataToSend['policy'])),
            ("rwdata", (curlRequest.FORM_FILE, dataToSend['rwdata'])),
            ("k", str(dataToSend['k']))]);

        curlRequest.perform();
        curlRequest.close();

        bodyAnswer = buffer.getvalue();
        return bodyAnswer;


    ##
    ## BRIEF:
    ## ------------------------------------------------------------------------
    ##
    ##
    def __sendCurlPost(self, dataToSend, postPlace):
        curlRequest = pycurl.Curl();
        buffer = StringIO()

        curlRequest.setopt(curlRequest.URL, WEB_BIND+":"+WEB_PORT+postPlace);
        curlRequest.setopt(pycurl.HTTPHEADER, ['Accept: application/json']);
        curlRequest.setopt(curlRequest.WRITEDATA, buffer);
        curlRequest.setopt(pycurl.POST, 1);
        curlRequest.setopt(pycurl.POSTFIELDS, json.dumps(dataToSend));
        curlRequest.perform();
        curlRequest.close();

        bodyAnswer = buffer.getvalue();
        return bodyAnswer;


    ##
    ## BRIEF:
    ## ------------------------------------------------------------------------
    ##
    ##
    def __validInstanceId(self, instanceId):
        return True;


## END CLASS.




###############################################################################
## MAIN                                                                      ##
###############################################################################
if __name__ == "__main__":

    try:
        main = Main();
        main.run(sys.argv);

    except ValueError as exceptionNotice:
        log(exceptionNotice);

    sys.exit(0);

## EOF.
