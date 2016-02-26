/*
 * @#PlatformComponentProfileCommunication.java - 2016
 * Copyright bitDubai.com., All rights reserved.
 * You may not modify, use, reproduce or distribute this software.
 * BITDUBAI/CONFIDENTIAL
 */
package org.fermat.p2p.server.app.stress.structure.commons.components;


import com.google.gson.Gson;

import java.io.Serializable;
import java.util.Objects;

import org.fermat.p2p.server.app.stress.structure.enums.NetworkServiceType;
import org.fermat.p2p.server.app.stress.structure.enums.PlatformComponentType;
import org.fermat.p2p.server.app.stress.structure.interfaces.Location;
import org.fermat.p2p.server.app.stress.structure.interfaces.PlatformComponentProfile;


public class PlatformComponentProfileCommunication implements PlatformComponentProfile, Serializable{
	

    /**
     * Represent the serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * Represent the identityPublicKey
     */
    private String identityPublicKey;

    /**
     * Represent the alias
     */
    private String alias;

    private String phrase;

    /**
     * Represent the name
     */
    private String name;

    /**
     * Represent the location
     */
    private DeviceLocation location;

    /**
     * Represent the platformComponentType
     */
    private PlatformComponentType platformComponentType;

    /**
     * Represent the networkServiceType
     */
    private NetworkServiceType networkServiceType;

    /**
     * Represent the communicationCloudClientIdentity
     */
    private String communicationCloudClientIdentity;

    /**
     * Represent the extraData
     */
    private String extraData;

    /**
     * Constructor
     */
    public PlatformComponentProfileCommunication() {
        super();
        this.alias = null;
        this.communicationCloudClientIdentity = null;
        this.identityPublicKey = null;
        this.location = null;
        this.name = null;
        this.networkServiceType = null;
        this.platformComponentType = null;
        this.extraData = null;
    }

    /**
     * Constructor whit parameters
     *
     * @param alias
     * @param communicationCloudClientIdentity
     * @param identityPublicKey
     * @param name
     * @param networkServiceType
     * @param platformComponentType
     */
    public PlatformComponentProfileCommunication(String alias, String communicationCloudClientIdentity, String identityPublicKey, Location location, String name, NetworkServiceType networkServiceType, PlatformComponentType platformComponentType, String extraData) {
        super();
        this.alias = alias;
        this.communicationCloudClientIdentity = communicationCloudClientIdentity;
        this.identityPublicKey = identityPublicKey;
        this.location = (DeviceLocation) location;
        this.name = name;
        this.networkServiceType = networkServiceType;
        this.platformComponentType = platformComponentType;
        this.extraData = extraData;
    }

    /**
     * (non-javadoc)
     * @see PlatformComponentProfile#getIdentityPublicKey()
     */
    @Override
    public String getIdentityPublicKey() {
        return identityPublicKey;
    }

    /**
     * Set the IdentityPublicKey
     * @param identityPublicKey
     */
    public void setIdentityPublicKey(String identityPublicKey) {
        this.identityPublicKey = identityPublicKey;
    }

    /**
     * (non-javadoc)
     * @see PlatformComponentProfile#getAlias()
     */
    @Override
    public String getAlias() {
        return alias;
    }


    /**
     * Set the Alias
     * @param alias
     */
    public void setAlias(String alias) {
        this.alias = alias;
    }

    /**
     * (non-javadoc)
     * @see PlatformComponentProfile#getName()
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Set the Name
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * (non-javadoc)
     * @see PlatformComponentProfile#getLocation()
     */
    @Override
    public Location getLocation() {
        return location;
    }

    /**
     * Set the Location
     * @param location
     */
    public void setLocation(DeviceLocation location) {
        this.location = location;
    }

    /**
     * (non-javadoc)
     * @see PlatformComponentProfile#getPlatformComponentType()
     */
    @Override
    public PlatformComponentType getPlatformComponentType() {
        return platformComponentType;
    }

    /**
     * Set the PlatformComponentType
     * @param platformComponentType
     */
    public void setPlatformComponentType(PlatformComponentType platformComponentType) {
        this.platformComponentType = platformComponentType;
    }

    /**
     * (non-javadoc)
     * @see PlatformComponentProfile#getNetworkServiceType()
     */
    @Override
    public NetworkServiceType getNetworkServiceType() {
        return networkServiceType;
    }

    /**
     * Set the NetworkServiceType
     * @param networkServiceType
     */
    public void setNetworkServiceType(NetworkServiceType networkServiceType) {
        this.networkServiceType = networkServiceType;
    }

    /**
     * (non-javadoc)
     * @see PlatformComponentProfile#getCommunicationCloudClientIdentity()
     */
    @Override
    public String getCommunicationCloudClientIdentity() {
        return communicationCloudClientIdentity;
    }

    /**
     * (non-javadoc)
     * @see PlatformComponentProfile#getExtraData()
     */
    @Override
    public String getExtraData() {
        return extraData;
    }

    /**
     * Set the ExtraData
     * @param extraData
     */
    public void setExtraData(String extraData) {
        this.extraData = extraData;
    }

    /**
     * (non-javadoc)
     * @see PlatformComponentProfile#toJson()
     */
    @Override
    public String toJson() {

        Gson gson = new Gson();
        return gson.toJson(this);
    }

    /**
     * (non-javadoc)
     * @see PlatformComponentProfile#fromJson(String)
     */
    @Override
    public PlatformComponentProfile fromJson(String json) {

        Gson gson = new Gson();
        return gson.fromJson(json, PlatformComponentProfileCommunication.class);
    }

    /**
     * (non-javadoc)
     * @see Object#equals(Object)
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PlatformComponentProfileCommunication)) return false;
        PlatformComponentProfileCommunication that = (PlatformComponentProfileCommunication) o;
        return Objects.equals(getIdentityPublicKey(), that.getIdentityPublicKey()) &&
                Objects.equals(getAlias(), that.getAlias()) &&
                Objects.equals(getName(), that.getName()) &&
                Objects.equals(getLocation(), that.getLocation()) &&
                Objects.equals(getPlatformComponentType(), that.getPlatformComponentType()) &&
                Objects.equals(getNetworkServiceType(), that.getNetworkServiceType()) &&
                Objects.equals(getCommunicationCloudClientIdentity(), that.getCommunicationCloudClientIdentity()) &&
                Objects.equals(getExtraData(), that.getExtraData());
    }

    /**
     * (non-javadoc)
     * @see Object#hashCode()
     */
    @Override
    public int hashCode() {
        return Objects.hash(getIdentityPublicKey(), getAlias(), getName(), getLocation(), getPlatformComponentType(), getNetworkServiceType(), getCommunicationCloudClientIdentity(), getExtraData());
    }

    /**
     * (non-javadoc)
     * @see Object#toString()
     */
    @Override
    public String toString() {
        return "PlatformComponentProfileCommunication{" +
                "alias='" + alias + '\'' +
                ", identityPublicKey='" + identityPublicKey + '\'' +
                ", name='" + name + '\'' +
                ", location =" + location +
                ", platformComponentType=" + platformComponentType +
                ", networkServiceType=" + networkServiceType +
                ", communicationCloudClientIdentity='" + communicationCloudClientIdentity + '\'' +
                ", extraData = "+extraData +
                '}';
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

}
