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
import java.util.concurrent.TimeUnit;

import javax.websocket.CloseReason;
import javax.websocket.DeploymentException;

import org.fermat.p2p.server.app.stress.structure.commons.components.DeviceLocation;
import org.fermat.p2p.server.app.stress.structure.commons.components.PlatformComponentProfileCommunication;
import org.fermat.p2p.server.app.stress.structure.commons.contents.FermatPacketCommunicationFactory;
import org.fermat.p2p.server.app.stress.structure.commons.contents.FermatPacketEncoder;
import org.fermat.p2p.server.app.stress.structure.crypto.asymmetric.ECCKeyPair;
import org.fermat.p2p.server.app.stress.structure.enums.FermatPacketType;
import org.fermat.p2p.server.app.stress.structure.enums.JsonAttNamesConstants;
import org.fermat.p2p.server.app.stress.structure.enums.NetworkServiceType;
import org.fermat.p2p.server.app.stress.structure.enums.PlatformComponentType;
import org.fermat.p2p.server.app.stress.structure.exceptions.CantRegisterComponentException;
import org.fermat.p2p.server.app.stress.structure.interfaces.FermatPacket;
import org.fermat.p2p.server.app.stress.structure.interfaces.Location;
import org.fermat.p2p.server.app.stress.structure.interfaces.PlatformComponentProfile;
import org.fermat.p2p.server.app.stress.structure.processors.CompleteRegistrationComponentTyrusPacketProcessor;
import org.fermat.p2p.server.app.stress.structure.processors.FailureComponentRegistrationRequestTyrusPacketProcessor;
import org.fermat.p2p.server.app.stress.structure.processors.ServerHandshakeRespondTyrusPacketProcessor;
import org.glassfish.tyrus.client.ClientManager;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

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
    
    private List<PlatformComponentProfile> listPlatformComponentProfileToRegister;
    private List<PlatformComponentProfile> listPlatformComponentProfileRegisteredSuccess;
    
    private Map<NetworkServiceType,PlatformComponentProfile> listOtherComponentToRegister;
    private Map<NetworkServiceType,PlatformComponentProfile> listOtherComponentToRegisteredSuccess;
    
	/*
     * Represent the location of an device
     */
    private static  Location location = new DeviceLocation();
    
    private ECCKeyPair clientIdentity;
    
    /**
     * Constructor whit parameters
     *
     * @param uri
     * @param eventManager
     */
    public WsCommunicationsTyrusCloudClientConnection(URI uri,  ECCKeyPair clientIdentity) throws IOException, DeploymentException {
        super();
        this.uri = uri;
        this.clientIdentity = clientIdentity;
        this.wsCommunicationsTyrusCloudClientChannel = new WsCommunicationsTyrusCloudClientChannel(this,  this.clientIdentity);
        this.webSocketContainer = ClientManager.createClient();
        listPlatformComponentProfileToRegister = new ArrayList<PlatformComponentProfile>();
        listPlatformComponentProfileRegisteredSuccess = new ArrayList<PlatformComponentProfile>();
        listOtherComponentToRegister = new HashMap<NetworkServiceType,PlatformComponentProfile>();
        listOtherComponentToRegisteredSuccess = new HashMap<NetworkServiceType,PlatformComponentProfile>();
    }
    
    public void initializeAndConnect() throws IOException, DeploymentException {
    	
        /*
         * Register the processors
         */
        registerFermatPacketProcessors();
        
        webSocketContainer = ClientManager.createClient();
        
        /*
         * Connect
         */
        webSocketContainer.connectToServer(wsCommunicationsTyrusCloudClientChannel, uri);
        
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
	
    public List<PlatformComponentProfile> getPlatformComponentProfileToRegister() {
		return listPlatformComponentProfileToRegister;
	}

	public List<PlatformComponentProfile> getPlatformComponentProfileRegisteredSuccess() {
		return listPlatformComponentProfileRegisteredSuccess;
	}
	
	public  Map<NetworkServiceType, PlatformComponentProfile> getListOtherComponentToRegister() {
		return listOtherComponentToRegister;
	}
	
	public Map<NetworkServiceType, PlatformComponentProfile> getListOtherComponentToRegisteredSuccess() {
		return listOtherComponentToRegisteredSuccess;
	}
	
	public Integer getTotalProfileToRegister(){
		return listPlatformComponentProfileToRegister.size() + listOtherComponentToRegister.size();
	}
	
	public Integer getTotalProfileRegisteredSuccess(){
		return listPlatformComponentProfileRegisteredSuccess.size() + listOtherComponentToRegisteredSuccess.size();
	}
	
	public void sendAllListPlatformComponentProfileToRegister() throws CantRegisterComponentException{
		
		/*
		 * set All PlatformComponentProfile to Register
		 */
		setLoaderListPlatformComponentProfileToRegister();
		
		for(PlatformComponentProfile pToRegister : listPlatformComponentProfileToRegister){
			
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
		
		/*
         * Clean all
         */
		listPlatformComponentProfileToRegister.clear();
		
		/*
		 *  set first the Cloud Client 
		 */
		/* P2P */
		listPlatformComponentProfileToRegister.add(wsCommunicationsTyrusCloudClientChannel.getPlatformComponentProfile());
		/* P2P */
		
		/* CBP */
		listPlatformComponentProfileToRegister.add(constructPlatformComponentProfileFactory(new ECCKeyPair().getPublicKey(),
				"Negotiation Transmission Network Service".toLowerCase(),
				"Negotiation Transmission Network Service",
				NetworkServiceType.NEGOTIATION_TRANSMISSION,
				PlatformComponentType.NETWORK_SERVICE,
				null));
		
		listPlatformComponentProfileToRegister.add(constructPlatformComponentProfileFactory(new ECCKeyPair().getPublicKey(),
				"Transaction Transmission Network Service".toLowerCase(),
				"Transaction Transmission Network Service",
				NetworkServiceType.TRANSACTION_TRANSMISSION,
				PlatformComponentType.NETWORK_SERVICE,
				null));
		
		listPlatformComponentProfileToRegister.add(constructPlatformComponentProfileFactory(new ECCKeyPair().getPublicKey(),
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
		
		listPlatformComponentProfileToRegister.add(constructPlatformComponentProfileFactory(new ECCKeyPair().getPublicKey(),
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
		
		listPlatformComponentProfileToRegister.add(constructPlatformComponentProfileFactory(new ECCKeyPair().getPublicKey(),
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
		
		listPlatformComponentProfileToRegister.add(constructPlatformComponentProfileFactory(new ECCKeyPair().getPublicKey(),
				"Crypto Transmission Network Service".toLowerCase(),
				"Crypto Transmission Network Service",
				NetworkServiceType.CRYPTO_TRANSMISSION,
				PlatformComponentType.NETWORK_SERVICE,
				null));
		
		listPlatformComponentProfileToRegister.add(constructPlatformComponentProfileFactory(new ECCKeyPair().getPublicKey(),
				"Crypto Payment Request Network Service".toLowerCase(),
				"Crypto Payment Request Network Service",
				NetworkServiceType.CRYPTO_PAYMENT_REQUEST,
				PlatformComponentType.NETWORK_SERVICE,
				null));
		
		listPlatformComponentProfileToRegister.add(constructPlatformComponentProfileFactory(new ECCKeyPair().getPublicKey(),
				"Crypto Addresses Network Service".toLowerCase(),
				"Crypto Addresses Network Service",
				NetworkServiceType.CRYPTO_ADDRESSES,
				PlatformComponentType.NETWORK_SERVICE,
				null));
		
		/* CCP */
		
		/* CHT */
		
		listPlatformComponentProfileToRegister.add(constructPlatformComponentProfileFactory(new ECCKeyPair().getPublicKey(),
				"Network Service Chat".toLowerCase(),
				"Network Service Chat",
				NetworkServiceType.CHAT,
				PlatformComponentType.NETWORK_SERVICE,
				null));
		
		/* CHT */
		
		/* DAP */
		
		listPlatformComponentProfileToRegister.add(constructPlatformComponentProfileFactory(new ECCKeyPair().getPublicKey(),
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
		
		listPlatformComponentProfileToRegister.add(constructPlatformComponentProfileFactory(new ECCKeyPair().getPublicKey(),
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
		
		listPlatformComponentProfileToRegister.add(constructPlatformComponentProfileFactory(new ECCKeyPair().getPublicKey(),
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
		
		listPlatformComponentProfileToRegister.add(constructPlatformComponentProfileFactory(new ECCKeyPair().getPublicKey(),
				"Network Service Asset Transmission".toLowerCase(),
				"Network Service Asset Transmission",
				NetworkServiceType.ASSET_TRANSMISSION,
				PlatformComponentType.NETWORK_SERVICE,
				null));
		
		/* DAP */
		
		
	}
	
	public void setOtherComponentProfileToRegister(NetworkServiceType networkServiceType) throws CantRegisterComponentException{
		
		if(listOtherComponentToRegister.containsKey(networkServiceType))
			registerComponentForCommunication(networkServiceType, listOtherComponentToRegister.get(networkServiceType));
		
	}
	
	public void CloseConnection() throws IOException{
		
		if(wsCommunicationsTyrusCloudClientChannel != null && wsCommunicationsTyrusCloudClientChannel.getClientConnection().isOpen())
			wsCommunicationsTyrusCloudClientChannel.getClientConnection().close(new CloseReason(CloseReason.CloseCodes.NORMAL_CLOSURE, "Close All Normally"));
	
	}
	
	
}
