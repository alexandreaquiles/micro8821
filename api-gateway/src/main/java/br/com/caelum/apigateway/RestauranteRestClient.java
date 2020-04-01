package br.com.caelum.apigateway;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="restaurante", url="${zuul.routes.monolito.url}")
interface RestauranteRestClient {

	@GetMapping("/restaurantes/{restauranteId}")
	Map<String, Object> porId(@PathVariable("restauranteId") Long restauranteId);
	
}
