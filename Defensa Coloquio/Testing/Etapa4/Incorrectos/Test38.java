// Referencia a un atributo desde un metodo estatico

class Main {

	static void main() {}

}

class A {

	private int a;

	static void m1() {
		int b;
		b = a + 1;
	}

}