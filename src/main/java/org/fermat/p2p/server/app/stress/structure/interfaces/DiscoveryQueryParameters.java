/*
 * @#DiscoveryQueryParameters.java - 2016
 * Copyright bitDubai.com., All rights reserved.
 * You may not modify, use, reproduce or distribute this software.
 * BITDUBAI/CONFIDENTIAL
 */
package org.fermat.p2p.server.app.stress.structure.interfaces;

import org.fermat.p2p.server.app.stress.structure.enums.NetworkServiceType;
import org.fermat.p2p.server.app.stress.structure.enums.PlatformComponentType;

public interface DiscoveryQueryParameters {
	

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
     * Get the distance from geo localization
     *
     * @return Double
     */
    Double getDistance();

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
     * Return the extra data
     *
     * @return String
     */
    String getExtraData();

    /**
     * Get the first record to make pagination
     *
     * @return int
     */
    Integer getOffset();

    /**
     * Get the number of max the record to return
     *
     * @return int
     */
    Integer getMax();

    /**
     * Return the platform component type from other component type,
     * this parameter indicate the filters are referent from other
     * platform component type, and need to find the component that match
     * with the PlatformComponentType specified in the other parameter
     *
     * @return PlatformComponentType
     */
    PlatformComponentType getFromOtherPlatformComponentType();

    /**
     * Get the network service type of the packet from other component type,
     * this parameter indicate the filters are referent from other
     * platform component type and need to find the component that match
     * with the NetworkServiceType specified in the other parameter
     *
     * @return NetworkServiceType
     */
    NetworkServiceType getFromOtherNetworkServiceType();

    /**
     * Convert this object to json string
     *
     * @return String json
     */
    String toJson();

    /**
     * Convert to DiscoveryQueryParameters from json
     *
     * @param json string object
     * @return DiscoveryQueryParameters
     */
    DiscoveryQueryParameters fromJson(String json);

}
