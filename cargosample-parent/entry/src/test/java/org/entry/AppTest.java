package org.entry;

import org.junit.Assert;
import org.junit.Test;


/**
 * Unit test for simple App.
 */
public class AppTest{
    @Test
      public void test1(){
      	System.out.println("this is test1");
      	String str1 = "abc";
      	String str2 = "bcd";
      	if(!str1.equals(str2)){
      		System.out.println("str1 不等于str2");
      	}else{
      		System.out.println("str1 等于str2");
      	}
      	
      }
}
