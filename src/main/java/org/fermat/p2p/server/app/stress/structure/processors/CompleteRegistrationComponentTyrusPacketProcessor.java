/*
 * @#CompleteRegistrationComponentTyrusPacketProcessor.java - 2016
 * Copyright bitDubai.com., All rights reserved.
 * You may not modify, use, reproduce or distribute this software.
 * BITDUBAI/CONFIDENTIAL
 */
package org.fermat.p2p.server.app.stress.structure.processors;

import org.fermat.p2p.server.app.stress.structure.channel.WsCommunicationsTyrusCloudClientChannel;
import org.fermat.p2p.server.app.stress.structure.commons.components.PlatformComponentProfileCommunication;
import org.fermat.p2p.server.app.stress.structure.crypto.asymmetric.AsymmetricCryptography;
import org.fermat.p2p.server.app.stress.structure.enums.FermatPacketType;
import org.fermat.p2p.server.app.stress.structure.enums.JsonAttNamesConstants;
import org.fermat.p2p.server.app.stress.structure.enums.NetworkServiceType;
import org.fermat.p2p.server.app.stress.structure.enums.PlatformComponentType;
import org.fermat.p2p.server.app.stress.structure.exceptions.CantRegisterComponentException;
import org.fermat.p2p.server.app.stress.structure.interfaces.FermatPacket;
import org.fermat.p2p.server.app.stress.structure.interfaces.PlatformComponentProfile;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class CompleteRegistrationComponentTyrusPacketProcessor extends FermatTyrusPacketProcessor{

	
    /**
     * Represent the gson
     */
    private Gson gson;

    /**
     * Represent the jsonParser
     */
    private JsonParser jsonParser;

    /**
     * Constructor
     */
    public CompleteRegistrationComponentTyrusPacketProcessor(WsCommunicationsTyrusCloudClientChannel wsCommunicationsTyrusCloudClientChannel){
        super(wsCommunicationsTyrusCloudClientChannel);
        gson = new Gson();
        jsonParser = new JsonParser();
    }
    
    @Override
    public void processingPackage(FermatPacket receiveFermatPacket) {
    	
    	 
        //System.out.println("CompleteRegistrationComponentTyrusPacketProcessor - processingPackage");

        String messageContentJsonStringRepresentation = null;

        if (getWsCommunicationsTyrusCloudClientChannel().isRegister()){

            /*
            * Get the platformComponentProfile from the message content and decrypt
            */
            //System.out.println(" CompleteRegistrationComponentTyrusPacketProcessor - decoding fermatPacket with client-identity ");
            messageContentJsonStringRepresentation = AsymmetricCryptography.decryptMessagePrivateKey(receiveFermatPacket.getMessageContent(), getWsCommunicationsTyrusCloudClientChannel().getClientIdentity().getPrivateKey());

        }else {

            /*
            * ---------------------------------------------------------------------------------------------------
            * IMPORTANT: This Message Content of this packet come encrypted with the temporal identity public key
            * at this moment the communication cloud client is noT register
            * ---------------------------------------------------------------------------------------------------
            * Get the platformComponentProfile from the message content and decrypt
            */
            //System.out.println(" CompleteRegistrationComponentTyrusPacketProcessor - decoding fermatPacket with temp-identity ");
            messageContentJsonStringRepresentation = AsymmetricCryptography.decryptMessagePrivateKey(receiveFermatPacket.getMessageContent(), getWsCommunicationsTyrusCloudClientChannel().getTemporalIdentity().getPrivateKey());

        }
        
        /*
         * Construct the json object
         */
        JsonObject contentJsonObject = jsonParser.parse(messageContentJsonStringRepresentation).getAsJsonObject();
        NetworkServiceType networkServiceTypeApplicant = gson.fromJson(contentJsonObject.get(JsonAttNamesConstants.NETWORK_SERVICE_TYPE).getAsString(), NetworkServiceType.class);
        PlatformComponentProfile platformComponentProfile = new PlatformComponentProfileCommunication().fromJson(contentJsonObject.get(JsonAttNamesConstants.PROFILE_TO_REGISTER).getAsString());
     
        
        //System.out.println(platformComponentProfile.toJson());
        
        
        if (platformComponentProfile.getPlatformComponentType() == PlatformComponentType.COMMUNICATION_CLOUD_CLIENT){
        	
            if(!getWsCommunicationsTyrusCloudClientChannel().getWsCommunicationsTyrusCloudClientConnection().getPlatformComponentProfileRegisteredSuccess().containsKey(platformComponentProfile.getNetworkServiceType())){
            	getWsCommunicationsTyrusCloudClientChannel().getWsCommunicationsTyrusCloudClientConnection().getPlatformComponentProfileRegisteredSuccess().put(networkServiceTypeApplicant, platformComponentProfile);
            }
        	
        	 /*
             * Mark as register
             */
            getWsCommunicationsTyrusCloudClientChannel().setIsRegister(Boolean.TRUE);
            
            /*
             * send register All the platformComponentProfile except the COMMUNICATION_CLOUD_CLIENT
             */
            try {
				getWsCommunicationsTyrusCloudClientChannel().getWsCommunicationsTyrusCloudClientConnection().sendAllListPlatformComponentProfileToRegister();
			} catch (CantRegisterComponentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
            
        }else if(platformComponentProfile.getPlatformComponentType() == PlatformComponentType.NETWORK_SERVICE){
        	
        	  if(!getWsCommunicationsTyrusCloudClientChannel().getWsCommunicationsTyrusCloudClientConnection().getPlatformComponentProfileRegisteredSuccess().containsKey(platformComponentProfile.getNetworkServiceType())){
              	getWsCommunicationsTyrusCloudClientChannel().getWsCommunicationsTyrusCloudClientConnection().getPlatformComponentProfileRegisteredSuccess().put(networkServiceTypeApplicant, platformComponentProfile);
              	getWsCommunicationsTyrusCloudClientChannel().getWsCommunicationsTyrusCloudClientConnection().setPlatformComponentProfileToNetworkService(networkServiceTypeApplicant);
        	  }
        	  
        	  /*
        	   * send register the Actor specific of the NETWORK_SERVICE received
        	   */
        	try {
				getWsCommunicationsTyrusCloudClientChannel().getWsCommunicationsTyrusCloudClientConnection().setOtherComponentProfileToRegister(platformComponentProfile.getNetworkServiceType());
			} catch (CantRegisterComponentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	  
        }else if(platformComponentProfile.getNetworkServiceType() == NetworkServiceType.UNDEFINED 
        		&& platformComponentProfile.getPlatformComponentType() != PlatformComponentType.COMMUNICATION_CLOUD_CLIENT){
        	
        	 if(!getWsCommunicationsTyrusCloudClientChannel().getWsCommunicationsTyrusCloudClientConnection().getListOtherComponentToRegisteredSuccess().containsKey(networkServiceTypeApplicant)){
               	getWsCommunicationsTyrusCloudClientChannel().getWsCommunicationsTyrusCloudClientConnection().getListOtherComponentToRegisteredSuccess().put(networkServiceTypeApplicant, platformComponentProfile);
               	getWsCommunicationsTyrusCloudClientChannel().getWsCommunicationsTyrusCloudClientConnection().requestDiscoveryRequestVpnConnection(networkServiceTypeApplicant, platformComponentProfile);
        	 }
        	
        }
        
        
    	
    }
    
    @Override
    public FermatPacketType getFermatPacketType() {
        return FermatPacketType.COMPLETE_COMPONENT_REGISTRATION;
    }
    
    
}
