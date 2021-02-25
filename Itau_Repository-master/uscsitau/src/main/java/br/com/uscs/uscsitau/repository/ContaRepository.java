package br.com.uscs.uscsitau.repository;

import br.com.uscs.uscsitau.model.ClienteVO;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.repository.CrudRepository;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.uscs.uscsitau.model.ContaVO;

import java.util.List;

@Repository
public interface ContaRepository extends CrudRepository<ContaVO, String>{

    @Query(value = "select * from conta where num_conta = (:num_conta)")
    List<ContaVO> getContaByNumConta(@Param("num_conta") String num_conta);
}
