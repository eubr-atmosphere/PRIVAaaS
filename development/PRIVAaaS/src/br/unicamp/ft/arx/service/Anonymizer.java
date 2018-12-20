package br.unicamp.ft.arx.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileOutputStream;
import java.io.File;
import java.io.FileInputStream;

import java.sql.*;

import javax.json.*;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import java.util.Arrays;
import java.util.Properties;

import org.deidentifier.arx.*;
import org.deidentifier.arx.aggregates.HierarchyBuilderRedactionBased;
import org.deidentifier.arx.aggregates.HierarchyBuilderRedactionBased.Order;
import org.deidentifier.arx.AttributeType.MicroAggregationFunction;
import org.deidentifier.arx.criteria.KAnonymity;
import org.deidentifier.arx.ARXAnonymizer;
import org.deidentifier.arx.ARXConfiguration;
import org.deidentifier.arx.ARXLattice.ARXNode;
import org.deidentifier.arx.ARXResult;
import org.deidentifier.arx.AttributeType.Hierarchy;
import org.deidentifier.arx.Data;
import org.deidentifier.arx.aggregates.StatisticsEquivalenceClasses;













public class Anonymizer extends Probe {

    /*
     **************************************************************************
     ** DEFINE                                                               **
     **************************************************************************
    */    
    public static final int DONT_FINISHED = -4;
    public static final int PUBLISH_ERROR = -3;
    public static final int SOCKETL_ERROR = -2;
    public static final int TIMEOUT_ERROR = -1;
    
    
    /*
     **************************************************************************
     ** ATTRIBUTES                                                           **
     **************************************************************************
    */
    private long             __startTime;
    private long             __stopTime;
    private JsonArray        __policyJsonArray;
    
    public boolean           screen = false;
    public Data              dataAll;
    public ARXResult         result;
    public ARXNode           node;
    public boolean           publish     = true;
    public int               k           = 0;
    
    private char             __separator = ';';
    private String           __endpoint  = "http://localhost:5005/monitor";
    private int              __resourceId= 8;
    private int              __probeId   = 8;
    private int              __maxLoop   = 5;
    private int              __dId1      = 27;
    private int              __dId2      = 28;
    private int              __dId3      = 29;
    private int              __dId4      = 30;
    private int              __dId5      = 31;
    private int              __dId6      = 32;
    private int              __port      = 6000;
    
    private double           __riskP;
    private double           __riskJ;
    private double           __riskM;
    
    private String           __lScore;
    private String           __hScore;
    
    private StatisticsEquivalenceClasses __equivalenceClasses;
    
    /*
     **************************************************************************
     ** SPECIAL METHOD                                                       **
     **************************************************************************
    */
    public static void main(String[] args) throws Exception, IOException {
    
        String saveInFile = "";
        String datadbFile = "";
        String policyFile = "";
        String configFile = "";

        /* Excute the arguments parse (six arguments -name value). */        
        if (args.length >= 6) {
            
            for (int i = 0; i < args.length; i++) {        
                
                if (args[i].equals("-o")) {
                    saveInFile = args[i+1];
                }
                if (args[i].equals("-p")) {
                    policyFile = args[i+1];
                }
                if (args[i].equals("-d")) {
                    datadbFile = args[i+1];
                }
                if (args[i].equals("-c")) {
                    configFile = args[i+1];
                }
            }
            
            Anonymizer obj = new Anonymizer();
            
            obj.screen = true;
            obj.load_config(configFile);
            obj.prepare_source(datadbFile, policyFile);
            obj.run();
            
            if (obj.publish == true) {
                int newK = 0;
            
                while (newK != DONT_FINISHED) {
                    /* If configured in the config file, publish the results to
                       monitor. */
                    newK = obj.publish_message_monitor();

                    if(newK == PUBLISH_ERROR) {
                        System.out.println("Wasn't possible publish infos!");
                        break;
                    }
                    else {
                        System.out.println("Send informations to monitor!!");
                    
                        if (newK == TIMEOUT_ERROR || newK == SOCKETL_ERROR) {
                            System.out.println("Problem with the actuator!");
                            break;
                        }
                    
                        /* If receive newk from actuator. So re-execute the ano
                           nymize*/
                        if (obj.k != newK) {       
                            System.out.println("New K value received: "+newK);
                        
                            /* Prepare k: */
                            obj.prepare_source(datadbFile, policyFile);
                            obj.k = newK;
                            obj.run();
                        }
                        else {
                            System.out.println("End of loop!");
                            break;
                        }
                    }
                }
            }
            
            obj.get_json_anonymized();
            obj.show_results();
            obj.save_in_file(saveInFile);
        }
        else {
            Anonymizer.__print_how_execute();
            System.exit(-1);
        }
    }


    
    
    /* ********************************************************************* */
    /* PUBLIC METHODS                                                        */
    /* ********************************************************************* */
    /*
     BRIEF: load some configs.
     --------------------------------------------------------------------------
    */
    public void load_config(String configFile) throws FileNotFoundException, 
                                                      IOException {
        
        if ( configFile.length() != 0 ) {
         Properties properties = new Properties();
            
         try (FileInputStream in = new FileInputStream(configFile)) {
            properties.load(in);
         }

         this.__separator = properties.getProperty("SEPARATOR").charAt(0);    
         this.__endpoint  = properties.getProperty("ENDPOINT" );
            
         this.__resourceId=Integer.parseInt(properties.getProperty("RESOURCE_ID"));
            
         this.__port   =Integer.parseInt(properties.getProperty("PORT"    ));
         this.__probeId=Integer.parseInt(properties.getProperty("PROBE_ID"));
         this.__maxLoop=Integer.parseInt(properties.getProperty("MAX_LOOP"));
            
         this.__dId1 = Integer.parseInt(properties.getProperty("D_ID_1"));
         this.__dId2 = Integer.parseInt(properties.getProperty("D_ID_2"));
         this.__dId3 = Integer.parseInt(properties.getProperty("D_ID_3"));
         this.__dId4 = Integer.parseInt(properties.getProperty("D_ID_4"));
         this.__dId5 = Integer.parseInt(properties.getProperty("D_ID_5"));
         this.__dId6 = Integer.parseInt(properties.getProperty("D_ID_6"));
            
         this.publish = Boolean.parseBoolean(properties.getProperty("PUBLISH"));
        }
    }
    
    
    /*
     BRIEF: prepare data to anonymization (to CSV inputstream).
     --------------------------------------------------------------------------
     @PARAM inputStreamCSV == CSV input stream.
     @PARAM policy         == json with policies to apply.
    */
    public void prepare_source(InputStream streamCSV, JsonObject policyJsonObj)
                                                 throws IOException, Exception {

        /* Get policies' array from json: */
        this.__policyJsonArray = policyJsonObj.getJsonArray("policy");
        
        /* Get k value. */
        JsonNumber number = (JsonNumber)policyJsonObj.get("k");
        this.k = number.intValue();
        
        /* Create data from inputStreamCSV: */      
        this.dataAll = Data.create(streamCSV, Charset.forName("UTF-8"),
                                                              this.__separator);
    }
    
    
    
    
    /*
     BRIEF: prepare data to anonymization (to dataset file).
     --------------------------------------------------------------------------
     @PARAM datasetFile == file with data to anonymization.
     @PARAM pFile       == string with json filename.
    */
    public void prepare_source(String datasetFile, String pFile) throws 
                                                                   IOException, 
                                                                   Exception {
        
        /* A factory for connections to the physical data source that this Data
           Source object represents. From dataset create a CVS file. */
        DataSource source = DataSource.createCSVSource(datasetFile, 
                                                       Charset.forName("UTF-8"),
                                                       this.__separator,
                                                       true);

        /* Execute the parse in json file, and extract the appropriate fields.*/
        this.__parse_json_policy(pFile);
        
        for (int i = 0; i < this.__policyJsonArray.size(); ++i) {

            JsonObject node = this.__policyJsonArray.getJsonObject(i);
            String field = node.getString("field");
                       
            /* Set the collum type (field) to string. */
            source.addColumn(field, DataType.STRING);
        }
 
        try {
            this.dataAll = Data.create(source);
        }
        catch (java.lang.IllegalArgumentException e) {
            if (this.screen == true) {
                System.err.println("Column not found: " + e + "\nTrace:");
                e.printStackTrace();
                System.exit(-1);
            }
            else {
                throw new Exception(e); 
            }
        }
    }

    
    
    
    /*
        BRIEF: execute the anonymization algorithm.
        -----------------------------------------------------------------------
    */
    public void run() throws IOException, Exception {
         
        /* Apply all policies receveid from policy file. */
        this.apply_policies();

        /* Creates a new configuration without tuple suppression. After adds a
           privacy model to the configuration. Allows for a certain percentage
           of outliers and thus triggers tuple suppression. */
        ARXConfiguration cfg = ARXConfiguration.create();

        cfg.addPrivacyModel(new KAnonymity(this.k));
        cfg.setSuppressionLimit(0d);

        /* Mensure exec time: start. */
        this.__startTime = System.currentTimeMillis();

        /* Offers several methods to define params and execute the ARX alg. */
        ARXAnonymizer anonymizer = new ARXAnonymizer();

        /* Encapsulates the results of an execution of the "ARX algorithm". */
        try {
            this.result = anonymizer.anonymize(this.dataAll, cfg);
            
        } catch (java.lang.IllegalArgumentException e) {
            if (this.screen == true) {
                System.err.println("Errors were found: " + e + "\nTrace:");
                e.printStackTrace();
                System.exit(-1);
            }
            else {
                throw new Exception(e); 
            }
        }
       
        /* Obtain the global optimum (ARXLattice.ARXNode): */
        this.node  = this.result.getGlobalOptimum();

        /* Mensure exec time: End. */
        this.__stopTime = System.currentTimeMillis();
        
        /* Create a population model. Obtain: sample size and the sampling frac-
           tion value. */        
        ARXPopulationModel pModel = ARXPopulationModel.create(
                                          this.dataAll.getHandle().getNumRows(),
                                          0.01d);

        this.__riskP = this.result.getOutput()
                                  .getRiskEstimator(pModel)
                                  .getSampleBasedReidentificationRisk()
                                  .getEstimatedProsecutorRisk();
        
        this.__riskJ = this.result.getOutput()
                                  .getRiskEstimator(pModel)
                                  .getSampleBasedReidentificationRisk()
                                  .getEstimatedJournalistRisk();
        
        this.__riskM = this.result.getOutput()
                                  .getRiskEstimator(pModel)
                                  .getSampleBasedReidentificationRisk()
                                  .getEstimatedMarketerRisk();
 
        this.__lScore = this.node.getLowestScore().toString() ;
        this.__hScore = this.node.getHighestScore().toString();
        
        /* Statistic: */
        this.__equivalenceClasses = this.result.getOutput(this.node, false)
                                    .getStatistics()
                                    .getEquivalenceClassStatistics();
    }


    
    
    /*
      BRIEF: return the resultset anonymized.
      ------------------------------------------------------------------------
    */
    public JsonArray get_json_anonymized() throws SQLException  {
        
        JsonArrayBuilder register = Json.createArrayBuilder();
        
        int colNum = this.result.getOutput().getNumColumns();
        int colRow = this.result.getOutput().getNumRows();
                
        String colVal;
        for (int i = 0; i < colRow; i++ ) {
            JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
            
            for (int j = 0; j < colNum; j++ ) {                
                colVal = this.result.getOutput().getValue(i,j);
                
                /* Get field name: */
                String field = this.dataAll.getHandle().getAttributeName(j);
                objectBuilder.add(field, String.valueOf(colVal));
            }
            
            register.add(objectBuilder);
        }   
        return register.build();
    }
    
    
    
    
    /*
      BRIEF: show the result from algorithm execution.
      ------------------------------------------------------------------------
    */
    public void show_results() {

        /* Get the quantity of quasi identiying attrs.Set the string to show.*/
        int size = 
            this.dataAll.getDefinition().getQuasiIdentifyingAttributes().size();
        
        /**/
        String numRows = this.dataAll.getHandle().getNumRows()+" records with "+
                         size + " quasi-identifiers";

        /*The class ARXLattice offers several methods for exploring the soluti-
          on space and for obtaining information about the properties of trans-
          formations (represented by the class ARXNode).*/
        int v1 = this.result.getLattice().getSize();      

        /* Get solutions (returns the transformation as an array). */        
        String v2 = Arrays.toString(this.node.getTransformation());
        
        /* Returns whether the search space has been characterized completely
           (i.e. whether an optimal solution has been determined, not whether
           all transformations have been materialized).*/
        boolean v3 = this.result.getLattice().isComplete();
        
        /* Time needed to reach the solution. */
        String v4 = this.result.getTime() + " [ms]";                
        
        /* Calculate the score. */
        String score = this.__lScore + "|" + this.__hScore;
        
        System.out.println("                                                 ");
        System.out.println("-- OUTPUT DATA                                   ");
        System.out.println("================================================ ");
        System.out.println("- Mixed Risk                                     ");
        System.out.println("*Prosecutor re-identification risk: "+this.__riskP);
        System.out.println("*Journalist re-identification risk: "+this.__riskJ);
        System.out.println("*Marketer   re-identification risk: "+this.__riskM);
        System.out.println("*K anonimity .....................: "+this.k      );
        System.out.println("                                    ");
        System.out.println("- Information Loss                  ");
        System.out.println("*.................................: "+score);
        System.out.println("                                    ");
        System.out.println("- Statistics                        ");
        System.out.println("*.................................: " + this.__equivalenceClasses);
        System.out.println("                                    ");        
        System.out.println("- Data:                             ");
        System.out.println("* ................................: "+numRows);
        System.out.println("                                    ");
        System.out.println("- Policies available                ");
        System.out.println("*.................................: " + v1);
        System.out.println("                                    ");
        System.out.println("- Solution                          ");
        System.out.println("* .................................:" + v2);
        System.out.println("* Optimal..........................:" + v3);
        System.out.println("* Time needed......................:" + v4);
        System.out.println("========================================" );
        System.out.println("Done!");
        System.out.println("*** Total Execution Time: "
                                 + (this.__stopTime - this.__startTime)+"[ms]");
    }  
    
    
    
    
    /*
    BRIEF: save the json value in file.
    ---------------------------------------------------------------------------
    @PARAM saveIn == file to save the output.
    */
    public void save_in_file(String saveInFile) throws Exception {
        
        /**/
        JsonArray jsonArrayResult = this.get_json_anonymized();
        
        File file = new File(saveInFile);
        file.createNewFile();

        try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {

            JsonWriter jsonWriter = Json.createWriter(fileOutputStream);
            
            jsonWriter.writeArray(jsonArrayResult);
            jsonWriter.close();

            /* Flush and close file output streams. */
            fileOutputStream.flush();
            fileOutputStream.close();
        }
        catch (Exception e) {
            if (this.screen == true) {
                System.err.println("Error: " + e + "\nTrace:");
                e.printStackTrace();
                System.exit(-1);
            }
            else {
                throw new Exception(e); 
            }
        }
    }    
    
    
    
    
    /*
     * BRIEF: send data to monitor and receive the new k value from actuator.
     * ------------------------------------------------------------------------
     */
    public int publish_message_monitor() throws IOException,
                                                        ClassNotFoundException {
    
        double l = Double.parseDouble(this.__lScore);
        double h = Double.parseDouble(this.__hScore);
        
        double score = l;
        if (h > score) {
            score = h;
        }
        
        /* Publish to monitor: k, risk and loss. */
        int newK = this.publish_message(this.__endpoint,
                                     System.currentTimeMillis(),
                                     this.__resourceId,
                                     this.__probeId,
                                     this.__dId1,
                                     this.__dId2,
                                     this.__dId3,
                                     this.__dId4,
                                     this.__dId5,
                                     this.__dId6,
                                     this.k,
                                     this.__riskP, 
                                     this.__riskJ,
                                     this.__riskM,
                                     score,
                                     this.__maxLoop,
                                     this.__port);

        return newK;
    }
    
    
    
    
    /* ********************************************************************* */
    /* PROTECTED METHODS                                                     */
    /* ********************************************************************* */
    /*
    BRIEF: apply the policies defineds in the policy file.
    ----------------------------------------------------------------------------
    */        
    protected void apply_policies() throws IOException {
        
        for (int i = 0; i < this.__policyJsonArray.size(); ++i) {

            JsonObject node = this.__policyJsonArray.getJsonObject(i);
            
            String field = node.getString("field");
            String data  = node.getString("data" );
                       
            switch (data) {
                case "1" :
                    /* Insensitive attrs will be kept as is. */
                    this.apply_insensitive_attr(field);
                    break;
                               
                case "2" :
                case "4" :
                    /* Directly-identifying attrs will be rm from the dataset.*/
                    this.apply_identifying_attr(field);
                    break;
                            
                case "3" :
                    /* Quasi-identifying attrs will be transformed by applying 
                       the provided generalization hierarch. */
                    this.apply_quasi_identifying_attr(field);
                            
                    /* Associates the given microaggregation func. When configu
                       ring microaggregation with this method generalization hi
                       erarchies will not be used for clustering attribute valu
                       es before aggregation. Creates a microaggregation functi
                       on using generalization. Ignores missing data. */
                    this.apply_micro_aggregation(field);
                    break;
                               
                case "SR":
                    /* Quasi-identifying attrs will be transformed by applying 
                       the provided generalization hierarchies. */
                    this.apply_quasi_identifying_attr(field);
                            
                    /* Enables building hierarchies for categorical and non-ca-
                       tegorical values using redaction(order is left-to-right).
                       Apply the suppressing. */
                    this.apply_hierarchy_leftToRight_attr(field);
                    break;
                               
                case "SL":
                    /* Quasi-identifying attributes will be transformed by app
                       lying the provided generalization hierarchies. */
                    this.apply_quasi_identifying_attr(field);
                            
                    /* Like apply_hierarchy_leftToRight_attr, but in inverse or
                       der: right-to-left.*/
                    this.apply_hierarchy_rightToLeft_attr(field);
                    break;
                    
                case "AG":
                case "DT":                    
                case "CT":
                    String hierarchyFile = node.getString("hierarchy_file");
                    
                    /* load and apply a custom hierarchy from a file (arg).*/
                    this.apply_hierarchy_attr(field, hierarchyFile);
                    break;

                default: 
                    throw new IllegalArgumentException("Invalid value"+ data);
            }
        }
    }
    
    
    
    
    /*
    BRIEF: insensitive attributes will be kept as is.
    ----------------------------------------------------------------------------
    @PARAM field == field (column) to apply the attribute.
    */
    protected void apply_insensitive_attr(String field) {
        
        this.dataAll.getDefinition().setAttributeType(field, 
                                           AttributeType.INSENSITIVE_ATTRIBUTE);
    }
    
    
    
    
    /*
    BRIEF: directly-identifying attributes will be removed from the dataset.
    ----------------------------------------------------------------------------
    @PARAM field == field (column) to apply the attribute.
    */
    protected void apply_identifying_attr(String field) {
        
        this.dataAll.getDefinition().setAttributeType(field, 
                                     AttributeType.IDENTIFYING_ATTRIBUTE);
    }
    
    
    
    
    /*
    BRIEF: quasi-identifying attributes will be transformed by applying the 
           provided generalization hierarchies.
    ----------------------------------------------------------------------------
    @PARAM field == field (column) to apply the attribute.
    */
    protected void apply_quasi_identifying_attr(String field) {
        
        this.dataAll.getDefinition().setAttributeType(field, 
                                     AttributeType.QUASI_IDENTIFYING_ATTRIBUTE);
    }


    
    
    /*
    BRIEF: load and apply a hierachy from a file.
    ----------------------------------------------------------------------------
    @PARAM field == field (column) to apply the attribute.
    */
    protected void apply_hierarchy_attr(String field, String filePath) throws 
                                                                   IOException {
        
        this.dataAll.getDefinition().setAttributeType(field, 
                       Hierarchy.create(filePath, StandardCharsets.UTF_8,
                       this.__separator));
    }


    
    
    /*
    BRIEF: Enables building hierarchies for categorical and non-categorical va-
           lues using redaction (order is left-to-right). Apply the suppressing.
    ----------------------------------------------------------------------------
    @PARAM field == field (column) to apply the attribute.
    */
    protected void apply_hierarchy_leftToRight_attr(String field) {

        HierarchyBuilderRedactionBased<?> supressing = 
                      HierarchyBuilderRedactionBased.create(Order.LEFT_TO_RIGHT, 
                                                            Order.LEFT_TO_RIGHT,
                                                            ' ','*');

        this.dataAll.getDefinition().setAttributeType(field, supressing);
    }
    

    
    
    /*
    BRIEF: Enables building hierarchies for categorical and non-categorical va
           lues using redaction (order is right-to-left). Apply the suppressing.
    ----------------------------------------------------------------------------
    @PARAM field == field (column) to apply the attribute.
    */
    protected void apply_hierarchy_rightToLeft_attr(String field) {

        HierarchyBuilderRedactionBased<?> supressing = 
                      HierarchyBuilderRedactionBased.create(Order.RIGHT_TO_LEFT, 
                                                            Order.RIGHT_TO_LEFT,
                                                            ' ','*');

        this.dataAll.getDefinition().setAttributeType(field, supressing);
    }

    
    
                  
    /*
    BRIEF: associates the given microaggregation function. When configuring mi
           croaggregation with this method generalization hierarchies will not
           be used for clustering attribute values before aggregation. Creates
           a microaggregation function using generalization. Ignores missing
           data.
    ----------------------------------------------------------------------------
    @PARAM field == field (column) to apply the attribute.
    */
    protected void apply_micro_aggregation(String field) {
        this.dataAll.getDefinition().setMicroAggregationFunction(field, 
                               MicroAggregationFunction.createGeneralization());
    }
    
    
    
    
    /*
      BRIEF: print how use the anonymization in cli.
      ------------------------------------------------------------------------
    */
    static private void __print_how_execute() {
        
        System.err.println("Few arguments received.");

        System.out.println("USAGE:");
        System.out.println("-------------------------------------------------");
        System.out.println("anonymization -k <val> -p <val> -d <val> -o <val> [-c <file>]");
        System.out.println("* -k: k-anonymization paramenter (int).");
        System.out.println("* -p: policies filepath (string).");
        System.out.println("* -d: input    filepath (string).");
        System.out.println("* -o: output   filepath (string).");
        System.out.println("* -c: config   filepath (string).");
    }
    
    
    
    
    /*
    BRIEF: execute the json parse.
    ----------------------------------------------------------------------------
    @PARAM pFile == file with json content.
    */
    private void __parse_json_policy(String pFile) throws FileNotFoundException, 
                                                          Exception  {
        
        JsonObject policyJsonObj = null;
        
        try (JsonReader jsonReader = Json.createReader(new FileReader(pFile))) { 
            policyJsonObj = jsonReader.readObject();
            jsonReader.close();
        } catch (JsonException e) {
            if (this.screen == true) {
                System.err.println("Error: " + e + "\nTrace:");
                e.printStackTrace();
                System.exit(-1);
            }
            else {
                throw new Exception(e); 
            }
        }
        
        /* Get policies' array from json: */
        this.__policyJsonArray = policyJsonObj.getJsonArray("policy");
        
        /* Get k value. */
        JsonNumber number = (JsonNumber)policyJsonObj.get("k");
        this.k = number.intValue();
    }
}

/* EOF */