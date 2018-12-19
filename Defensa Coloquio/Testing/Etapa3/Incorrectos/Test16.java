// Redefinicion de metodo invalida por no coincidir forma
class Main {
	static void main() {}
}
class A {
	dynamic int m1() {}
}
class B extends A {
	static int m1() {}
}