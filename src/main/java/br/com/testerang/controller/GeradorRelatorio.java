package br.com.testerang.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

import br.com.testerang.model.UnidadeDeSaude;
import br.com.testerang.utility.NegocioException;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JRDesignBand;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignField;
import net.sf.jasperreports.engine.design.JRDesignSection;
import net.sf.jasperreports.engine.design.JRDesignStaticText;
import net.sf.jasperreports.engine.design.JRDesignTextField;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.type.HorizontalTextAlignEnum;

@ApplicationScoped
public class GeradorRelatorio implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final int PAGE_WIDTH = 842;
	private static final int PAGE_HEIGHT = 595;
	private static final int MARGIN = 30;
	private static final int CONTENT_WIDTH = PAGE_WIDTH - (MARGIN * 2);

	public void gerar(List<UnidadeDeSaude> unidades) throws NegocioException {
		FacesContext context = FacesContext.getCurrentInstance();
		if (context == null) {
			throw new NegocioException("Contexto JSF não disponível para gerar relatório.");
		}

		HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
		byte[] pdf = gerarPdf(unidades);

		try {
			response.reset();
			response.setContentType("application/pdf");
			response.setContentLength(pdf.length);
			response.setHeader("Content-Disposition", "inline; filename=\"relatorio.pdf\"");
			response.getOutputStream().write(pdf);
			response.getOutputStream().flush();

			context.responseComplete();
		} catch (IOException e) {
			throw new NegocioException("Não foi possível gerar o relatório.", e);
		}
	}

	byte[] gerarPdf(List<UnidadeDeSaude> unidades) throws NegocioException {
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
			JasperPrint print = JasperFillManager.fillReport(
					criarRelatorio(),
					new HashMap<>(),
					new JRBeanCollectionDataSource(normalizar(unidades)));

			JasperExportManager.exportReportToPdfStream(print, baos);
			return baos.toByteArray();
		} catch (JRException | IOException e) {
			throw new NegocioException("Não foi possível gerar o relatório.", e);
		}
	}

	private JasperReport criarRelatorio() throws JRException {
		JasperDesign design = new JasperDesign();
		design.setName("unidades_de_saude");
		design.setPageWidth(PAGE_WIDTH);
		design.setPageHeight(PAGE_HEIGHT);
		design.setLeftMargin(MARGIN);
		design.setRightMargin(MARGIN);
		design.setTopMargin(MARGIN);
		design.setBottomMargin(MARGIN);

		adicionarCampo(design, "id", Long.class);
		adicionarCampo(design, "nomeEstabelecimento", String.class);
		adicionarCampo(design, "cepInicio", String.class);
		adicionarCampo(design, "cepFinal", String.class);
		adicionarCampo(design, "cnes", String.class);

		design.setTitle(criarTitulo());
		design.setColumnHeader(criarCabecalho());
		((JRDesignSection) design.getDetailSection()).addBand(criarDetalhe());

		return JasperCompileManager.compileReport(design);
	}

	private JRDesignBand criarTitulo() {
		JRDesignBand band = new JRDesignBand();
		band.setHeight(54);
		band.addElement(textoEstatico("Relatório de unidades de saúde", 0, 0, CONTENT_WIDTH, 28, 18, true));
		band.addElement(textoEstatico("Cadastro por faixa de CEP", 0, 28, CONTENT_WIDTH, 18, 10, false));
		return band;
	}

	private JRDesignBand criarCabecalho() {
		JRDesignBand band = new JRDesignBand();
		band.setHeight(24);
		band.addElement(textoEstatico("ID", 0, 0, 45, 20, 10, true));
		band.addElement(textoEstatico("Nome do estabelecimento", 55, 0, 350, 20, 10, true));
		band.addElement(textoEstatico("CEP inicial", 415, 0, 105, 20, 10, true));
		band.addElement(textoEstatico("CEP final", 535, 0, 105, 20, 10, true));
		band.addElement(textoEstatico("CNES", 655, 0, 100, 20, 10, true));
		return band;
	}

	private JRDesignBand criarDetalhe() {
		JRDesignBand band = new JRDesignBand();
		band.setHeight(22);
		band.addElement(campo("$F{id}", 0, 0, 45, 18));
		band.addElement(campo("$F{nomeEstabelecimento}", 55, 0, 350, 18));
		band.addElement(campo("$F{cepInicio}", 415, 0, 105, 18));
		band.addElement(campo("$F{cepFinal}", 535, 0, 105, 18));
		band.addElement(campo("$F{cnes}", 655, 0, 100, 18));
		return band;
	}

	private static void adicionarCampo(JasperDesign design, String nome, Class<?> type) throws JRException {
		JRDesignField field = new JRDesignField();
		field.setName(nome);
		field.setValueClass(type);
		design.addField(field);
	}

	private static JRDesignStaticText textoEstatico(
			String texto,
			int x,
			int y,
			int width,
			int height,
			float fontSize,
			boolean bold) {
		JRDesignStaticText staticText = new JRDesignStaticText();
		staticText.setText(texto);
		staticText.setX(x);
		staticText.setY(y);
		staticText.setWidth(width);
		staticText.setHeight(height);
		staticText.setFontName("SansSerif");
		staticText.setFontSize(fontSize);
		staticText.setBold(bold);
		return staticText;
	}

	private static JRDesignTextField campo(String expressao, int x, int y, int width, int height) {
		JRDesignTextField textField = new JRDesignTextField();
		textField.setX(x);
		textField.setY(y);
		textField.setWidth(width);
		textField.setHeight(height);
		textField.setFontName("SansSerif");
		textField.setFontSize(9f);
		textField.setBlankWhenNull(true);
		textField.setHorizontalTextAlign(HorizontalTextAlignEnum.LEFT);

		JRDesignExpression expression = new JRDesignExpression();
		expression.setText(expressao);
		textField.setExpression(expression);

		return textField;
	}

	private static List<UnidadeDeSaude> normalizar(List<UnidadeDeSaude> unidades) {
		return unidades == null ? Collections.emptyList() : unidades;
	}
}
