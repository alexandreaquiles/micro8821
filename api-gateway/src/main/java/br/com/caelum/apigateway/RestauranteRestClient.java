package br.com.caelum.apigateway;

import java.util.HashMap;
import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="monolito", fallback = RestauranteRestClientFallback.class)
interface RestauranteRestClient {

	@GetMapping("/restaurantes/{restauranteId}")
	Map<String, Object> porId(@PathVariable("restauranteId") Long restauranteId);
	
}

@Component
class RestauranteRestClientFallback implements RestauranteRestClient {

	@Override
	public Map<String, Object> porId(Long restauranteId) {
		//TODO: busca do cache
		HashMap<String, Object> resultado = new HashMap<>();
		resultado.put("restauranteId", restauranteId);
		resultado.put("outraInformacao", "valorDaOutraInformacao");
		return resultado;
	}
	
}
