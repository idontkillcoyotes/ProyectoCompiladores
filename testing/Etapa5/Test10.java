// Llamada a un metodo redefinido

class Main {

	static void main() {
		A a;
		a = new B();
		(a.m1());
	}

}

class A {

	A() {}

	dynamic void m1() {
		(System.printI(1));
	}

}

class B extends A {

	B() {}

	dynamic void m1() {
		(System.printI(10));
	}

}