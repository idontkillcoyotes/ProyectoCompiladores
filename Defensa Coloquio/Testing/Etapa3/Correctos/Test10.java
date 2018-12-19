// Redefinicion de un metodo de una clase ancestro no directa
class Main {
	static void main() {}
}
class A {
	dynamic int m1() {}
}
class B extends A {
}
class C extends B {
	dynamic int m1() {}
}