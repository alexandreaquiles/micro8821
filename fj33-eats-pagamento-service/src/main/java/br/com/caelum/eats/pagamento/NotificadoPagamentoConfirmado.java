package br.com.caelum.eats.pagamento;

import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import br.com.caelum.eats.pagamento.AmqPagamentoConfig.PagamentoSource;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Service
@AllArgsConstructor
class NotificadoPagamentoConfirmado {

	private PagamentoSource source;
	
	void notifica(Pagamento pagamento) {
		
		Long pagamentoId = pagamento.getId();
		Long pedidoId = pagamento.getPedidoId();

		PagamentoConfirmado pagamentoConfirmado 
			= new PagamentoConfirmado(pagamentoId , pedidoId);
		
		Message<?> message = MessageBuilder
				.withPayload(pagamentoConfirmado).build();
		source.pagamentosConfirmados().send(message);
	
	}
	
	
}

@Data
@AllArgsConstructor
@NoArgsConstructor
class PagamentoConfirmado {
	
	private Long pagamentoId;
	private Long pedidoId;
	
}







