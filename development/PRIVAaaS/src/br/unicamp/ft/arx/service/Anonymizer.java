package br.unicamp.ft.arx.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.FileNotFoundException;
import java.io.FileInputStream;
import java.io.StringReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;


import java.sql.*;

import javax.json.*;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

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
    
    
    /*
     **************************************************************************
     ** ATTRIBUTES                                                           **
     **************************************************************************
    */
    private long             __startTime;
    private long             __stopTime;
    private char             __separator;
    private JsonObject       __policyJson;
    
    public Data              dataAll;
    public ARXResult         result;
    public ARXNode           node;
     
        
    
    
    /*
     **************************************************************************
     ** SPECIAL METHOD                                                       **
     **************************************************************************
    */
    public static void main(String[] args) throws Exception, IOException {
        
        InputStream inputStream = System.in;     

        JsonObject policyJson;
        try (JsonReader jReader = Json.createReader(new StringReader(args[0]))){
            policyJson = jReader.readObject();
        }
        
        /* Create an object Anonymizer: */            
        Anonymizer anonymizerObj = new Anonymizer();            
        
        anonymizerObj.load_config_basic();
        anonymizerObj.run(inputStream, policyJson, Integer.parseInt(args[1]));
        anonymizerObj.show_results();
    }


    
    
    /* ********************************************************************* */
    /* PUBLIC METHODS                                                        */
    /* ********************************************************************* */
    /*
     BRIEF: load some configs.
     --------------------------------------------------------------------------
    */
    public void load_config_basic() throws FileNotFoundException, IOException {
        
        Properties properties = new Properties();

        try (FileInputStream in = new FileInputStream("config.ini")) {
           properties.load(in);
        }

        this.__separator = properties.getProperty("SEPARATOR").charAt(0);
    }
    
    
    
    
    /*
        BRIEF: execute the anonymization algorithm.
        -----------------------------------------------------------------------
        @PARAM csv    == CSV input stream;
        @PARAM policy == json with policies to apply;
        @PARAM k      == k value.
    */
    public void run(InputStream csv, JsonObject policy, int k)
                                                 throws IOException, Exception {
     
        /* Create data from inputStreamCSV: */      
        this.dataAll=Data.create(csv,Charset.forName("UTF-8"),this.__separator);
        
        /* Apply all policies receveid from policy file. */
        this.apply_policies(policy.getJsonArray("policy"));
        
        /* Creates a new configuration without tuple suppression. After adds a
           privacy model to the configuration. Allows for a certain percentage
           of outliers and thus triggers tuple suppression. */
        ARXConfiguration cfg = ARXConfiguration.create();

        cfg.addPrivacyModel(new KAnonymity(k));
        cfg.setSuppressionLimit(0d);

        /* Offers several methods to define params and execute the ARX alg. */
        ARXAnonymizer anonymizer = new ARXAnonymizer();

        /* Mensure exec time: start. */
        this.__startTime = System.currentTimeMillis();
        
        /* Encapsulates the results of an execution of the "ARX algorithm". */
        try {
            this.result = anonymizer.anonymize(this.dataAll, cfg);
            
        } catch (java.lang.IllegalArgumentException e) {
            System.err.println("Errors were found: " + e + "\nTrace:");
            System.exit(-1);
        }
       
        /* Obtain the global optimum (ARXLattice.ARXNode): */
        this.node  = this.result.getGlobalOptimum();

        /* Mensure exec time: End. */
        this.__stopTime = System.currentTimeMillis();
    }

    
    
    
    /*
      BRIEF: show the result from algorithm execution.
      ------------------------------------------------------------------------
    */
    public void show_results() throws SQLException {
        double riskP;
        double riskJ;
        double riskM;
    
        StatisticsEquivalenceClasses equivalenceClasses;
    
        /* Create a population model. Obtain: sample size and the sampling frac-
           tion value. */        
        ARXPopulationModel pModel = ARXPopulationModel.create(
                                          this.dataAll.getHandle().getNumRows(),
                                          0.01d);

        riskP = this.result.getOutput()
                           .getRiskEstimator(pModel)
                           .getSampleBasedReidentificationRisk()
                           .getEstimatedProsecutorRisk();
        
        riskJ = this.result.getOutput()
                           .getRiskEstimator(pModel)
                           .getSampleBasedReidentificationRisk()
                           .getEstimatedJournalistRisk();
        
        riskM = this.result.getOutput()
                           .getRiskEstimator(pModel)
                           .getSampleBasedReidentificationRisk()
                           .getEstimatedMarketerRisk();
        
        /* Statistic: */
        equivalenceClasses = this.result.getOutput(this.node, false)
                                        .getStatistics()
                                        .getEquivalenceClassStatistics();
    
        /* Get the quantity of quasi identiying attrs.Set the string to show.*/
        int size = 
            this.dataAll.getDefinition().getQuasiIdentifyingAttributes().size();
        
        /**/
        JsonObjectBuilder jResult = Json.createObjectBuilder();
        
        jResult.add("start_time"      , this.__startTime);
        jResult.add("stop_time"       , this.__stopTime );
        jResult.add("risk_prosecutor" , riskP);
        jResult.add("risk_journalist" , riskJ);
        jResult.add("risk_marketer"   , riskM);
        jResult.add("low_score"       , this.node.getLowestScore().toString() );
        jResult.add("high_score"      , this.node.getHighestScore().toString());
        jResult.add("polices"         , this.result.getLattice().getSize()    );
        jResult.add("optimal"         , this.result.getLattice().isComplete() );
        jResult.add("time_needed"     , this.result.getTime());
        jResult.add("solutions"       , Arrays.toString(this.node.getTransformation()));
        jResult.add("quasi_identiying",size);
        jResult.add("number_rows"     , this.dataAll.getHandle().getNumRows());
        jResult.add("equivalence_classes", equivalenceClasses.toString());

        /* Get the execution return and write in json format. */
        JsonObject resultJsonParameters = jResult.build();

        /* Get the data anonymizated in json format. */
        JsonArray resultJsonData = this.get_json_anonymized();
        
        /**/
        JsonObjectBuilder returnJson = Json.createObjectBuilder();
        returnJson.add("parameter", resultJsonParameters);
        returnJson.add("data"     , resultJsonData      );
        JsonObject returnJsonObject = returnJson.build();
        
        System.out.println(returnJsonObject);
    }  


    
    
    /* ********************************************************************* */
    /* PROTECTED METHODS                                                     */
    /* ********************************************************************* */
    /*
      BRIEF: return the resultset anonymized.
      ------------------------------------------------------------------------
    */
    protected JsonArray get_json_anonymized() throws SQLException  {
        
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
    BRIEF: apply the policies defineds in the policy file.
    ----------------------------------------------------------------------------
    @PARAM policyJsonArray == policy definied in json format.
    */        
    protected void apply_policies(JsonArray policyJsonArray) throws IOException{
        
        for (int i = 0; i < policyJsonArray.size(); ++i) {

            JsonObject node = policyJsonArray.getJsonObject(i);
            
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
}

/* EOF */