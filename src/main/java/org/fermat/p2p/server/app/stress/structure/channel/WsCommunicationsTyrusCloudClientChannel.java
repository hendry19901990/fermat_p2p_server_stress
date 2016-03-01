/*
 * @#WsCommunicationsTyrusCloudClientChannel.java - 2016
 * Copyright bitDubai.com., All rights reserved.
 * You may not modify, use, reproduce or distribute this software.
 * BITDUBAI/CONFIDENTIAL
 */
package org.fermat.p2p.server.app.stress.structure.channel;


import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.nio.ByteBuffer;

import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.PongMessage;
import javax.websocket.Session;
import javax.websocket.DeploymentException;

import org.fermat.p2p.server.app.stress.structure.commons.contents.FermatPacketDecoder;
import org.fermat.p2p.server.app.stress.structure.conf.CLoudClientConfigurator;
import org.fermat.p2p.server.app.stress.structure.crypto.asymmetric.AsymmetricCryptography;
import org.fermat.p2p.server.app.stress.structure.crypto.asymmetric.ECCKeyPair;
import org.fermat.p2p.server.app.stress.structure.enums.FermatPacketType;
import org.fermat.p2p.server.app.stress.structure.enums.NetworkServiceType;
import org.fermat.p2p.server.app.stress.structure.enums.PlatformComponentType;
import org.fermat.p2p.server.app.stress.structure.interfaces.FermatPacket;
import org.fermat.p2p.server.app.stress.structure.interfaces.PlatformComponentProfile;
import org.fermat.p2p.server.app.stress.structure.processors.FermatTyrusPacketProcessor;


@ClientEndpoint(configurator = CLoudClientConfigurator.class)
public class WsCommunicationsTyrusCloudClientChannel {
	
	
	  /**
     * Represent the temporalIdentity
     */
    private ECCKeyPair temporalIdentity;

    /**
     * Represent the clientIdentity
     */
    private ECCKeyPair clientIdentity;
    
    /**
     * Represent the serverIdentity
     */
    private String serverIdentity;
    
    /**
     * Represent the platformComponentProfile
     */
    private PlatformComponentProfile platformComponentProfile;
    
    /**
     * Represent is the client is register with the server
     */
    private boolean isRegister;
    
    /**
     * Holds the packet processors objects
     */
    private Map<FermatPacketType, CopyOnWriteArrayList<FermatTyrusPacketProcessor>> packetProcessorsRegister;
    
    private WsCommunicationsTyrusCloudClientConnection wsCommunicationsTyrusCloudClientConnection;
	
   /**
     * Represent the clientConnection
     */
    private Session clientConnection;
    
    public WsCommunicationsTyrusCloudClientChannel(WsCommunicationsTyrusCloudClientConnection wsCommunicationsTyrusCloudClientConnection, ECCKeyPair clientIdentity) throws IOException, DeploymentException{
    	
        this.clientIdentity = clientIdentity;
        this.temporalIdentity = CLoudClientConfigurator.tempIdentity;
        this.packetProcessorsRegister = new ConcurrentHashMap<>();
        this.wsCommunicationsTyrusCloudClientConnection = wsCommunicationsTyrusCloudClientConnection;
        this.isRegister = Boolean.FALSE;
        
        
        setPlatformComponentProfile(wsCommunicationsTyrusCloudClientConnection.constructPlatformComponentProfileFactory(clientIdentity.getPublicKey(), "WsCommunicationsCloudClientChannel",  "Web Socket Communications Cloud Client", NetworkServiceType.UNDEFINED, PlatformComponentType.COMMUNICATION_CLOUD_CLIENT, null));
    	
    }
	
    @OnOpen
    public void onOpen(final Session session) {

        System.out.println(" --------------------------------------------------------------------- ");
        System.out.println(" WsCommunicationsTyrusCloudClientChannel - Starting method onOpen");
        System.out.println(" WsCommunicationsTyrusCloudClientChannel - id = "+session.getId());
        System.out.println(" WsCommunicationsTyrusCloudClientChannel - url = "+session.getRequestURI());

        this.clientConnection = session;
    }
    
    public void sendMessage(final String message) {
    	
    	clientConnection.getAsyncRemote().sendText(message);
    	
    }
    
    
    
    @OnMessage
    public void onMessage(String fermatPacketEncode) {
    	
    	   //System.out.println(" --------------------------------------------------------------------- ");
           //System.out.println(" WsCommunicationsTyrusCloudClientChannel - Starting method onMessage(String)");
          // System.out.println(" WsCommunicationsTyrusCloudClientChannel - encode fermatPacket " + fermatPacketEncode);

           FermatPacket fermatPacketReceive = null;

           /*
            * If the client is no register
            */
           if (!isRegister){

              // System.out.println(" WsCommunicationsTyrusCloudClientChannel - decoding fermatPacket with temp-identity ");

               /**
                * Decode the message with the temporal identity
                */
               fermatPacketReceive = FermatPacketDecoder.decode(fermatPacketEncode, temporalIdentity.getPrivateKey());

           }else {

              // System.out.println(" WsCommunicationsTyrusCloudClientChannel - decoding fermatPacket with client-identity ");

               /**
                * Decode the message with the client identity
                */
               fermatPacketReceive = FermatPacketDecoder.decode(fermatPacketEncode, clientIdentity.getPrivateKey());

               /*
                * Validate the signature
                */
               validateFermatPacketSignature(fermatPacketReceive);
           }
           
           //verify if is packet supported
           if (packetProcessorsRegister.containsKey(fermatPacketReceive.getFermatPacketType())){

                /*
                * Call the processors for this packet
                */
               for (FermatTyrusPacketProcessor fermatPacketProcessor :packetProcessorsRegister.get(fermatPacketReceive.getFermatPacketType())) {

                   /*
                    * Processor make his job
                    */
                   fermatPacketProcessor.processingPackage(fermatPacketReceive);
               }

           }else {

               System.out.println(" WsCommunicationsTyrusCloudClientChannel - Packet type " + fermatPacketReceive.getFermatPacketType() + "is not supported");

           }
    	
    }
    
    @OnClose
    public void onClose(final Session session, final CloseReason reason) {
    	
        System.out.println(" --------------------------------------------------------------------- ");
        System.out.println(" WsCommunicationsTyrusCloudClientChannel - Starting method onClose");
    	
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
    
    public void sendPing() throws IOException {

        //System.out.println(" WsCommunicationsTyrusCloudClientChannel - Sending ping to the node...");

        String pingString = "PING";
        ByteBuffer pingData = ByteBuffer.allocate(pingString.getBytes().length);
        pingData.put(pingString.getBytes()).flip();
        getClientConnection().getBasicRemote().sendPing(pingData);

    }
    
    @OnMessage
    public void onPongMessage(PongMessage message) {
        //System.out.println(" WsCommunicationsTyrusCloudClientChannel - Pong message receive from server = " + message.getApplicationData().asCharBuffer().toString());
    }

    
    private void validateFermatPacketSignature(FermatPacket fermatPacketReceive){

        //System.out.println(" WsCommunicationsTyrusCloudClientChannel - validateFermatPacketSignature");

         /*
         * Validate the signature
         */
        boolean isValid = AsymmetricCryptography.verifyMessageSignature(fermatPacketReceive.getSignature(), fermatPacketReceive.getMessageContent(), getServerIdentity());

       // System.out.println(" WsCommunicationsTyrusCloudClientChannel - isValid = " + isValid);

        /*
         * if not valid signature
         */
        if (!isValid){
            throw new RuntimeException("Fermat Packet received has not a valid signature, go to close this connection maybe is compromise");
        }

    }
    
    public void registerFermatPacketProcessor(FermatTyrusPacketProcessor fermatPacketProcessor) {


        //Validate if a previous list created
        if (packetProcessorsRegister.containsKey(fermatPacketProcessor.getFermatPacketType())){

            /*
             * Add to the existing list
             */
            packetProcessorsRegister.get(fermatPacketProcessor.getFermatPacketType()).add(fermatPacketProcessor);

        }else{

            /*
             * Create a new list and add the fermatPacketProcessor
             */
            CopyOnWriteArrayList<FermatTyrusPacketProcessor> fermatPacketProcessorList = new CopyOnWriteArrayList<>();
            fermatPacketProcessorList.add(fermatPacketProcessor);

            /*
             * Add to the packetProcessorsRegister
             */
            packetProcessorsRegister.put(fermatPacketProcessor.getFermatPacketType(), fermatPacketProcessorList);
        }

    }
    
    
    /**
     * Get the Client Identity
     * @return ECCKeyPair
     */
    public ECCKeyPair getClientIdentity() {
        return clientIdentity;
    }

    /**
     * Get Temporal Identity
     * @return ECCKeyPair
     */
    public ECCKeyPair getTemporalIdentity() {
        return temporalIdentity;
    }

    /**
     * Get Server Identity
     *
     * @return String
     */
    public String getServerIdentity() {
        return serverIdentity;
    }
    
    /**
     * Set Server Identity
     * @param serverIdentity
     */
    public void setServerIdentity(String serverIdentity) {
        this.serverIdentity = serverIdentity;
    }

    /**
     * Clean all packet processors registered
     */
    public void cleanPacketProcessorsRegistered(){
        packetProcessorsRegister.clear();
    }


    /**
     * Get the PlatformComponentProfile
     *
     * @return PlatformComponentProfile
     */
    public PlatformComponentProfile getPlatformComponentProfile() {
        return platformComponentProfile;
    }
    
    /**
     * Set the PlatformComponentProfile
     * @param platformComponentProfile
     */
    public void setPlatformComponentProfile(PlatformComponentProfile platformComponentProfile) {
        this.platformComponentProfile = platformComponentProfile;
    }

    /**
     * Get the isActive value
     * @return boolean
     */
    public boolean isRegister() {
        return isRegister;
    }

    /**
     * Set the isActive
     * @param isRegister
     */
    public void setIsRegister(boolean isRegister) {
        this.isRegister = isRegister;
    }
    
    /**
     * Get the IdentityPublicKey
     * @return String
     */
    public String getIdentityPublicKey(){
        return clientIdentity.getPublicKey();
    }

    /**
     * Get the clientConnection value
     *
     * @return clientConnection current value
     */
    public Session getClientConnection() {
        return clientConnection;
    }
    
    public WsCommunicationsTyrusCloudClientConnection getWsCommunicationsTyrusCloudClientConnection() {
        return wsCommunicationsTyrusCloudClientConnection;
    }
    
    

}
