/*
 * @#WsCommunicationTyrusVPNClient.java - 2016
 * Copyright bitDubai.com., All rights reserved.
 * You may not modify, use, reproduce or distribute this software.
 * BITDUBAI/CONFIDENTIAL
 */
package org.fermat.p2p.server.app.stress.structure.channel.vpn;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.websocket.CloseReason;
import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.MessageHandler;
import javax.websocket.Session;

import org.fermat.p2p.server.app.stress.structure.commons.contents.FermatMessageCommunication;
import org.fermat.p2p.server.app.stress.structure.commons.contents.FermatPacketCommunicationFactory;
import org.fermat.p2p.server.app.stress.structure.commons.contents.FermatPacketDecoder;
import org.fermat.p2p.server.app.stress.structure.commons.contents.FermatPacketEncoder;
import org.fermat.p2p.server.app.stress.structure.crypto.asymmetric.AsymmetricCryptography;
import org.fermat.p2p.server.app.stress.structure.crypto.asymmetric.ECCKeyPair;
import org.fermat.p2p.server.app.stress.structure.enums.FermatPacketType;
import org.fermat.p2p.server.app.stress.structure.interfaces.FermatMessage;
import org.fermat.p2p.server.app.stress.structure.interfaces.FermatPacket;
import org.fermat.p2p.server.app.stress.structure.interfaces.PlatformComponentProfile;


public class WsCommunicationTyrusVPNClient  extends Endpoint{
	
	 /*
     * Represent the vpnClientIdentity
     */
    private ECCKeyPair vpnClientIdentity;

    /*
     * Represent the remoteParticipant of the vpn
     */
    private PlatformComponentProfile remoteParticipant;

    /*
     * Represent the remoteParticipantNetworkService of the vpn
     */
    private PlatformComponentProfile remoteParticipantNetworkService;

    /*
     * Represent the vpnServerIdentity
     */
    private String vpnServerIdentity;

    /*
     * Represent the pending incoming messages cache
     */
    private List<FermatMessage> pendingIncomingMessages;

    /*
     * Represent the wsCommunicationVPNClientManagerAgent
     */
    private WsCommunicationTyrusVPNClientManagerAgent wsCommunicationTyrusVPNClientManagerAgent;

    /*
     * Represent the clientConnection
     */
    private Session vpnClientConnection;
	
	public Session getVpnClientConnection() {
		return vpnClientConnection;
	}

	public WsCommunicationTyrusVPNClient(WsCommunicationTyrusVPNClientManagerAgent wsCommunicationTyrusVPNClientManagerAgent, ECCKeyPair vpnClientIdentity, PlatformComponentProfile remoteParticipant, PlatformComponentProfile remoteParticipantNetworkService, String vpnServerIdentity){
		
		 this.wsCommunicationTyrusVPNClientManagerAgent = wsCommunicationTyrusVPNClientManagerAgent;
	     this.vpnClientIdentity = vpnClientIdentity;
	     this.remoteParticipant = remoteParticipant;
	     this.remoteParticipantNetworkService = remoteParticipantNetworkService;
	     this.vpnServerIdentity = vpnServerIdentity;
	     this.pendingIncomingMessages = new ArrayList<FermatMessage>();
		
	}
	
	@Override
    public void onOpen(Session session, EndpointConfig config) {
		 
		System.out.println(" --------------------------------------------------------------------- ");
	    System.out.println(" WsCommunicationVPNClient - Starting method onOpen");
	    System.out.println(" WsCommunicationVPNClient - Session id = " + session.getId());
	    this.vpnClientConnection = session;

	    /*
	     * Configure message handler
	     */
	     session.addMessageHandler(new MessageHandler.Whole<String>() {
	          @Override
	          public void onMessage(String fermatPacketEncode) {
	                processMessage(fermatPacketEncode);
	          }
	     });
	        
		 
	}
	
    public void processMessage(String fermatPacketEncode) {
    	
    	FermatPacket fermatPacketReceive = FermatPacketDecoder.decode(fermatPacketEncode, vpnClientIdentity.getPrivateKey());
       
    	/*
         * Validate the signature
         */
        validateFermatPacketSignature(fermatPacketReceive);
        
        if (fermatPacketReceive.getFermatPacketType() == FermatPacketType.MESSAGE_TRANSMIT){

            /*
             * Get the platformComponentProfile from the message content and decrypt
             */
            String messageContentJsonStringRepresentation = AsymmetricCryptography.decryptMessagePrivateKey(fermatPacketReceive.getMessageContent(), vpnClientIdentity.getPrivateKey());

            System.out.println("WsCommunicationVPNClient - messageContentJsonStringRepresentation = "+messageContentJsonStringRepresentation);

            /*
             * Get the message object
             */
            FermatMessage fermatMessage = new FermatMessageCommunication().fromJson(messageContentJsonStringRepresentation);

            System.out.println("WsCommunicationVPNClient - fermatMessage = "+fermatMessage);

            /*
             * Add to the list
             */
            //pendingIncomingMessages.add(fermatMessage);
            
            wsCommunicationTyrusVPNClientManagerAgent.handleNewMessageReceived(remoteParticipantNetworkService.getNetworkServiceType(), remoteParticipant, fermatMessage);

        }else {
            System.out.println("WsCommunicationVPNClient - Packet type " + fermatPacketReceive.getFermatPacketType() + "is not supported");
        }
		 
	}
	
	@Override
	public void onClose(final Session session, final CloseReason reason){
		 System.out.println("Socket " + session.getId() + " is disconnect! code = " + reason.getCloseCode() + "[" + reason.getCloseCode().getCode() + "] reason = " + reason.getReasonPhrase());
	}
	
	@Override
	public void onError(Session session, Throwable t) {
		
	    try {

            System.out.println(" --------------------------------------------------------------------- ");
            System.out.println(" WsCommunicationVPNClient - Starting method onError");
            t.printStackTrace();
            vpnClientConnection.close(new CloseReason(CloseReason.CloseCodes.PROTOCOL_ERROR, t.getMessage()));

        } catch (IOException e) {
            e.printStackTrace();
        }
		 
	}
	
	 
    public void close() {
		
        try {

            System.out.println(" WsCommunicationVPNClient - close connection");
            if(vpnClientConnection.isOpen()) {
                vpnClientConnection.close(new CloseReason(CloseReason.CloseCodes.NORMAL_CLOSURE, "The cloud client close the connection, intentionally."));
            }
           
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
		
	}
	
	private void validateFermatPacketSignature(FermatPacket fermatPacketReceive){
		
	      System.out.println(" WsCommunicationVPNClient - validateFermatPacketSignature");

	         /*
	         * Validate the signature
	         */
	        boolean isValid = AsymmetricCryptography.verifyMessageSignature(fermatPacketReceive.getSignature(), fermatPacketReceive.getMessageContent(), vpnServerIdentity);

	        System.out.println(" WsCommunicationVPNClient - isValid = " + isValid);

	        /*
	         * if not valid signature
	         */
	        if (!isValid){
	            throw new RuntimeException("Fermat Packet received has not a valid signature, go to close this connection maybe is compromise");
	        }
		
	}
	
	public void sendMessage(FermatMessage fermatMessage){
		

        /*
         * Validate parameter
         */
        if (fermatMessage == null){
            throw new IllegalArgumentException("The fermatMessage is required, can not be null");
        }

         /*
         * Construct a fermat packet whit the message to transmit
         */
        FermatPacket fermatPacketRequest = FermatPacketCommunicationFactory.constructFermatPacketEncryptedAndSinged(vpnServerIdentity,                  //Destination
                                                                                                                    vpnClientIdentity.getPublicKey(),   //Sender
                                                                                                                    fermatMessage.toJson(),             //Message Content
                                                                                                                    FermatPacketType.MESSAGE_TRANSMIT,  //Packet type
                                                                                                                    vpnClientIdentity.getPrivateKey()); //Sender private key


        if (vpnClientConnection.isOpen()){
            /*
             * Send the encode packet to the server
             */
             vpnClientConnection.getAsyncRemote().sendText(FermatPacketEncoder.encode(fermatPacketRequest));
        }
		
	}
	
    public int getUnreadMessagesCount() {

        /*
         * Validate if not null
         */
        if (pendingIncomingMessages != null){

            /*
             * Return the size of the cache
             */
            return pendingIncomingMessages.size();

        }

        /*
         * Empty cache return 0
         */
        return 0;
    }
    
    public FermatMessage readNextMessage() {

        /*
         * Validate no empty
         */
        if(!pendingIncomingMessages.isEmpty()) {

            /*
             * Return the next message
             */
            return pendingIncomingMessages.iterator().next();

        }else {

            //TODO: CREATE A APPROPRIATE EXCEPTION
            throw new RuntimeException();
        }

    }
    
    public void removeMessageRead(FermatMessage fermatMessage) {

        /*
         * Remove the message
         */
        pendingIncomingMessages.remove(fermatMessage);
    }
    
    public PlatformComponentProfile getRemoteParticipant() {
        return remoteParticipant;
    }

    /*
     *
     * @see CommunicationsVPNConnection#getRemoteParticipantNetworkService()
     */
    public PlatformComponentProfile getRemoteParticipantNetworkService() {
        return remoteParticipantNetworkService;
    }
    
    public String toString() {
        return "WsCommunicationVPNClient{" +
                "vpnClientIdentity=" + vpnClientIdentity +
                ", remoteParticipant=" + remoteParticipant +
                ", remoteParticipantNetworkService=" + remoteParticipantNetworkService +
                ", vpnServerIdentity='" + vpnServerIdentity + '\'' +
                '}';
    }
    
    public boolean isConnected() {
        return vpnClientConnection.isOpen();
    }

}
