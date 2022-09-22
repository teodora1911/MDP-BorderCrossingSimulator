package tesi.util.exceptions;

public class LoadingConfigurationFileException extends Exception {
	private static final long serialVersionUID = -3254992531844258992L;

	public LoadingConfigurationFileException() {
		super();
	}

	public LoadingConfigurationFileException(String message) {
		super(message);
	}

	public LoadingConfigurationFileException(Throwable cause) {
		super(cause);
	}

	public LoadingConfigurationFileException(String message, Throwable cause) {
		super(message, cause);
	}

	public LoadingConfigurationFileException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
