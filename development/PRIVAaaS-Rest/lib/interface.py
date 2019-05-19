#!/usr/bin/python








###############################################################################
## IMPORT                                                                    ##
###############################################################################
import json;







###############################################################################
## DEFINITIONS                                                               ##
###############################################################################
SUCCESS=0
PROBLEM=1

SUCCESS    = 0x0000;
FINISHED   = 0x0001;
DONT_EXIST = 0x0002;
EXIST      = 0x0003;
RUNNING    = 0x0010;
GET_RESULT = 0x0011;
GET_K      = 0x0012;
ERROR      = 0x0013;
FAILED     = 0x0014;






###############################################################################
## PROCEDURES                                                                ##
###############################################################################








###############################################################################
## CLASSES                                                                   ##
###############################################################################
class Show_in_Shell:

    """
    ---------------------------------------------------------------------------
    """


    ##########################################################################
    ## ATTRIBUTES                                                           ##
    ##########################################################################


    ##########################################################################
    ## SPECIAL METHODS                                                      ##
    ##########################################################################


    ##########################################################################
    ## PUBLIC METHODS                                                       ##
    ##########################################################################
    ##
    ## BRIEF: 
    ## -----------------------------------------------------------------------
    ## @PARAM arguments == arguments to show.
    ##
    def list(self, arguments):

        if len(arguments) != 0:
            print "INSTANCES"
            print "---------------------------------------------------"
            for instanceKey in arguments.keys():
                print instanceKey,arguments[instanceKey];
            print "---------------------------------------------------"
        else:
            print 'Instances not found!'

    ##
    ## BRIEF: 
    ## -----------------------------------------------------------------------
    ## @PARAM arguments == arguments to show.
    ##
    def status(self, arguments):
        if   int(arguments['status']) == FINISHED:
            print "Finished!"

        elif int(arguments['status']) == RUNNING :
            print "Running!!"

        elif int(arguments['status']) == FAILED  :
            print "Failed!!!"
        else:
            print arguments;


    ##
    ## BRIEF: 
    ## -----------------------------------------------------------------------
    ## @PARAM arguments == arguments to show.
    ##
    def create(self, arguments):
        if int(arguments["statusReturn"]) == SUCCESS:
            status = "create with sucess!"
        else:
            status = "a problem was found - code " + arguments['status'];

        print "InstanceID: " + str(arguments["instanceID"]);
        print "Status....: " + status;


    ##
    ## BRIEF: 
    ## -----------------------------------------------------------------------
    ## @PARAM arguments == arguments to show.
    ##
    def finish(self, arguments):

 
       print "STATUS"
       print "---------------------------------------------------"
       print "status: " + str(arguments["status"]);
       print "---------------------------------------------------"

       print "METRICS"
       if arguments.has_key("metrics"):
           print(json.dumps(arguments["metrics"], indent=4, sort_keys=True)); 
       print "---------------------------------------------------"

       if arguments.has_key("dataAnonymized"):
           print(json.dumps(arguments["dataAnonymized"], 
                            indent=4, 
                            sort_keys=True)); 





    ##########################################################################
    ## PRIVATE METHODS                                                      ##
    ##########################################################################
## END CLASS.




###############################################################################
## MAIN                                                                      ##
###############################################################################
if __name__ == "__main__":

    try:
        main = Show_in_Shell();

    except ValueError as exceptionNotice:
        log(exceptionNotice);

    sys.exit(0);

## EOF.
