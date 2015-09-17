package util;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import pojo.CXFreturnResult;
import pojo.InitialDataObject;

public class ServerSocketHandlerThreadPool {
	static int coreSize = 0;
	static int maxSize = 0;
	static int aliveTime = 0;
	static{
		ReadProperties pro = new ReadProperties();
		pro.setPropertiesFilePath("ThreadPoolSetting.properties");
		coreSize = Integer.parseInt(pro.getKeyValue("serverCorePoolSize"));
		maxSize = Integer.parseInt(pro.getKeyValue("serverMaximumPoolSize"));
		aliveTime = Integer.parseInt(pro.getKeyValue("serverKeepAliveTime"));
		System.out.println("serverCoreSize = "+coreSize);
		System.out.println("serverMaxSize = "+maxSize);
		System.out.println("serverAliveTime = "+aliveTime);
	}

	public static final ExecutorService pool = new ThreadPoolExecutor(coreSize, maxSize, aliveTime, TimeUnit.SECONDS,new LinkedBlockingDeque<Runnable>() );
	static long serverStartTime =System.currentTimeMillis();
	
	//����̸߳��̳߳ش���
	public static void addThreadToPoll(final Socket socket){
		
		Runnable run = new Runnable(){

			public void run() {
				
				ObjectOutputStream oos = null;
				ObjectInputStream ois = null;
				try {
					
					// ����������
					ois = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
					//���������
					oos = new ObjectOutputStream( socket.getOutputStream() );
					//����һ������
					Object object = ois.readObject();
					InitialDataObject dataObject = (InitialDataObject) object;

					HttpClientHandler4X client4X = new HttpClientHandler4X();
					CXFreturnResult result = client4X.getHandler(dataObject);
					
					if(result == null){
						result = new CXFreturnResult();
						result.setDataId(dataObject.getDataId());
					}
					
					oos.writeObject(result);
					oos.flush();
					
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}finally {
					if(oos !=null){
						try {
							oos.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					if(ois != null){
						try {
							ois.close();
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
		pool.execute(run);
	}

	/**
	 * �ر��̳߳�
	 */
	public static void shutDownPool(){
		
		pool.shutdown();
	}


}
