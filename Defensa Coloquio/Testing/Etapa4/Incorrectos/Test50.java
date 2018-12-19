// Acceso a un metodo con distinta tipo de parametros

class Main {

	static void main() {}

}

class A {

	dynamic void m1() {
		(m2('a'));
	}

	dynamic void m2(int a) {}

}