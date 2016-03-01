/*
 * @#IntraActorNetworkServicePlugin.java - 2016
 * Copyright bitDubai.com., All rights reserved.
 * You may not modify, use, reproduce or distribute this software.
 * BITDUBAI/CONFIDENTIAL
 */
package org.fermat.p2p.server.app.stress.structure.network_services.CCP.intra_actor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.websocket.CloseReason;

import org.fermat.p2p.server.app.stress.structure.channel.vpn.WsCommunicationTyrusVPNClient;
import org.fermat.p2p.server.app.stress.structure.channel.vpn.WsCommunicationTyrusVPNClientManagerAgent;
import org.fermat.p2p.server.app.stress.structure.enums.Actors;
import org.fermat.p2p.server.app.stress.structure.enums.FermatMessageContentType;
import org.fermat.p2p.server.app.stress.structure.enums.NetworkServiceType;
import org.fermat.p2p.server.app.stress.structure.enums.ccp_intra_actor.ActorProtocolState;
import org.fermat.p2p.server.app.stress.structure.enums.ccp_intra_actor.NotificationDescriptor;
import org.fermat.p2p.server.app.stress.structure.exceptions.FMPException;
import org.fermat.p2p.server.app.stress.structure.interfaces.FermatMessage;
import org.fermat.p2p.server.app.stress.structure.interfaces.PlatformComponentProfile;
import org.fermat.p2p.server.app.stress.structure.commons.contents.*;
import org.fermat.p2p.server.app.stress.structure.crypto.asymmetric.AsymmetricCryptography;
import org.fermat.p2p.server.app.stress.structure.crypto.asymmetric.ECCKeyPair;

public class IntraActorNetworkServicePlugin {
	
	/*
	 * Represent the Profile of the Plugin
	 */
	private PlatformComponentProfile platformComponentProfileRoot;
	
	/*
	 * Represent the Profile of the Actor
	 */
	private PlatformComponentProfile platformComponentProfileActor;
	
	/*
	 * Represent the Profile of the Base
	 */
	private NetworkServiceType networkServiceTypeBase;
	
	private List<PlatformComponentProfile> listRemotePlatformComponentProfile;
	
	private ECCKeyPair identity;
	
	public IntraActorNetworkServicePlugin(){
		listRemotePlatformComponentProfile = new ArrayList<PlatformComponentProfile>();
	}
	
	public ECCKeyPair getIdentity() {
		return identity;
	}

	public void setIdentity(ECCKeyPair identity) {
		this.identity = identity;
	}
	
	/*
	 * send request connection to Actor
	 */
	public void initializationCommunication(PlatformComponentProfile remotePlatformComponentProfile){
		
		WsCommunicationTyrusVPNClient communicationTyrusVPNClient = WsCommunicationTyrusVPNClientManagerAgent.getInstance().getActiveVpnConnection(networkServiceTypeBase, remotePlatformComponentProfile); 
		
		   UUID newNotificationID = UUID.randomUUID();
           NotificationDescriptor notificationDescriptor = NotificationDescriptor.ASKFORACCEPTANCE;
           long currentTime = System.currentTimeMillis();
           ActorProtocolState protocolState = ActorProtocolState.PROCESSING_SEND;
           
           String image = "";
           String messageContent = "";
           
           ActorNetworkServiceRecord actorNetworkServiceRecord = new ActorNetworkServiceRecord(
        		   newNotificationID,
        		   platformComponentProfileActor.getAlias(),
        		   "OK!",
        		   image.getBytes(),
        		   notificationDescriptor,
        		   Actors.INTRA_USER,
        		   Actors.INTRA_USER,
        		   platformComponentProfileActor.getIdentityPublicKey(),
        		   remotePlatformComponentProfile.getIdentityPublicKey(),
        		   currentTime,
        		   protocolState,
        		   false,
        		   1,
        		   null
        		   );
		
           messageContent = actorNetworkServiceRecord.toJson();
           
	     /*
         * Created the message
         */
        try {
			FermatMessage fermatMessage = FermatMessageCommunicationFactory.constructFermatMessage(platformComponentProfileActor.getIdentityPublicKey(),//Sender
					remotePlatformComponentProfile.getIdentityPublicKey(), //Receiver
			        messageContent, //Message Content
			        FermatMessageContentType.TEXT);
			
			/*
			 * send message
			 */
			communicationTyrusVPNClient.sendMessage(fermatMessage);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//Type
	}
	
	public List<PlatformComponentProfile> getListRemotePlatformComponentProfile() {
		return listRemotePlatformComponentProfile;
	}

	public void setRemotePlatformComponentProfile(PlatformComponentProfile RemotePlatformComponentProfile) {
		
		if(!listRemotePlatformComponentProfile.contains(RemotePlatformComponentProfile))
			this.listRemotePlatformComponentProfile.add(RemotePlatformComponentProfile);
		
	}

	public void setPlatformComponentProfileRoot(NetworkServiceType networkServiceTypeBase, PlatformComponentProfile platformComponentProfileRoot, PlatformComponentProfile platformComponentProfileActor) {
		this.networkServiceTypeBase = networkServiceTypeBase;
		this.platformComponentProfileRoot = platformComponentProfileRoot;
		this.platformComponentProfileActor = platformComponentProfileActor;
	}

	public NetworkServiceType getNetworkServiceTypeBase() {
		return networkServiceTypeBase;
	}
	
	public PlatformComponentProfile getPlatformComponentProfileRoot() {
		return platformComponentProfileRoot;
	}


  	public void sethandleNewMessageReceived(PlatformComponentProfile remotePlatformComponentProfile, FermatMessage fermatMessage){
  		
  		WsCommunicationTyrusVPNClient communicationTyrusVPNClient = WsCommunicationTyrusVPNClientManagerAgent.getInstance().getActiveVpnConnection(networkServiceTypeBase, remotePlatformComponentProfile); 
		
  		/*
         * Decrypt the message content
         */
  		String messageContent = AsymmetricCryptography.decryptMessagePrivateKey(fermatMessage.getContent(), identity.getPrivateKey());
  		
  		ActorNetworkServiceRecord actorNetworkServiceRecord = ActorNetworkServiceRecord.fronJson(messageContent);
  		
  		switch (actorNetworkServiceRecord.getNotificationDescriptor()) {
  		
  		case ASKFORACCEPTANCE:
  			
  	      System.out.println("----------------------------\n" +
                  "MENSAJE LLEGO EXITOSAMENTE:" + actorNetworkServiceRecord.getActorSenderAlias()
                  + "\n-------------------------------------------------");
  			
  	      	actorNetworkServiceRecord.changeDescriptor(NotificationDescriptor.ACCEPTED);
  			actorNetworkServiceRecord.changeState(ActorProtocolState.PROCESSING_RECEIVE);
  			actorNetworkServiceRecord.setFlagReadead(false);
  			respondReceiveAndDoneCommunication(communicationTyrusVPNClient, actorNetworkServiceRecord);
  			
  			
  		    try {

  			   TimeUnit.SECONDS.sleep(1);
  			} catch (InterruptedException e) {
  			   e.printStackTrace();
  			}
  		    
  		    actorNetworkServiceRecord.changeDescriptor(NotificationDescriptor.RECEIVED);
  		  	respondReceiveAndDoneCommunication(communicationTyrusVPNClient, actorNetworkServiceRecord);
  			
  			break;
  			
  		case ACCEPTED:
  			
  		  System.out.println("----------------------------\n" +
                  "MENSAJE ACCEPTED LLEGÓ BIEN: CASE ACCEPTED" + actorNetworkServiceRecord.getActorSenderAlias()
                  + "\n-------------------------------------------------");
  		
  		   actorNetworkServiceRecord.changeDescriptor(NotificationDescriptor.RECEIVED);
           actorNetworkServiceRecord.changeState(ActorProtocolState.DONE);
           actorNetworkServiceRecord.setFlagReadead(false);
          
           respondReceiveAndDoneCommunication(communicationTyrusVPNClient, actorNetworkServiceRecord);
  			
  			break;
  			
  		case RECEIVED:
  			
            System.out.println("----------------------------\n" +
                    "Message Delivery " +
                    "Close Connection" + actorNetworkServiceRecord.getActorSenderAlias()
                    + "\n-------------------------------------------------");
  			
  		   communicationTyrusVPNClient.close();
  		   WsCommunicationTyrusVPNClientManagerAgent.getInstance().closeRemoteVpnConnection(networkServiceTypeBase, remotePlatformComponentProfile.getIdentityPublicKey());
			
  			break;
  			
  		case DENIED:
  			
            System.out.println("----------------------------\n" +
                    "MENSAJE DENIED LLEGÓ BIEN: CASE DENIED" + actorNetworkServiceRecord.getActorDestinationPublicKey()
                    + "\n-------------------------------------------------");
  			
  			actorNetworkServiceRecord.changeDescriptor(NotificationDescriptor.DENIED);
            actorNetworkServiceRecord.changeState(ActorProtocolState.DONE);
            actorNetworkServiceRecord.changeState(ActorProtocolState.PROCESSING_RECEIVE);
            actorNetworkServiceRecord.setFlagReadead(false);
            
            respondReceiveAndDoneCommunication(communicationTyrusVPNClient, actorNetworkServiceRecord);
  			
  			break;
  			
  		case DISCONNECTED:
  			
            System.out.println("----------------------------\n" +
                    "MENSAJE DISCONNECTED LLEGÓ BIEN: CASE DISCONNECTED" + actorNetworkServiceRecord.getActorSenderAlias()
                    + "\n-------------------------------------------------");
  			
            actorNetworkServiceRecord.changeDescriptor(NotificationDescriptor.DISCONNECTED);
            actorNetworkServiceRecord.changeState(ActorProtocolState.DONE);
            actorNetworkServiceRecord.changeState(ActorProtocolState.PROCESSING_RECEIVE);
            actorNetworkServiceRecord.setFlagReadead(false);
            
            respondReceiveAndDoneCommunication(communicationTyrusVPNClient, actorNetworkServiceRecord);
  			
  			break;
  			
  	   default:

           break;
           
  		}
  	}
	

  	
    private ActorNetworkServiceRecord changeActor(ActorNetworkServiceRecord actorNetworkServiceRecord) {
        // change actor
        String actorDestination = actorNetworkServiceRecord.getActorDestinationPublicKey();
        String actorSender = actorNetworkServiceRecord.getActorSenderPublicKey();
        actorNetworkServiceRecord.setActorDestinationPublicKey(actorSender);
        actorNetworkServiceRecord.setActorSenderPublicKey(actorDestination);
        
        return actorNetworkServiceRecord;
    }
    
    private void respondReceiveAndDoneCommunication(WsCommunicationTyrusVPNClient communicationTyrusVPNClient, ActorNetworkServiceRecord actorNetworkServiceRecord) {
    	
    	 actorNetworkServiceRecord = changeActor(actorNetworkServiceRecord);
         UUID newNotificationID = UUID.randomUUID();
         long currentTime = System.currentTimeMillis();
         ActorProtocolState protocolState = ActorProtocolState.PROCESSING_SEND;
         //actorNetworkServiceRecord.changeDescriptor(NotificationDescriptor.RECEIVED);
         
         ActorNetworkServiceRecord actorNetworkServiceRecordToSend = new ActorNetworkServiceRecord(
                 newNotificationID,
                 actorNetworkServiceRecord.getActorSenderAlias(),
                 actorNetworkServiceRecord.getActorSenderPhrase(),
                 actorNetworkServiceRecord.getActorSenderProfileImage(),
                 actorNetworkServiceRecord.getNotificationDescriptor(),
                 actorNetworkServiceRecord.getActorDestinationType(),
                 actorNetworkServiceRecord.getActorSenderType(),
                 actorNetworkServiceRecord.getActorSenderPublicKey(),
                 actorNetworkServiceRecord.getActorDestinationPublicKey(),
                 currentTime,
                 protocolState,
                 false,
                 1,
                 actorNetworkServiceRecord.getId()
         );
         
         String messageContent = actorNetworkServiceRecordToSend.toJson();
         
         
         try {
        	 
 			FermatMessage fermatMessage = FermatMessageCommunicationFactory.constructFermatMessage(actorNetworkServiceRecord.getActorSenderPublicKey(),//Sender
 					actorNetworkServiceRecord.getActorDestinationPublicKey(), //Receiver
 			        messageContent, //Message Content
 			        FermatMessageContentType.TEXT);
 			
 			/*
 			 * send message
 			 */
 			communicationTyrusVPNClient.sendMessage(fermatMessage);
 			
 		} catch (Exception e) {
 			e.printStackTrace();
 		}
    	
    	
    }


}
