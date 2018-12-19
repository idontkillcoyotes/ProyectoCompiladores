// Incompatibilidad de tipos en una asignacion

class Main {

	static void main() {}

}

class A {

	dynamic void m1() {
		C c;
		c = new B();
	}

}

class B {

}

class C extends B {

}