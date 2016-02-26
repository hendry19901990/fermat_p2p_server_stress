/*
 * @#DeviceLocation.java - 2016
 * Copyright bitDubai.com., All rights reserved.
 * You may not modify, use, reproduce or distribute this software.
 * BITDUBAI/CONFIDENTIAL
 */
package org.fermat.p2p.server.app.stress.structure.commons.components;

import java.util.Random;

import org.fermat.p2p.server.app.stress.structure.enums.LocationProvider;
import org.fermat.p2p.server.app.stress.structure.interfaces.Location;

public class DeviceLocation implements Location{
	
    /**
     * Location interface member variables.
     */

    private Double latitude;
    private Double longitude;
    private Long time;
    private Double altitude;
    private LocationProvider provider;

    // Public constructor declarations.
    public DeviceLocation(){
        this.latitude  = new Random().nextDouble();
        this.longitude = new Random().nextDouble();
        this.time      = System.currentTimeMillis();
        this.altitude  = new Random().nextDouble();
        this.provider  = LocationProvider.NETWORK;
    }

    /**
     * <p>DeviceLocation implementation constructor
     *
     * @param latitude Double actual device latitude
     * @param longitude Double actual device longitude
     * @param time Long actual device location time
     * @param altitude Double actual device  altitude
     * @param provider enum LocationProvider actual location provider network
     */
   public DeviceLocation(Double latitude, Double longitude, Long time, Double altitude, LocationProvider provider){
       this.latitude = latitude;
       this.longitude = longitude;
       this.time = time;
       this.altitude = altitude;
       this.provider = provider;
   }


    /**
     * Location interfaces implementation.
     */

    /**
     *<p>This method gets de actual device altitude
     *
     * @return double altitude
     */
    @Override
    public Double getAltitude() {
        return altitude;
    }

    /**
     * Set the Altitude
     * @param altitude
     */
    public void setAltitude(Double altitude) {
        this.altitude = altitude;
    }

    /**
     *<p>This method gets de actual device latitude
     *
     * @return double latitude
     */
    @Override
    public Double getLatitude() {
        return latitude;
    }

    /**
     * Set the Latitude
     * @param latitude
     */
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    /**
     *<p>This method gets de actual device longitude
     *
     * @return double longitude
     */
    @Override
    public Double getLongitude() {
        return longitude;
    }

    /**
     * Set the Longitude
     * @param longitude
     */
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    /**
     *<p>This method gets de actual location network provider
     *
     * @return LocationProvider enum
     */
    @Override
    public LocationProvider getProvider() {
        return provider;
    }

    /**
     * Set the Provider
     * @param provider
     */
    public void setProvider(LocationProvider provider) {
        this.provider = provider;
    }

    /**
     *<p>This method gets de actual device location time
     *
     * @return long time
     */
    @Override
    public Long getTime() {
        return time;
    }

    /**
     * Set the Time
     * @param time
     */
    public void setTime(Long time) {
        this.time = time;
    }

}
