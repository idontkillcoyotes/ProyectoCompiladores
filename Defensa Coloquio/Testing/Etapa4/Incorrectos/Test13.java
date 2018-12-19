// Referencia a this desde un metodo estatico

class Main {

	static void main() {}

}

class A {

	static void m1() {
		A a;
		a = this;
	}

}