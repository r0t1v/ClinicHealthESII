package dev.clinic.simple.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A EnderecoPaciente.
 */
@Entity
@Table(name = "endereco_paciente")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class EnderecoPaciente implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cidade")
    private String cidade;

    @Column(name = "logradouro")
    private String logradouro;

    @Column(name = "bairro")
    private String bairro;

    @Column(name = "numero")
    private Integer numero;

    @Column(name = "referencia")
    private String referencia;

    @Column(name = "cep")
    private String cep;

    @ManyToOne
    @JsonIgnoreProperties(value = { "cpf", "cpfs", "cpfs", "cpf" }, allowSetters = true)
    private Paciente paciente;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EnderecoPaciente id(Long id) {
        this.id = id;
        return this;
    }

    public String getCidade() {
        return this.cidade;
    }

    public EnderecoPaciente cidade(String cidade) {
        this.cidade = cidade;
        return this;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getLogradouro() {
        return this.logradouro;
    }

    public EnderecoPaciente logradouro(String logradouro) {
        this.logradouro = logradouro;
        return this;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getBairro() {
        return this.bairro;
    }

    public EnderecoPaciente bairro(String bairro) {
        this.bairro = bairro;
        return this;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public Integer getNumero() {
        return this.numero;
    }

    public EnderecoPaciente numero(Integer numero) {
        this.numero = numero;
        return this;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public String getReferencia() {
        return this.referencia;
    }

    public EnderecoPaciente referencia(String referencia) {
        this.referencia = referencia;
        return this;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public String getCep() {
        return this.cep;
    }

    public EnderecoPaciente cep(String cep) {
        this.cep = cep;
        return this;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public Paciente getPaciente() {
        return this.paciente;
    }

    public EnderecoPaciente paciente(Paciente paciente) {
        this.setPaciente(paciente);
        return this;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EnderecoPaciente)) {
            return false;
        }
        return id != null && id.equals(((EnderecoPaciente) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EnderecoPaciente{" +
            "id=" + getId() +
            ", cidade='" + getCidade() + "'" +
            ", logradouro='" + getLogradouro() + "'" +
            ", bairro='" + getBairro() + "'" +
            ", numero=" + getNumero() +
            ", referencia='" + getReferencia() + "'" +
            ", cep='" + getCep() + "'" +
            "}";
    }
}
