/**
 * 
 */
package com.waylau.essentialjava.exception.trywithresources;

/**
 * @author <a href="http://www.waylau.com">waylau.com</a>
 * @date 2016年2月3日
 */
public class Demo {
    public static void main(String[] args) {
        try(Resource res = new Resource()) {
            res.doSome();
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }
}

class Resource implements AutoCloseable {
    void doSome() {
        System.out.println("do something");
    }
    @Override
    public void close() throws Exception {
        System.out.println("resource is closed");
    }
}