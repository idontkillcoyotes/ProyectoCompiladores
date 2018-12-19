// Acceso a un metodo estatico desde un constructor

class Main {

	static void main() {}

}

class A {

	A() {
		(m1());
	}

	static void m1() {}

}