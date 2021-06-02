package com.bancoexterior.app.util;

import java.io.IOException;
import java.util.List;
 
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
 
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


import com.bancoexterior.app.convenio.model.Movimiento;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserExcelExporter {
	private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private List<Movimiento> listaMovimientos;
     
    public UserExcelExporter(List<Movimiento> listaMovimientos) {
       log.info("me llamo");
       log.info("listaMovimientos: "+listaMovimientos);
    	this.listaMovimientos = listaMovimientos;
        workbook = new XSSFWorkbook();
    }
 
 
    private void writeHeaderLine() {
        sheet = workbook.createSheet("Users");
         
        Row row = sheet.createRow(0);
         
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);
         
        createCell(row, 0, "Cod. Operacion", style);      
        createCell(row, 1, "Fecha Operacion", style);       
        createCell(row, 2, "Cuenta en divisas", style);    
        createCell(row, 3, "Cuenta en Bolivares", style);
        createCell(row, 4, "Monto Divisa", style);
        createCell(row, 5, "Monto Bs", style);      
        createCell(row, 6, "Estatus", style);       
        
         
    }
     
    private void createCell(Row row, int columnCount, Object value, CellStyle style) {
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        }else {
            cell.setCellValue((String) value);
        }
        cell.setCellStyle(style);
    }
     
    private void writeDataLines() {
        int rowCount = 1;
 
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        style.setFont(font);
                 
        for (Movimiento movimiento : listaMovimientos) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;
             
            createCell(row, columnCount++, movimiento.getCodOperacion(), style);
            createCell(row, columnCount++, movimiento.getFechaOperacion(), style);
            createCell(row, columnCount++, movimiento.getCuentaDivisa(), style);
            createCell(row, columnCount++, movimiento.getCuentaNacional(), style);
            createCell(row, columnCount++, movimiento.getMontoDivisa().toString(), style);
            createCell(row, columnCount++, movimiento.getMontoBsCliente().toString(), style);
            log.info("estatus: "+movimiento.getEstatus());
            if(movimiento.getEstatus() == 0) {
            	createCell(row, columnCount++, "Por Aprobar", style);
            }else {
            	if(movimiento.getEstatus() == 1) {
                	createCell(row, columnCount++, "Aprobada Automática", style);
                }else {
                	if(movimiento.getEstatus() == 2) {
                    	createCell(row, columnCount++, "Aprobada Funcional", style);
                    }else {
                    	if(movimiento.getEstatus() == 3) {
                        	createCell(row, columnCount++, "Rechazada Automática", style);
                        }else {
                        	createCell(row, columnCount++, "Rechazada Funcional", style);
                        }
                    }
                }
            }
            	
            
             
        }
    }
     
    public void export(HttpServletResponse response) throws IOException {
        writeHeaderLine();
        writeDataLines();
         
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
         
        outputStream.close();
         
    }
}
