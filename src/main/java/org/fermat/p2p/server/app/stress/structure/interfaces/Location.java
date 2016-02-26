/*
 * @#Location.java - 2016
 * Copyright bitDubai.com., All rights reserved.
 * You may not modify, use, reproduce or distribute this software.
 * BITDUBAI/CONFIDENTIAL
 */
package org.fermat.p2p.server.app.stress.structure.interfaces;


import org.fermat.p2p.server.app.stress.structure.enums.LocationProvider;

public interface Location {
	
	Double getLatitude();

    Double getLongitude();

    Double getAltitude();

    /**
     * @return the last update time of the coordinates.
     */
    Long getTime();

    /**
     * @return the provider that you use to get the coordinates, gps, wifi, etc.
     */
    LocationProvider getProvider();

}
