/*
 * @#WsCommunicationsTyrusCloudClientChannel.java - 2016
 * Copyright bitDubai.com., All rights reserved.
 * You may not modify, use, reproduce or distribute this software.
 * BITDUBAI/CONFIDENTIAL
 */
package org.fermat.p2p.server.app.stress.structure.channel;


import java.io.IOException;

import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.DeploymentException;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;

import org.fermat.p2p.server.app.stress.structure.conf.CLoudClientConfigurator;



@ClientEndpoint(configurator = CLoudClientConfigurator.class)
public class WsCommunicationsTyrusCloudClientChannel {
	
	
   /**
     * Represent the clientConnection
     */
    private Session clientConnection;
	
    @OnOpen
    public void onOpen(final Session session) {

        System.out.println(" --------------------------------------------------------------------- ");
        System.out.println(" WsCommunicationsTyrusCloudClientChannel - Starting method onOpen");
        System.out.println(" WsCommunicationsTyrusCloudClientChannel - id = "+session.getId());
        System.out.println(" WsCommunicationsTyrusCloudClientChannel - url = "+session.getRequestURI());

        this.clientConnection = session;
    }
    
    @OnMessage
    public void onMessage(String fermatPacketEncode) {
    	
    	
    }
    
    @OnClose
    public void onClose(final Session session, final CloseReason reason) {
    	
    	
    }
    
    @OnError
    public void onError(Session session, Throwable t) {
        try {
            System.out.println(" --------------------------------------------------------------------- ");
            System.out.println(" WsCommunicationsTyrusCloudClientChannel - Starting method onError");
            t.printStackTrace();
            clientConnection.close(new CloseReason(CloseReason.CloseCodes.PROTOCOL_ERROR, t.getMessage()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
