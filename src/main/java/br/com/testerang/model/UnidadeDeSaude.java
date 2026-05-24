package br.com.testerang.model;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
	name = "unidade_de_saude",
	uniqueConstraints = {
		@UniqueConstraint(name = "uk_unidade_de_saude_cnes", columnNames = "cnes")
	}
)
public class UnidadeDeSaude implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true, nullable = false, length = 7)
	private String cnes;

	@Column(nullable = false, length = 150)
	private String nomeEstabelecimento;

	@Column(nullable = false, length = 9)
	private String cepInicio;

	@Column(nullable = false, length = 9)
	private String cepFinal;

	public UnidadeDeSaude() {
	}

	public UnidadeDeSaude(UnidadeDeSaude unidade) {
		this.id = unidade.id;
		this.cnes = unidade.cnes;
		this.nomeEstabelecimento = unidade.nomeEstabelecimento;
		this.cepInicio = unidade.cepInicio;
		this.cepFinal = unidade.cepFinal;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}

		if (!(object instanceof UnidadeDeSaude)) {
			return false;
		}

		UnidadeDeSaude other = (UnidadeDeSaude) object;
		return id != null && Objects.equals(id, other.id);
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}

	@Override
	public String toString() {
		return "UnidadeDeSaude [id=" + id + ", cnes=" + cnes + ", nomeEstabelecimento=" + nomeEstabelecimento
				+ ", cepInicio=" + cepInicio + ", cepFinal=" + cepFinal + "]";
	}
}
