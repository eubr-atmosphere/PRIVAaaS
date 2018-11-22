#!/usr/bin/env python








###############################################################################
## IMPORT                                                                    ##
###############################################################################
import os;
import json;

from jsonschema import Draft4Validator;
from flask import Flask;
from flask import request;

import logging
import logging.config



logger = logging.getLogger(__name__)
logger.info('Starting Monitor Server Python')






###############################################################################
## DEFINES                                                                   ##
###############################################################################
DEBUG="False"

BIND="0.0.0.0"
PORT=5000








###############################################################################
## PROCEDURES                                                                ##
###############################################################################
app = Flask(__name__)

print "-----------------------------------------------------------------------"
print "Starting Monitor Server Python";
print "-----------------------------------------------------------------------"

## Load JSON schema:
with open('schemas/atmosphere_tma-m_schema.json') as f:
    tmaMSchema = json.load(f);

## Validate the schema:
validator = Draft4Validator(tmaMSchema);


##
## BRIEF: Receive messages: 
## ----------------------------------------------------------------------------
##
@app.route('/monitor', methods=['GET', 'POST'])
def process_message():
    logger.info('3Processing Request %s', str(request))

    ## Reject GET:
    if request.method == 'GET':
        return "Method GET is not supported!"

    ## Load json file:
    input = request.get_json(force=True);

    print "Processing Request " + str(input);

    return validate_schema(input);
## END.


##
## BRIEF: validate the message schema: 
## ----------------------------------------------------------------------------
##
def validate_schema(inputMsg):
    try:
        ## check if there are errors in json file and return the result:
        errors = [error.message for error in validator.iter_errors(inputMsg)];

        if errors:
            ## Calculate the number o errors found:
            qtdeErrors = str(len(errors));

            ##
            response = "Number of erros: " + qtdeErrors+"\n"+ str(errors)+"\n";
            return response;
        else:
  	    ## Convert dictionary into string. Kafka only accept messages at by-
            ## tes or string format.
            jd = json.dumps(inputMsg);

            # Sending message
            #producer.send_messages('topic-monitor', jd)
            return "0k";

    except Exception, e:
        print "Error Code -1: " + str(e);
## END.



# load logging configuration file
def setup_logging(default_path='logging.json', env_key='LOG_CFG'):
    path = default_path
    value = os.getenv(env_key, None)
    if value:
        path = value
    if os.path.exists(path):
        with open(path, 'rt') as f:
            config = json.load(f)
        logging.config.dictConfig(config)
    else:
        logging.basicConfig(level=logging.DEBUG)






###############################################################################
## MAIN                                                                      ##
###############################################################################
if __name__ == '__main__':
    setup_logging()
    logger = logging.getLogger(__name__)
    #app.run(debug='True', host='0.0.0.0', port=5000, ssl_context=('cert.pem', 'key.pem'))
    app.run(debug=DEBUG, host=BIND, port=PORT);

## EOF.
