/*
 * @#FermatMessageCommunication.java - 2016
 * Copyright bitDubai.com., All rights reserved.
 * You may not modify, use, reproduce or distribute this software.
 * BITDUBAI/CONFIDENTIAL
 */
package org.fermat.p2p.server.app.stress.structure.commons.contents;


import com.google.gson.Gson;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;
import java.util.UUID;
import org.fermat.p2p.server.app.stress.structure.enums.FermatMessageContentType;
import org.fermat.p2p.server.app.stress.structure.enums.FermatMessagesStatus;
import org.fermat.p2p.server.app.stress.structure.interfaces.FermatMessage;


public class FermatMessageCommunication implements FermatMessage, Serializable{
	
	/**
     * Represent the serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * Represent the id of the message
     */
    private UUID id;

    /**
     * Represent the sender of the message
     */
    private String sender;

    /**
     * Represent the receiver of the message
     */
    private String receiver;

    /**
     * Represent the content
     */
    private String content;

    /**
     * Represent the shipping timestamp of the message
     */
    private Timestamp shippingTimestamp;

    /**
     * Represent the delivery timestamp of the message
     */
    private Timestamp deliveryTimestamp;

    /**
     * Represent the failCount
     */
    private int failCount = 0;

    /**
     * Represent the status
     */
    private FermatMessagesStatus fermatMessagesStatus;

    /**
     * Represent the signature
     */
    private String signature;

    /**
     * Represent the fermatMessageContentType
     */
    private FermatMessageContentType fermatMessageContentType;

    /**
     * Constructor
     */
    public FermatMessageCommunication() {
       super();
       this.id = UUID.randomUUID();
       this.failCount = new Integer(0);
    }

    /**
     * Constructor with parameters
     *
     * @param content
     * @param deliveryTimestamp
     * @param fermatMessageContentType
     * @param fermatMessagesStatus
     * @param receiver
     * @param sender
     * @param shippingTimestamp
     * @param signature
     */
    public FermatMessageCommunication(String content, Timestamp deliveryTimestamp, FermatMessageContentType fermatMessageContentType, FermatMessagesStatus fermatMessagesStatus, String receiver, String sender, Timestamp shippingTimestamp, String signature) {

        this.id = UUID.randomUUID();
        this.content = content;
        this.deliveryTimestamp = deliveryTimestamp;
        this.fermatMessageContentType = fermatMessageContentType;
        this.fermatMessagesStatus = fermatMessagesStatus;
        this.receiver = receiver;
        this.sender = sender;
        this.shippingTimestamp = shippingTimestamp;
        this.signature = signature;
        this.failCount = new Integer(0);
    }

    /**
     * (no-javadoc)
     * @see FermatMessage#getContent()
     */
    @Override
    public String getContent(){
        return content;
    }

    /**
     * Set the content
     *
     * @param content
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * (no-javadoc)
     * @see FermatMessage#getDeliveryTimestamp()
     */
    @Override
    public Timestamp getDeliveryTimestamp() {
        return deliveryTimestamp;
    }

    /**
     * Set the DeliveryTimestamp
     *
     * @param deliveryTimestamp
     */
    public void setDeliveryTimestamp(Timestamp deliveryTimestamp) {
        this.deliveryTimestamp = deliveryTimestamp;
    }

    /**
     * (no-javadoc)
     * @see FermatMessage#getFermatMessageContentType()
     */
    @Override
    public FermatMessageContentType getFermatMessageContentType() {
        return fermatMessageContentType;
    }

    /**
     * Set the FermatMessageContentType
     * @param fermatMessageContentType
     */
    public void setFermatMessageContentType(FermatMessageContentType fermatMessageContentType) {
        this.fermatMessageContentType = fermatMessageContentType;
    }

    /**
     * (no-javadoc)
     * @see FermatMessage#getFermatMessagesStatus()
     */
    public FermatMessagesStatus getFermatMessagesStatus() {
        return fermatMessagesStatus;
    }

    /**
     * Set the FermatMessagesStatus
     * @param fermatMessagesStatus
     */
    public void setFermatMessagesStatus(FermatMessagesStatus fermatMessagesStatus) {
        this.fermatMessagesStatus = fermatMessagesStatus;
    }

    /**
     * (no-javadoc)
     * @see FermatMessage#getId()
     */
    @Override
    public UUID getId() {
        return id;
    }

    /**
     * Set the Id
     * @param id
     */
    public void setId(UUID id) {
        this.id = id;
    }

    /**
     * (no-javadoc)
     * @see FermatMessage#getReceiver()
     */
    @Override
    public String getReceiver() {
        return receiver;
    }

    /**
     * Set the Receiver
     * @param receiver
     */
    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    /**
     * (no-javadoc)
     * @see FermatMessage#getSender()
     */
    @Override
    public String getSender() {
        return sender;
    }

    /**
     * Set the Sender
     * @param sender
     */
    public void setSender(String sender) {
        this.sender = sender;
    }

    /**
     * (no-javadoc)
     * @see FermatMessage#getShippingTimestamp()
     */
    public Timestamp getShippingTimestamp() {
        return shippingTimestamp;
    }

    /**
     * Set the ShippingTimestamp
     * @param shippingTimestamp
     */
    public void setShippingTimestamp(Timestamp shippingTimestamp) {
        this.shippingTimestamp = shippingTimestamp;
    }

    /**
     * (no-javadoc)
     * @see FermatMessage#getFailCount()
     */
    public int getFailCount() {
        return failCount;
    }

    /**
     * Set the failCount
     * @param failCount
     */
    public void setFailCount(Integer failCount) {
        this.failCount = failCount;
    }

    /**
     * (no-javadoc)
     * @see FermatMessage#getSignature()
     */
    @Override
    public String getSignature() {
        return signature;
    }

    /**
     * Set the signature
     * @param signature
     */
    public void setSignature(String signature) {
        this.signature = signature;
    }

    /**
     * (no-javadoc)
     * @see FermatMessage#toJson()
     */
    @Override
    public String toJson() {

        Gson gson = new Gson();
        return gson.toJson(this);
    }

    /**
     * (no-javadoc)
     * @see FermatMessage#fromJson(String)
     */
    @Override
    public FermatMessage fromJson(String json) {

        Gson gson = new Gson();
        return gson.fromJson(json, FermatMessageCommunication.class);
    }

    /**
     * (no-javadoc)
     * @see Object#equals(Object)
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FermatMessageCommunication)) return false;
        FermatMessageCommunication that = (FermatMessageCommunication) o;
        return Objects.equals(getId(), that.getId());
    }

    /**
     * (no-javadoc)
     * @see Object#hashCode()
     */
    @Override
    public int hashCode() {
        return Objects.hash(getId(), getSender(), getReceiver(), getContent(), getShippingTimestamp(), getDeliveryTimestamp(), getFermatMessagesStatus(), getSignature(), getFermatMessageContentType());
    }

    /**
     * (no-javadoc)
     * @see Object#toString()
     */
    @Override
    public String toString() {
        return "FermatMessageCommunication{" +
                "content=" + content +
                ", id=" + id +
                ", sender='" + sender + '\'' +
                ", receiver='" + receiver + '\'' +
                ", shippingTimestamp=" + shippingTimestamp +
                ", deliveryTimestamp=" + deliveryTimestamp +
                ", fermatMessagesStatus=" + fermatMessagesStatus +
                ", signature='" + signature + '\'' +
                ", failCount='" + failCount + '\'' +
                ", fermatMessageContentType=" + fermatMessageContentType +
                '}';
    }
	

}
