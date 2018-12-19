// Redefinicion valida de un metodo //CHECK ERROR
class Main {
	static void main() {}
}
class A {
	dynamic void m1() {}
}
class B extends A {
	dynamic void m1() {}
}