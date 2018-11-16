// Referencia a un metodo redefinido

class Main {

	static void main() {
		B b;
		b = new B();
		(b.m1());
	}

}

class A {

	A() {}

	dynamic void a1() {
		(System.printIln(1));
	}

	dynamic void m1() {
		(System.printIln(2));
	}

}

class B extends A {

	B() {}

	dynamic void m1() {
		(System.printIln(3));
	}

	dynamic void m2() {
		(System.printIln(4));
	}

}