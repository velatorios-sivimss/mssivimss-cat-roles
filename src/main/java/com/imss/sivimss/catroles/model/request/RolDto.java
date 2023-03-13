package com.imss.sivimss.catroles.model.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class RolDto {

	private Integer id;
	private String des_rol;
	private String estatus;
	private String cve_matricula_alta;
	private Integer nivel;
}
