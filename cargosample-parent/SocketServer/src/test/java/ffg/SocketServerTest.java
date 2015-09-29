package ffg;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import net.sourceforge.groboutils.junit.v1.MultiThreadedTestRunner;
import net.sourceforge.groboutils.junit.v1.TestRunnable;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.Test;

import pojo.CXFreturnResult;
import pojo.InitialDataObject;
import pojo.SocketCommunicationConfirmData;
import util.HttpClientHandler4X;
import util.ServerSocketHandlerThreadPool;
import dao.DataBaseMethodHandler;
import dao.DataBaseMethodHandlerImpl;
public class SocketServerTest {
	static int count = 0;
	public static int totalNum=0;
	static boolean FLAG = true;
    static int controlcount=0;
	@Test
	public void test1() throws Exception {
		
		ServerSocket ss = null;
		DataBaseMethodHandler dataHandler = new DataBaseMethodHandlerImpl();
		dataHandler.initDBEnvironment();
		totalNum = dataHandler.selectIniCount();
		try {
			ss = new ServerSocket(7777);
			System.out.println("start to accept...");
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//初始socket通讯确认数据
		DataBaseMethodHandler handler = new DataBaseMethodHandlerImpl();
		handler.initDBEnvironment();
		handler.createSocketComConData();
		int id = handler.getMaxSSCdataID();
		SocketCommunicationConfirmData data = null;
		TestRunnable[] trs = new TestRunnable[totalNum];
		
		while (true) {
			try {
				FIRST:
				if(controlcount == 0){
					HttpClient httpclient = new DefaultHttpClient();
					String url = "http://localhost:18080/SocketClient/HandlerServlet?id="+id;
					HttpGet httpget = new HttpGet(url);
					try {
						httpclient.execute(httpget);
						System.out.println("Server  = "+System.currentTimeMillis());
						SECOND:
						while(true){
							Thread.sleep(1000);
							data = handler.querySCCdata(id);
							if("1".equals(data.getCommunication_status())){
								String updateSql="update tb_socketcommunicationconfirm set communication_status='2' where id="+id;
								handler.updateSocketComConData(updateSql);
								//成功之后将所有的数据库连接关掉
								handler.closeAll();
								++controlcount;
								break FIRST;
							}else{
								if(++controlcount<100){
									break SECOND;
								}else{
									//失败之后将所有的数据库连接关掉
									handler.closeAll();
									throw new Exception("fail to communicate with the socketclient !");
								}
								
							}
						}
						
					} catch (ClientProtocolException e1) {
						e1.printStackTrace();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					
				}
				System.out.println(System.currentTimeMillis());
				System.out.println("totalNum :"+totalNum+"  count: "+ count);
				if(count==SocketServerTest.totalNum && count!=0){
					
					//启用多线程@Test
					MultiThreadedTestRunner mttr = new MultiThreadedTestRunner(trs);
					try { 
						// 开发并发执行数组里定义的内容 
						mttr.runTestRunnables(); 
					} catch (Throwable e) { 
						e.printStackTrace(); 
					} 
					
					
					try {
						Thread.sleep(6000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					break;
				}else{
					//监听端口，并接受来访socket
					final Socket socket = ss.accept();
					++count;
					System.out.println(count);
					
					 TestRunnable runner = new TestRunnable() { 
			                @Override 
			                public void runTest() throws Throwable {
			    				
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
			            
			            trs[count-1]=runner;
					
					//ServerSocketHandlerThreadPool.addThreadToPoll(socket);	
				}
			
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		//shutdownPools();
		

	}
	
	/**
	 * 关闭线程池
	 */
	public static void shutdownPools(){
		ServerSocketHandlerThreadPool.pool.shutdown();
	}
	
}
