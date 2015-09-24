package ireport;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import startcontrol.SocketClient;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRPdfExporterParameter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

public class JasperReportController {

	public static String genFile(String path, String reportType,
			String templatefile, String outputpath, String sql[],
			String driver, String connect, String username, String password,
			List mapList) throws Exception {

		Map<String, Object> params = new HashMap<String, Object>();

		int i;
		params.put("Query_USER_SQL", sql[0]);
		for (i = 1; i < sql.length; i++) {
			params.put("Query_USER_SQL_" + i, sql[i]);
		}
		JasperReport jasperReport = null;
		JasperPrint jasperPrint;

		System.out.println("driver:" + driver);
		Class.forName(driver);// 加载数据库引擎，返回给定字符串名的类
		Connection conn = DriverManager.getConnection(connect, username,
				password);
		File[] file = new File[sql.length];
		String[] FileName = new String[sql.length];

		for (i = 1; i < sql.length; i++) {
			FileName[0] = path + templatefile + ".jasper";
			FileName[i] = path + "sub" + templatefile + i + ".jasper";
			file[0] = new File(FileName[0]);
			file[i] = new File(FileName[i]);

			jasperReport = (JasperReport) JRLoader.loadObject(file[i]);
			jasperReport = (JasperReport) JRLoader.loadObject(file[0]);

			if (!file[i].exists()) {

				JasperDesign jasperDesignSR = JRXmlLoader.load(path + "sub"
						+ templatefile + i + ".jrxml");

				JasperDesign jasperDesign = JRXmlLoader.load(path
						+ templatefile + ".jrxml");
				jasperReport = JasperCompileManager.compileReport(jasperDesign);
				JasperReport jasperReportSR = JasperCompileManager
						.compileReport(jasperDesignSR);

				params.put("SUBREPORT_DIR" + i, jasperReportSR);
			} else {

				params.put("SUBREPORT_DIR" + i, file[i]);// 存放子报表的路径
			}
		}
		jasperPrint = JasperFillManager.fillReport(jasperReport, params, conn);
		String fileName = "";
		File fi = new File(outputpath);
		if (!fi.exists()) {
			fi.mkdir();
		}
		if (reportType.equals("Pdf")) {
			fileName = reportType + "_" + GetFileName() + ".pdf";
			JRPdfExporter exporter = new JRPdfExporter();
			exporter.setParameter(JRPdfExporterParameter.JASPER_PRINT,
					jasperPrint);
			exporter.setParameter(JRPdfExporterParameter.CHARACTER_ENCODING,
					"UTF-8");
			exporter.setParameter(JRPdfExporterParameter.OUTPUT_FILE_NAME,
					outputpath + "/" + fileName);
			exporter.exportReport();
		} else if (reportType.equals("Excel")) {
			fileName = reportType + "_" + GetFileName() + ".xls";
			JRXlsExporter exporter = new JRXlsExporter();
			exporter.setParameter(JRXlsExporterParameter.JASPER_PRINT,
					jasperPrint);
			exporter.setParameter(JRXlsExporterParameter.CHARACTER_ENCODING,
					"UTF-8");
			exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET,
					Boolean.FALSE);
			exporter.setParameter(
					JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND,
					Boolean.FALSE);
			exporter.setParameter(JRXlsExporterParameter.OUTPUT_FILE_NAME,
					outputpath + "/" + fileName);
			exporter.exportReport();
		} else if (reportType.equals("All")) {
			fileName = reportType + "_" + GetFileName() + ".pdf";
			JRPdfExporter exporter = new JRPdfExporter();
			exporter.setParameter(JRPdfExporterParameter.JASPER_PRINT,
					jasperPrint);
			exporter.setParameter(JRPdfExporterParameter.CHARACTER_ENCODING,
					"UTF-8");
			exporter.setParameter(JRPdfExporterParameter.OUTPUT_FILE_NAME,
					outputpath + "/" + fileName);
			exporter.exportReport();
			String fileName2 = reportType + "_" + GetFileName() + ".xls";
			JRXlsExporter exporter2 = new JRXlsExporter();
			exporter2.setParameter(JRXlsExporterParameter.JASPER_PRINT,
					jasperPrint);
			exporter2.setParameter(JRXlsExporterParameter.CHARACTER_ENCODING,
					"UTF-8");
			exporter2
					.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET,
							Boolean.FALSE);
			exporter2.setParameter(
					JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND,
					Boolean.FALSE);
			exporter2.setParameter(JRXlsExporterParameter.OUTPUT_FILE_NAME,
					outputpath + "/" + fileName2);
			exporter2.exportReport();

		}

		return fileName;

	}

	public static String GetFileName() {
		java.util.Date dt = new java.util.Date();
		SimpleDateFormat fmt = new SimpleDateFormat("YYYYMMddHHmmssSSSS");
		String ndt = fmt.format(dt);
		return ndt;
	}
	
	public static void main(String args[]){
		
		System.out.println(SocketClient.class.getResource("/").getPath().toString()+"ireportfile/");
	}

}
