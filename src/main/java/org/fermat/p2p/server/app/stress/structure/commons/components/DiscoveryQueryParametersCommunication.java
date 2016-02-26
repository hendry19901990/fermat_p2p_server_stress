/*
 * @#DiscoveryQueryParametersCommunication.java - 2016
 * Copyright bitDubai.com., All rights reserved.
 * You may not modify, use, reproduce or distribute this software.
 * BITDUBAI/CONFIDENTIAL
 */
package org.fermat.p2p.server.app.stress.structure.commons.components;

import java.io.Serializable;
import com.google.gson.Gson;
import java.util.Objects;

import org.fermat.p2p.server.app.stress.structure.enums.NetworkServiceType;
import org.fermat.p2p.server.app.stress.structure.enums.PlatformComponentType;
import org.fermat.p2p.server.app.stress.structure.interfaces.DiscoveryQueryParameters;
import org.fermat.p2p.server.app.stress.structure.interfaces.Location;

public class DiscoveryQueryParametersCommunication implements DiscoveryQueryParameters, Serializable{
	

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

    /**
     * Represent the name
     */
    private String name;

    /**
     * Represent the location
     */
    private DeviceLocation location;

    /**
     * Represent the distance
     */
    private Double distance;

    /**
     * Represent the platformComponentType
     */
    private PlatformComponentType platformComponentType;

    /**
     * Represent the networkServiceType
     */
    private NetworkServiceType networkServiceType;

    /**
     * Represent the extraData
     */
    private String extraData;

    /**
     * Represent the offset
     */
    private Integer offset;

    /**
     * Represent the max
     */
    private Integer max;

    /**
     * Represent the fromOtherPlatformComponentType
     */
    private PlatformComponentType fromOtherPlatformComponentType;

    /**
     * Represent the fromOtherNetworkServiceType
     */
    private NetworkServiceType fromOtherNetworkServiceType;


    /**
     * Constructor
     */
    public DiscoveryQueryParametersCommunication() {
        super();
        this.alias = null;
        this.identityPublicKey = null;
        this.location = null;
        this.distance = null;
        this.name = null;
        this.networkServiceType = null;
        this.platformComponentType = null;
        this.extraData = null;
        this.offset = new Integer(0);
        this.max = new Integer(0);
        this.fromOtherPlatformComponentType = null;
        this.fromOtherNetworkServiceType = null;
    }

    /**
     * Constructor with parameters
     *
     * @param alias
     * @param identityPublicKey
     * @param location
     * @param distance
     * @param name
     * @param networkServiceType
     * @param platformComponentType
     * @param extraData
     * @param offset
     * @param max
     * @param fromOtherPlatformComponentType
     * @param fromOtherNetworkServiceType
     */
    public DiscoveryQueryParametersCommunication(String alias, String identityPublicKey, Location location, Double distance, String name, NetworkServiceType networkServiceType, PlatformComponentType platformComponentType, String extraData, Integer offset, Integer max, PlatformComponentType fromOtherPlatformComponentType, NetworkServiceType fromOtherNetworkServiceType) {
        super();
        this.alias = alias;
        this.identityPublicKey = identityPublicKey;
        this.location = (DeviceLocation) location;
        this.distance = distance;
        this.name = name;
        this.networkServiceType = networkServiceType;
        this.platformComponentType = platformComponentType;
        this.extraData = extraData;
        this.offset = offset;
        this.max = max;
        this.fromOtherPlatformComponentType = fromOtherPlatformComponentType;
        this.fromOtherNetworkServiceType = fromOtherNetworkServiceType;
    }

    /**
     * (non-javadoc)
     * @see DiscoveryQueryParameters#getIdentityPublicKey()
     */
    @Override
    public String getIdentityPublicKey() {
        return identityPublicKey;
    }

    /**
     * (non-javadoc)
     * @see DiscoveryQueryParameters#getAlias()
     */
    @Override
    public String getAlias() {
        return alias;
    }


     /**
     * (non-javadoc)
     * @see DiscoveryQueryParameters#getName()
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * (non-javadoc)
     * @see DiscoveryQueryParameters#getLocation()
     */
    @Override
    public Location getLocation() {
        return location;
    }

    /**
     * (non-javadoc)
     * @see DiscoveryQueryParameters#getDistance()
     */
    @Override
    public Double getDistance() {
        return distance;
    }

    /**
     * (non-javadoc)
     * @see DiscoveryQueryParameters#getPlatformComponentType()
     */
    @Override
    public PlatformComponentType getPlatformComponentType() {
        return platformComponentType;
    }

    /**
     * (non-javadoc)
     * @see DiscoveryQueryParameters#getNetworkServiceType()
     */
    @Override
    public NetworkServiceType getNetworkServiceType() {
        return networkServiceType;
    }

    /**
     * (non-javadoc)
     * @see DiscoveryQueryParameters#getExtraData()
     */
    @Override
    public String getExtraData() {
        return extraData;
    }

    /**
     * (non-javadoc)
     * @see DiscoveryQueryParameters#getOffset()
     */
    public Integer getOffset(){
        return offset;
    }

    /**
     * (non-javadoc)
     * @see DiscoveryQueryParameters#getMax()
     */
    public Integer getMax(){
        return max;
    }

    /**
     * (non-javadoc)
     * @see DiscoveryQueryParameters#getFromOtherPlatformComponentType()
     */
    @Override
    public PlatformComponentType getFromOtherPlatformComponentType() {
        return fromOtherPlatformComponentType;
    }

    /**
     * (non-javadoc)
     * @see DiscoveryQueryParameters#getFromOtherNetworkServiceType()
     */
    @Override
    public NetworkServiceType getFromOtherNetworkServiceType() {
        return fromOtherNetworkServiceType;
    }

    /**
     * (non-javadoc)
     * @see DiscoveryQueryParameters#toJson()
     */
    @Override
    public String toJson() {

        Gson gson = new Gson();
        return gson.toJson(this);
    }

    /**
     * (non-javadoc)
     * @see DiscoveryQueryParameters#fromJson(String)
     */
    @Override
    public DiscoveryQueryParametersCommunication fromJson(String json) {

        Gson gson = new Gson();
        return gson.fromJson(json, DiscoveryQueryParametersCommunication.class);
    }

    /**
     * (non-javadoc)
     * @see Object#equals(Object)
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DiscoveryQueryParametersCommunication)) return false;
        DiscoveryQueryParametersCommunication that = (DiscoveryQueryParametersCommunication) o;
        return Objects.equals(getIdentityPublicKey(), that.getIdentityPublicKey()) &&
                Objects.equals(getAlias(), that.getAlias()) &&
                Objects.equals(getName(), that.getName()) &&
                Objects.equals(getLocation(), that.getLocation()) &&
                Objects.equals(getDistance(), that.getDistance()) &&
                Objects.equals(getPlatformComponentType(), that.getPlatformComponentType()) &&
                Objects.equals(getNetworkServiceType(), that.getNetworkServiceType()) &&
                Objects.equals(getExtraData(), that.getExtraData()) &&
                Objects.equals(offset, that.offset) &&
                Objects.equals(getMax(), that.getMax()) &&
                Objects.equals(getFromOtherPlatformComponentType(), that.getFromOtherPlatformComponentType()) &&
                Objects.equals(getFromOtherNetworkServiceType(), that.getFromOtherNetworkServiceType());
    }

    /**
     * (non-javadoc)
     * @see Object#hashCode()
     */
    @Override
    public int hashCode() {
        return Objects.hash(getIdentityPublicKey(), getAlias(), getName(), getLocation(), getDistance(), getPlatformComponentType(), getNetworkServiceType(), getExtraData(), offset, getMax(), getFromOtherPlatformComponentType(), getFromOtherNetworkServiceType());
    }

    /**
     * (non-javadoc)
     * @see Object#toString()
     */
    @Override
    public String toString() {
        return "DiscoveryQueryParametersCommunication{" +
                "alias='" + alias + '\'' +
                ", identityPublicKey='" + identityPublicKey + '\'' +
                ", name='" + name + '\'' +
                ", location=" + location +
                ", distance=" + distance +
                ", platformComponentType=" + platformComponentType +
                ", networkServiceType=" + networkServiceType +
                ", extraData='" + extraData + '\'' +
                ", offset=" + offset +
                ", max=" + max +
                ", fromOtherPlatformComponentType=" + fromOtherPlatformComponentType +
                ", fromOtherNetworkServiceType=" + fromOtherNetworkServiceType +
                '}';
    }

}
