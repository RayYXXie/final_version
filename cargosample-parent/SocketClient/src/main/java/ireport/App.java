package ireport;

import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import net.sf.jasperreports.engine.JRException;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws Exception
    {
        //System.out.println( "Hello World!" );
    	Integer subSize=1;
    	String reportType="All";//Excel Pdf
    	String path="D:/Jreport/";
    	String templatefile="report";
    	//String templatefile1="D:/Jreport/s1.jasper";
    	String outputpath="D:/output";
    	
    	
    	
    	String[] sql = {"select * from tb_expecteddata where dataid<3000","",""};
    	String driver="com.mysql.jdbc.Driver";
    	String connect="jdbc:mysql://192.168.4.61:3306/test";
    	String username="root";
    	String password="";
    	//reportType="Excel" or "Pdf" or "All";
    //JasperReportController.genPDFFile( reportType, templatefile, templatefile1, outputpath, sql, sql1, driver, connect, username,password,null);
    	//JasperReportController.genExcelFile( reportType, templatefile, templatefile1, outputpath, sql, sql1, driver, connect, username,password,null);
    	JasperReportController.genFile(path, reportType, templatefile, outputpath, sql, driver, connect, username,password,null);
    }


}
