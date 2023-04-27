package com.imss.sivimss.catroles.service.impl;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.imss.sivimss.catroles.beans.Rol;
import com.imss.sivimss.catroles.exception.BadRequestException;
import com.imss.sivimss.catroles.model.request.ReporteDto;
import com.imss.sivimss.catroles.model.request.RolRequest;
import com.imss.sivimss.catroles.model.request.UsuarioDto;
import com.imss.sivimss.catroles.model.request.UsuarioRequest;
import com.imss.sivimss.catroles.model.response.RolResponse;
import com.imss.sivimss.catroles.service.RolService;
import com.imss.sivimss.catroles.util.AppConstantes;
import com.imss.sivimss.catroles.util.ConvertirGenerico;
import com.imss.sivimss.catroles.util.DatosRequest;
import com.imss.sivimss.catroles.util.ProviderServiceRestTemplate;
import com.imss.sivimss.catroles.util.Response;
import com.imss.sivimss.catroles.util.MensajeResponseUtil;

@Service
public class RolServiceImpl  implements RolService {
	
	private static final String AGREGADO_CORRECTAMENTE = "30"; // Agregado correctamente.
	private static final String MODIFICADO_CORRECTAMENTE = "18";  // Modificado correctamente.
	private static final String DESACTIVADO_CORRECTAMENTE = "19";  // Desactivado correctamente.
	private static final String SIN_INFORMACION = "45";  // No se encontró información relacionada a tu búsqueda.
	private static final String ACTIVADO_CORRECTAMENTE = "69";  // Activado correctamente.
	private static final String ERROR_AL_DESCARGAR_DOCUMENTO= "64"; // Error en la descarga del documento.Intenta nuevamente.


	@Value("${endpoints.dominio-consulta}")
	private String urlConsulta;
	
	@Value("${endpoints.dominio-consulta-paginado}")
	private String urlConsultaPaginado;
	
	@Value("${endpoints.dominio-crear}")
	private String urlCrear;
	
	@Value("${endpoints.dominio-actualizar}")
	private String urlActualizar;
	
	@Value("${formato_fecha}")
	private String formatoFecha;


	@Value("${reportes.pdf-reporteGestionRol}")
	private String nombrePdfReportes;

	@Value("${endpoints.ms-reportes}")
	private String urlReportes;
	
	@Autowired
	private ProviderServiceRestTemplate providerRestTemplate;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Override
	public Response<?> consultarRoles(DatosRequest request, Authentication authentication) throws IOException {
		Rol rol= new Rol();
		return MensajeResponseUtil.mensajeConsultaResponse( providerRestTemplate.consumirServicio(rol.obtenerRoles(request, formatoFecha).getDatos(), urlConsultaPaginado,
				authentication), SIN_INFORMACION );
	}

	@Override
	public Response<?> catalogoRol(DatosRequest request, Authentication authentication) throws IOException {
		Rol rol= new Rol();
		List<RolResponse> rolResponses;
		Response<?> response =  providerRestTemplate.consumirServicio(rol.catalogoRol().getDatos(),urlConsulta, authentication);
		if (response.getCodigo() == 200) {
			rolResponses = Arrays.asList(modelMapper.map(response.getDatos(), RolResponse[].class));
			response.setDatos(ConvertirGenerico.convertInstanceOfObject(rolResponses));
		}
		return response;
	}
	
	@Override
	public Response<?> buscarFiltrosRol(DatosRequest request, Authentication authentication) throws IOException {
		Gson gson = new Gson();

		String datosJson = String.valueOf(request.getDatos().get(AppConstantes.DATOS));
		UsuarioRequest usuarioRequest = gson.fromJson(datosJson, UsuarioRequest.class);
		
		Rol rol = new Rol(usuarioRequest);

		return MensajeResponseUtil.mensajeConsultaResponse(providerRestTemplate.consumirServicio(rol.buscarFiltrosRol(request, rol, formatoFecha).getDatos(), urlConsultaPaginado,
				authentication), SIN_INFORMACION);
	}

	@Override
	public Response<?> detalleRol(DatosRequest request, Authentication authentication) throws IOException {
		Rol rol = new Rol();
		
		return MensajeResponseUtil.mensajeConsultaResponse(providerRestTemplate.consumirServicio(rol.detalleRol(request, formatoFecha).getDatos(), urlConsulta,
				authentication), SIN_INFORMACION);
	}

	@Override
	public Response<?> agregarRol(DatosRequest request, Authentication authentication) throws IOException {
		Gson gson = new Gson();

		String datosJson = String.valueOf(request.getDatos().get(AppConstantes.DATOS));
		
		UsuarioDto usuarioDto = gson.fromJson((String) authentication.getPrincipal(), UsuarioDto.class);

		RolRequest rolRequest = new RolRequest();
		try {
			rolRequest = gson.fromJson(datosJson, RolRequest.class);
		} catch (Exception e) { 
			throw new BadRequestException(HttpStatus.BAD_REQUEST, "Json incorrecto o mal formado");
		}
		Rol rol = new Rol(rolRequest);
		rol.setClaveAlta(usuarioDto.getIdUsuario().toString());
		return MensajeResponseUtil.mensajeResponse( providerRestTemplate.consumirServicio(rol.insertar().getDatos(), urlCrear,authentication), AGREGADO_CORRECTAMENTE);
	}

	@Override
	public Response<?> actualizarRol(DatosRequest request, Authentication authentication) throws IOException {
		Gson gson = new Gson();

		String datosJson = String.valueOf(request.getDatos().get(AppConstantes.DATOS));
		UsuarioDto usuarioDto = gson.fromJson((String) authentication.getPrincipal(), UsuarioDto.class);

		RolRequest rolRequest = gson.fromJson(datosJson, RolRequest.class);
		if (rolRequest.getIdRol() == null) {
			throw new BadRequestException(HttpStatus.BAD_REQUEST, "Informacion incompleta");
		}
		Rol rol = new Rol(rolRequest);
		rol.setClaveModifica(usuarioDto.getIdUsuario().toString());
		return MensajeResponseUtil.mensajeResponse(providerRestTemplate.consumirServicio(rol.actualizar().getDatos(), urlActualizar,
				authentication), MODIFICADO_CORRECTAMENTE);
	}
	
	@Override
	public Response<?> cambiarEstatusRol(DatosRequest request, Authentication authentication) throws IOException {
		Gson gson = new Gson();

		String datosJson = String.valueOf(request.getDatos().get(AppConstantes.DATOS));
		UsuarioDto usuarioDto = gson.fromJson((String) authentication.getPrincipal(), UsuarioDto.class);

		RolRequest rolRequest = gson.fromJson(datosJson, RolRequest.class);
		if (rolRequest.getIdRol() == null) {
			throw new BadRequestException(HttpStatus.BAD_REQUEST, "Informacion incompleta");
		}
		Rol rol = new Rol(rolRequest);
		rol.setClaveModifica(usuarioDto.getIdUsuario().toString());
		
		if (rol.getEstatusRol()  == 1) {
			return MensajeResponseUtil.mensajeResponse(providerRestTemplate.consumirServicio(rol.cambiarEstatus().getDatos(), urlActualizar,
					authentication), ACTIVADO_CORRECTAMENTE);
		} else {
			return MensajeResponseUtil.mensajeResponse(providerRestTemplate.consumirServicio(rol.cambiarEstatus().getDatos(), urlActualizar,
					authentication), DESACTIVADO_CORRECTAMENTE);
		}
	}

	@Override
	public Response<?> generarDocumento(DatosRequest request, Authentication authentication)throws IOException {
		Gson gson = new Gson();
		String datosJson = String.valueOf(request.getDatos().get(AppConstantes.DATOS));
		RolRequest rolRequest = gson.fromJson(datosJson, RolRequest.class);
		Rol rol= new Rol();
		rol = new Rol(rolRequest);
		ReporteDto reporteDto= gson.fromJson(datosJson, ReporteDto.class);
		Map<String, Object> envioDatos = rol.generarReporte(reporteDto,nombrePdfReportes);
		return MensajeResponseUtil.mensajeConsultaResponse(providerRestTemplate.consumirServicioReportes(envioDatos, urlReportes, authentication)
				, ERROR_AL_DESCARGAR_DOCUMENTO);
		
	}

}