package br.unicamp.ft.arx.service;


import eu.atmosphere.tmaf.monitor.client.BackgroundClient;
import eu.atmosphere.tmaf.monitor.message.Data;
import eu.atmosphere.tmaf.monitor.message.Message;
import eu.atmosphere.tmaf.monitor.message.Observation;

import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;










public class Probe {
    /* ********************************************************************* */
    /* DEFINES                                                               */
    /* ********************************************************************* */
    private static final Logger LOGGER = LoggerFactory.getLogger(Probe.class);

    /* ********************************************************************* */
    /* ATTRIBUTES                                                            */
    /* ********************************************************************* */
    BackgroundClient client;
    
    
    /* ********************************************************************* */
    /* PUBLIC METHODS                                                        */
    /* ********************************************************************* */
    /*
    * BRIEF: publish the message to monitor.
    * --------------------------------------------------------------------------
    *
    */
    public int publish_message(String endpoint,
                               long timeStamp,
                               int resourceId,
                               int k,
                               double riskP,
                               double riskJ, 
                               double riskM,
                               double lScore,
                               double hScore) {
       
        
        boolean connectOk = this.__connect_monitor(endpoint);
        if (connectOk == true) {
            
            /* Create a new message to send to monitor: */
            Message message = this.client.createMessage();
            
            message.setProbeId(2222);
            
            /* Set the message ID. */      
            message.setResourceId(resourceId);
            
            Observation observation1 = new Observation(10000,10.01);
            Data data1 = new Data(Data.Type.MEASUREMENT, 2, observation1);
            message.addData(data1);
            //message.addData(new Data(Data.Type.MEASUREMENT, 2, new Observation(10000,10.01)));
            //message.addData(new Data(Data.Type.MEASUREMENT, 3, new Observation(10000,10.01)));
            /*
            Observation observation1 = new Observation(timeStamp, k     );
            Observation observation2 = new Observation(timeStamp, riskP );
            Observation observation3 = new Observation(timeStamp, riskJ );
            Observation observation4 = new Observation(timeStamp, riskM );
            Observation observation5 = new Observation(timeStamp, lScore);
            Observation observation6 = new Observation(timeStamp, hScore);
            
            Data data1 = new Data(Data.Type.MEASUREMENT, 10, observation1);
            Data data2 = new Data(Data.Type.MEASUREMENT, 20, observation2);
            Data data3 = new Data(Data.Type.MEASUREMENT, 21, observation3);
            Data data4 = new Data(Data.Type.MEASUREMENT, 22, observation4);
            Data data5 = new Data(Data.Type.MEASUREMENT, 30, observation5);
            Data data6 = new Data(Data.Type.MEASUREMENT, 31, observation6);

            message.addData(data1);
            message.addData(data2);
            message.addData(data3);
            message.addData(data4);
            message.addData(data5);
            message.addData(data6);
              */  
            
            int returnPublish = this.client.send(message);
            
            this.__disconnect_monitor();
            if (returnPublish == 1) {    
                return 1;
            }
            return 0;

        }
        return 1;
    }
   
    
    /* ********************************************************************* */
    /* PRIVATE METHODS                                                       */
    /* ********************************************************************* */
    /*
    * BRIEF: connect to monitor.
    * --------------------------------------------------------------------------
    *
    */
    private boolean __connect_monitor(String endpoint) {
        this.client = new BackgroundClient("http://127.0.0.1:5000/monitor");
//        this.client = new SynchronousClient("http://127.0.0.1:5000/monitor");
        this.client.authenticate(2222, "pass".getBytes());
    
        boolean start = this.client.start();
        return start;
    }
    
    
   /*
    * BRIEF: Disconnect from monitor.
    * --------------------------------------------------------------------------
    */
    private void __disconnect_monitor() {
        
        try {
            Thread.sleep(100000);
            
            boolean stop = this.client.stop();
            
        } catch (InterruptedException | ExecutionException ex) {
            ex.printStackTrace();
        }

        this.client.shutdown();
    }
}   

/* EOF */