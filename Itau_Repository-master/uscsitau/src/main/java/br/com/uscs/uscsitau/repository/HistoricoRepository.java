package br.com.uscs.uscsitau.repository;

import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.uscs.uscsitau.model.HistoricoVO;

import java.util.List;
import java.util.UUID;

@Repository
public interface HistoricoRepository extends CrudRepository<HistoricoVO, String>{
    @Query(value = "select * from historico where id = (:id)")
    List<HistoricoVO> getHistoricoById(@Param("id") UUID id);
}
