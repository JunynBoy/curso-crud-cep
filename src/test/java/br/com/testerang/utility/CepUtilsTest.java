package br.com.testerang.utility;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class CepUtilsTest {

	@Test
	void normalizaCepMantendoFormatoPadrao() throws NegocioException {
		assertEquals("12345-678", CepUtils.normalizar("12345678"));
		assertEquals("12345-678", CepUtils.normalizar("12345-678"));
	}

	@Test
	void rejeitaCepComQuantidadeInvalidaDeDigitos() {
		assertThrows(NegocioException.class, () -> CepUtils.normalizar("123"));
		assertThrows(NegocioException.class, () -> CepUtils.normalizar("123456789"));
	}

	@Test
	void identificaIntervalosSobrepostos() {
		assertTrue(CepUtils.intervalosSobrepostos(100, 200, 150, 300));
		assertTrue(CepUtils.intervalosSobrepostos(100, 200, 50, 100));
		assertFalse(CepUtils.intervalosSobrepostos(100, 200, 201, 300));
	}
}
