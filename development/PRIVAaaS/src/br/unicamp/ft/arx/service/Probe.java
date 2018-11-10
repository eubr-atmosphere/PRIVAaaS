package br.unicamp.ft.arx.service;


import eu.atmosphere.tmaf.monitor.client.SynchronousClient;
import eu.atmosphere.tmaf.monitor.message.Data;
import eu.atmosphere.tmaf.monitor.message.Message;
import eu.atmosphere.tmaf.monitor.message.Observation;











public class Probe {
    /* ********************************************************************* */
    /* DEFINES                                                               */
    /* ********************************************************************* */


    /* ********************************************************************* */
    /* ATTRIBUTES                                                            */
    /* ********************************************************************* */
    //BackgroundClient client;
    SynchronousClient client;
    
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
                               int probeId,
                               int k,
                               double riskP,
                               double riskJ, 
                               double riskM,
                               double lScore,
                               double hScore) {
    
        
        this.client = new SynchronousClient(endpoint);
        this.client.authenticate(probeId, "pass".getBytes());
    
           
        /* Create a new message to send to monitor: */
        Message message = this.client.createMessage();
            
        message.setProbeId(probeId);
            
        /* Set the message ID. */      
        message.setResourceId(resourceId);
            
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
            
        int returnPublish = this.client.send(message);
            
        return returnPublish;
    }
}
/* EOF */
