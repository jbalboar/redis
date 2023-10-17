package pe.gob.sunat.redis.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import pe.gob.sunat.redis.bean.DetalleUsuarioBean;
import pe.gob.sunat.redis.bean.RespuestaDetalleUsuarioBean;
import pe.gob.sunat.redis.bean.RespuestaUsuariosBean;
import pe.gob.sunat.redis.bean.UsuariosBean;
import pe.gob.sunat.redis.entities.Usuario;

@Service
public class RedisServiceImpl implements RedisService {

	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	@Override
	public void saveData(String key, String value) {
		redisTemplate.opsForValue().set(key, value);	
	}

	@Override
	public String getData(String key) {
		 return redisTemplate.opsForValue().get(key);
	}

	@Override
	public RespuestaUsuariosBean getUsuarios(String key) {
		RespuestaUsuariosBean bean = null; 
		ListOperations<String, String> listOps = redisTemplate.opsForList();		
		List<String> lst=  listOps.range(key, 0, -1);
		
		if(lst!=null && !lst.isEmpty()) {
			bean = new RespuestaUsuariosBean();
			bean.setOrigen("REDIS");
			List<UsuariosBean> lstUsuarios = new ArrayList<UsuariosBean>();
			for (String usuario : lst) {
				UsuariosBean uBean = new UsuariosBean();
				uBean.setUser(usuario);
				lstUsuarios.add(uBean);
			}
			bean.setLstUsuarios(lstUsuarios);
		}
		return bean;
	}

	@Override
	public RespuestaDetalleUsuarioBean getDetalleUsuario(String key) {
		RespuestaDetalleUsuarioBean bean = new RespuestaDetalleUsuarioBean();
		String jsonString = getData(key);		
		
		if(jsonString == null)
			return null;
		
		DetalleUsuarioBean userBean = new DetalleUsuarioBean();
		bean.setOrigen("REDIS");
		userBean = convertJsonStringToObject(jsonString, DetalleUsuarioBean.class);
		bean.setDatos(userBean);
		System.out.println(jsonString);
		
		return bean;
	}
	
	
    private <T> T convertJsonStringToObject(String jsonString, Class<T> clazz) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(jsonString, clazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting JSON string to object", e);
        }
    }

}
