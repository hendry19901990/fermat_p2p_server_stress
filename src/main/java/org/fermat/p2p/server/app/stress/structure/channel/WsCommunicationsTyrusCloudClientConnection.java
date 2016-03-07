/*
 * @#WsCommunicationsTyrusCloudClientConnection.java - 2016
 * Copyright bitDubai.com., All rights reserved.
 * You may not modify, use, reproduce or distribute this software.
 * BITDUBAI/CONFIDENTIAL
 */
package org.fermat.p2p.server.app.stress.structure.channel;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.websocket.ClientEndpointConfig;
import javax.websocket.CloseReason;
import javax.websocket.DeploymentException;

import org.fermat.p2p.server.app.stress.structure.channel.vpn.WsCommunicationTyrusVPNClientManagerAgent;
import org.fermat.p2p.server.app.stress.structure.commons.components.DeviceLocation;
import org.fermat.p2p.server.app.stress.structure.commons.components.DiscoveryQueryParametersCommunication;
import org.fermat.p2p.server.app.stress.structure.commons.components.PlatformComponentProfileCommunication;
import org.fermat.p2p.server.app.stress.structure.commons.contents.FermatPacketCommunicationFactory;
import org.fermat.p2p.server.app.stress.structure.commons.contents.FermatPacketEncoder;
import org.fermat.p2p.server.app.stress.structure.conf.CLoudClientConfigurator;
import org.fermat.p2p.server.app.stress.structure.crypto.asymmetric.ECCKeyPair;
import org.fermat.p2p.server.app.stress.structure.enums.FermatPacketType;
import org.fermat.p2p.server.app.stress.structure.enums.JsonAttNamesConstants;
import org.fermat.p2p.server.app.stress.structure.enums.NetworkServiceType;
import org.fermat.p2p.server.app.stress.structure.enums.PlatformComponentType;
import org.fermat.p2p.server.app.stress.structure.exceptions.CantEstablishConnectionException;
import org.fermat.p2p.server.app.stress.structure.exceptions.CantRegisterComponentException;
import org.fermat.p2p.server.app.stress.structure.interfaces.DiscoveryQueryParameters;
import org.fermat.p2p.server.app.stress.structure.interfaces.FermatMessage;
import org.fermat.p2p.server.app.stress.structure.interfaces.FermatPacket;
import org.fermat.p2p.server.app.stress.structure.interfaces.Location;
import org.fermat.p2p.server.app.stress.structure.interfaces.PlatformComponentProfile;
import org.fermat.p2p.server.app.stress.structure.processors.CompleteComponentConnectionRequestTyrusPacketProcessor;
import org.fermat.p2p.server.app.stress.structure.processors.CompleteRegistrationComponentTyrusPacketProcessor;
import org.fermat.p2p.server.app.stress.structure.processors.ComponentConnectionRespondTyrusPacketProcessor;
import org.fermat.p2p.server.app.stress.structure.processors.FailureComponentRegistrationRequestTyrusPacketProcessor;
import org.fermat.p2p.server.app.stress.structure.processors.ServerHandshakeRespondTyrusPacketProcessor;
import org.glassfish.tyrus.client.ClientManager;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.google.gson.JsonParser;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.fermat.p2p.server.app.stress.structure.network_services.CCP.intra_actor.IntraActorNetworkServicePlugin;

public class WsCommunicationsTyrusCloudClientConnection {
	
	/**
     * Represent the wsCommunicationsTyrusCloudClientChannel
     */
    private WsCommunicationsTyrusCloudClientChannel wsCommunicationsTyrusCloudClientChannel;
    
	/**
     * Represent the webSocketContainer
     */
    private ClientManager webSocketContainer;
    
    /**
     * Represent the uri
     */
    private URI uri;
    
    private Map<NetworkServiceType,PlatformComponentProfile> listPlatformComponentProfileToRegister;
    private Map<NetworkServiceType,PlatformComponentProfile> listPlatformComponentProfileRegisteredSuccess;
    
    private Map<NetworkServiceType,PlatformComponentProfile> listOtherComponentToRegister;
    private Map<NetworkServiceType,PlatformComponentProfile> listOtherComponentToRegisteredSuccess;
    
    private Map<NetworkServiceType,List<PlatformComponentProfile>> listOfRequestConnect;
    private Map<NetworkServiceType,List<PlatformComponentProfile>> listOfRequestConnectSuccess;

	/*
     * Represent the location of an device
     */
    private static Location location = new DeviceLocation();
    
    private ECCKeyPair clientIdentity;
    
    private CLoudClientConfigurator cloudClientConfigurator;
    
	  /**
     * Represent the temporalIdentity
     */
    private ECCKeyPair temporalIdentity;
    
    /*
     * IP of the Cloud Server
     */
    private String ServerIp;
    
	/*
     * Port Default of the Cloud Server
     */
    private Integer ServerPort;
    
    /* Network Services */
    
    private IntraActorNetworkServicePlugin intraActorNetworkServicePluginNS;
    
    private WsCommunicationTyrusVPNClientManagerAgent wsCommunicationTyrusVPNClientManagerAgent;
    
    /* Network Services */
    
    /*
     * Constructor whit parameters
     *
     * @param uri
     * @param eventManager
     */
    public WsCommunicationsTyrusCloudClientConnection(URI uri,  ECCKeyPair clientIdentity, String ServerIp, Integer ServerPort) throws IOException, DeploymentException {
        super();
        this.uri = uri;
        this.clientIdentity = clientIdentity;
        this.ServerIp = ServerIp;
        this.ServerPort = ServerPort;
        this.temporalIdentity = new ECCKeyPair();
        this.wsCommunicationsTyrusCloudClientChannel = new WsCommunicationsTyrusCloudClientChannel(this,  this.clientIdentity, this.temporalIdentity);
        this.cloudClientConfigurator = new CLoudClientConfigurator(this.temporalIdentity);
        this.listPlatformComponentProfileToRegister = new  HashMap<NetworkServiceType,PlatformComponentProfile>();
        this.listPlatformComponentProfileRegisteredSuccess = new HashMap<NetworkServiceType,PlatformComponentProfile>();
        this.listOtherComponentToRegister = new HashMap<NetworkServiceType,PlatformComponentProfile>();
        this.listOtherComponentToRegisteredSuccess = new HashMap<NetworkServiceType,PlatformComponentProfile>();
        this.listOfRequestConnect = new HashMap<NetworkServiceType,List<PlatformComponentProfile>>();
        this.listOfRequestConnectSuccess = new HashMap<NetworkServiceType,List<PlatformComponentProfile>>();
        this.wsCommunicationTyrusVPNClientManagerAgent = new WsCommunicationTyrusVPNClientManagerAgent();
        this.intraActorNetworkServicePluginNS = new IntraActorNetworkServicePlugin(this.wsCommunicationTyrusVPNClientManagerAgent);
    }
    
    public void initializeAndConnect() throws IOException, DeploymentException {
    	
        /*
         * Register the processors
         */
        registerFermatPacketProcessors();
        
		/*
		 * set All PlatformComponentProfile to Register
		 */
		setLoaderListPlatformComponentProfileToRegister();
        
        webSocketContainer = ClientManager.createClient();
        
        ClientEndpointConfig clientConfig = ClientEndpointConfig.Builder.create()
                .configurator(cloudClientConfigurator)
                .build();

        
        /*
         * Connect
         */
        webSocketContainer.connectToServer(wsCommunicationsTyrusCloudClientChannel, clientConfig, uri);
        
    }
    
    /**
     * Register fermat packet processors whit this communication channel
     */
    private void registerFermatPacketProcessors(){
    	
    	/*
         * Clean all
         */
        wsCommunicationsTyrusCloudClientChannel.cleanPacketProcessorsRegistered();
        
        /*
         * Register the packet processors
         */
        wsCommunicationsTyrusCloudClientChannel.registerFermatPacketProcessor(new ServerHandshakeRespondTyrusPacketProcessor(wsCommunicationsTyrusCloudClientChannel));
        wsCommunicationsTyrusCloudClientChannel.registerFermatPacketProcessor(new CompleteRegistrationComponentTyrusPacketProcessor(wsCommunicationsTyrusCloudClientChannel));
        wsCommunicationsTyrusCloudClientChannel.registerFermatPacketProcessor(new FailureComponentRegistrationRequestTyrusPacketProcessor(wsCommunicationsTyrusCloudClientChannel));
        wsCommunicationsTyrusCloudClientChannel.registerFermatPacketProcessor(new ComponentConnectionRespondTyrusPacketProcessor(wsCommunicationsTyrusCloudClientChannel, this.wsCommunicationTyrusVPNClientManagerAgent));
        wsCommunicationsTyrusCloudClientChannel.registerFermatPacketProcessor(new CompleteComponentConnectionRequestTyrusPacketProcessor(wsCommunicationsTyrusCloudClientChannel));
    }
	
	
	public PlatformComponentProfile constructPlatformComponentProfileFactory(String identityPublicKey, String alias, String name, NetworkServiceType networkServiceType, PlatformComponentType platformComponentType, String extraData){

        try {
        	
            //Validate parameters
            if ((identityPublicKey == null || identityPublicKey.equals("")) ||
                    (alias == null || alias.equals(""))                     ||
                    (name == null || name.equals(""))                   ||
                    networkServiceType == null         ||
                    platformComponentType == null  ){
            	
            	
                throw new IllegalArgumentException("All argument are required, can not be null ");

            }
                 
            /*
             * Construct a PlatformComponentProfile instance
             */
            return new PlatformComponentProfileCommunication(alias,
            		clientIdentity.getPublicKey(),
                    identityPublicKey,
                    location,
                    name,
                    networkServiceType,
                    platformComponentType,
                    extraData);

        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }

    }
	
	public void registerComponentForCommunication(NetworkServiceType networkServiceNetworkServiceTypeApplicant, PlatformComponentProfile platformComponentProfile) throws CantRegisterComponentException {

        //System.out.println("WsCommunicationsCloudClientConnection - registerComponentForCommunication");

            /*
             * Validate parameter
             */
        if (platformComponentProfile == null){

            throw new IllegalArgumentException("The platformComponentProfile is required, can not be null");
        }

        try {

            Gson gson = new Gson();
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty(JsonAttNamesConstants.NETWORK_SERVICE_TYPE, networkServiceNetworkServiceTypeApplicant.toString());
            jsonObject.addProperty(JsonAttNamesConstants.PROFILE_TO_REGISTER, platformComponentProfile.toJson());

             /*
             * Construct a fermat packet whit the PlatformComponentProfile
             */
            FermatPacket fermatPacket = FermatPacketCommunicationFactory.constructFermatPacketEncryptedAndSinged(wsCommunicationsTyrusCloudClientChannel.getServerIdentity(),                  //Destination
                    wsCommunicationsTyrusCloudClientChannel.getClientIdentity().getPublicKey(),   //Sender
                    gson.toJson(jsonObject),                                                 //Message Content
                    FermatPacketType.COMPONENT_REGISTRATION_REQUEST,                         //Packet type
                    wsCommunicationsTyrusCloudClientChannel.getClientIdentity().getPrivateKey()); //Sender private key


            String fermatPacketEncode = FermatPacketEncoder.encode(fermatPacket);

            //System.out.println("WsCommunicationsCloudClientConnection - wsCommunicationsTyrusCloudClientChannel.getClientConnection().isOpen() " + wsCommunicationsTyrusCloudClientChannel.getClientConnection().isOpen());
            if (wsCommunicationsTyrusCloudClientChannel.getClientConnection().isOpen()){

                /*
                 * Send the encode packet to the server
                 */
                wsCommunicationsTyrusCloudClientChannel.sendMessage(fermatPacketEncode);

            }else{
                //wsCommunicationsTyrusCloudClientChannel.raiseClientConnectionLooseNotificationEvent();
                throw new Exception("Client Connection is Close");
            }


        }catch (Exception e){
            System.out.println("WsCommunicationsCloudClientConnection: "+e.getStackTrace());
            CantRegisterComponentException pluginStartException = new CantRegisterComponentException(CantRegisterComponentException.DEFAULT_MESSAGE, e, e.getLocalizedMessage(), e.getLocalizedMessage());
            throw pluginStartException;

        }

    }
	
	
	private void requestDiscoveryVpnConnection(PlatformComponentProfile applicantParticipant, PlatformComponentProfile applicantNetworkService, PlatformComponentProfile remoteParticipant) throws CantEstablishConnectionException{
		
		 /*
         * Validate parameter
         */
        if (applicantParticipant == null || applicantNetworkService == null || remoteParticipant == null){

            throw new IllegalArgumentException("All parameters are required, can not be null");
        }
        
        /*
         * Validate are the  type NETWORK_SERVICE
         */
        if (applicantNetworkService.getPlatformComponentType() != PlatformComponentType.NETWORK_SERVICE){
            throw new IllegalArgumentException("The PlatformComponentProfile of the applicantNetworkService has to be NETWORK_SERVICE ");
        }

        /*
         * Validate are the  type NETWORK_SERVICE
         */
        if (applicantParticipant.getIdentityPublicKey().equals(remoteParticipant.getIdentityPublicKey())){
            throw new IllegalArgumentException("The applicant and remote can not be the same component");
        }
        
        try {
        /*
         * Construct the json object
         */
        Gson gson = new Gson();
        JsonObject packetContent = new JsonObject();
        packetContent.addProperty(JsonAttNamesConstants.APPLICANT_PARTICIPANT_VPN, applicantParticipant.toJson());
        packetContent.addProperty(JsonAttNamesConstants.APPLICANT_PARTICIPANT_NS_VPN, applicantNetworkService.toJson());
        packetContent.addProperty(JsonAttNamesConstants.REMOTE_PARTICIPANT_VPN, remoteParticipant.toJson());

        /*
         * Convert to json representation
         */
        String packetContentJson = gson.toJson(packetContent);
        
        /*
         * Construct a fermat packet whit the request
         */
        FermatPacket fermatPacketRespond = FermatPacketCommunicationFactory.constructFermatPacketEncryptedAndSinged(wsCommunicationsTyrusCloudClientChannel.getServerIdentity(),                  //Destination
                wsCommunicationsTyrusCloudClientChannel.getClientIdentity().getPublicKey(),   //Sender
                packetContentJson,                                                  //Message Content
                FermatPacketType.DISCOVERY_COMPONENT_CONNECTION_REQUEST,                 //Packet type
                wsCommunicationsTyrusCloudClientChannel.getClientIdentity().getPrivateKey()); //Sender private key
        
        if (wsCommunicationsTyrusCloudClientChannel.getClientConnection().isOpen()){

            /*
             * Send the encode packet to the server
             */
            wsCommunicationsTyrusCloudClientChannel.sendMessage(FermatPacketEncoder.encode(fermatPacketRespond));

        }else{
        	
        	 throw new Exception("Client Connection is Close");
             
        }
        
        }catch (Exception e){
        	
        	System.out.println("WsCommunicationsCloudClientConnection: "+e.getStackTrace());
        	
        }
        
        
	}
	
    public Map<NetworkServiceType, PlatformComponentProfile> getPlatformComponentProfileToRegister() {
		return listPlatformComponentProfileToRegister;
	}

	public Map<NetworkServiceType, PlatformComponentProfile> getPlatformComponentProfileRegisteredSuccess() {
		return listPlatformComponentProfileRegisteredSuccess;
	}
	
	public  Map<NetworkServiceType, PlatformComponentProfile> getListOtherComponentToRegister() {
		return listOtherComponentToRegister;
	}
	
	public Map<NetworkServiceType, PlatformComponentProfile> getListOtherComponentToRegisteredSuccess() {
		return listOtherComponentToRegisteredSuccess;
	}
	
	public void setPlatformComponentProfileToNetworkService(NetworkServiceType networkServiceType){
		
		switch(networkServiceType){
		
		case INTRA_USER:
			
			  intraActorNetworkServicePluginNS.setPlatformComponentProfileRoot(networkServiceType, listPlatformComponentProfileToRegister.get(networkServiceType), listOtherComponentToRegister.get(networkServiceType));
			
			  break;
			
			  
		default:
			break;
		
		}
		
	}
	
	public Map<NetworkServiceType, List<PlatformComponentProfile>> getListOfRequestConnect() {
		return listOfRequestConnect;
	}
	
	public Integer getTotalListOfRequestConnect(){
		
		int total =  0;
		
		if (listOfRequestConnect != null){
			
			for(List<PlatformComponentProfile> lp : listOfRequestConnect.values()){
				if(lp != null)
				   total = total + lp.size();				
			}
			
		}
			
		
		return total;
		
	}
	
	public Integer getTotalListOfRequestConnectSuccess(){
		
		int total =  0;
		
		if (listOfRequestConnectSuccess != null){
			
			for(List<PlatformComponentProfile> lp : listOfRequestConnectSuccess.values()){
				if(lp != null)
				   total = total + lp.size();				
			}
			
		}
		
		return total;
		
	}

	public Map<NetworkServiceType, List<PlatformComponentProfile>> getListOfRequestConnectSuccess() {
		return listOfRequestConnectSuccess;
	}
	
    public String getServerIp() {
		return ServerIp;
	}

	public Integer getServerPort() {
		return ServerPort;
	}
	
	public void storagelistOfRequestConnectSuccess(NetworkServiceType networkServiceType, PlatformComponentProfile remoteComponent){
		
		if(listOfRequestConnectSuccess.containsKey(networkServiceType)){
			
			if(!listOfRequestConnectSuccess.get(networkServiceType).contains(remoteComponent))
				listOfRequestConnectSuccess.get(networkServiceType).add(remoteComponent);
			
			
		}else{
			
			List<PlatformComponentProfile> listNew = new ArrayList<PlatformComponentProfile>();
			listNew.add(remoteComponent);
			
			listOfRequestConnectSuccess.put(networkServiceType, listNew);
		}
		
		switch(networkServiceType){
		
			case INTRA_USER:
				
				intraActorNetworkServicePluginNS.setRemotePlatformComponentProfile(remoteComponent);
				
				break;
				
			default:
				break;
		
		
		}
		
		
			
		
	}
	
	public Integer getTotalProfileToRegister(){
		
		int listRegistered =  0;
		int listRegisteredOther = 0;
		
		if(listPlatformComponentProfileToRegister != null)
			listRegistered = listPlatformComponentProfileToRegister.size();
		
		if(listOtherComponentToRegister != null)
			listRegisteredOther = listOtherComponentToRegister.size();
		
		return listRegistered + listRegisteredOther;
	}
	
	public Integer getTotalProfileRegisteredSuccess(){
		
		int listRegistered =  0;
		int listRegisteredOther = 0;
		
		if(listPlatformComponentProfileRegisteredSuccess != null)
			listRegistered = listPlatformComponentProfileRegisteredSuccess.size();
		
		if(listOtherComponentToRegisteredSuccess != null)
			listRegisteredOther = listOtherComponentToRegisteredSuccess.size();
		
		return listRegistered + listRegisteredOther;
	}
	
	public void sendAllListPlatformComponentProfileToRegister() throws CantRegisterComponentException{
			
		for(PlatformComponentProfile pToRegister : listPlatformComponentProfileToRegister.values()){
			
			if(pToRegister.getPlatformComponentType() != PlatformComponentType.COMMUNICATION_CLOUD_CLIENT){
				registerComponentForCommunication(pToRegister.getNetworkServiceType(), pToRegister);
				try {

		           /*
		            * Wait 1 second to avoid that the Network Services are Initialized completely
		            */
		           TimeUnit.SECONDS.sleep(1);
		        } catch (InterruptedException e) {
		                e.printStackTrace();
		        }
			}
			
		}
				
	}
	
	/*
	 * set All PlatformComponentProfile to Register
	 */
	private void setLoaderListPlatformComponentProfileToRegister(){

		ECCKeyPair identity = null;
		
		/*
         * Clean all
         */
		listPlatformComponentProfileToRegister.clear();
		
		/*
		 *  set first the Cloud Client 
		 */
		/* P2P */
		listPlatformComponentProfileToRegister.put(NetworkServiceType.UNDEFINED, wsCommunicationsTyrusCloudClientChannel.getPlatformComponentProfile());
		/* P2P */
		
		/* CBP */
		listPlatformComponentProfileToRegister.put(NetworkServiceType.NEGOTIATION_TRANSMISSION, constructPlatformComponentProfileFactory(new ECCKeyPair().getPublicKey(),
				"Negotiation Transmission Network Service".toLowerCase(),
				"Negotiation Transmission Network Service",
				NetworkServiceType.NEGOTIATION_TRANSMISSION,
				PlatformComponentType.NETWORK_SERVICE,
				null));
		
		listPlatformComponentProfileToRegister.put(NetworkServiceType.TRANSACTION_TRANSMISSION, constructPlatformComponentProfileFactory(new ECCKeyPair().getPublicKey(),
				"Transaction Transmission Network Service".toLowerCase(),
				"Transaction Transmission Network Service",
				NetworkServiceType.TRANSACTION_TRANSMISSION,
				PlatformComponentType.NETWORK_SERVICE,
				null));
		
		listPlatformComponentProfileToRegister.put(NetworkServiceType.CRYPTO_BROKER, constructPlatformComponentProfileFactory(new ECCKeyPair().getPublicKey(),
				"Crypto Broker Actor Network Service".toLowerCase(),
				"Crypto Broker Actor Network Service",
				NetworkServiceType.CRYPTO_BROKER,
				PlatformComponentType.NETWORK_SERVICE,
				null));
		/*
		 * Actor of CRYPTO_BROKER
		 */
		listOtherComponentToRegister.put(NetworkServiceType.CRYPTO_BROKER, constructPlatformComponentProfileFactory(new ECCKeyPair().getPublicKey(),
				"ActorCryptoBroker".toLowerCase(),
				"Actor Crypto Broker",
				NetworkServiceType.UNDEFINED,
				PlatformComponentType.ACTOR_CRYPTO_BROKER,
				null));
		
		listPlatformComponentProfileToRegister.put(NetworkServiceType.CRYPTO_CUSTOMER, constructPlatformComponentProfileFactory(new ECCKeyPair().getPublicKey(),
				"Crypto Customer Actor Network Service".toLowerCase(),
				"Crypto Customer Actor Network Service",
				NetworkServiceType.CRYPTO_CUSTOMER,
				PlatformComponentType.NETWORK_SERVICE,
				null));
		/*
		 * Actor of CRYPTO_CUSTOMER
		 */
		listOtherComponentToRegister.put(NetworkServiceType.CRYPTO_CUSTOMER, constructPlatformComponentProfileFactory(new ECCKeyPair().getPublicKey(),
				"ActorCryptoCustomer".toLowerCase(),
				"Actor Crypto Customer",
				NetworkServiceType.UNDEFINED,
				PlatformComponentType.ACTOR_CRYPTO_CUSTOMER,
				null));	
		
		/* CBP */
		
		/* CCP */
		
		identity = new ECCKeyPair();
		intraActorNetworkServicePluginNS.setIdentity(identity);
		listPlatformComponentProfileToRegister.put(NetworkServiceType.INTRA_USER, constructPlatformComponentProfileFactory(identity.getPublicKey(),
				"Intra actor Network Service".toLowerCase(),
				"Intra actor Network Service",
				NetworkServiceType.INTRA_USER,
				PlatformComponentType.NETWORK_SERVICE,
				null));
		
		/*
		 * Actor of INTRA_USER
		 */
		
		listOtherComponentToRegister.put(NetworkServiceType.INTRA_USER, constructPlatformComponentProfileFactory(new ECCKeyPair().getPublicKey(),
				"ActorIntraUser".toLowerCase(),
				"Actor Intra User",
				NetworkServiceType.UNDEFINED,
				PlatformComponentType.ACTOR_INTRA_USER,
				null));	
		
		listPlatformComponentProfileToRegister.put(NetworkServiceType.CRYPTO_TRANSMISSION, constructPlatformComponentProfileFactory(new ECCKeyPair().getPublicKey(),
				"Crypto Transmission Network Service".toLowerCase(),
				"Crypto Transmission Network Service",
				NetworkServiceType.CRYPTO_TRANSMISSION,
				PlatformComponentType.NETWORK_SERVICE,
				null));
		
		listPlatformComponentProfileToRegister.put(NetworkServiceType.CRYPTO_PAYMENT_REQUEST, constructPlatformComponentProfileFactory(new ECCKeyPair().getPublicKey(),
				"Crypto Payment Request Network Service".toLowerCase(),
				"Crypto Payment Request Network Service",
				NetworkServiceType.CRYPTO_PAYMENT_REQUEST,
				PlatformComponentType.NETWORK_SERVICE,
				null));
		
		listPlatformComponentProfileToRegister.put(NetworkServiceType.CRYPTO_ADDRESSES, constructPlatformComponentProfileFactory(new ECCKeyPair().getPublicKey(),
				"Crypto Addresses Network Service".toLowerCase(),
				"Crypto Addresses Network Service",
				NetworkServiceType.CRYPTO_ADDRESSES,
				PlatformComponentType.NETWORK_SERVICE,
				null));
		
		/* CCP */
		
		/* CHT */
		
		listPlatformComponentProfileToRegister.put(NetworkServiceType.CHAT, constructPlatformComponentProfileFactory(new ECCKeyPair().getPublicKey(),
				"Network Service Chat".toLowerCase(),
				"Network Service Chat",
				NetworkServiceType.CHAT,
				PlatformComponentType.NETWORK_SERVICE,
				null));
		
		/* CHT */
		
		/* DAP */
		
		listPlatformComponentProfileToRegister.put(NetworkServiceType.ASSET_ISSUER_ACTOR, constructPlatformComponentProfileFactory(new ECCKeyPair().getPublicKey(),
				"Actor Network Service Asset Issuer".toLowerCase(),
				"Actor Network Service Asset Issuer",
				NetworkServiceType.ASSET_ISSUER_ACTOR,
				PlatformComponentType.NETWORK_SERVICE,
				null));
		
		/*
		 * Actor of ASSET_ISSUER_ACTOR
		 */
		listOtherComponentToRegister.put(NetworkServiceType.ASSET_ISSUER_ACTOR, constructPlatformComponentProfileFactory(new ECCKeyPair().getPublicKey(),
				"ActorAssetIssuer".toLowerCase(),
				"Actor Asset Issuer",
				NetworkServiceType.UNDEFINED,
				PlatformComponentType.ACTOR_ASSET_ISSUER,
				null));	
		
		listPlatformComponentProfileToRegister.put(NetworkServiceType.ASSET_USER_ACTOR,constructPlatformComponentProfileFactory(new ECCKeyPair().getPublicKey(),
				"Actor Network Service Asset User".toLowerCase(),
				"Actor Network Service Asset User",
				NetworkServiceType.ASSET_USER_ACTOR,
				PlatformComponentType.NETWORK_SERVICE,
				null));
		
		/*
		 * Actor of ASSET_USER_ACTOR
		 */
		listOtherComponentToRegister.put(NetworkServiceType.ASSET_USER_ACTOR, constructPlatformComponentProfileFactory(new ECCKeyPair().getPublicKey(),
				"ActorAssetUser".toLowerCase(),
				"Actor Asset User",
				NetworkServiceType.UNDEFINED,
				PlatformComponentType.ACTOR_ASSET_USER,
				null));	
		
		listPlatformComponentProfileToRegister.put(NetworkServiceType.ASSET_REDEEM_POINT_ACTOR,constructPlatformComponentProfileFactory(new ECCKeyPair().getPublicKey(),
				"Actor Network Service Asset RedeemPoint".toLowerCase(),
				"Actor Network Service Asset RedeemPoint",
				NetworkServiceType.ASSET_REDEEM_POINT_ACTOR,
				PlatformComponentType.NETWORK_SERVICE,
				null));
		
		/*
		 * Actor of ASSET_REDEEM_POINT_ACTOR
		 */
		listOtherComponentToRegister.put(NetworkServiceType.ASSET_REDEEM_POINT_ACTOR, constructPlatformComponentProfileFactory(new ECCKeyPair().getPublicKey(),
				"ActorAssetRedeemPoint".toLowerCase(),
				"Actor Asset Redeem Point",
				NetworkServiceType.UNDEFINED,
				PlatformComponentType.ACTOR_ASSET_REDEEM_POINT,
				null));	
		
		listPlatformComponentProfileToRegister.put(NetworkServiceType.ASSET_TRANSMISSION, constructPlatformComponentProfileFactory(new ECCKeyPair().getPublicKey(),
				"Network Service Asset Transmission".toLowerCase(),
				"Network Service Asset Transmission",
				NetworkServiceType.ASSET_TRANSMISSION,
				PlatformComponentType.NETWORK_SERVICE,
				null));
		
		/* DAP */
		
		
	}
	
	 public DiscoveryQueryParameters constructDiscoveryQueryParamsFactory(PlatformComponentType platformComponentType, NetworkServiceType networkServiceType, String alias, String identityPublicKey, Location location, Double distance, String name, String extraData, Integer offset, Integer max, PlatformComponentType fromOtherPlatformComponentType, NetworkServiceType fromOtherNetworkServiceType){

	        //Validate parameters
	        if (platformComponentType == null && networkServiceType == null){
	            throw new IllegalArgumentException("The platformComponentType and networkServiceType argument are required, can not be null ");
	        }

	        /*
	         * Construct a PlatformComponentProfile instance
	         */
	        return new DiscoveryQueryParametersCommunication(alias, identityPublicKey, location, distance, name, networkServiceType, platformComponentType, extraData, offset, max, fromOtherPlatformComponentType, fromOtherNetworkServiceType);

	}
	 
	 public List<PlatformComponentProfile> requestListComponentRegistered(DiscoveryQueryParameters discoveryQueryParameters) throws Exception {

	        System.out.println("WsCommunicationsCloudClientConnection - new requestListComponentRegistered");
	        List<PlatformComponentProfile> resultList = null;

	        /*
	         * Validate parameter
	         */
	        if (discoveryQueryParameters == null){
	            throw new IllegalArgumentException("The discoveryQueryParameters is required, can not be null");
	        }

	        try {
	        	
	        	resultList = new ArrayList<>();

	            /*
	             * Construct the parameters
	             */
	            MultiValueMap<String,Object> parameters = new LinkedMultiValueMap<>();
	            parameters.add(JsonAttNamesConstants.NAME_IDENTITY, wsCommunicationsTyrusCloudClientChannel.getIdentityPublicKey());
	            parameters.add(JsonAttNamesConstants.DISCOVERY_PARAM, discoveryQueryParameters.toJson());

	            // Create a new RestTemplate instance
	            RestTemplate restTemplate = new RestTemplate();
	            //String respond = restTemplate.postForObject("http://" + getServerIp() + ":" + getServerPort() + "/fermat/api/components/registered", parameters, String.class);
	            String respond = restTemplate.postForObject("http://" + getServerIp() + ":" + getServerPort() + "/fermat/components/registered", parameters, String.class);


	            /*
	             * if respond have the result list
	             */
	            if (respond.contains(JsonAttNamesConstants.RESULT_LIST)){

	                /*
	                 * Decode into a json object
	                 */
	                JsonParser parser = new JsonParser();
	                JsonObject respondJsonObject = (JsonObject) parser.parse(respond.toString());

	                 /*
	                 * Get the receivedList
	                 */
	                Gson gson = new Gson();
	                resultList = gson.fromJson(respondJsonObject.get(JsonAttNamesConstants.RESULT_LIST).getAsString(), new TypeToken<List<PlatformComponentProfileCommunication>>() {
	                }.getType());

	                System.out.println("WsCommunicationsCloudClientConnection - resultList.size() = " + resultList.size());

	            }else {
	                System.out.println("WsCommunicationsCloudClientConnection - Requested list is not available, resultList.size() = " + resultList.size());
	            }

	        }catch (Exception e){
	            e.printStackTrace();

	        }

	        return resultList;
	    }
	
	public void setOtherComponentProfileToRegister(NetworkServiceType networkServiceType) throws CantRegisterComponentException{
		
		if(listOtherComponentToRegister.containsKey(networkServiceType))
			registerComponentForCommunication(networkServiceType, listOtherComponentToRegister.get(networkServiceType));
		
	}
	
	public void CloseConnection() throws IOException{
		
		if(wsCommunicationsTyrusCloudClientChannel != null && wsCommunicationsTyrusCloudClientChannel.getClientConnection() != null && wsCommunicationsTyrusCloudClientChannel.getClientConnection().isOpen())
			wsCommunicationsTyrusCloudClientChannel.getClientConnection().close(new CloseReason(CloseReason.CloseCodes.NORMAL_CLOSURE, "Close All Normally"));
	
		wsCommunicationTyrusVPNClientManagerAgent.closeAllVpnConnections();
	}
	
  	public WsCommunicationsTyrusCloudClientChannel getWsCommunicationsTyrusCloudClientChannel() {
  		
  		if(wsCommunicationsTyrusCloudClientChannel != null)
  			return wsCommunicationsTyrusCloudClientChannel;
  		else
  			return null;
  		
	}
  	
   /*
  	* receive the Actor Profile registered then send requestList and send a request connection to a Actor choosed of the list
  	*/
  	public void requestDiscoveryRequestVpnConnection(NetworkServiceType networkServiceTypeApplicant, PlatformComponentProfile platformComponentProfile) {
  		
  		
  		if(platformComponentProfile.getPlatformComponentType() == PlatformComponentType.ACTOR_INTRA_USER && !listOfRequestConnect.containsKey(networkServiceTypeApplicant)){
  			
  			List<PlatformComponentProfile> listCandidates = null;
  			

            /* This is for test and example of how to use
                    * Construct the filter
            */
            DiscoveryQueryParameters discoveryQueryParameters = constructDiscoveryQueryParamsFactory(
            		platformComponentProfile.getPlatformComponentType(), //PlatformComponentType you want to find
            		platformComponentProfile.getNetworkServiceType(),     //NetworkServiceType you want to find
                    null,                     // alias
                    null,                     // identityPublicKey
                    null,                     // location
                    null,                     // distance
                    null,                     // name
                    null,                     // extraData
                    null,                     // offset
                    null,                     // max
                    null,                     // fromOtherPlatformComponentType, when use this filter apply the identityPublicKey
                    null
            );// fromOtherNetworkServiceType,    when use this filter apply the identityPublicKey
            
            try {
            	listCandidates = requestListComponentRegistered(discoveryQueryParameters);
			} catch (Exception e) {
				e.printStackTrace();
			}
            
            
             if(listCandidates != null){
            	
            	/*
            	 *  get three Ramdom Candidate and then storage in the Map listOfRequestConnect
            	 *  and send one by one the DiscoveryRequestVpnConnection
            	 */
           		PlatformComponentProfile platformComponentProfileFirstCandidate  = null; 
            	PlatformComponentProfile platformComponentProfileSecondCandidate = null; 
            	PlatformComponentProfile platformComponentProfileThirdCandidate  = null; 
            	int n1 = 0, n2 = 0, n3 = 0;
            	
            	if(listCandidates.size() == 1){
            	
            		platformComponentProfileFirstCandidate  = listCandidates.get(0);
            	
            	}else if(listCandidates.size() > 1){
            		
            		n1 = new Random().nextInt(listCandidates.size() - 1);
            		platformComponentProfileFirstCandidate  = listCandidates.get(n1);
            		
            	}
            	
            	if(listCandidates.size() >= 2){
            		
            		while(true){
            			n2 = new Random().nextInt(listCandidates.size() - 1);
            			if(n2 != n1){
            				break;
            			}
            		}
            		
            		platformComponentProfileSecondCandidate = listCandidates.get(n2);
            		
            	}
            	
            	if(listCandidates.size() >= 3){  
            		
            		int candidatesAleatory = (listCandidates.size() == 3)? 3 : listCandidates.size() - 1;
            		
            		while(true){
            			n3 = new Random().nextInt(candidatesAleatory);
            			if(n3 != n1 && n3 != n2){
            				break;
            			}
            		}
            		
            		platformComponentProfileThirdCandidate  = listCandidates.get(n3);
            		
            	}
            	            	
            	List<PlatformComponentProfile> listCandidatesToStrorage = new ArrayList<PlatformComponentProfile>();
            	
            	if(platformComponentProfileFirstCandidate != null)
            		listCandidatesToStrorage.add(platformComponentProfileFirstCandidate);
            	
            	if(platformComponentProfileSecondCandidate != null)
            		listCandidatesToStrorage.add(platformComponentProfileSecondCandidate);
            	
            	if(platformComponentProfileThirdCandidate != null)
            		listCandidatesToStrorage.add(platformComponentProfileThirdCandidate);
            	
            	listOfRequestConnect.put(networkServiceTypeApplicant, listCandidatesToStrorage);
            	
            	
            	try {
            		
            		if(platformComponentProfileFirstCandidate != null)
            			requestDiscoveryVpnConnection(platformComponentProfile,
            					listPlatformComponentProfileRegisteredSuccess.get(networkServiceTypeApplicant),
            					platformComponentProfileFirstCandidate);
					
					if(platformComponentProfileSecondCandidate != null){
						requestDiscoveryVpnConnection(platformComponentProfile,
								listPlatformComponentProfileRegisteredSuccess.get(networkServiceTypeApplicant),
								platformComponentProfileSecondCandidate);
					}
					
					if(platformComponentProfileThirdCandidate != null){
						requestDiscoveryVpnConnection(platformComponentProfile,
								listPlatformComponentProfileRegisteredSuccess.get(networkServiceTypeApplicant),
								platformComponentProfileThirdCandidate);
					}
					
					
					
				} catch (CantEstablishConnectionException e) {
					e.printStackTrace();
				}
            	
            } 

  			
  		}
  		
  	}
  	
  	public void handleNewMessageReceived(NetworkServiceType networkServiceTypeApplicant, PlatformComponentProfile platformComponentProfileRemote, FermatMessage fermatMessage){
  		
		switch(networkServiceTypeApplicant){
		
			case INTRA_USER:
				
				intraActorNetworkServicePluginNS.sethandleNewMessageReceived(platformComponentProfileRemote,fermatMessage);
				
				break;
				
			default:
				break;
		
		
		}
  		
  	}
  	
  	
}
