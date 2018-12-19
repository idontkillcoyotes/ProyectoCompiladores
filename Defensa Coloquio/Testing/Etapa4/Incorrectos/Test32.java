// Acceso a un metodo no accesible

class Main {

	static void main() {}

}

class A {

	dynamic void m1() {
		(m2());
	}

}

class B {

	dynamic void m2() {}

}