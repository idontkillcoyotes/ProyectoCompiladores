// Variables inicializadas correctamente para todos los casos

class Main {

	static  void main() {}

}

class A {

	private B b;

	dynamic void m1() {
		int c;
		b.a = 1;
		c = b.a + 1;
	}

}

class B {

	public int a;

}