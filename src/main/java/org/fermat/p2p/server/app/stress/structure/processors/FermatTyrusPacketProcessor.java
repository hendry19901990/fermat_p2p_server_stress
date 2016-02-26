/*
 * @#FermatTyrusPacketProcessor.java - 2016
 * Copyright bitDubai.com., All rights reserved.
 * You may not modify, use, reproduce or distribute this software.
 * BITDUBAI/CONFIDENTIAL
 */
package org.fermat.p2p.server.app.stress.structure.processors;

import org.fermat.p2p.server.app.stress.structure.channel.WsCommunicationsTyrusCloudClientChannel;
import org.fermat.p2p.server.app.stress.structure.enums.FermatPacketType;
import org.fermat.p2p.server.app.stress.structure.interfaces.FermatPacket;

public abstract class FermatTyrusPacketProcessor {
	
	/**
     * Represent the wsCommunicationsCloudClientChannel
     */
    private WsCommunicationsTyrusCloudClientChannel wsCommunicationsTyrusCloudClientChannel;

    /**
     * Constructor
     * @param wsCommunicationsTyrusCloudClientChannel
     */
    FermatTyrusPacketProcessor(WsCommunicationsTyrusCloudClientChannel wsCommunicationsTyrusCloudClientChannel){
        this.wsCommunicationsTyrusCloudClientChannel = wsCommunicationsTyrusCloudClientChannel;
    }

    /**
     * Method that contain the logic to process the packet
     */
    public abstract void processingPackage(final FermatPacket receiveFermatPacket);
    
    /**
     * Return the FermatPacketType that it processes
     *
     * @return FermatPacketType
     */
    public abstract FermatPacketType getFermatPacketType();


    /**
     * Get the wsCommunicationsTyrusCloudClientChannel value
     *
     * @return wsCommunicationsTyrusCloudClientChannel current value
     */
    public WsCommunicationsTyrusCloudClientChannel getWsCommunicationsTyrusCloudClientChannel() {
        return wsCommunicationsTyrusCloudClientChannel;
    }

}
