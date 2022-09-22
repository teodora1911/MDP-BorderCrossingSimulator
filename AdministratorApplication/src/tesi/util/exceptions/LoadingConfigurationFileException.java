package tesi.util.exceptions;

@SuppressWarnings("serial")
public class LoadingConfigurationFileException extends Exception {

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

	public LoadingConfigurationFileException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
