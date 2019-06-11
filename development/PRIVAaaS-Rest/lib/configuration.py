#!/usr/bin/python








###############################################################################
## IMPORT                                                                    ##
###############################################################################
from ConfigParser import SafeConfigParser;

import sys;







###############################################################################
## CLASSES                                                                   ##
###############################################################################
class Configparse:

    """
    ---------------------------------------------------------------------------
    """


    ##########################################################################
    ## ATTRIBUTES                                                           ##
    ##########################################################################
    __filename = None;


    ##########################################################################
    ## SPECIAL METHODS                                                      ##
    ##########################################################################
    def __init__(self, filename):
        self.__filename = filename;


    ##########################################################################
    ## PUBLIC METHODS                                                       ##
    ##########################################################################
    ##
    ## BRIEF:
    ## ----------------------------------------------------------------------
    def get_config(self):
        parser = SafeConfigParser();

        ## Read config from filename. Make a parse:
        parser.read(self.__filename);

        data = {};
        for section in parser.sections():
            data[section] = {};
            for name, value in parser.items(section):
                data[section][name] = value;

        return data;
## EOF.



###############################################################################
## MAIN                                                                      ##
###############################################################################
if __name__ == "__main__":

    try:
        config = Configparse("../conf/privaaasd.ini");

        ## Data:
        data = config.get_config();

    except ValueError as exceptionNotice:
        print exceptionNotice;

    sys.exit(0);

## EOF.