/*
 * @#WsCommunicationTyrusVPNClientManagerAgent.java - 2016
 * Copyright bitDubai.com., All rights reserved.
 * You may not modify, use, reproduce or distribute this software.
 * BITDUBAI/CONFIDENTIAL
 */
package org.fermat.p2p.server.app.stress.structure.channel.vpn;


import org.fermat.p2p.server.app.stress.structure.channel.WsCommunicationsTyrusCloudClientConnection;
import org.fermat.p2p.server.app.stress.structure.commons.components.PlatformComponentProfileCommunication;
import org.fermat.p2p.server.app.stress.structure.conf.CloudClientVpnConfigurator;
import org.fermat.p2p.server.app.stress.structure.crypto.asymmetric.ECCKeyPair;
import org.fermat.p2p.server.app.stress.structure.enums.NetworkServiceType;
import org.fermat.p2p.server.app.stress.structure.interfaces.FermatMessage;
import org.fermat.p2p.server.app.stress.structure.interfaces.PlatformComponentProfile;
import org.glassfish.tyrus.client.ClientManager;

import java.io.IOException;
import java.net.URI;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.websocket.ClientEndpointConfig;
import javax.websocket.DeploymentException;


public class WsCommunicationTyrusVPNClientManagerAgent {
	
    /**
     *  Represent the instance
     */
   // private static WsCommunicationTyrusVPNClientManagerAgent instance = new WsCommunicationTyrusVPNClientManagerAgent();

    /**
     * Represent the instance.vpnClientActiveCache;
     */
    private Map<NetworkServiceType, Map<String, WsCommunicationTyrusVPNClient>> vpnClientActiveCache;
    
    private WsCommunicationsTyrusCloudClientConnection WsCommunicationsTyrusCloudClientConnection;

    /**
     * Constructor
     */
    public WsCommunicationTyrusVPNClientManagerAgent(){
       this.vpnClientActiveCache = new ConcurrentHashMap<>();
    }
    
    public void createNewWsCommunicationVPNClient(URI serverURI, String vpnServerIdentity, PlatformComponentProfile participant, PlatformComponentProfile remotePlatformComponentProfile, PlatformComponentProfile remoteParticipantNetworkService, WsCommunicationsTyrusCloudClientConnection WsCommunicationsTyrusCloudClientConnection) throws IOException, DeploymentException {

        /*
         * Create the identity
         */
        ECCKeyPair vpnClientIdentity = new ECCKeyPair();


        /*
         * Clean the extra data to reduce size
         */
        PlatformComponentProfileCommunication registerParticipant = (PlatformComponentProfileCommunication) participant;
        registerParticipant.setExtraData(null);

        /*
         * Construct the vpn client
         */
        WsCommunicationTyrusVPNClient newPpnClient = new WsCommunicationTyrusVPNClient(this, vpnClientIdentity, remotePlatformComponentProfile, remoteParticipantNetworkService, vpnServerIdentity);

        System.out.println("GUARDANDO LA CONEXION EN CACHE CON NS = " + remoteParticipantNetworkService.getNetworkServiceType());
        System.out.println("GUARDANDO LA CONEXION EN CACHE CON PK = " + remotePlatformComponentProfile.getIdentityPublicKey());

        /*
         * Add to the vpn client active
         */
        if (this.vpnClientActiveCache.containsKey(remoteParticipantNetworkService.getNetworkServiceType())){

        	this.vpnClientActiveCache.get(remoteParticipantNetworkService.getNetworkServiceType()).put(remotePlatformComponentProfile.getIdentityPublicKey(), newPpnClient);

        }else {

            Map<String, WsCommunicationTyrusVPNClient> newMap = new ConcurrentHashMap<>();
            newMap.put(remotePlatformComponentProfile.getIdentityPublicKey(), newPpnClient);
            this.vpnClientActiveCache.put(remoteParticipantNetworkService.getNetworkServiceType(), newMap);
        }

        CloudClientVpnConfigurator cloudClientVpnConfigurator = new CloudClientVpnConfigurator(vpnClientIdentity, remoteParticipantNetworkService, participant, remotePlatformComponentProfile);

        ClientEndpointConfig clientConfig = ClientEndpointConfig.Builder.create()
                                                                        .configurator(cloudClientVpnConfigurator)
                                                                        .build();

        ClientManager clientManager = ClientManager.createClient();

        /*
        ClientManager.ReconnectHandler reconnectHandler = new ClientManager.ReconnectHandler() {
            @Override
            public boolean onDisconnect(CloseReason closeReason) {
                System.out.println("### Reconnecting... ");
                return true;
            }
            @Override
            public boolean onConnectFailure(Exception exception) {
                // Thread.sleep(...) to avoid potential DDoS when you don't limit number of reconnects.
                return true;
            }
        };
        /*
         *  Add Property RECONNECT_HANDLER to reconnect automatically
         */
       /* clientManager.getProperties().put(ClientProperties.RECONNECT_HANDLER, reconnectHandler);
        */

        clientManager.connectToServer(newPpnClient, clientConfig, serverURI);
        
        this.WsCommunicationsTyrusCloudClientConnection = WsCommunicationsTyrusCloudClientConnection;

    }
    
    public synchronized WsCommunicationTyrusVPNClient getActiveVpnConnection(NetworkServiceType applicantNetworkServiceType, PlatformComponentProfile remotePlatformComponentProfile){

        System.out.println("WsCommunicationVPNClientManagerAgent getActiveVpnConnection - remotePlatformComponentProfile = "+remotePlatformComponentProfile.getAlias());
        System.out.println("WsCommunicationVPNClientManagerAgent getActiveVpnConnection - applicantNetworkServiceType = "+applicantNetworkServiceType);

        for (NetworkServiceType networkServiceType: this.vpnClientActiveCache.keySet()) {
            System.out.println("WsCommunicationVPNClientManagerAgent networkServiceType available= "+networkServiceType);
        }

        System.out.println("WsCommunicationVPNClientManagerAgent instance.vpnClientActiveCache.containsKey(applicantNetworkServiceType) = "+this.vpnClientActiveCache.containsKey(applicantNetworkServiceType));

        if (this.vpnClientActiveCache.containsKey(applicantNetworkServiceType)){

            System.out.println("WsCommunicationVPNClientManagerAgent - instance.vpnClientActiveCache.get(applicantNetworkServiceType).size() = "+this.vpnClientActiveCache.get(applicantNetworkServiceType).size());

            System.out.println("---------------------CLAVE DISPONIBLES:"+this.vpnClientActiveCache.keySet().toString()+"------------------------------------");
            System.out.println("---------------------CLAVES BUSCADA:"+remotePlatformComponentProfile.getIdentityPublicKey()+"------------------------------------");

            if (this.vpnClientActiveCache.get(applicantNetworkServiceType).containsKey(remotePlatformComponentProfile.getIdentityPublicKey())){
                return this.vpnClientActiveCache.get(applicantNetworkServiceType).get(remotePlatformComponentProfile.getIdentityPublicKey());
            }else {

                System.out.println("WsCommunicationVPNClientManagerAgent getActiveVpnConnection - pk = "+remotePlatformComponentProfile.getIdentityPublicKey());
                throw new IllegalArgumentException("The remote pk is no valid, do not exist a vpn connection for this pk = "+remotePlatformComponentProfile.getIdentityPublicKey());
            }

        }else {
            throw new IllegalArgumentException("The applicantNetworkServiceType is no valid, do not exist a vpn connection for this ");
        }
    }
    
    public void closeAllVpnConnections(){

        try {

            System.out.println("WsCommunicationVPNClientManagerAgent - closeAllVpnConnections()");

            if (!this.vpnClientActiveCache.isEmpty()) {

                for (NetworkServiceType networkServiceType : this.vpnClientActiveCache.keySet()) {

                    for (String remote : this.vpnClientActiveCache.get(networkServiceType).keySet()) {
                        WsCommunicationTyrusVPNClient wsCommunicationVPNClient = this.vpnClientActiveCache.get(networkServiceType).get(remote);
                        wsCommunicationVPNClient.close();
                        this.vpnClientActiveCache.get(networkServiceType).remove(remote);
                    }
                }

            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }
    
    public void closeRemoteVpnConnection(NetworkServiceType networkServiceType, String identityPublicKeyRemote){
    	
    	if(this.vpnClientActiveCache.containsKey(networkServiceType))
    		this.vpnClientActiveCache.get(networkServiceType).remove(identityPublicKeyRemote);
    	
    	
    }
    
    /*public static WsCommunicationTyrusVPNClientManagerAgent getInstance() {
    	//return this;
        return null;
    }   */
    
    
    public void handleNewMessageReceived(NetworkServiceType networkServiceTypeApplicant, PlatformComponentProfile platformComponentProfileRemote, FermatMessage fermatMessage){
    	this.WsCommunicationsTyrusCloudClientConnection.handleNewMessageReceived(networkServiceTypeApplicant, platformComponentProfileRemote, fermatMessage);
    }
    

}
