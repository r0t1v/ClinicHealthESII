package dev.clinic.simple.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ContaClinica.
 */
@Entity
@Table(name = "conta_clinica")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ContaClinica implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "cpf", nullable = false)
    private String cpf;

    @Column(name = "senha")
    private String senha;

    @JsonIgnoreProperties(value = { "cpf", "cpfs", "cpfs", "cpf" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Paciente cpf;

    @OneToMany(mappedBy = "contaClinica")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "contaClinica" }, allowSetters = true)
    private Set<TipoConvenio> cpfs = new HashSet<>();

    @OneToMany(mappedBy = "contaClinica")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "nomemedico", "tipoexame", "valorexames", "contaClinica" }, allowSetters = true)
    private Set<Exame> cpfs = new HashSet<>();

    @JsonIgnoreProperties(value = { "cpf", "cpfs", "cpfs", "cpf" }, allowSetters = true)
    @OneToOne(mappedBy = "cpf")
    private Paciente cpf;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ContaClinica id(Long id) {
        this.id = id;
        return this;
    }

    public String getCpf() {
        return this.cpf;
    }

    public ContaClinica cpf(String cpf) {
        this.cpf = cpf;
        return this;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getSenha() {
        return this.senha;
    }

    public ContaClinica senha(String senha) {
        this.senha = senha;
        return this;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Paciente getCpf() {
        return this.cpf;
    }

    public ContaClinica cpf(Paciente paciente) {
        this.setCpf(paciente);
        return this;
    }

    public void setCpf(Paciente paciente) {
        this.cpf = paciente;
    }

    public Set<TipoConvenio> getCpfs() {
        return this.cpfs;
    }

    public ContaClinica cpfs(Set<TipoConvenio> tipoConvenios) {
        this.setCpfs(tipoConvenios);
        return this;
    }

    public ContaClinica addCpf(TipoConvenio tipoConvenio) {
        this.cpfs.add(tipoConvenio);
        tipoConvenio.setContaClinica(this);
        return this;
    }

    public ContaClinica removeCpf(TipoConvenio tipoConvenio) {
        this.cpfs.remove(tipoConvenio);
        tipoConvenio.setContaClinica(null);
        return this;
    }

    public void setCpfs(Set<TipoConvenio> tipoConvenios) {
        if (this.cpfs != null) {
            this.cpfs.forEach(i -> i.setContaClinica(null));
        }
        if (tipoConvenios != null) {
            tipoConvenios.forEach(i -> i.setContaClinica(this));
        }
        this.cpfs = tipoConvenios;
    }

    public Set<Exame> getCpfs() {
        return this.cpfs;
    }

    public ContaClinica cpfs(Set<Exame> exames) {
        this.setCpfs(exames);
        return this;
    }

    public ContaClinica addCpf(Exame exame) {
        this.cpfs.add(exame);
        exame.setContaClinica(this);
        return this;
    }

    public ContaClinica removeCpf(Exame exame) {
        this.cpfs.remove(exame);
        exame.setContaClinica(null);
        return this;
    }

    public void setCpfs(Set<Exame> exames) {
        if (this.cpfs != null) {
            this.cpfs.forEach(i -> i.setContaClinica(null));
        }
        if (exames != null) {
            exames.forEach(i -> i.setContaClinica(this));
        }
        this.cpfs = exames;
    }

    public Paciente getCpf() {
        return this.cpf;
    }

    public ContaClinica cpf(Paciente paciente) {
        this.setCpf(paciente);
        return this;
    }

    public void setCpf(Paciente paciente) {
        if (this.cpf != null) {
            this.cpf.setCpf(null);
        }
        if (cpf != null) {
            cpf.setCpf(this);
        }
        this.cpf = paciente;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ContaClinica)) {
            return false;
        }
        return id != null && id.equals(((ContaClinica) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ContaClinica{" +
            "id=" + getId() +
            ", cpf='" + getCpf() + "'" +
            ", senha='" + getSenha() + "'" +
            "}";
    }
}
