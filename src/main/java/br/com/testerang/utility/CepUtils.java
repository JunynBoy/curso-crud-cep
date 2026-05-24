package br.com.testerang.utility;

public final class CepUtils {

	private static final int CEP_LENGTH = 8;

	private CepUtils() {
	}

	public static String normalizar(String cep) throws NegocioException {
		String digits = somenteDigitos(cep);
		if (digits.length() != CEP_LENGTH) {
			throw new NegocioException("CEP deve conter 8 dígitos.");
		}

		return digits.substring(0, 5) + "-" + digits.substring(5);
	}

	public static int paraNumero(String cep) throws NegocioException {
		String digits = somenteDigitos(cep);
		if (digits.length() != CEP_LENGTH) {
			throw new NegocioException("CEP inválido encontrado: " + cep);
		}

		return Integer.parseInt(digits);
	}

	public static boolean intervalosSobrepostos(int inicio, int fim, int outroInicio, int outroFim) {
		return inicio <= outroFim && fim >= outroInicio;
	}

	private static String somenteDigitos(String value) {
		if (value == null) {
			return "";
		}

		return value.replaceAll("\\D", "");
	}
}
