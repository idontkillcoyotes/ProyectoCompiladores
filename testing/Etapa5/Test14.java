// Referencia a un metodo publico de una clase

class Main {

	static public void main() {
		A a;
		a = new A(1, 2);
		(a.m1());
	}

}

class A {

	public int a;
	public int b;

	A(int a1, int b1) {
		a = a1;
		b = b1;
	}

	dynamic public void m1() {
		(System.printIln(a));
		(System.printIln(b));
	}

}