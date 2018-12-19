// Redefinicion de metodo invalida por no coincidir tipos de parametros
class Main {
	static void main() {}
}
class A {
	dynamic int m1(int a) {}
}
class B extends A {
}
class C extends B {
	dynamic int m1(char b) {}
}