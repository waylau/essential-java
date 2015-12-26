/**
 * 
 */
package com.waylau.essentialjava.object.interfacebiycledemo;

/**
 * @author <a href="http://www.waylau.com">waylau.com</a>
 * @date 2015年12月25日
 */
interface Bicycle {

	// wheel revolutions per minute
	void changeCadence(int newValue);

	void changeGear(int newValue);

	void speedUp(int increment);

	void applyBrakes(int decrement);
}
