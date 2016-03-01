/*
 * @#IntraActorNetworkServicePlugin.java - 2016
 * Copyright bitDubai.com., All rights reserved.
 * You may not modify, use, reproduce or distribute this software.
 * BITDUBAI/CONFIDENTIAL
 */
package org.fermat.p2p.server.app.stress.structure.network_services.CCP.intra_actor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.fermat.p2p.server.app.stress.structure.channel.vpn.WsCommunicationTyrusVPNClient;
import org.fermat.p2p.server.app.stress.structure.channel.vpn.WsCommunicationTyrusVPNClientManagerAgent;
import org.fermat.p2p.server.app.stress.structure.enums.FermatMessageContentType;
import org.fermat.p2p.server.app.stress.structure.enums.NetworkServiceType;
import org.fermat.p2p.server.app.stress.structure.enums.ccp_intra_actor.ActorProtocolState;
import org.fermat.p2p.server.app.stress.structure.enums.ccp_intra_actor.NotificationDescriptor;
import org.fermat.p2p.server.app.stress.structure.exceptions.FMPException;
import org.fermat.p2p.server.app.stress.structure.interfaces.FermatMessage;
import org.fermat.p2p.server.app.stress.structure.interfaces.PlatformComponentProfile;
import org.fermat.p2p.server.app.stress.structure.commons.contents.*;

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
	
	public IntraActorNetworkServicePlugin(){
		listRemotePlatformComponentProfile = new ArrayList<PlatformComponentProfile>();
	}
	
	/*
	 * send request connection to Actor
	 */
	public void initializationCommunication(PlatformComponentProfile remotePlatformComponentProfile){
		
		WsCommunicationTyrusVPNClient WsCommunicationTyrusVPNClient = WsCommunicationTyrusVPNClientManagerAgent.getInstance().getActiveVpnConnection(networkServiceTypeBase, remotePlatformComponentProfile); 
		
		   UUID newNotificationID = UUID.randomUUID();
           NotificationDescriptor notificationDescriptor = NotificationDescriptor.ASKFORACCEPTANCE;
           long currentTime = System.currentTimeMillis();
           ActorProtocolState protocolState = ActorProtocolState.PROCESSING_SEND;
           
           

           String messageContent = null;
		
	     /*
         * Created the message
         */
        try {
			FermatMessage fermatMessage = FermatMessageCommunicationFactory.constructFermatMessage(platformComponentProfileActor.getIdentityPublicKey(),//Sender
					remotePlatformComponentProfile.getIdentityPublicKey(), //Receiver
			        messageContent, //Message Content
			        FermatMessageContentType.TEXT);
		} catch (FMPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//Type
	}
	
	public List<PlatformComponentProfile> getListRemotePlatformComponentProfile() {
		return listRemotePlatformComponentProfile;
	}

	public void setRemotePlatformComponentProfile(PlatformComponentProfile RemotePlatformComponentProfile) {
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
  		
  		WsCommunicationTyrusVPNClient WsCommunicationTyrusVPNClient = WsCommunicationTyrusVPNClientManagerAgent.getInstance().getActiveVpnConnection(networkServiceTypeBase, remotePlatformComponentProfile); 
		
  		
  		
  		
  	}
	



}
