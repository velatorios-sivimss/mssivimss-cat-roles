package com.imss.sivimss.catroles.controller;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.imss.sivimss.catroles.service.RolService;
import com.imss.sivimss.catroles.util.DatosRequest;
import com.imss.sivimss.catroles.util.LogUtil;
import com.imss.sivimss.catroles.util.ProviderServiceRestTemplate;
import com.imss.sivimss.catroles.util.Response;

import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/roles")
public class RolController {
	
	@Autowired
	private RolService rolService;
	
	@Autowired
	private ProviderServiceRestTemplate providerRestTemplate;

	@Autowired
	private LogUtil logUtil;
	
	private static final String CONSULTA = "consulta";
	
	@PostMapping("/consulta")
	@CircuitBreaker(name = "msflujo", fallbackMethod = "fallbackGenerico")
	@Retry(name = "msflujo", fallbackMethod = "fallbackGenerico")
	@TimeLimiter(name = "msflujo")
	public CompletableFuture<Object> consultaLista(@RequestBody DatosRequest request,Authentication authentication) throws IOException {
		Response<Object> response =   rolService.consultarRoles(request,authentication);
		
		return CompletableFuture.supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo())));
	}
	
	@PostMapping
	@CircuitBreaker(name = "msflujo", fallbackMethod = "fallbackGenerico")
	@Retry(name = "msflujo", fallbackMethod = "fallbackGenerico")
	@TimeLimiter(name = "msflujo")
	public CompletableFuture<Object> catalogo(@RequestBody DatosRequest request,Authentication authentication) throws IOException {
		Response<Object> response =   rolService.catalogoRol(request,authentication);
		
		return CompletableFuture.supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo())));
	}
	
	@PostMapping("/buscar-filtros")
	@CircuitBreaker(name = "msflujo", fallbackMethod = "fallbackGenerico")
	@Retry(name = "msflujo", fallbackMethod = "fallbackGenerico")
	@TimeLimiter(name = "msflujo")
	public CompletableFuture<Object> buscar(@RequestBody DatosRequest request,Authentication authentication) throws IOException {
		Response<Object> response =   rolService.buscarFiltrosRol(request,authentication);
		
		return CompletableFuture.supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo())));
	}

	@PostMapping("/detalle")
	@CircuitBreaker(name = "msflujo", fallbackMethod = "fallbackGenerico")
	@Retry(name = "msflujo", fallbackMethod = "fallbackGenerico")
	@TimeLimiter(name = "msflujo")
	public CompletableFuture<Object> detalle(@RequestBody DatosRequest request,Authentication authentication) throws IOException {
		Response<Object> response =  rolService.detalleRol(request,authentication);
		return CompletableFuture.supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo())));
	}
	
	@PostMapping("/agregar")
	@CircuitBreaker(name = "msflujo", fallbackMethod = "fallbackGenerico")
	@Retry(name = "msflujo", fallbackMethod = "fallbackGenerico")
	@TimeLimiter(name = "msflujo")
	public CompletableFuture<Object> agregar(@RequestBody DatosRequest request,Authentication authentication) throws IOException {
		Response<Object> response =  rolService.agregarRol(request,authentication);
		return CompletableFuture.supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo())));
	}
	
	@PostMapping("/actualizar")
	@CircuitBreaker(name = "msflujo", fallbackMethod = "fallbackGenerico")
	@Retry(name = "msflujo", fallbackMethod = "fallbackGenerico")
	@TimeLimiter(name = "msflujo")
	public CompletableFuture<Object> actualizar(@RequestBody DatosRequest request,Authentication authentication) throws IOException {
		Response<Object> response =  rolService.actualizarRol(request,authentication);
		return CompletableFuture.supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo())));
	}
	
	@PostMapping("/cambiar-estatus")
	@CircuitBreaker(name = "msflujo", fallbackMethod = "fallbackGenerico")
	@Retry(name = "msflujo", fallbackMethod = "fallbackGenerico")
	@TimeLimiter(name = "msflujo")
	public CompletableFuture<Object>  cambiarEstatusRol(@RequestBody DatosRequest request,Authentication authentication) throws IOException {		
		Response<Object> response = rolService.cambiarEstatusRol(request,authentication);
		return CompletableFuture.supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo())));
	}
	@PostMapping("/generarDocumento")
	@CircuitBreaker(name = "msflujo", fallbackMethod = "fallbackGenerico")
	@Retry(name = "msflujo", fallbackMethod = "fallbackGenerico")
	@TimeLimiter(name = "msflujo")
	public CompletableFuture<Object>  generarDocumento(@RequestBody DatosRequest request,Authentication authentication) throws IOException {
	
		Response<Object> response =  rolService.generarDocumento(request,authentication);
		return CompletableFuture.supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo())));  
	}
	/**
	 * fallbacks generico
	 * 
	 * @return respuestas
	 * @throws IOException 
	 */
	private CompletableFuture<Object> fallbackGenerico(@RequestBody DatosRequest request, Authentication authentication,
			CallNotPermittedException e) throws IOException {
		Response<Object> response = providerRestTemplate.respuestaProvider(e.getMessage());
		 logUtil.crearArchivoLog(Level.INFO.toString(),this.getClass().getSimpleName(),this.getClass().getPackage().toString(),e.getMessage(),CONSULTA + " " + request.getDatos().toString(),authentication);
		return CompletableFuture
				.supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo())));
	}

	private CompletableFuture<Object> fallbackGenerico(@RequestBody DatosRequest request, Authentication authentication,
			RuntimeException e)  throws IOException  {
		Response<Object> response = providerRestTemplate.respuestaProvider(e.getMessage());
		 logUtil.crearArchivoLog(Level.INFO.toString(),this.getClass().getSimpleName(),this.getClass().getPackage().toString(),e.getMessage(),CONSULTA+" "+request.getDatos().toString(),authentication);
		
		return CompletableFuture
				.supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo())));
	}

	private CompletableFuture<Object> fallbackGenerico(@RequestBody DatosRequest request, Authentication authentication,
			NumberFormatException e)  throws IOException  {
		Response<Object> response = providerRestTemplate.respuestaProvider(e.getMessage());
		 logUtil.crearArchivoLog(Level.INFO.toString(),this.getClass().getSimpleName(),this.getClass().getPackage().toString(),e.getMessage(),CONSULTA + " " + request.getDatos().toString(),authentication);
		
		return CompletableFuture
				.supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo())));
	}

}