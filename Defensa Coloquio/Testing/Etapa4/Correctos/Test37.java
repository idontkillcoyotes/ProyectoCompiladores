// Sentencia de retorno con un tipo correcto

class Main {

	static void main() {}

}

class A {

	dynamic B m1() {
		C c;
		c = new C();
		return c;
	}

}

class B {

}

class C extends B {

}