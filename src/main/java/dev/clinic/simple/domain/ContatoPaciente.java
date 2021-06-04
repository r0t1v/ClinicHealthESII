package dev.clinic.simple.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ContatoPaciente.
 */
@Entity
@Table(name = "contato_paciente")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ContatoPaciente implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tipo")
    private String tipo;

    @Column(name = "conteudo")
    private String conteudo;

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

    public ContatoPaciente id(Long id) {
        this.id = id;
        return this;
    }

    public String getTipo() {
        return this.tipo;
    }

    public ContatoPaciente tipo(String tipo) {
        this.tipo = tipo;
        return this;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getConteudo() {
        return this.conteudo;
    }

    public ContatoPaciente conteudo(String conteudo) {
        this.conteudo = conteudo;
        return this;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    public Paciente getPaciente() {
        return this.paciente;
    }

    public ContatoPaciente paciente(Paciente paciente) {
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
        if (!(o instanceof ContatoPaciente)) {
            return false;
        }
        return id != null && id.equals(((ContatoPaciente) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ContatoPaciente{" +
            "id=" + getId() +
            ", tipo='" + getTipo() + "'" +
            ", conteudo='" + getConteudo() + "'" +
            "}";
    }
}
