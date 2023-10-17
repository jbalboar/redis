package pe.gob.sunat.redis.service;

import pe.gob.sunat.redis.bean.RespuestaDetalleUsuarioBean;
import pe.gob.sunat.redis.bean.RespuestaUsuariosBean;

public interface RedisService {
	
	public void saveData(String key, String value);
	
	public String getData(String key);
	
	public RespuestaUsuariosBean getUsuarios(String key);

	public RespuestaDetalleUsuarioBean getDetalleUsuario(String key);
}
