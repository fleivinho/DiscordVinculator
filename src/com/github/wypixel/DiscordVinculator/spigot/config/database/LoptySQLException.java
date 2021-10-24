package com.github.wypixel.DiscordVinculator.spigot.config.database;

public class LoptySQLException extends RuntimeException {

	private static final long serialVersionUID = -1746878370430655863L;

	public LoptySQLException() {
		super();
	}

	public LoptySQLException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public LoptySQLException(String message, Throwable cause) {
		super(message, cause);
	}

	public LoptySQLException(String message) {
		super(message);
	}

	public LoptySQLException(Throwable cause) {
		super(cause);
	}

}
