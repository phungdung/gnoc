/**
 * @(#)AffectedServicesForm.java ,
 * Copyright 2011 Viettel Telecom. All rights reserved
 * VIETTEL PROPRIETARY/CONFIDENTIAL
 */

package com.viettel.gnoc.maintenance.dto;

import javax.xml.bind.annotation.XmlRootElement;

/**
* @author anhmv6
* @version 1.0
* @since 8/14/2015 9:50 AM
*/
@XmlRootElement(name = "Result")
public class Result {

    @Override
    public String toString() {
        return "status=" + status + ", message=" + message;
    }

    private Long status;
    private String message;

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}


