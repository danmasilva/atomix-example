package com.sd.dfc.server;

//exceção para sair da aplicação caso ela não possa ser iniciada por falta de argumentos.
public class ExitException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public ExitException(String message) {
		super(message);
	}
	
}
