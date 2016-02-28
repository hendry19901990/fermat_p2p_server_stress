/*
 * @#WsCommunicationsCloudClientSupervisorConnectionAgent.java - 2016
 * Copyright bitDubai.com., All rights reserved.
 * You may not modify, use, reproduce or distribute this software.
 * BITDUBAI/CONFIDENTIAL
 */
package org.fermat.p2p.server.app.stress.structure.channel;

import javax.websocket.Session;



public class WsCommunicationsCloudClientSupervisorConnectionAgent extends Thread{
	
	 /*
     * Represent the WsCommunicationsTyrusCloudClientConnection
     */
	private WsCommunicationsTyrusCloudClientConnection WsCommunicationsTyrusCloudClientConnection;
	
	/*
	 * Constructor with parameters
	 */
	public WsCommunicationsCloudClientSupervisorConnectionAgent(WsCommunicationsTyrusCloudClientConnection WsCommunicationsTyrusCloudClientConnection){
		this.WsCommunicationsTyrusCloudClientConnection = WsCommunicationsTyrusCloudClientConnection;
	}
	
	@Override
	public void run() {
		
		if(getConnection() != null){
			
			 try {
				 
			     if (getConnection().isOpen()) 
	                    getWsCommunicationsTyrusCloudClientChannel().sendPing();
	                
			 }catch (Exception ex) {
				 System.out.println(" WsCommunicationsCloudClientSupervisorConnectionAgent - Error occurred sending ping to the node, closing the connection to remote node");
			 }
			
		}
		 
	}
	
	 /*
     * Get the connection
     * @return Session
     */
    private Session getConnection(){
    	
    	if(WsCommunicationsTyrusCloudClientConnection.getWsCommunicationsTyrusCloudClientChannel() != null)
    		return WsCommunicationsTyrusCloudClientConnection.getWsCommunicationsTyrusCloudClientChannel().getClientConnection();
    	else
    		return null;
    	
    }
    
    /*
     * Get the WsCommunicationsTyrusCloudClientChannel
     * @return WsCommunicationsTyrusCloudClientChannel
     */
    private WsCommunicationsTyrusCloudClientChannel getWsCommunicationsTyrusCloudClientChannel(){
    	return WsCommunicationsTyrusCloudClientConnection.getWsCommunicationsTyrusCloudClientChannel();
    }

}
