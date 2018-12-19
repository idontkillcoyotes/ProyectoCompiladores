// Acceso a un metodo estatico desde un metodo estatico

class Main {

	static void main() {}

}

class A {

	static void m1() {
		(m2());
	}

	static void m2() {}

}