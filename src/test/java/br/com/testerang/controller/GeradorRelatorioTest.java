package br.com.testerang.controller;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import br.com.testerang.model.UnidadeDeSaude;

class GeradorRelatorioTest {

	@Test
	void deveGerarPdfDoRelatorioEmMemoria() throws Exception {
		byte[] pdf = new GeradorRelatorio().gerarPdf(List.of(unidade()));

		assertTrue(pdf.length > 0);
		assertTrue(new String(pdf, 0, 4).startsWith("%PDF"));
	}

	private static UnidadeDeSaude unidade() {
		UnidadeDeSaude unidade = new UnidadeDeSaude();
		unidade.setId(1L);
		unidade.setNomeEstabelecimento("Unidade Central");
		unidade.setCepInicio("10000-000");
		unidade.setCepFinal("10000-100");
		unidade.setCnes("1234567");
		return unidade;
	}
}
