// Referencia a un metodo redefinido

class Main {

	static void main() {
		A a1;
		a1 = new B();
		(a1.m1());
	}

}

class A {

	A() {}

	dynamic void a1() {
		(System.printSln("a1A"));
	}

	dynamic void m1() {
		(System.printSln("m1A"));
	}

}

class B extends A {

	B() {}

	dynamic void m1() {
		(System.printSln("m1B"));
	}

	dynamic void m2() {
		(System.printSln("m2B"));
	}

}