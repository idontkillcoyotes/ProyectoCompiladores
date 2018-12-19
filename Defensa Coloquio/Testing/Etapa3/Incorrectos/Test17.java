// Redefinicion de metodo invalida por no coincidir tipo de retorno
class Main {
	static void main() {}
}
class A {
	dynamic int m1() {}
}
class B extends A {
	dynamic char m1() {}
}