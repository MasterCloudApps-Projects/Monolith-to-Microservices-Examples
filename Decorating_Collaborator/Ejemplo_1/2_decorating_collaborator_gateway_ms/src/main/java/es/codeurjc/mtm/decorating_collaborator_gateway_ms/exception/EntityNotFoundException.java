package es.codeurjc.mtm.decorating_collaborator_gateway_ms.exception;

public class EntityNotFoundException extends RuntimeException {

  private static final long serialVersionUID = 7878359411214073889L;

  public EntityNotFoundException() {
    super();
  }

  public EntityNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }

  public EntityNotFoundException(String message) {
    super(message);
  }

  public EntityNotFoundException(final Throwable cause) {
    super(cause);
  }

}