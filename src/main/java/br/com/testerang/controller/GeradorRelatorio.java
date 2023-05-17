package br.com.testerang.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

import br.com.testerang.model.UnidadeDeSaude;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;



public class GeradorRelatorio {
	
	private HttpServletResponse response;
	private FacesContext context;
	private ByteArrayOutputStream baos;
	private InputStream stream;
	private Connection con;
	
	
	
	
	
	public GeradorRelatorio() {
		this.context = FacesContext.getCurrentInstance();
		this.response = (HttpServletResponse) context.getExternalContext().getResponse();
	}
	
	public void getGeradorRelatorio(List<UnidadeDeSaude> lista) {
		
		
		stream = this.getClass().getResourceAsStream("/report/report1.jasper");	
		
		Map<String, Object> params = new HashMap<String, Object>();
		baos = new ByteArrayOutputStream();
		try {
			JasperReport report = (JasperReport) JRLoader.loadObject(stream);
			JasperPrint print = JasperFillManager.fillReport(report , params, getConnection() );                                                               
			JasperExportManager.exportReportToPdfStream(print, baos);
			
			response.reset();
			response.setContentType("application/pdf");
			response.setContentLength(baos.size());
			response.setHeader("Content-disposition", "inline: filename=relatorio.pdf");
			response.getOutputStream().write(baos.toByteArray());
			response.getOutputStream().flush();
			response.getOutputStream().close();
			
			context.responseComplete();
		} catch (JRException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		

	}
	
	
	public Connection getConnection() {
		
		try {
			
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/rangdatabase", "root", "Mg131730");
			return con;

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		return con;
	}
	
	
	
	
	
	
	
}
