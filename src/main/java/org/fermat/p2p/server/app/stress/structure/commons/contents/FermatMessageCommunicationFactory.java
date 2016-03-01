package org.fermat.p2p.server.app.stress.structure.commons.contents;


import java.sql.Timestamp;

import org.fermat.p2p.server.app.stress.structure.crypto.asymmetric.AsymmetricCryptography;
import org.fermat.p2p.server.app.stress.structure.crypto.asymmetric.ECCKeyPair;
import org.fermat.p2p.server.app.stress.structure.enums.FermatMessageContentType;
import org.fermat.p2p.server.app.stress.structure.enums.FermatMessagesStatus;
import org.fermat.p2p.server.app.stress.structure.exceptions.FMPException;
import org.fermat.p2p.server.app.stress.structure.exceptions.MalformedFMPPacketException;
import org.fermat.p2p.server.app.stress.structure.interfaces.FermatMessage;


public class FermatMessageCommunicationFactory {
	
	/**
     * Construct a FermatMessage encrypted with the destination identity public key and signed
     * whit the private key passed as an argument
     *
     * @param senderIdentity
     * @param receiverIdentityPublicKey
     * @param content
     * @param fermatMessageContentType
     * @return FermatMessage
     * @throws FMPException
     */
    public static FermatMessage constructFermatMessageEncryptedAndSinged(final ECCKeyPair senderIdentity, final String receiverIdentityPublicKey, final String content, final FermatMessageContentType fermatMessageContentType) throws FMPException{

        String messageHash = AsymmetricCryptography.encryptMessagePublicKey(content, receiverIdentityPublicKey);
        String signature   = AsymmetricCryptography.createMessageSignature(messageHash, senderIdentity.getPrivateKey());

        return new FermatMessageCommunication(messageHash, null, fermatMessageContentType, FermatMessagesStatus.PENDING_TO_SEND, receiverIdentityPublicKey, senderIdentity.getPublicKey(), new Timestamp(System.currentTimeMillis()), signature);
    }


    /**
     * Construct a FermatMessage with parameters
     *
     * @param senderIdentityPublicKey
     * @param receiverIdentityPublicKey
     * @param content
     * @param fermatMessageContentType
     * @return FermatMessage
     * @throws FMPException
     */
    public static FermatMessage constructFermatMessage(final String senderIdentityPublicKey, final String receiverIdentityPublicKey, final String content, final FermatMessageContentType fermatMessageContentType) throws FMPException{

        return new FermatMessageCommunication(content, null, fermatMessageContentType, FermatMessagesStatus.PENDING_TO_SEND, receiverIdentityPublicKey, senderIdentityPublicKey, new Timestamp(System.currentTimeMillis()), null);
    }


    /**
     * Construct a FermatMessageCommunication from a json string
     *
     * @param jsonMessageData
     * @return FermatPacketCommunication
     * @throws FMPException
     */
	public static FermatMessage constructFermatMessageFromJsonString(String jsonMessageData) throws FMPException {

        try {

            /*
             * Validate the data
             */
            validatePacketDataString(jsonMessageData);

            /**
             * Create a temporal object
             */
            FermatMessageCommunication temp = new FermatMessageCommunication();

            /*
             * Convert to the object
             */
            return temp.fromJson(jsonMessageData);

        }catch (Exception exception){

            throw new MalformedFMPPacketException(MalformedFMPPacketException.DEFAULT_MESSAGE, exception, null, "The message data is not properly assembled");
        }

	}

    /**
     * Validate the json string data
     *
     * @param jsomPacketData
     * @throws FMPException
     */
    private static void validatePacketDataString(final String jsomPacketData) throws FMPException {

        if(jsomPacketData == null){
            throw new MalformedFMPPacketException(MalformedFMPPacketException.DEFAULT_MESSAGE, null, "", "The packet data is null");
        }

        if(jsomPacketData.isEmpty()) {
            throw new MalformedFMPPacketException(MalformedFMPPacketException.DEFAULT_MESSAGE, null, "", "The packet data is empty");
        }
    }

}
