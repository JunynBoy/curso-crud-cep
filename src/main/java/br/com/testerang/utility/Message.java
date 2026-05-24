package br.com.testerang.utility;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

public final class Message {

	private Message() {
	}

	public static void info(String text) {
		add(FacesMessage.SEVERITY_INFO, text);
	}

	public static void erro(String text) {
		add(FacesMessage.SEVERITY_ERROR, text);
	}

	public static void warn(String text) {
		add(FacesMessage.SEVERITY_WARN, text);
	}

	private static void add(FacesMessage.Severity severity, String text) {
		FacesContext context = FacesContext.getCurrentInstance();
		if (context == null) {
			return;
		}

		context.addMessage(null, new FacesMessage(severity, text, null));
	}
}
