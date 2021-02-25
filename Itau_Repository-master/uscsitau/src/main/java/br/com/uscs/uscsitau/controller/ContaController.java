package br.com.uscs.uscsitau.controller;

import br.com.uscs.uscsitau.controller.dto.ContaDTO;
import br.com.uscs.uscsitau.errorhandling.AppException;
import br.com.uscs.uscsitau.errorhandling.ErrorCode;
import br.com.uscs.uscsitau.kafka.OrderProducer;
import br.com.uscs.uscsitau.model.ContaVO;
import br.com.uscs.uscsitau.repository.ContaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.sql.Timestamp;
import java.util.List;

@RestController
@RequestMapping(value="/conta")
public class ContaController {
	
	@Autowired
	ContaRepository contaRepository;

	private final OrderProducer orderProducer;

	public ContaController(OrderProducer orderProducer) {
		this.orderProducer = orderProducer;
	}

	@GetMapping("/lista")
	public List<ContaVO> listaContas(){
		return (List<ContaVO>) contaRepository.findAll();
	}

	@PostMapping("/credito")
	public ResponseEntity creditarConta(@RequestBody ContaDTO contaDTO){
		try {

			String num_conta;

			num_conta = contaDTO.getNum_conta().replaceAll("/\\D/g", "");

			num_conta = num_conta.replaceAll("([0-9]{8})([0-9]{1})", "$1-$2");

			if (contaRepository.getContaByNumConta(num_conta).isEmpty()) {
				return ResponseEntity.status(404).body(new AppException(ErrorCode.ACCOUNT_NOT_FOUND));
			}

			ContaVO contaVO = contaRepository.getContaByNumConta(num_conta).get(0);

			contaVO.setSaldo(contaVO.getSaldo() + contaDTO.getCredito());

			contaRepository.save(contaVO);

			orderProducer.send(contaVO, "credito de: " + contaDTO.getCredito(), new Timestamp(System.currentTimeMillis()), 1);

			return ResponseEntity.ok().body(contaVO);

		} catch (Exception ex) {
			return ResponseEntity.status(500).body(new AppException(ErrorCode.BAD_REQUEST));
		}

	}

	@PostMapping("/debito")
	public ResponseEntity debitarConta(@RequestBody ContaDTO contaDTO){
		try {

			String num_conta;

			num_conta = contaDTO.getNum_conta().replaceAll("/\\D/g", "");

			num_conta = contaDTO.getNum_conta().replaceAll("([0-9]{8})([0-9]{1})", "$1-$2");

			if (contaRepository.getContaByNumConta(num_conta).isEmpty()) {
				return ResponseEntity.status(404).body(new AppException(ErrorCode.ACCOUNT_NOT_FOUND));
			}

			ContaVO contaVO = contaRepository.getContaByNumConta(num_conta).get(0);

			if (contaVO.getSaldo() - contaDTO.getDebito() < 0) {

				orderProducer.send(contaVO, "Tentativa de debito de: " + contaDTO.getDebito(), new Timestamp(System.currentTimeMillis()), 0);
				return ResponseEntity.status(404).body(new AppException(ErrorCode.INSUFFICIENT_FUNDS));
			} else {
				contaVO.setSaldo(contaVO.getSaldo() - contaDTO.getDebito());
			}

			contaRepository.save(contaVO);

			orderProducer.send(contaVO, "Debito de: " + contaDTO.getDebito(), new Timestamp(System.currentTimeMillis()), 1);

			return ResponseEntity.ok().body(contaVO);

		} catch (Exception ex) {
			return ResponseEntity.status(500).body(new AppException(ErrorCode.BAD_REQUEST));
		}

	}

	@GetMapping("/{num_conta}")
	public ResponseEntity getContaByPK(@PathVariable(value = "num_conta") String num_conta) {
		try {

			num_conta = num_conta.replaceAll("/\\D/g", "");

			num_conta = num_conta.replaceAll("([0-9]{8})([0-9]{1})", "$1-$2");

			if (contaRepository.getContaByNumConta(num_conta).isEmpty()) {

				return ResponseEntity.status(404).body(new AppException(ErrorCode.ACCOUNT_NOT_FOUND));
			}

			ContaVO contaCorrenteVO = contaRepository.getContaByNumConta(num_conta).get(0);

			return ResponseEntity.ok().body(contaCorrenteVO);

		} catch (Exception ex) {
			return ResponseEntity.status(500).body(new AppException(ErrorCode.BAD_REQUEST));
		}
	}

}
