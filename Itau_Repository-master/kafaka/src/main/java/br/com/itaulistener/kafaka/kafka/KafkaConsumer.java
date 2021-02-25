package br.com.itaulistener.kafaka.kafka;//package br.com.uscs.uscsitau.kafka;

import br.com.itaulistener.kafaka.controller.dto.HistoricoDTO;
import br.com.itaulistener.kafaka.model.HistoricoVO;
import br.com.itaulistener.kafaka.repository.HistoricoRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@EnableKafka
@KafkaListener(topics = "foo")
public class KafkaConsumer {

	@Autowired
	HistoricoRepository historicoRepository;

	@KafkaHandler
	public void consume(@Payload String text) throws JsonProcessingException {
		System.out.println("Evento recebido: " + text);

		HistoricoDTO historicoDTO = new ObjectMapper().readValue(text, HistoricoDTO.class);

		HistoricoVO historicoVO = new HistoricoVO();
		historicoVO.setId(historicoDTO.getId());
		historicoVO.setNum_conta(historicoDTO.getNum_conta());
		historicoVO.setTipo_de_transacao(historicoDTO.getTipo_de_transacao());
		historicoVO.setData(historicoDTO.getData());
		historicoVO.setStatus(historicoDTO.getStatus());

		historicoRepository.save(historicoVO);
	}

}