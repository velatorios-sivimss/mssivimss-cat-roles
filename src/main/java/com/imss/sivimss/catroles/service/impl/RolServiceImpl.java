package com.imss.sivimss.catroles.service.impl;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import javax.xml.bind.DatatypeConverter;

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
import com.imss.sivimss.catroles.util.LogUtil;
import com.imss.sivimss.catroles.util.ProviderServiceRestTemplate;
import com.imss.sivimss.catroles.util.Response;

import lombok.extern.slf4j.Slf4j;

import com.imss.sivimss.catroles.util.MensajeResponseUtil;

@Service
@Slf4j
public class RolServiceImpl implements RolService {

	private static final String AGREGADO_CORRECTAMENTE = "30"; // Agregado correctamente.
	private static final String MODIFICADO_CORRECTAMENTE = "18"; // Modificado correctamente.
	private static final String DESACTIVADO_CORRECTAMENTE = "19"; // Desactivado correctamente.
	private static final String SIN_INFORMACION = "45"; // No se encontró información relacionada a tu búsqueda.
	private static final String ACTIVADO_CORRECTAMENTE = "69"; // Activado correctamente.
	private static final String ERROR_AL_DESCARGAR_DOCUMENTO = "64"; // Error en la descarga del documento.Intenta  nuevamente.
	private static final String CONSULTA = "consulta";
	private static final String ALTA = "alta";
	private static final String GENERACION = "Generacion de Documento";
	private static final String MODIFICACION = "modificacion";

	Response<?> response;

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

	@Autowired
	private LogUtil logUtil;


	@Override
	public Response<?> consultarRoles(DatosRequest request, Authentication authentication) throws IOException {
		Rol rol = new Rol();
		try {
			// logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(),
			//		this.getClass().getPackage().toString(), "consultarRoles", CONSULTA, authentication);
			response = providerRestTemplate.consumirServicio(rol.obtenerRoles(request, formatoFecha).getDatos(), urlConsultaPaginado, authentication);
			return MensajeResponseUtil.mensajeConsultaResponse(response, SIN_INFORMACION);
		} catch (Exception e) {
			String consulta = rol.obtenerRoles(request, formatoFecha).getDatos().get(AppConstantes.QUERY).toString();
			String decoded = new String(DatatypeConverter.parseBase64Binary(consulta));
			log.error("Error al ejecutar el query " + decoded);
			// logUtil.crearArchivoLog(Level.WARNING.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), "Fallo al ejecutar el query: " + decoded, CONSULTA,
			//		authentication);
			throw new IOException("52", e.getCause());
		}
	}

	@Override
	public Response<?> catalogoRol(DatosRequest request, Authentication authentication) throws IOException {
		Rol rol = new Rol();
		try {
			List<RolResponse> rolResponses;
			// logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(),
			//		this.getClass().getPackage().toString(), "catalogoRol", CONSULTA, authentication);
			response = providerRestTemplate.consumirServicio(rol.catalogoRol().getDatos(), urlConsulta, authentication);
			if (response.getCodigo() == 200) {
				rolResponses = Arrays.asList(modelMapper.map(response.getDatos(), RolResponse[].class));
				response.setDatos(ConvertirGenerico.convertInstanceOfObject(rolResponses));
			}
			return MensajeResponseUtil.mensajeConsultaResponse(response, SIN_INFORMACION);
		} catch (Exception e) {
			String consulta = rol.catalogoRol().getDatos().get(AppConstantes.QUERY).toString();
			String decoded = new String(DatatypeConverter.parseBase64Binary(consulta));
			log.error("Error al ejecutar el query " + decoded);
			// logUtil.crearArchivoLog(Level.WARNING.toString(), this.getClass().getSimpleName(),
			//		this.getClass().getPackage().toString(), "Fallo al ejecutar el query: " + decoded, CONSULTA,
			//		authentication);
			throw new IOException("52", e.getCause());
		}
	}

	@Override
	public Response<?> buscarFiltrosRol(DatosRequest request, Authentication authentication) throws IOException {
		Gson gson = new Gson();

		String datosJson = String.valueOf(request.getDatos().get(AppConstantes.DATOS));
		UsuarioRequest usuarioRequest = gson.fromJson(datosJson, UsuarioRequest.class);

		Rol rol = new Rol(usuarioRequest);

		try {
			// logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(),
			//		this.getClass().getPackage().toString(), "buscarFiltrosRol", CONSULTA, authentication);
			response = providerRestTemplate.consumirServicio(
					rol.buscarFiltrosRol(request, rol, formatoFecha).getDatos(), urlConsultaPaginado, authentication);
			return MensajeResponseUtil.mensajeConsultaResponse(response, SIN_INFORMACION);
		} catch (Exception e) {
			String consulta = rol.buscarFiltrosRol(request, rol, formatoFecha).getDatos().get(AppConstantes.QUERY)
					.toString();
			String decoded = new String(DatatypeConverter.parseBase64Binary(consulta));
			log.error("Error al ejecutar el query " + decoded);
			// logUtil.crearArchivoLog(Level.WARNING.toString(), this.getClass().getSimpleName(),
			//		this.getClass().getPackage().toString(), "Fallo al ejecutar el query: " + decoded, CONSULTA,
			//		authentication);
			throw new IOException("52", e.getCause());
		}
	}

	@Override
	public Response<?> detalleRol(DatosRequest request, Authentication authentication) throws IOException {

		Gson gson = new Gson();

		String datosJson = String.valueOf(request.getDatos().get(AppConstantes.DATOS));
		UsuarioRequest usuarioRequest = gson.fromJson(datosJson, UsuarioRequest.class);

		Rol rol = new Rol(usuarioRequest);

		try {
			// logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(),	this.getClass().getPackage().toString(), "detalleRol", CONSULTA, authentication);
			response = providerRestTemplate.consumirServicio(rol.detalleRol(request, rol, formatoFecha).getDatos(),
					urlConsulta, authentication);

			return MensajeResponseUtil.mensajeConsultaResponse(response, SIN_INFORMACION);
		} catch (Exception e) {
			String consulta = rol.detalleRol(request, rol, formatoFecha).getDatos().get(AppConstantes.QUERY).toString();
			String decoded = new String(DatatypeConverter.parseBase64Binary(consulta));
			log.error("Error al ejecutar el query " + decoded);
			// logUtil.crearArchivoLog(Level.WARNING.toString(), this.getClass().getSimpleName(),
			//		this.getClass().getPackage().toString(), "Fallo al ejecutar el query: " + decoded, CONSULTA,
			//		authentication);
			throw new IOException("52", e.getCause());
		}
	}

	@Override
	public Response<?> agregarRol(DatosRequest request, Authentication authentication) throws IOException {
		Gson gson = new Gson();

		String datosJson = String.valueOf(request.getDatos().get(AppConstantes.DATOS));

		UsuarioDto usuarioDto = gson.fromJson((String) authentication.getPrincipal(), UsuarioDto.class);

		RolRequest rolRequest = new RolRequest();

		rolRequest = gson.fromJson(datosJson, RolRequest.class);
		Rol rol = new Rol(rolRequest);
		try {
			rol.setClaveAlta(usuarioDto.getIdUsuario().toString());
			// logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(),	this.getClass().getPackage().toString(), "agregarRol", ALTA, authentication);
			response = providerRestTemplate.consumirServicio(rol.insertar().getDatos(), urlCrear, authentication);
			return MensajeResponseUtil.mensajeResponse(response, AGREGADO_CORRECTAMENTE);
		} catch (Exception e) {
			String consulta = rol.insertar().getDatos().get(AppConstantes.QUERY).toString();
			String decoded = new String(DatatypeConverter.parseBase64Binary(consulta));
			log.error("Error al ejecutar el query " + decoded);
			// logUtil.crearArchivoLog(Level.WARNING.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), "Fallo al ejecutar el query: " + decoded, ALTA,
			//		authentication);
			throw new IOException("52", e.getCause());
		}
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
		rol.setClaveAlta(usuarioDto.getIdUsuario().toString());
		try {
			// logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(),	this.getClass().getPackage().toString(), "actualizarRol", MODIFICACION, authentication);
			response = providerRestTemplate.consumirServicio(rol.actualizar().getDatos(), urlActualizar, authentication);
			return MensajeResponseUtil.mensajeResponse(response, MODIFICADO_CORRECTAMENTE);
		} catch (Exception e) {
			String consulta = rol.actualizar().getDatos().get(AppConstantes.QUERY).toString();
			String decoded = new String(DatatypeConverter.parseBase64Binary(consulta));
			log.error("Error al ejecutar el query " + decoded);
			// logUtil.crearArchivoLog(Level.WARNING.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), "Fallo al ejecutar el query: " + decoded, MODIFICACION,
			//		authentication);
			throw new IOException("52", e.getCause());
		}
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

		try {
			// logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(),	this.getClass().getPackage().toString(), "cambiarEstatusRol", MODIFICACION, authentication);
			response = providerRestTemplate.consumirServicio(rol.cambiarEstatus().getDatos(), urlActualizar, authentication);
			if (rol.getEstatusRol() == 1) {
				return MensajeResponseUtil.mensajeResponse(response, ACTIVADO_CORRECTAMENTE);
			} else {
				return MensajeResponseUtil.mensajeResponse(response, DESACTIVADO_CORRECTAMENTE);
			}
		} catch (Exception e) {
			String consulta = rol.cambiarEstatus().getDatos().get(AppConstantes.QUERY).toString();
			String decoded = new String(DatatypeConverter.parseBase64Binary(consulta));
			log.error("Error al ejecutar el query " + decoded);
			// logUtil.crearArchivoLog(Level.WARNING.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), "Fallo al ejecutar el query: " + decoded, MODIFICACION,
			//		authentication);
			throw new IOException("52", e.getCause());
		}
	}

	@Override
	public Response<?> generarDocumento(DatosRequest request, Authentication authentication) throws IOException {
		Gson gson = new Gson();
		String datosJson = String.valueOf(request.getDatos().get(AppConstantes.DATOS));
		RolRequest rolRequest = gson.fromJson(datosJson, RolRequest.class);
		Rol rol = new Rol();
		rol = new Rol(rolRequest);
		ReporteDto reporteDto = gson.fromJson(datosJson, ReporteDto.class);
		Map<String, Object> envioDatos = rol.generarReporte(reporteDto, nombrePdfReportes);
		try {
			// logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(),	this.getClass().getPackage().toString(), "generarDocumento", GENERACION, authentication);
			response = providerRestTemplate.consumirServicioReportes(envioDatos, urlReportes, authentication);
		return MensajeResponseUtil.mensajeConsultaResponse(response,
				ERROR_AL_DESCARGAR_DOCUMENTO);
		} catch (Exception e) {
			String consulta = rol.generarReporte(reporteDto, nombrePdfReportes).get("condicion").toString();
			String decoded = new String(DatatypeConverter.parseBase64Binary(consulta));
			log.error("Error al ejecutar el query " + decoded);
			// logUtil.crearArchivoLog(Level.WARNING.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), "Fallo al ejecutar el query: " + consulta, GENERACION,
			//		authentication);
			throw new IOException("52", e.getCause());
		}

	}

}