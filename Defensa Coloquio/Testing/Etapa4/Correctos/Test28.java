// Expresion binaria valida

class Main {

	static void main() {}

}

class A {

	static void m1(){
		A a;		
		B b;
		b = new B();
		a = new A();
		if (a == b) {}
	}

}

class B extends A {

}