package com.imss.sivimss.catroles.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreType;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
@ToString
@JsonIgnoreType(value = true)
public class RolResponse {

	@JsonProperty(value = "id")
	private Integer ID_ROL;
	
	@JsonProperty(value = "des_rol")
	private String DES_ROL;
	
	@JsonProperty(value = "estatus")
	private String ESTATUS;
	
	@JsonProperty(value = "nivel")
	private Integer ID_OFICINA;
	
	@JsonProperty(value = "velatorio")
	private Integer ID_VELATORIO;
	
	@JsonProperty(value = "fCreacion")
	private String FEC_ALTA;
	
	@JsonProperty(value = "claveAlta")
	private String CVE_MATRICULA_ALTA;
	
	@JsonProperty(value = "fActualizacion")
	private String FEC_ACTUALIZACION;
	
	@JsonProperty(value = "claveModifica")
	private String CVE_MATRICULA_MODIFICA;
	
	@JsonProperty(value = "fBaja")
	private String FEC_BAJA;
	
	@JsonProperty(value = "claveBaja")
	private String CVE_MATRICULA_BAJA;
	
	
}