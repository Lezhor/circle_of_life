package com.android.circleoflife.communication;

public interface Protocol {

    /**
     * Returns the current Protocol Name
     * @return protocol name
     */
    String getProtocolName();

    /**
     * Get current version of the syncProtocol
     * @return version of this protocol
     */
    String getVersion();

}
