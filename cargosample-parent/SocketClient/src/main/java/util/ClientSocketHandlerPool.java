package util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import dao.DataBaseMethodHandler;
import dao.DataBaseMethodHandlerImpl;
import pojo.CXFreturnResult;
import pojo.ExpectedDataObject;
import pojo.InitialDataObject;
import pojo.ResultDataObject;
import startcontrol.SocketClient;

public class ClientSocketHandlerPool {
	static int coreSize = 0;
	static int maxSize = 0;
	static int aliveTime = 0;
	static{
		
		ReadProperties pro = new ReadProperties();
		pro.setPropertiesFilePath("ThreadPoolSetting.properties");
		coreSize = Integer.parseInt(pro.getKeyValue("clientCorePoolSize"));
		maxSize = Integer.parseInt(pro.getKeyValue("clientMaximumPoolSize"));
		aliveTime = Integer.parseInt(pro.getKeyValue("clientKeepAliveTime"));
		System.out.println("clientCoreSize = "+coreSize);
		System.out.println("clientMaxSize = "+maxSize);
		System.out.println("clientAliveTime = "+aliveTime);

	}

	
	public static final ExecutorService threadPool = new ThreadPoolExecutor(coreSize, maxSize, aliveTime, TimeUnit.SECONDS,new LinkedBlockingDeque<Runnable>() );

	/**
	 * ��ÿһ����ݽ���һ���̣߳�ͬʱ���߳̽����̳߳ع���
	 * @param socket ��������ͨѶsocket
	 * @param object ��������case��һ�����
	 */
	public static void addSocketToThreadPool(final Socket socket, final InitialDataObject object) {

		Runnable run = new Runnable() {

			public void run() {
				
				ObjectInputStream ois = null;
				ObjectOutputStream oos = null;
				Object obj = null;
				CXFreturnResult result = null;
				try {
					
					oos = new ObjectOutputStream(socket.getOutputStream());
					oos.writeObject(object);
					oos.flush();
					ois = new ObjectInputStream(socket.getInputStream());
					obj = ois.readObject();
					result = (CXFreturnResult) obj;
					String dataId = result.getDataId();
					
					DataBaseMethodHandler dataHandler = new DataBaseMethodHandlerImpl();
					dataHandler.initDBEnvironment();
					ExpectedDataObject expectData = dataHandler.getDataObjectById(dataId);
					
					
					//Assert.assertEquals(result.getExpectedValue(),expectData.getExpectedValue());
					
					String passResult = "2";
					if(Float.parseFloat(result.getExpectedValue())== expectData.getExpectedValue()){
						passResult = "1";
					}
					
					ResultDataObject insertObj = new ResultDataObject();
					insertObj.setDataId(result.getDataId());
					insertObj.setAccount(result.getAccount());
					insertObj.setActuallyValue(Float.parseFloat(result.getExpectedValue()));
					insertObj.setExpectedValue(expectData.getExpectedValue());
					
					insertObj.setModifyTime(DateFormatUtil.getFormatNowTime("yyyyMMddHHmmss"));
					insertObj.setMsgType(result.getMsgType());
					insertObj.setOnbehalfOfCompId(result.getOnBehalfOfCompID());
					insertObj.setResult(passResult);
					insertObj.setTransactTime(result.getTransactTime());
					dataHandler.initDBEnvironment();
					dataHandler.insertDataToDB(insertObj);
					SocketClient.processMonitor();
					
					
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}finally {
					
					if(ois != null){
						try {
							ois.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					if(oos !=null){
						try {
							oos.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					if (socket != null) {
						try {
							socket.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		};
		threadPool.execute(run);

	}
	
	
	/**
	 * �ر��̳߳�
	 */
	public static void shutDownThreadPool(){
		threadPool.shutdown();
	}


}
