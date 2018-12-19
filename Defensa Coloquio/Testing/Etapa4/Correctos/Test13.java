// Llamadas de metodos encadenadas

class Main {

	static  void main() {}

}

class A {

	dynamic void m1() {
		int a;
		int c;
		c = 1;
		A b;
		b = this;
		a = b.m2().m3(c);
	}

	dynamic A m2() {
		return new A();
	}

	dynamic int m3(int b) {
		return 1;
	}

}