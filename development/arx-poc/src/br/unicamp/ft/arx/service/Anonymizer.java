package br.unicamp.ft.arx.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import java.util.StringTokenizer;
import java.util.Arrays;

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
import org.deidentifier.arx.metric.InformationLoss;










public class Anonymizer {

    /*
     **************************************************************************
     ** DEFINE                                                               **
     **************************************************************************
    */
    public static final String FILE_PATH_1 = "arx-poc/hierarchy/birthdate.csv";
    public static final String FILE_PATH_2 = "arx-poc/hierarchy/age.csv";
    public static final String FILE_PATH_3 = "arx-poc/hierarchy/custom.csv";
   
    
    /*
     **************************************************************************
     ** ATTRIBUTES                                                           **
     **************************************************************************
    */
    private long   startTime;
    private long   stopTime;
    private String saveIn;
    private String policy;
    private String datadb;
    private int    k;
    public  Data   dataAll;

    
    /*
     **************************************************************************
     ** SPECIAL METHOD                                                       **
     **************************************************************************
    */
    public static void main(String[] args) throws Exception, IOException {
        
        try {
            Anonymizer obj = new Anonymizer();
            obj.run(args);
        }
        catch (Exception e) {
            e.printStackTrace ();
        }
    }


    /* ********************************************************************* */
    /* PUBLIC METHODS                                                        */
    /* ********************************************************************* */
    /*
        BRIEF: execute the anonymazation algorithm.
        -----------------------------------------------------------------------
        @PARAM args[0] == file with dataset to anonymization.
        @PARAM args[1] == file with policy used.
        @PARAM args[2] == file to save the output.
        @PARAM args[3] == k value.

    */
    public void run(String[] args) throws Exception, IOException {

        String datadbFile = "";
        String policyFile = "";
        int    k = 0;
        
        
        /* Excute the arguments parse (six arguments -name value). */        
        if (args.length == 8) {
            for (int i = 0; i < args.length; i++) {        
                
                if (args[i].equals("-output")) {
                    this.saveIn = args[i+1];
                }
                
                if (args[i].equals("-policy")) {
                    policyFile = args[i+1];
                }
                
                if (args[i].equals("-datadb")) {
                    datadbFile = args[i+1];
                }
                
                if (args[i].equals("-k")) {
                    k = Integer.parseInt(args[i+1]);
                }
            }
        }
        else {
            System.err.println("Few arguments received.");

            System.out.println("USAGE:");
            System.out.println("anonymization -k <value> -policy <value> -datadb <value> -output <value>");
            System.out.println("* -k.....: k-anonymization paramenter (int).");
            System.out.println("* -policy: policies file path (string).");
            System.out.println("* -datadb: input    file path (string).");
            System.out.println("* -output: output   file path (string).");
            System.exit(1);
        }
    
        /* Execution stages:
           -----------------------------------#
           1 - prepare receveide data.
           2 - apply the policies.
           3 - create a configuration.
           4 - apply the anonymization.
           5 - show the results.
           -----------------------------------#
        */
       
        /* Prepare the data received to apply anomalyzation algorithm (ARX) */
        this.dataAll = preparate_source(datadbFile, policyFile);   
               
        /* Apply all policies receveid from policy file. */
        this.apply_policies(policyFile);
              
        /* Creates a new configuration without tuple suppression. After adds a
           privacy model to the configuration. Allows for a certain percentage
           of outliers and thus triggers tuple suppression. */
        ARXConfiguration cfg = ARXConfiguration.create();
        cfg.addPrivacyModel(new KAnonymity(k));
        cfg.setSuppressionLimit(0d);

        /* Mensure exec time: start. */
        this.startTime = System.currentTimeMillis();
        
        /* This class offers several methods to define parameters and execute
           the ARX algorithm. */
        ARXAnonymizer anonymizer = new ARXAnonymizer();
        ARXResult     result     = anonymizer.anonymize(this.dataAll, cfg);
        ARXNode       node       = result.getGlobalOptimum();

        /* Mensure exec time: End. */
        this.stopTime = System.currentTimeMillis();

        /* Show result. */
        show_results(node, result, k);
    }


    /* ********************************************************************* */
    /* PROTECTED METHODS                                                     */
    /* ********************************************************************* */
    /*
    BRIEF: apply the policies defineds in the policy file.
    ----------------------------------------------------------------------------
    @PARAM policyFile == file with policies to apply.
    */        
    protected void apply_policies(String policyFile) {
        
        try {
            
            BufferedReader lines=new BufferedReader(new FileReader(policyFile));

            StringTokenizer policyLineWords = null;
            String line                     = null;
            
            String field;
            String data ;

            while ((line = lines.readLine()) != null) {

                /* Separete the word by delimiter. The delimiter is ';'. */
                policyLineWords = new StringTokenizer(line, ";");

                while (policyLineWords.hasMoreTokens()) {
                                        
                    field = policyLineWords.nextToken();
                    data  = policyLineWords.nextToken();

                    switch (data) {
                        case "1" :
                            /* Insensitive attributes will be kept as is. */
                            this.apply_insensitive_attr(field);
                            break;
                               
                        case "2" :
                            /* Directly-identifying attributes will be removed
                            from the dataset. */
                            this.apply_identifying_attr(field);
                            break;
                            
                               
                        case "3" :
                            /* Quasi-identifying attributes will be transformed
                               by applying the provided generalization hierarch
                               ies. */
                            this.apply_quasi_identifying_attr(field);
                            
                            /* Associates the given microaggregation func. When
                               configuring microaggregation with this method ge
                               neralization hierarchies will not be used for cl
                               ustering attribute values before aggregation. Cr
                               eates a microaggregation function using generali
                               zation. Ignores missing data. */
                            this.apply_micro_aggregation(field);
                            break;
                            
                        case "4" :
                            /* Directly-identifying attributes will be removed
                            from the dataset. */
                            this.apply_identifying_attr(field);
                            break;
                               
                        case "SR":
                            /* quasi-identifying attributes will be transformed
                               by applying the provided generalization hierarch
                               ies. */
                            this.apply_quasi_identifying_attr(field);
                            
                            /* Enables building hierarchies for categorical and
                               non-categorical values using redaction (order is
                               left-to-right). Apply the suppressing. */
                            this.apply_hierarchy_leftToRight_attr(field);
                            break;
                               
                        case "SL":
                            /* quasi-identifying attributes will be transformed
                               by applying the provided generalization hierarch
                               ies. */
                            this.apply_quasi_identifying_attr(field);
                            
                            /* Enables building hierarchies for categorical and
                               non-categorical values using redaction (order is
                               right-to-left). Apply the suppressing. */
                            this.apply_hierarchy_rightToLeft_attr(field);
                            break;
                               
                        case "DT":
                            /* load and apply a hierachy from a file (arg). */
                            this.apply_hierarchy_attr(field, this.FILE_PATH_1);
                            break;
                               
                        case "AG":
                            /* load and apply a hierachy from a file (arg). */
                            this.apply_hierarchy_attr(field, this.FILE_PATH_2);
                            break;
                               
                        case "CT":
                            /* load and apply a hierachy from a file (arg). */
                            this.apply_hierarchy_attr(field, this.FILE_PATH_3);
                            break;
                            
                        default: 
                            throw new IllegalArgumentException("Invalid vaue"
                                                                    + data);
                    }
                }
            }           
            lines.close();
            
        } catch (
            IOException | NumberFormatException e) {
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
                       Hierarchy.create(filePath, StandardCharsets.UTF_8, ';'));
    }


    /*
    BRIEF: Enables building hierarchies for categorical and non-categorical va
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
     BRIEF: prepare data to anonymization.
     --------------------------------------------------------------------------
     @PARAM datasetFile == file with data to anonymization.
     @PARAM policyFile  == file with the policies to apply.
    */
    protected Data preparate_source(String datasetFile,String policyFile) throws 
                                                                   IOException {

        /* A factory for connections to the physical data source that this Data
           Source object represents. From dataset create a CVS file. */
        DataSource source = DataSource.createCSVSource(datasetFile, 
                                                       Charset.forName("UTF-8"),
                                                       ';',
                                                       true);

        try {
            
            /* Reads text from a character-input stream,buffering characters so
               as to provide for the efficient reading of characters, arrays,
               and lines. */
            BufferedReader lines=new BufferedReader(new FileReader(policyFile));
            
            StringTokenizer policyLineWords = null;
            String line                     = null;

            String field;
            String data ;

            while ((line = lines.readLine()) != null) {
                
                /* Separete the word by delimiter. The delimiter is ';'. */
                policyLineWords = new StringTokenizer(line, ";");

                /* Tests if there are more tokens available from this tokenizer
                   's string (policy file line). */
                while (policyLineWords.hasMoreTokens()) {
                   field = policyLineWords.nextToken();
                   data  = policyLineWords.nextToken();

                   /**/
                   source.addColumn(field, DataType.STRING);
                }
            }
            lines.close();

         } catch (
            IOException | NumberFormatException e) {
         }
        
         return Data.create(source);
    }


    /*
      BRIEF: show the result from algorithm execution.
      ------------------------------------------------------------------------
      @PARAM node   == 
      @PARAM result == result from algorithm apply.
      @PARAM k      == k anonymization.
    */
    protected void show_results(ARXNode node, ARXResult result, int k) {
        
        /* Create a population model. Obtain the sample size and the sampling
           fraction value. */        
        ARXPopulationModel pModel = 
                ARXPopulationModel.create(this.dataAll.getHandle().getNumRows(),
                0.01d);

        double riskP = result.getOutput()
                             .getRiskEstimator(pModel)
                             .getSampleBasedReidentificationRisk()
                             .getEstimatedProsecutorRisk();
        
        double riskJ = result.getOutput()
                             .getRiskEstimator(pModel)
                             .getSampleBasedReidentificationRisk()
                             .getEstimatedJournalistRisk();
        
        double riskM = result.getOutput()
                             .getRiskEstimator(pModel)
                             .getSampleBasedReidentificationRisk()
                             .getEstimatedMarketerRisk();
               
        /*
        v0 == lScore;
        v1 == hScore;
        v2 == statistic;
        v3 == data;
        v4 == policy;
        v5 == solutions;
        v6 == isComplete;
        v7 == time needed.
        */            
        
        InformationLoss<?> v0 = result.getGlobalOptimum().getLowestScore ();
        InformationLoss<?> v1 = result.getGlobalOptimum().getHighestScore();
        
        StatisticsEquivalenceClasses v2 = result.getOutput(result.getGlobalOptimum(), false).getStatistics().getEquivalenceClassStatistics();
        
        String v3 = "";
        v3 = v3 + this.dataAll.getHandle().getView().getNumRows();
        v3 = v3 + " records with ";
        v3 = v3 + this.dataAll.getDefinition().getQuasiIdentifyingAttributes().size();
        v3 = v3 + " quasi-identifiers";
        
        int     v4 = result.getLattice().getSize();
        String  v5 = Arrays.toString(node.getTransformation());
        boolean v6 = result.getLattice().isComplete();
        String  v7 = result.getTime() + " [ms]";
        
        
        
        /* PRINT */       
        System.out.println("");
        System.out.println("-- OUTPUT DATA \n ==========================="  );
        System.out.println("- Mixed Risk");
        System.out.println("  * Prosecutor re-identification risk: " + riskP);
        System.out.println("  * Journalist re-identification risk: " + riskJ);
        System.out.println("  * Marketer   re-identification risk: " + riskM);
        System.out.println("  * K anonimity .....................: " + k    );
        System.out.println("");
        System.out.println("- Information Loss " );
        System.out.println("   *.................................: "+v0+"/"+v1);
        System.out.println("");
        System.out.println("- Statistics");
        System.out.println("   *.................................: " + v2);
        System.out.println("");        
        System.out.println("- Data: ");
        System.out.println("   *.................................: " + v3);
        System.out.println("");
        System.out.println("- Policies available ");
        System.out.println("   *.................................: " + v4);
        System.out.println("");
        System.out.println("- Solution ");
        System.out.println("  * .................................: " + v5);
        System.out.println("  * Optimal..........................: " + v6);
        System.out.println("  * Time needed......................: " + v7);
        System.out.println("================================================");
        System.out.println("");
        
        /* Write to the file: */
        System.out.print(" - Writing data...");
        try {
            result.getOutput(false).save(this.saveIn, ';');
        }   
        catch (Exception e) {
            e.printStackTrace();
        }
        
        System.out.println("Done!");
        System.out.println("*** Total Execution Time: " + (this.stopTime - this.startTime)+"[ms]");
    }
}