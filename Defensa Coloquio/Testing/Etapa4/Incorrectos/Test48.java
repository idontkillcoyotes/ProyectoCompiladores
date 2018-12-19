// Acceso a un metodo estatico de una clase no definida

class Main {

	static void main() {}

}

class A {

	dynamic void m1() {
		(B.m2());
	}

}