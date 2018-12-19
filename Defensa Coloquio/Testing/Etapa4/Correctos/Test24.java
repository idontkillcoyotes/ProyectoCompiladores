// Referencia a this desde un metodo

class Main {

	static  void main() {}

}

class A {

	dynamic void m1() {
		A a;
		a = this;
	}

}