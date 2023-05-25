package com.imss.sivimss.catroles.beans;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.DatatypeConverter;

import com.imss.sivimss.catroles.model.request.RolRequest;
import com.imss.sivimss.catroles.model.request.UsuarioRequest;
import com.imss.sivimss.catroles.util.AppConstantes;
import com.imss.sivimss.catroles.util.DatosRequest;
import com.imss.sivimss.catroles.util.QueryHelper;
import com.imss.sivimss.catroles.model.request.ReporteDto;

import java.nio.charset.StandardCharsets;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
public class Rol {
	
	private Integer idRol;
	private String desRol;
	private Integer estatusRol;
	private Integer nivel;
	private String claveAlta;
	private String claveModifica;
	private String claveBaja;
	
	private static final String CURRENT_TIMESTAMP = "CURRENT_TIMESTAMP()";
	private static final String ID_USUARIO_MODIFICA = "ID_USUARIO_MODIFICA";
	private static final String FEC_ACTUALIZACION = "FEC_ACTUALIZACION";
	private static final String ID_USUARIO_BAJA = "ID_USUARIO_BAJA";
	private static final String UPDATE_SVC_ROL = "UPDATE SVC_ROL";
	private static final String CVE_ESTATUS = "IND_ACTIVO";
	private static final String FEC_BAJA = "FEC_BAJA";
	private static final String DES_ROL = "DES_ROL";
	private static final String ID_ROL = "ID_ROL = ";
	private static final String NULL = "NULL";


	public Rol(RolRequest rolRequest) {
		this.idRol = rolRequest.getIdRol();
		this.desRol = rolRequest.getDesRol();
		this.estatusRol = rolRequest.getEstatusRol();
		this.nivel= rolRequest.getNivel();
	}
	public Rol(UsuarioRequest usuarioRequest) {
		this.nivel= usuarioRequest.getIdOficina();
		this.idRol = usuarioRequest.getIdRol();
	}
	
	public DatosRequest obtenerRoles(DatosRequest request, String formatoFecha) {
		String query = "SELECT R.ID_ROL AS idRol, R.DES_ROL AS desRol,"
				+ " NO.ID_OFICINA AS nivelOficina, NO.DES_NIVELOFICINA AS desNivelOficina, date_format(R.FEC_ALTA,'" + formatoFecha+ "') AS fCreacion,"
				+ " R.IND_ACTIVO AS estatus FROM SVC_ROL AS R INNER JOIN SVC_NIVEL_OFICINA NO  ON R.ID_OFICINA = NO.ID_OFICINA "
				+ " ORDER BY ID_ROL ASC";
		request.getDatos().put(AppConstantes.QUERY, queryEncoded(query));

		return request;
	}

	public DatosRequest catalogoRol() {
		DatosRequest request = new DatosRequest();
		Map<String, Object> parametro = new HashMap<>();
		String query = "SELECT * FROM SVC_ROL R  ORDER BY ID_ROL ASC";
		parametro.put(AppConstantes.QUERY, queryEncoded(query));
		request.setDatos(parametro);
		return request;
	}

	public DatosRequest buscarFiltrosRol(DatosRequest request, Rol rol, String formatoFecha) {
		StringBuilder query = new StringBuilder(" SELECT  ID_ROL as idRol, DES_ROL as desRol, NO.ID_OFICINA AS nivelOficina, NO.DES_NIVELOFICINA AS desNivelOficina, "
				+ " R.IND_ACTIVO AS estatusRol, date_format(R.FEC_ALTA,'"+ formatoFecha+ "') AS fCreacion FROM SVC_ROL AS R "
				+ " INNER JOIN SVC_NIVEL_OFICINA NO  ON R.ID_OFICINA = NO.ID_OFICINA ");
		query.append(" WHERE IFNULL(ID_ROL,0) > 0" );
		if (rol.getNivel() != null) {
			query.append(" AND R.ID_OFICINA = ").append(this.getNivel());
		}
		if (this.getIdRol() != null) {
			query.append(" AND R.ID_ROL = ").append(this.getIdRol());
		}
		
		query.append(" ORDER BY R.ID_ROL ASC");
		
		request.getDatos().put(AppConstantes.QUERY, queryEncoded(query.toString()));
		return request;
	}
	


	public DatosRequest detalleRol(DatosRequest request, Rol rol, String formatoFecha) {
		String query = "SELECT R.ID_ROL as id, R.DES_ROL as desRol, \r\n"
				+ "NO.ID_OFICINA AS nivelOficina, NO.DES_NIVELOFICINA AS desNivelOficina, R.IND_ACTIVO AS estatusRol,\r\n"
				+ "date_format(R.FEC_ALTA, '"+ formatoFecha+ "') AS fCreacion FROM SVC_ROL AS R INNER JOIN SVC_NIVEL_OFICINA NO  ON R.ID_OFICINA = NO.ID_OFICINA WHERE ID_ROL = " 
				+ rol.getIdRol() + " ORDER BY ID_ROL DESC";

		request.getDatos().remove("id");
		request.getDatos().put(AppConstantes.QUERY, queryEncoded(query));
		return request;
	}

	public DatosRequest insertar() {
		DatosRequest request = new DatosRequest();
		Map<String, Object> parametro = new HashMap<>();
		this.desRol = limpiaCaracteresDesRol(this.desRol);
		final QueryHelper q = new QueryHelper("INSERT INTO SVC_ROL");
		q.agregarParametroValues(DES_ROL, "'" + this.desRol + "'");
		q.agregarParametroValues(CVE_ESTATUS, "1");
		q.agregarParametroValues("FEC_ALTA", CURRENT_TIMESTAMP);
		q.agregarParametroValues("ID_USUARIO_ALTA", "'" + this.claveAlta + "'");
		q.agregarParametroValues(FEC_ACTUALIZACION, NULL );
		q.agregarParametroValues(ID_USUARIO_MODIFICA, NULL);
		q.agregarParametroValues(ID_USUARIO_BAJA, NULL);
		q.agregarParametroValues(FEC_BAJA , NULL);
		q.agregarParametroValues("ID_OFICINA", "" + this.nivel + "");
		
		String query = q.obtenerQueryInsertar();
		parametro.put(AppConstantes.QUERY, queryEncoded(query));
		
		request.setDatos(parametro);
		
		return request;
	}

	public DatosRequest actualizar() {
		DatosRequest request = new DatosRequest();
		Map<String, Object> parametro = new HashMap<>();

		final QueryHelper q = new QueryHelper(UPDATE_SVC_ROL);
		q.agregarParametroValues(DES_ROL, "'" + this.desRol + "'");
		q.agregarParametroValues(CVE_ESTATUS, "" + this.estatusRol +"");
		q.agregarParametroValues("ID_OFICINA", "" + this.nivel + "");
		q.agregarParametroValues(ID_USUARIO_MODIFICA, "'" + this.claveModifica + "'");
		q.agregarParametroValues(FEC_ACTUALIZACION, CURRENT_TIMESTAMP);
		q.addWhere(ID_ROL + this.idRol);
		
		String query = q.obtenerQueryActualizar();
		parametro.put(AppConstantes.QUERY, queryEncoded(query));
		request.setDatos(parametro);
		
		return request;
	}
	
	public DatosRequest cambiarEstatus() {
		DatosRequest request = new DatosRequest();
		Map<String, Object> parametro = new HashMap<>();

		final QueryHelper q = new QueryHelper(UPDATE_SVC_ROL);
		q.agregarParametroValues(CVE_ESTATUS, "" + this.estatusRol +"");
		q.agregarParametroValues(ID_USUARIO_MODIFICA, "'" + this.claveModifica + "'");
		q.agregarParametroValues(FEC_ACTUALIZACION, CURRENT_TIMESTAMP);
		q.addWhere(ID_ROL + this.idRol);
		
		String query = q.obtenerQueryActualizar();
		parametro.put(AppConstantes.QUERY, queryEncoded(query));
		request.setDatos(parametro);
		
		return request;
	}

	public Map<String, Object> generarReporte(ReporteDto reporteDto, String nombrePdfReportes) {
		Map<String, Object> envioDatos = new HashMap<>();
		String condicion = " ";
		if ( this.idRol != null && this.nivel != null ) {
			condicion = " AND sr.ID_OFICINA = " + this.nivel + " AND sr.ID_ROL = " + this.idRol ;
		}
		else if (this.idRol != null ) {
			condicion =  " AND sr.ID_ROL = " + this.idRol ;
		}else if (this.nivel != null ) {
			condicion = " AND sr.ID_OFICINA = " + this.nivel ;
		}
		condicion = condicion + " ORDER BY sr.ID_ROL ASC";
		envioDatos.put("condicion", condicion);
		envioDatos.put("tipoReporte", reporteDto.getTipoReporte());
		envioDatos.put("rutaNombreReporte", nombrePdfReportes);

		return envioDatos;
	}
	
	private String limpiaCaracteresDesRol(String str) {
		str = str.replace("'", "\"");
		return str;
	}
	private String queryEncoded (String str) {
		return DatatypeConverter.printBase64Binary(str.getBytes(StandardCharsets.UTF_8));
	}
}