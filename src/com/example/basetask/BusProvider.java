package com.example.basetask;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.EBean.Scope;

import com.squareup.otto.BasicBus;

@EBean(scope = Scope.Singleton)
public class BusProvider extends BasicBus {

	// private static Bus bus;
	//
	// public static Bus getInstance() {
	// if (bus == null) {
	// bus = new Bus();
	// }
	// return bus;
	// }

}
