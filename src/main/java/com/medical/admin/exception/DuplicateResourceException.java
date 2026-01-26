
package com.medical.admin.exception;

public class DuplicateResourceException extends RuntimeException {
    
   
	private static final long serialVersionUID = 1L;

	public DuplicateResourceException(String field, String value) {
        super(field + " already exists: " + value);
    }
}