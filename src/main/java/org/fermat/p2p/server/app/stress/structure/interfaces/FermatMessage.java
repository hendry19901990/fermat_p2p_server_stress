package org.fermat.p2p.server.app.stress.structure.interfaces;

import org.fermat.p2p.server.app.stress.structure.enums.FermatMessageContentType;
import org.fermat.p2p.server.app.stress.structure.enums.FermatMessagesStatus;

import java.sql.Timestamp;
import java.util.UUID;

public interface FermatMessage {
	
	 /**
     * Get the id of the package
     */
    public UUID getId();

    /**
     * Get the sender of the packet
     *
     * @return String
     */
    public String getSender();

    /**
     * Get the receiver of the packet
     *
     * @return String
     */
    public String getReceiver();

    /**
     * Get the Content
     *
     * @return String
     */
    public String getContent();

    /**
     * Get the delivery timestamp
     *
     * @return Timestamp
     */
    public Timestamp getDeliveryTimestamp();

    /**
     * Get the Shipping Timestamp
     *
     * @return Timestamp
     */
    public Timestamp getShippingTimestamp();

    /**
     * Get the FailCount, this value indicate
     * when try to send a message an fail
     *
     * Note: This is only for outgoing message
     *
     * @return
     */
    public int getFailCount();

    /**
     * Get the FermatMessagesStatus
     *
     * @return FermatMessagesStatus
     */
    public FermatMessagesStatus getFermatMessagesStatus();

    /**
     * Get the signature of the packet
     *
     * @return String
     */
    public String getSignature();

    /**
     * Get the Fermat Message Content Type
     *
     * @return NetworkServiceType
     */
    public FermatMessageContentType getFermatMessageContentType();

    /**
     * Convert this object to json string
     *
     * @return String json
     */
    public String toJson();

    /**
     * Convert to FermatPacketCommunication from json
     *
     * @param json string object
     * @return FermatMessage
     */
    public FermatMessage fromJson(String json);

}
