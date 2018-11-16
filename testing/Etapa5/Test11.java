// Uso de atributos heredados

class Main {

	static public void main() {
		B b;
		b = new B(1, 2, 3);
		(b.m1());
	}

}

class A {

	protected int a;
	protected int b;

	A() {}

}

class B extends A {

	private int c;

	B(int x, int y, int z) {
		a = x;
		b = y;
		c = z;
	}

	dynamic public void m1() {
		(System.printIln(a));
		(System.printIln(b));
		(System.printIln(c));
	}

}