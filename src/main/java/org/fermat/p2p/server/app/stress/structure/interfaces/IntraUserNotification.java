/*
 * @#IntraUserNotification.java - 2016
 * Copyright bitDubai.com., All rights reserved.
 * You may not modify, use, reproduce or distribute this software.
 * BITDUBAI/CONFIDENTIAL
 */
package org.fermat.p2p.server.app.stress.structure.interfaces;

import java.util.UUID;

import org.fermat.p2p.server.app.stress.structure.enums.Actors;
import org.fermat.p2p.server.app.stress.structure.enums.ccp_intra_actor.NotificationDescriptor;

public interface IntraUserNotification {
	

    /**
     * The method <code>getPublicKeyOfTheSender</code> tells us the public key
     * of the intra user sending the notification
     *
     * @return the public key
     */

    String getActorSenderAlias();

    byte[] getActorSenderProfileImage();

    //new

    String getActorSenderPhrase();

    UUID getId();

    Actors getActorDestinationType();

    String getActorDestinationPublicKey();

    String getActorSenderPublicKey();

    Actors getActorSenderType();

    void setFlagReadead(boolean flagReadead);

    /**
     * The method <code>getNotificationDescriptor</code> tells us the nature of the notification
     *
     * @return the descriptor of the notification
     */
    NotificationDescriptor getNotificationDescriptor();

    long getSentDate();
	

}
