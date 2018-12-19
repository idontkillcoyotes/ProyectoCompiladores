// Acceso al valor de retorno de un metodo void

class Main {

	static void main() {}

}

class A {

	dynamic void m1() { }

}

class B {

	private A a;

	dynamic void m1() {
		int c;
		c = a.m1().b;
	}

}