package com.duangframework.license.client;

/**
 * @author duang
 */
public class LicenseException extends RuntimeException {
    public LicenseException(String message) {
        super(message);
    }

    public LicenseException(String message, Throwable cause) {
        super(message, cause);
    }
}
