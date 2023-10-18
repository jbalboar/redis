package pe.gob.sunat.redis.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import pe.gob.sunat.redis.entities.Usuario;

public class ExcelUtil {

	public static List<Usuario> obtenerDatosExcel(InputStream archivoInputStream) throws IOException{
		List<Usuario> lstDetalle = new ArrayList<Usuario>();

        try (Workbook workbook = new XSSFWorkbook(archivoInputStream)) {
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();

            // Omitir la fila de encabezado si es necesario
            // rowIterator.next();

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Usuario bean = new Usuario();
                bean.setUsuario(row.getCell(0).getStringCellValue());
                bean.setNombre(row.getCell(1).getStringCellValue());
                bean.setApellido(row.getCell(2).getStringCellValue());
                bean.setDireccion(row.getCell(3).getStringCellValue());
                lstDetalle.add(bean);
            }
        }
        
		return lstDetalle;
	}
}
