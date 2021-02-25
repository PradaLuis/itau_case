package br.com.itaulistener.kafaka.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import br.com.itaulistener.kafaka.model.HistoricoVO;

@Repository
public interface HistoricoRepository extends CrudRepository<HistoricoVO, String>{

}
