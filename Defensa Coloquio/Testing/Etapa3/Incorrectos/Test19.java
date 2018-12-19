// Redefinicion de metodo invalida por no coincidir tipos de parametros
class Main {
	static void main() {}
}
class A {
	dynamic int m1(int a, int b) {}
}
class B extends A {
	dynamic int m1(int b, char a) {}
}