// Acceso a un metodo estatico no definido

class Main {

	static void main() {}

}

class A {

	dynamic void m1() {
		(B.m2());
	}

}

class B {

}