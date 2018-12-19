// Constructor con tipos de parametros incorrecta

class Main {

	static void main() {}

}

class A {

	dynamic void m1() {
		B b;
		b = new B('a');
	}

}

class B {

	B(int a) {}

}