// Acceso al valor de retorno de un metodo void

class Main {

	static void main() {}

}

class A {

	dynamic void m1() {
		A a;
		a = 1;
		int b;
		b = a.m2().c;
	}

	static void m2() {}

}