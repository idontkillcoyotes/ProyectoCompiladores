// Acceso a un metodo no definido

class Main {

	static void main() {}

}

class A {

	dynamic void m1() {
		(m2());
	}

}