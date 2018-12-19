// Asignacion valida con tipos compatibles

class Main {

	static  void main() {}

}

class A {

	dynamic void m1() {
		B b;
		b = new C();
	}

}

class B {

}

class C extends B {

}