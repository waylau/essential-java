package com.waylau.essentialjava.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 通过反射了解注解的例子.
 * 
 * @since 1.0.0 2017年3月14日
 * @author <a href="https://waylau.com">Way Lau</a> 
 */
public class AnnotationDemo {

	/**
	 * @param args
	 * @throws Exception 
	 * @throws Throwable 
	 */
	public static void main(String[] args) throws Exception, Throwable {
		AnnotationTest test = new AnnotationTest();
		
		test.execute();

		// 获取 AnnotationTest 的Class实例
	    Class<AnnotationTest> c = AnnotationTest.class;
	    
	    // 获取需要处理的方法Method实例
	    Method method = c.getMethod("execute", new Class[]{});
	    
	    // 判断该方法是否包含 MyAnnotation 注解
	    if(method.isAnnotationPresent(MyAnnotation.class)){
	    	
	        // 获取该方法的 MyAnnotation 注解实例
	        MyAnnotation myAnnotation = method.getAnnotation(MyAnnotation.class);
	        
	        // 执行该方法
	        method.invoke(test, new Object[]{});
	        
	        // 获取 myAnnotation 的属性值
	        String company = myAnnotation.company();
	        System.out.println(company);
	    }
	    
	    // 获取方法上的所有注解
	    Annotation[] annotations = method.getAnnotations();
	    for(Annotation annotation : annotations){
	        System.out.println(annotation);
	    }
	}

}

class AnnotationTest {

	@MyAnnotation(company="https://waylau.com")
    public void execute(){
        System.out.println("do something~");
    }
}
 