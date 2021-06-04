package dev.clinic.simple.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A TipoConvenio.
 */
@Entity
@Table(name = "tipo_convenio")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class TipoConvenio implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "convenio")
    private String convenio;

    @Column(name = "codigoconvenio")
    private Integer codigoconvenio;

    @Column(name = "nomeconvenio")
    private String nomeconvenio;

    @Column(name = "contato")
    private String contato;

    @ManyToOne
    @JsonIgnoreProperties(value = { "cpf", "cpfs", "cpfs", "cpf" }, allowSetters = true)
    private ContaClinica contaClinica;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TipoConvenio id(Long id) {
        this.id = id;
        return this;
    }

    public String getConvenio() {
        return this.convenio;
    }

    public TipoConvenio convenio(String convenio) {
        this.convenio = convenio;
        return this;
    }

    public void setConvenio(String convenio) {
        this.convenio = convenio;
    }

    public Integer getCodigoconvenio() {
        return this.codigoconvenio;
    }

    public TipoConvenio codigoconvenio(Integer codigoconvenio) {
        this.codigoconvenio = codigoconvenio;
        return this;
    }

    public void setCodigoconvenio(Integer codigoconvenio) {
        this.codigoconvenio = codigoconvenio;
    }

    public String getNomeconvenio() {
        return this.nomeconvenio;
    }

    public TipoConvenio nomeconvenio(String nomeconvenio) {
        this.nomeconvenio = nomeconvenio;
        return this;
    }

    public void setNomeconvenio(String nomeconvenio) {
        this.nomeconvenio = nomeconvenio;
    }

    public String getContato() {
        return this.contato;
    }

    public TipoConvenio contato(String contato) {
        this.contato = contato;
        return this;
    }

    public void setContato(String contato) {
        this.contato = contato;
    }

    public ContaClinica getContaClinica() {
        return this.contaClinica;
    }

    public TipoConvenio contaClinica(ContaClinica contaClinica) {
        this.setContaClinica(contaClinica);
        return this;
    }

    public void setContaClinica(ContaClinica contaClinica) {
        this.contaClinica = contaClinica;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TipoConvenio)) {
            return false;
        }
        return id != null && id.equals(((TipoConvenio) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TipoConvenio{" +
            "id=" + getId() +
            ", convenio='" + getConvenio() + "'" +
            ", codigoconvenio=" + getCodigoconvenio() +
            ", nomeconvenio='" + getNomeconvenio() + "'" +
            ", contato='" + getContato() + "'" +
            "}";
    }
}
