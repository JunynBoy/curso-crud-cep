package br.com.testerang.model;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "estabelecimento")
public class UnidadeDeSaude implements Serializable , Base {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private Long id;
	@Column(unique = true, nullable = false, length = 7)
	private String cnes;
	private String nomeEstabelecimento;
	private String cepInicio;
	private String cepFinal;

	public String getCnes() {
		return cnes;
	}

	public void setCnes(String cnes) {
		this.cnes = cnes;
	}

	public String getNomeEstabelecimento() {
		return nomeEstabelecimento;
	}

	public void setNomeEstabelecimento(String nomeEstabelecimento) {
		this.nomeEstabelecimento = nomeEstabelecimento;
	}

	
	public String getCepInicio() {
		return cepInicio;
	}

	public void setCepInicio(String cepInicio) {
		this.cepInicio = cepInicio;
	}

	public String getCepFinal() {
		return cepFinal;
	}

	public void setCepFinal(String cepFinal) {
		this.cepFinal = cepFinal;
	}

	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public int hashCode() {
		return Objects.hash(cepFinal, cepInicio, cnes, id, nomeEstabelecimento);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UnidadeDeSaude other = (UnidadeDeSaude) obj;
		return Objects.equals(cepFinal, other.cepFinal) && Objects.equals(cepInicio, other.cepInicio)
				&& Objects.equals(cnes, other.cnes) && Objects.equals(id, other.id)
				&& Objects.equals(nomeEstabelecimento, other.nomeEstabelecimento);
	}

	@Override
	public String toString() {
		return "UnidadeDeSaude [id=" + id + ", cnes=" + cnes + ", nomeEstabelecimento=" + nomeEstabelecimento
				+ ", cepInicio=" + cepInicio + ", cepFinal=" + cepFinal + "]";
	}
	
	
	

}
