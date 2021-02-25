package br.com.uscs.uscsitau.kafka;

import br.com.uscs.uscsitau.controller.dto.HistoricoDTO;
import br.com.uscs.uscsitau.repository.HistoricoRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import java.sql.Timestamp;
import java.util.UUID;

@Component
public class OrderProducer {

    @Autowired
    HistoricoRepository historicoRepository;

    @Value("${spring.kafka.consumer.topic}")
    private String orderTopic;

    private final KafkaTemplate kafkaTemplate;

    public OrderProducer(final KafkaTemplate kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(final @RequestBody Object obj, String tipoDeOper, Timestamp date, Integer status) throws JsonProcessingException {

        Gson gson = new Gson();
        HistoricoDTO historicoDTO = gson.fromJson(new ObjectMapper().writeValueAsString(obj), HistoricoDTO.class);
        historicoDTO.setId(UUID.randomUUID());
        historicoDTO.setTipo_de_transacao(tipoDeOper);
        historicoDTO.setData(date);
        historicoDTO.setStatus(status);

        if (historicoRepository.getHistoricoById(historicoDTO.getId()).isEmpty()) {
            kafkaTemplate.send(orderTopic, new ObjectMapper().writeValueAsString(historicoDTO));
        }

    }
}
