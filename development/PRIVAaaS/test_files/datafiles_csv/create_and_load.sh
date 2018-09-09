#!/bin/bash




###############################################################################
## DEFINITION                                                                ##
###############################################################################
FILES="ADULTO.csv  CMC.csv  DERMAOLOGY.csv  HEPATITIS.csv  INTERNET.csv  MAMOGRAIA.csv  RISK_FACTORS_CERVICAL_CANCER.csv  WDBC.csv"

DBNAME="PRIVaaS"
DBUSER="root"
DBPASS="password"

TEMPLATE_NAME="template.sql"








###############################################################################
## MAIN                                                                      ##
###############################################################################
mysql -u ${DBUSER} -p${DBPASS} < ${TEMPLATE_NAME}

for FILE in ${FILES}; do
    mysqlimport --ignore-lines=1 --fields-terminated-by=, --verbose --local -u ${DBUSER} -p${DBPASS} ${DBNAME} ${FILE}
done

## EOF
