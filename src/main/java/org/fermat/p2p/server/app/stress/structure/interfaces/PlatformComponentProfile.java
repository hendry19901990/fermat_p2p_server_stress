/*
 * @#PlatformComponentProfile.java - 2016
 * Copyright bitDubai.com., All rights reserved.
 * You may not modify, use, reproduce or distribute this software.
 * BITDUBAI/CONFIDENTIAL
 */
package org.fermat.p2p.server.app.stress.structure.interfaces;

import org.fermat.p2p.server.app.stress.structure.enums.NetworkServiceType;
import org.fermat.p2p.server.app.stress.structure.enums.PlatformComponentType;

public interface PlatformComponentProfile extends Cloneable{
	
	/**
     * Return the public key that represent the identity of the component
     *
     * @return String
     */
    String getIdentityPublicKey();

    /**
     * Get the alias
     *
     * @return String
     */
    String getAlias();

    /**
     * Get the name
     *
     * @return String
     */
    String getName();

    /**
     * Get the location for geo localization
     *
     * @return Double
     */
    Location getLocation();

    /**
     * Return the platform component type
     *
     * @return PlatformComponentType
     */
    PlatformComponentType getPlatformComponentType();

    /**
     * Get the network service type of the packet
     *
     * @return NetworkServiceType
     */
    NetworkServiceType getNetworkServiceType();

    /**
     * Return the public key that represent the identity of the Web Socket Communication Cloud Client,
     * that this component use like communication channel
     *
     * @return String
     */
    String getCommunicationCloudClientIdentity();

    /**
     * Return the extra data
     *
     * @return String
     */
    String getExtraData();

    /**
     * Convert this object to json string
     *
     * @return String json
     */
    String toJson();

    /**
     * Convert to PlatformComponentProfile from json
     *
     * @param json string object
     * @return PlatformComponentProfile
     */
    PlatformComponentProfile fromJson(String json);

}
