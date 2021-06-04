package dev.clinic.simple.repository;

import dev.clinic.simple.domain.TipoConvenio;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the TipoConvenio entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TipoConvenioRepository extends JpaRepository<TipoConvenio, Long>, JpaSpecificationExecutor<TipoConvenio> {}
