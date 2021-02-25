package br.com.uscs.uscsitau.repository;

import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.uscs.uscsitau.model.ClienteVO;

import java.util.List;

@Repository
public interface ClienteRepository extends CrudRepository<ClienteVO, String> {

    @Query(value = "select * from cliente where cpf_cnpj = (:cpf_cnpj)")
    List<ClienteVO> getClienteByCPFCNPJ(@Param("cpf_cnpj") String cpf_cnpj);

}
