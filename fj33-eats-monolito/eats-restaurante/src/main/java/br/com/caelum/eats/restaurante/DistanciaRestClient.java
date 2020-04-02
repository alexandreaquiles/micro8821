package br.com.caelum.eats.restaurante;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
class DistanciaRestClient {

	private RestTemplate restTemplate;
	private String distanciaServiceUrl;
	
	
	public DistanciaRestClient(RestTemplate restTemplate, @Value("${configuracao.distancia.service.url}") String distanciaServiceUrl) {
		this.restTemplate = restTemplate;
		this.distanciaServiceUrl = distanciaServiceUrl;
	}

	void novoRestauranteAprovado(Restaurante restaurante) {
		
		RestauranteParaDistancia restauranteParaDistancia = new RestauranteParaDistancia(restaurante);
		
		String url = distanciaServiceUrl + "/restaurantes";
		ResponseEntity<RestauranteParaDistancia> responseEntity = restTemplate
														.postForEntity(url , restauranteParaDistancia, RestauranteParaDistancia.class);
		
		HttpStatus statusCode = responseEntity.getStatusCode();
		if (!HttpStatus.CREATED.equals(statusCode)) {
			throw new RuntimeException("Status diferente do esperado: " + statusCode);
		}
		
	}
	
	@Retryable(maxAttempts = 5, backoff = @Backoff(delay = 2000, multiplier = 2))
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	void restauranteAtualizado(Restaurante restaurante) {
		log.info("monólito tentando chamar distancia-service");

		RestauranteParaDistancia restauranteParaDistancia = new RestauranteParaDistancia(restaurante);
		
		String url = distanciaServiceUrl + "/restaurantes/" + restauranteParaDistancia.getId();
		
		restTemplate.put(url, restauranteParaDistancia);
	}
	
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class RestauranteParaDistancia {
	
	private Long id;
	private String cep;
	private Long tipoDeCozinhaId;
	
	RestauranteParaDistancia(Restaurante r) {
		this(r.getId(), r.getCep(), r.getTipoDeCozinha().getId());
	}
	
}
