package org.tron.justlend.justlendapiserver.core;


public class ServiceException extends RuntimeException {
  public ServiceException() {
  }

  public ServiceException(String message) {
    super(message);
  }

  public ServiceException(String message, Throwable cause) {
    super(message, cause);
  }
}
