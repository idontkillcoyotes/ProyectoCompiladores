// Acceso a un metodo no estatico como estatico

class Main {

	static void main() {}

}

class A {

	dynamic void m1() {
		(B.m2());
	}

}

class B {

	dynamic void m2() {}

}