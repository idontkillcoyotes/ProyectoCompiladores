// Acceso a un metodo estatico desde un metodo no estatico

class Main {

	static void main() {}

}

class A {

	dynamic void m1() {
		(m2());
	}

	static void m2() {}

}