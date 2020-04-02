package br.com.caelum.eats.distancia;

import java.net.URI;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@AllArgsConstructor
class RestaurantesController {
	
	private RestauranteRepository repo;
	
	@PostMapping("/restaurantes")
	ResponseEntity<Restaurante> adiciona(@RequestBody Restaurante restaurante, UriComponentsBuilder uriBuilder) {
		Restaurante salvo = repo.insert(restaurante);
		log.info("Inseriu o restaurante:  " + restaurante);
		
		URI location = uriBuilder
							.path("/restaurantes/{id}")
							.buildAndExpand(salvo.getId())
							.toUri();

		return ResponseEntity.created(location)
					  .contentType(MediaType.APPLICATION_JSON)
					  .body(salvo);
	}
	
	@PutMapping("/restaurantes/{id}")
	Restaurante atualiza(@PathVariable("id") Long id, @RequestBody Restaurante restaurante) {
		log.info("Atualizando o restaurante:  " + restaurante);

		if (!repo.existsById(id)) {
			throw new ResourceNotFoundException();
		}
		return repo.save(restaurante);
	}
	
	
	
	
}
