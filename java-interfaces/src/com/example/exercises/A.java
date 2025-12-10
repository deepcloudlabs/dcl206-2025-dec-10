package com.example.exercises;

@SuppressWarnings("unused")
public abstract class A {
	private int x;

	public A(int x) {
		this.x = x;
	}

	public void fun() {
	}

	public abstract void gun();
}

class B extends A {

	public B() {
		super(42);
	}

	@Override
	public void gun() {
	}
}

abstract interface I {
	public static final int x = 42;

	abstract int fun();

	public int gun();

	public abstract int sun();

	int run();
}

interface Service {
	int fun();

	int gun();

	default int sun() {
		return 549;
	}
}

class StandardService implements Service {

	@Override
	public int fun() {
		return 42;
	}

	@Override
	public int gun() {
		return 108;
	}

}

interface P {
	default int fun() {
		return 42;
	}
}

interface Q {
	default int fun() {
		return 108;
	}
}

class R implements P, Q {
	@Override
	public int fun() {
		return 549;
	}
}