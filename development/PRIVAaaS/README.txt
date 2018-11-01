USAGE:
=========
java -jar dist/PRIVAaaSAllInOneJar.jar -d test_files/weekly-usage.csv -p test_files/param.csv -o test_01.csv

where -d refers to the input file, -p refers to the policy, -o refers to the output anonymized file that will be generated.

=========

POLICIES:

The policies must specify all the fields from the database (otherwise they will not appear  in the anonymized dataset). For each field you must specify the technique to be applied according to the parameters establised below:

1 =  INSENSITIVE_ATTRIBUTE (will not be anonymized)
2 =  IDENTIFYING_ATTRIBUTE (suppression)
3 =  QUASI_IDENTIFYING_ATTRIBUTE (generalization) 
4 =  SENSITIVE_ATTRIBUTE (suppression)
SR = QUASI_IDENTIFYING_ATTRIBUTE(supression starting from Right);
SL = QUASI_IDENTIFYING_ATTRIBUTE(supression starting from Left);
DT = QUASI_IDENTIFYING_ATTRIBUTE(generalization - Use Hierarchy "birthdate.csv");
AG = QUASI_IDENTIFYING_ATTRIBUTE(generalization - Use Hierarchy "age.csv");
CT = QUASI_IDENTIFYING_ATTRIBUTE(generalization - Use Hierarchy “time.csv");

The “Hierachy” directory have some specifications for some commom quasi-identifiers (age, gender, marital status, etc.). For generalizing different quasi-identifiers it is necessary to create a new hierarchy file.

You must set the correct path of the “Hierarchy” directory in the policy.