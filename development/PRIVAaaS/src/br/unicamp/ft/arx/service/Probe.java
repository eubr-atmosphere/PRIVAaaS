package br.unicamp.ft.arx.service;


import eu.atmosphere.tmaf.monitor.client.SynchronousClient;
import eu.atmosphere.tmaf.monitor.message.Data;
import eu.atmosphere.tmaf.monitor.message.Message;
import eu.atmosphere.tmaf.monitor.message.Observation;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.ws.rs.InternalServerErrorException;









public class Probe {
    /* ********************************************************************* */
    /* DEFINES                                                               */
    /* ********************************************************************* */
    public static final int SOCKET_PORT     = 6000;
    public static final int SOCKET_TIME_OUT = 1200000;

    /* ********************************************************************* */
    /* ATTRIBUTES                                                            */
    /* ********************************************************************* */
    
    
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
                               int dId1,
                               int dId2,
                               int dId3,
                               int dId4,
                               int dId5,
                               int dId6,
                               int k,
                               double riskP,
                               double riskJ, 
                               double riskM,
                               double lScore,
                               double hScore,
                               int max_loop) throws IOException {
        
        SynchronousClient client = new SynchronousClient(endpoint);
        client.authenticate(probeId, "pass".getBytes());
    
        /* Create a new message to send to monitor: */
        Message message = client.createMessage();
            
        message.setProbeId(probeId);
            
        /* Set the message ID. */      
        message.setResourceId(resourceId);
            
        Observation observation1 = new Observation(timeStamp, k     );
        Observation observation2 = new Observation(timeStamp, riskP );
        Observation observation3 = new Observation(timeStamp, riskJ );
        Observation observation4 = new Observation(timeStamp, riskM );
        Observation observation5 = new Observation(timeStamp, lScore);
        Observation observation6 = new Observation(timeStamp, hScore);
            
        Data data1 = new Data(Data.Type.MEASUREMENT, dId1, observation1);
        Data data2 = new Data(Data.Type.MEASUREMENT, dId2, observation2);
        Data data3 = new Data(Data.Type.MEASUREMENT, dId3, observation3);
        Data data4 = new Data(Data.Type.MEASUREMENT, dId4, observation4);
        Data data5 = new Data(Data.Type.MEASUREMENT, dId5, observation5);
        Data data6 = new Data(Data.Type.MEASUREMENT, dId6, observation6);

        message.addData(data1);
        message.addData(data2);
        message.addData(data3);
        message.addData(data4);
        message.addData(data5);
        message.addData(data6);

        int result = -1;        
        int valRet =  0;
        int count  =  0;
        
        while (valRet != 2 && count != max_loop) {                                
            try {
                valRet = client.send(message);
            } catch (InternalServerErrorException e) {
                System.out.println(e);
                valRet = 0;
            } catch (Exception e) {
                System.out.println(e);
                valRet = 0;
            }
            count = count + 1;
        }
        
        /* Wait the result of actuator (update the k?) */
        if (valRet != 0) {
            result = this.__listen_socket();
        }
        
        return result;
    }
    
    /* ********************************************************************* */
    /* PRIVATE METHODS                                                       */
    /* ********************************************************************* */
    /*
    * BRIEF: listen return from actuator.
    * --------------------------------------------------------------------------
    */
    private int __listen_socket() throws IOException {
        
        int k = -1;
        
        try (ServerSocket server = new ServerSocket(SOCKET_PORT)) {
            try {
                /* Non block: timeout after 120 seconds: */
                server.setSoTimeout(SOCKET_TIME_OUT);
                
                Socket client = server.accept();
                
                DataInputStream input=new DataInputStream(client.getInputStream());
                k = input.readInt();
                
                input.close();
                client.close();
            }
            catch(IOException e) {
                return -2;
            }
            finally {
                server.close();
            }
        }
        catch(Exception e) {
            return -2;
        }
        return k;
    }
}
/* EOF */
