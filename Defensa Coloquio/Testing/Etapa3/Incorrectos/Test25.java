// Redefinicion de metodo invalida por no coincidir modificador de enlace
class Main {
	static void main() {}
}
class A {
	dynamic int m1() {}
}
class B extends A {
}
class C extends B {
	static int m1() {}
}