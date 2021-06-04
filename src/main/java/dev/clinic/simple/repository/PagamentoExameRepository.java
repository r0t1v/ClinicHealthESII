package dev.clinic.simple.repository;

import dev.clinic.simple.domain.PagamentoExame;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the PagamentoExame entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PagamentoExameRepository extends JpaRepository<PagamentoExame, Long>, JpaSpecificationExecutor<PagamentoExame> {}
