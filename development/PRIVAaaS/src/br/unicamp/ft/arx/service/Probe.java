package br.unicamp.ft.arx.service;


import eu.atmosphere.tmaf.monitor.client.SynchronousClient;
import eu.atmosphere.tmaf.monitor.message.Data;
import eu.atmosphere.tmaf.monitor.message.Message;
import eu.atmosphere.tmaf.monitor.message.Observation;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import javax.ws.rs.InternalServerErrorException;








public class Probe {
    /* ********************************************************************* */
    /* DEFINES                                                               */
    /* ********************************************************************* */
    public static final int PUBLISH_ERROR = -3;
    public static final int SOCKETL_ERROR = -2;
    public static final int TIMEOUT_ERROR = -1;
    
    public static final int SOCKET_TIME_OUT = 60000;

    /* ********************************************************************* */
    /* ATTRIBUTES                                                            */
    /* ********************************************************************* */
    int __socketPort = 0;
    
    
    
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
                               double score,
                               int max_loop,
                               int socketPort) throws IOException,
                                                      ClassNotFoundException {
    

        long timeStampSeg  = timeStamp/1000;
        
        this.__socketPort = socketPort;
        
        SynchronousClient client = new SynchronousClient(endpoint);
        client.authenticate(probeId, "pass".getBytes());

        /* Create a new message to send to monitor: */
        Message message = client.createMessage();
            
        message.setProbeId(probeId);
            
        /* Set the message ID. */      
        message.setResourceId(resourceId);
            
        Observation observation1 = new Observation(timeStampSeg, k    );
        Observation observation2 = new Observation(timeStampSeg, riskP);
        Observation observation3 = new Observation(timeStampSeg, riskJ);
        Observation observation4 = new Observation(timeStampSeg, riskM);
        Observation observation5 = new Observation(timeStampSeg, score);
        
        Data data1 = new Data(Data.Type.MEASUREMENT, dId1, observation1);
        Data data2 = new Data(Data.Type.MEASUREMENT, dId2, observation2);
        Data data3 = new Data(Data.Type.MEASUREMENT, dId3, observation3);
        Data data4 = new Data(Data.Type.MEASUREMENT, dId4, observation4);
        Data data5 = new Data(Data.Type.MEASUREMENT, dId5, observation5);

        message.addData(data1);
        message.addData(data2);
        message.addData(data3);
        message.addData(data4);
        message.addData(data5);

        int valRet =  0;
        int count  =  0;
        
        while (valRet != 2 && count != max_loop) {                                
            try {
                valRet = client.send(message);
            } catch (InternalServerErrorException e) {
                System.out.println(e);
                valRet = PUBLISH_ERROR;
            } catch (Exception e) {
                System.out.println(e);
                valRet = PUBLISH_ERROR;
            }
            count = count + 1;
        }
        
        /* Wait the result of actuator (update the k?) */
        if (valRet != PUBLISH_ERROR) {
            valRet = this.__listen_socket();
        }
        
        return valRet;
    }
    
    
    /* ********************************************************************* */
    /* PRIVATE METHODS                                                       */
    /* ********************************************************************* */
    /*
    * BRIEF: listen return from actuator.
    * --------------------------------------------------------------------------
    */
    private int __listen_socket() throws IOException, ClassNotFoundException {
        
        int k = -1;
               
        try (ServerSocket server = new ServerSocket(this.__socketPort)) {
            
            try {
                /* Non block: timeout after 120 seconds: */
                server.setSoTimeout(SOCKET_TIME_OUT);
                
                Socket client = server.accept();

                DataInputStream is = new DataInputStream(
                              new BufferedInputStream(client.getInputStream()));
                
                k = Integer.parseInt(is.readLine());
                client.close();
            }
            catch (SocketException e) {
                return TIMEOUT_ERROR;
            }
            catch(IOException e     ) {
                return SOCKETL_ERROR;
            }
            finally {
                server.close();
            }
        }
        catch(Exception e) {
            return SOCKETL_ERROR;
        }
        return k;
    }
}

/* EOF */