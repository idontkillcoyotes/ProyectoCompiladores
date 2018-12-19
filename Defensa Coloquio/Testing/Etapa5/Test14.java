// Referencia a un metodo de una clase

class Main {

	static void main() {
		(System.printSln("test14"));
		A a;
		a = new A(17, 42);
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

	dynamic void m1() {
		(System.printIln(a));
		(System.printIln(b));
	}

}