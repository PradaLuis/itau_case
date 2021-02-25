package br.com.uscs.uscsitau.controller;

import br.com.uscs.uscsitau.errorhandling.AppException;
import br.com.uscs.uscsitau.errorhandling.ErrorCode;
import br.com.uscs.uscsitau.model.HistoricoVO;
import br.com.uscs.uscsitau.repository.HistoricoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value="/historico")
public class HistoricoController {

	@Autowired
	HistoricoRepository historicoRepository;

	@GetMapping("/lista")
	public ResponseEntity listaHistoricos(){
		try {
			List<HistoricoVO> historicoVOS = (List<HistoricoVO>) historicoRepository.findAll();

		return ResponseEntity.ok().body(historicoVOS);

		} catch (Exception ex) {
			return ResponseEntity.status(500).body(new AppException(ErrorCode.BAD_REQUEST));
		}
	}

}
