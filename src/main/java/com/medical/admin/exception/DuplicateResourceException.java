
package com.medical.admin.exception;

public class DuplicateResourceException extends RuntimeException {
    
    public DuplicateResourceException(String field, String value) {
        super(field + " already exists: " + value);
    }
}