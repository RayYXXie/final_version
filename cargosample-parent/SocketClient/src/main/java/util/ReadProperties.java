package util;

import ireport.JasperReportController;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;

import startcontrol.SocketClient;

public class ReadProperties {
	public String filePath = "";

	public void setPropertiesFilePath(String fPath) {
		this.filePath = fPath;
	}

	public String getKeyValue(String str) {
		Properties pro = new Properties();
		BufferedInputStream in = null;
		try {
			in = new BufferedInputStream(ReadProperties.class.getClassLoader()
					.getResourceAsStream(this.filePath));
			pro.load(in);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String valueStr = pro.getProperty(str);
		if (valueStr == null) {
			valueStr = pro.getProperty("A");
		}
		return valueStr;

	}

	public String[] getSqlArrayFromProperties() {
		Properties pro = new Properties();
		BufferedInputStream in = null;
		try {
			in = new BufferedInputStream(ReadProperties.class.getClassLoader()
					.getResourceAsStream(this.filePath));
			pro.load(in);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		ArrayList<String> list = new ArrayList<String>();
	
		
		for (Object name : pro.keySet().toArray()) {
			list.add(pro.getProperty(name.toString()));
		}
		
		String[] sqlArr = null;
		if(list !=null){
			sqlArr = new String[list.size()];
			int i = 0;
			for(int k=list.size()-1;k>=0;k--){
				sqlArr[i] = list.get(k);
				i++;
			}
		}
		
		return sqlArr;
	}

	
	public String[] getSortedSqlArrayFromProperties(){
		
		Properties pro = new Properties();
		BufferedInputStream in = null;
		
		try {
			in = new BufferedInputStream(ReadProperties.class.getClassLoader()
					.getResourceAsStream(this.filePath));
			pro.load(in);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Object[] keyObjArr =pro.keySet().toArray();
		int[] keyIntArr = new int[keyObjArr.length];
		for(int i=0;i<keyObjArr.length;i++){
			keyIntArr[i]=Integer.parseInt(keyObjArr[i].toString().trim());
		}
		Arrays.sort(keyIntArr);
		
		String[] sqlArr = new String[keyIntArr.length];
		String keyValue="";
	    for(int k=0;k<keyIntArr.length;k++){
	    	keyValue =keyIntArr[k]+"";
	    	sqlArr[k]=pro.getProperty(keyValue);
	    }
	    return sqlArr;
	}
	
	public static void main(String args[]){
		
		ReadProperties pro = new ReadProperties();
    	pro.setPropertiesFilePath("ireportConfig.properties");
    	String reportType=pro.getKeyValue("reportType")==null?"":pro.getKeyValue("reportType");
		String modelFilePath=SocketClient.class.getResource("/").getPath().toString()+"ireportfile/";
		System.out.println("modelFilePath = "+modelFilePath);
		String modelFileName=pro.getKeyValue("modelFileName")==null?"":pro.getKeyValue("modelFileName");
		String outputPath=pro.getKeyValue("outputPath")==null?"":pro.getKeyValue("outputPath");
		String sqlDriver=pro.getKeyValue("sqlDriver")==null?"":pro.getKeyValue("sqlDriver");
		String connectUrl=pro.getKeyValue("connectUrl")==null?"":pro.getKeyValue("connectUrl");
		String userName=pro.getKeyValue("userName")==null?"":pro.getKeyValue("userName");
		String password=pro.getKeyValue("password")==null?"":pro.getKeyValue("password");
		
		
    	pro.setPropertiesFilePath("ireportSQLConfig.properties");
    	String [] sqlArr = pro.getSortedSqlArrayFromProperties();
    

    	
    	try {
			JasperReportController.genFile(modelFilePath, reportType, modelFileName, outputPath, sqlArr, sqlDriver, connectUrl, userName,password,null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
	}
	
}
