package com.imss.sivimss.catroles.controller;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.AllArgsConstructor;

import com.imss.sivimss.catroles.service.RolService;
import com.imss.sivimss.catroles.util.DatosRequest;
import com.imss.sivimss.catroles.util.Response;

@AllArgsConstructor
@RestController
@RequestMapping("/")
public class RolController {
	
private RolService rolService;
	
	@PostMapping("roles/consulta")
	public Response<?> consultaLista(@RequestBody DatosRequest request,Authentication authentication) throws IOException {
		return rolService.consultarRoles(request,authentication);
	}
	
	@PostMapping("rol")
	public Response<?> catalogo(@RequestBody DatosRequest request,Authentication authentication) throws IOException {
		return rolService.catalogoRol(request,authentication);
	}
	
	@PostMapping("rol/buscar")
	public Response<?> buscar(@RequestBody DatosRequest request,Authentication authentication) throws IOException {
		return rolService.buscarRol(request,authentication);
	}

	@PostMapping("rol/detalle")
	public Response<?> detalle(@RequestBody DatosRequest request,Authentication authentication) throws IOException {
		return rolService.detalleRol(request,authentication);
	}
	
	@PostMapping("rol/agregar")
	public Response<?> agregar(@RequestBody DatosRequest request,Authentication authentication) throws IOException {
		return rolService.agregarRol(request,authentication);
	}
	
	@PostMapping("rol/actualizar")
	public Response<?> actualizar(@RequestBody DatosRequest request,Authentication authentication) throws IOException {
		return rolService.actualizarRol(request,authentication);
	}
	
	@PostMapping("rol/cambiar-estatus")
	public Response<?> cambiarEstatus(@RequestBody DatosRequest request,Authentication authentication) throws IOException {
		return rolService.cambiarEstatusRol(request,authentication);
	}
}
