package pe.gob.sunat.redis.service;

import pe.gob.sunat.redis.bean.DetalleUsuarioBean;
import pe.gob.sunat.redis.bean.RespuestaDetalleUsuarioBean;
import pe.gob.sunat.redis.bean.RespuestaUsuariosBean;

public interface MongoService {
	public RespuestaUsuariosBean getUsuarios();

	public RespuestaDetalleUsuarioBean getDetalleUsuario(String user);

	public void guardarUsuario(DetalleUsuarioBean bean);

	public void eliminarUsuario(String user);

	public void cargaMasiva();
}
