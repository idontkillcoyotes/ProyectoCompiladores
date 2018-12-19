// Llamada a un metodo redefinido
class Main {
	static void main() {
		A a;
		a = new C();
		(a.m1());
	}
}
class A {
	A() {}
	dynamic void m1() {
		(System.printSln("m1A"));
	}
}
class B extends A {
	B() {}
	dynamic void m1() {
		(System.printSln("m1B++"));
	}
}
class C extends B {
	C() {}
	dynamic void m1() {
		(System.printSln("m1C!"));
	}
}