/**
 * 
 */
package com.waylau.essentialjava.object.interfacebiycledemo;

/**
 * @author <a href="http://www.waylau.com">waylau.com</a>
 * @date 2015年12月25日
 */
class ACMEBicycle implements Bicycle {

	int cadence = 0;
	int speed = 0;
	int gear = 1;

	// The compiler will now require that methods
	// changeCadence, changeGear, speedUp, and applyBrakes
	// all be implemented. Compilation will fail if those
	// methods are missing from this class.

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.waylau.essentialjava.interfaceBicycleDemo.Bicycle#changeCadence(int)
	 */
	@Override
	public void changeCadence(int newValue) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.waylau.essentialjava.interfaceBicycleDemo.Bicycle#changeGear(int)
	 */
	@Override
	public void changeGear(int newValue) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.waylau.essentialjava.interfaceBicycleDemo.Bicycle#speedUp(int)
	 */
	@Override
	public void speedUp(int increment) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.waylau.essentialjava.interfaceBicycleDemo.Bicycle#applyBrakes(int)
	 */
	@Override
	public void applyBrakes(int decrement) {
		// TODO Auto-generated method stub

	}

}
