package dev.clinic.simple.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Medico.
 */
@Entity
@Table(name = "medico")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Medico implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "crm")
    private Integer crm;

    @Column(name = "nome")
    private Integer nome;

    @Column(name = "graduacao")
    private String graduacao;

    @Column(name = "atuacao")
    private String atuacao;

    @JsonIgnoreProperties(value = { "nomemedico", "tipoexame", "valorexames", "contaClinica" }, allowSetters = true)
    @OneToOne(mappedBy = "nomemedico")
    private Exame nome;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Medico id(Long id) {
        this.id = id;
        return this;
    }

    public Integer getCrm() {
        return this.crm;
    }

    public Medico crm(Integer crm) {
        this.crm = crm;
        return this;
    }

    public void setCrm(Integer crm) {
        this.crm = crm;
    }

    public Integer getNome() {
        return this.nome;
    }

    public Medico nome(Integer nome) {
        this.nome = nome;
        return this;
    }

    public void setNome(Integer nome) {
        this.nome = nome;
    }

    public String getGraduacao() {
        return this.graduacao;
    }

    public Medico graduacao(String graduacao) {
        this.graduacao = graduacao;
        return this;
    }

    public void setGraduacao(String graduacao) {
        this.graduacao = graduacao;
    }

    public String getAtuacao() {
        return this.atuacao;
    }

    public Medico atuacao(String atuacao) {
        this.atuacao = atuacao;
        return this;
    }

    public void setAtuacao(String atuacao) {
        this.atuacao = atuacao;
    }

    public Exame getNome() {
        return this.nome;
    }

    public Medico nome(Exame exame) {
        this.setNome(exame);
        return this;
    }

    public void setNome(Exame exame) {
        if (this.nome != null) {
            this.nome.setNomemedico(null);
        }
        if (nome != null) {
            nome.setNomemedico(this);
        }
        this.nome = exame;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Medico)) {
            return false;
        }
        return id != null && id.equals(((Medico) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Medico{" +
            "id=" + getId() +
            ", crm=" + getCrm() +
            ", nome=" + getNome() +
            ", graduacao='" + getGraduacao() + "'" +
            ", atuacao='" + getAtuacao() + "'" +
            "}";
    }
}
