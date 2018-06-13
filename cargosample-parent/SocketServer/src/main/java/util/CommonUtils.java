package util;

import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentMap;

//import junit.framework.Assert;

import org.apache.log4j.Logger;

import pojo.InitialDataObject;

public class CommonUtils {
	
		

	/**
	 * 鍒濆鍖朿ase鎴愬姛鐘舵�丮ap value 榛樿缃负1锛堟垚鍔燂級
	 * 
	 * @param list
	 * @param map
	 */
	public static void fillStatusMap(List<InitialDataObject> list,
			ConcurrentMap<String, String> map) {

		for (int i = 0; i < list.size(); i++) {
			String caseId = list.get(i).getDataId();
			if (caseId == null)
				continue;
			map.put(list.get(i).getDataId(), "1");
		}

	}

	/**
	 * 灏嗘渶缁堢殑缁撴灉鎵撳嵃鍒發og4j鐨勬棩蹇楁枃浠朵腑
	 * @param map
	 * @param logger
	 */
	public static void assertStatusMapValue(ConcurrentMap<String, String> map, Logger logger) {
		Iterator<Entry<String, String>> iterator = map.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<String, String> e = iterator.next();
			String key = e.getKey();
			String value = e.getValue();
			//Assert.assertEquals(value, "1");
			value = value.equals("1")?"鎴愬姛":"澶辫触";
			logger.info("caseId = "+key+"鐨凜ase鍏舵祴璇曠粨鏋滀负:"+value); 
			logger.info("caseId = "+key+"鐨凜ase鍏舵祴璇曠粨鏋滀负:"+value); 
			
		}

	}

}
