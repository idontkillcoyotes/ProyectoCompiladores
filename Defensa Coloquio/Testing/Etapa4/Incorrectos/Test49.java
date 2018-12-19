// Acceso a un metodo con distinta cantidad de parametros

class Main {

	static void main() {}

}

class A {

	dynamic void m1() {
		(m2());
	}

	dynamic void m2(int a) {}

}