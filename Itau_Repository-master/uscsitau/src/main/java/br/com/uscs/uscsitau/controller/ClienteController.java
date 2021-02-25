package br.com.uscs.uscsitau.controller;

import br.com.uscs.uscsitau.controller.dto.ClienteDTO;
import br.com.uscs.uscsitau.errorhandling.AppException;
import br.com.uscs.uscsitau.errorhandling.ErrorCode;
import br.com.uscs.uscsitau.kafka.OrderProducer;
import br.com.uscs.uscsitau.model.ClienteVO;
import br.com.uscs.uscsitau.model.ContaVO;
import br.com.uscs.uscsitau.repository.ClienteRepository;
import br.com.uscs.uscsitau.repository.ContaRepository;
import br.com.uscs.uscsitau.utils.CpfCnpj;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;
import java.util.Random;


@RestController
@RequestMapping(value="/clientes")

public class ClienteController {

    @Autowired
    ClienteRepository clienteRepository;

    @Autowired
    ContaRepository contaRepository;

    private final OrderProducer orderProducer;

    public ClienteController(OrderProducer orderProducer) {
        this.orderProducer = orderProducer;
    }

    @GetMapping("/lista")
    public List<ClienteVO> listaClientes(){
        return (List<ClienteVO>) clienteRepository.findAll();
    }

    @GetMapping("/{cpf}")
    public ResponseEntity buscaClientes(@PathVariable (value = "cpf") String cpf){

        try {

        CpfCnpj cpfCnpj = new CpfCnpj(cpf);

        if (clienteRepository.getClienteByCPFCNPJ(cpfCnpj.getCpfCnpj()).isEmpty()) {
            return ResponseEntity.status(404).body(new AppException(ErrorCode.CPF_CNPJ_NOT_FOUND));
        }

        ClienteVO clienteVO = clienteRepository.getClienteByCPFCNPJ(cpf).get(0);

        return ResponseEntity.ok().body(clienteVO);

        } catch (Exception ex) {
            return ResponseEntity.status(500).body(new AppException(ErrorCode.BAD_REQUEST));
        }
    }

    @PostMapping("/salvar")
    public ResponseEntity salvaClientes(@RequestBody ClienteDTO clienteDTO) {

        try {

            CpfCnpj cpfCnpjCadastro = new CpfCnpj(clienteDTO.getCpf_cnpj());
            ClienteVO clienteVO = new ClienteVO();

            if (!cpfCnpjCadastro.isValid()) {
                return ResponseEntity.badRequest().body(new AppException(ErrorCode.CPF_CNPJ_INVALID));
            }

            if (clienteRepository.getClienteByCPFCNPJ(new CpfCnpj(clienteDTO.getCpf_cnpj()).getCpfCnpj()).isEmpty()) {

                ContaVO contaVO = new ContaVO();
                contaVO.setAgencia(String.valueOf(new Random().nextInt(9999 - 1111) + 1 + 1111));
                contaVO.setSaldo(0);
                String num_conta = String.valueOf(new Random().nextInt(999999999 - 111111111) + 1 + 111111111).replaceAll("/\\D/g", "");
                num_conta = num_conta.replaceAll("([0-9]{8})([0-9]{1})", "$1-$2");
                contaVO.setNum_conta(num_conta);
                contaVO.setDac(Integer.parseInt(contaVO.getAgencia().replaceAll("([0-9]{3})([0-9]{1})", "$2")));
                contaRepository.save(contaVO);

                clienteVO.setNome(clienteDTO.getNome());
                clienteVO.setCpf_cnpj(new CpfCnpj(clienteDTO.getCpf_cnpj()).getCpfCnpj());
                clienteVO.setTipo_de_cliente(new CpfCnpj(clienteDTO.getCpf_cnpj()).isPJ() ? "PJ" : "PF");
                clienteVO.setEndereco(clienteDTO.getEndereco());
                clienteVO.setRenda(clienteDTO.getRenda());
                clienteVO.setRazao_social(clienteDTO.getRazao_social());
                clienteVO.setIncr_estadual(clienteDTO.getIncr_estadual());
                clienteVO.setNum_conta(contaVO.getNum_conta());

                clienteRepository.save(clienteVO);

                orderProducer.send(clienteVO, "Cadastro de cliente", new Timestamp(System.currentTimeMillis()), 1);

            } else {
                return ResponseEntity.status(400).body(new AppException(ErrorCode.CPF_CNPJ_ALREADY_EXISTS));
            }

            return ResponseEntity.ok().body(clienteVO);


        } catch (Exception ex) {
            return ResponseEntity.status(500).body(new AppException(ErrorCode.BAD_REQUEST));
        }

    }
    
    @DeleteMapping("/deletar") //Se colocado apenas o CPF o cliente é deletado! Forma de se colocar no Postman  {"cpf_cnpj": "445.000.000-15"}
    public ResponseEntity deletaClientes(@RequestBody ClienteDTO clienteDTO) {

        try {

            CpfCnpj cpfcnpj = new CpfCnpj(clienteDTO.getCpf_cnpj());

            if (clienteRepository.getClienteByCPFCNPJ(cpfcnpj.getCpfCnpj()).isEmpty()) {
                return ResponseEntity.status(404).body(new AppException(ErrorCode.CPF_CNPJ_NOT_FOUND));
            }

            ClienteVO clienteVO = clienteRepository.getClienteByCPFCNPJ(cpfcnpj.getCpfCnpj()).get(0);
            ContaVO contaVO = contaRepository.getContaByNumConta(clienteVO.getNum_conta()).get(0);

            clienteRepository.delete(clienteVO);
            contaRepository.delete(contaVO);

            orderProducer.send(clienteVO, "Cliente deletado", new Timestamp(System.currentTimeMillis()), 1);

            return ResponseEntity.status(204).build();

        } catch (Exception ex) {

            return ResponseEntity.status(500).body(new AppException(ErrorCode.BAD_REQUEST));
        }

    }
    
    @PutMapping("/atualizar") //Utilizado para atualização de cadastros
    public ResponseEntity atualizaClientes(@RequestBody ClienteDTO clienteDTO) {

        try {

            CpfCnpj cpfCnpjCadastro = new CpfCnpj(clienteDTO.getCpf_cnpj());

            if (!cpfCnpjCadastro.isValid()) {
                return ResponseEntity.badRequest().body(new AppException(ErrorCode.CPF_CNPJ_INVALID));
            }

            if (clienteRepository.getClienteByCPFCNPJ(new CpfCnpj(clienteDTO.getCpf_cnpj()).getCpfCnpj()).isEmpty()) {
                return ResponseEntity.badRequest().body(new AppException(ErrorCode.CPF_CNPJ_NOT_FOUND));
            } else {
                ClienteVO vo = clienteRepository.getClienteByCPFCNPJ(new CpfCnpj(clienteDTO.getCpf_cnpj()).getCpfCnpj()).get(0);
                vo.setNome(clienteDTO.getNome());
                vo.setEndereco(clienteDTO.getEndereco());
                vo.setRenda(clienteDTO.getRenda());
                vo.setRazao_social(clienteDTO.getRazao_social());
                vo.setIncr_estadual(clienteDTO.getIncr_estadual());
                clienteRepository.save(vo);

                return ResponseEntity.status(200).body(vo);
            }

        } catch (Exception ex) {
            return ResponseEntity.status(500).body(new AppException(ErrorCode.BAD_REQUEST));
        }
    }
}
