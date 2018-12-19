// Uso de atributos heredados
class Main {
	static void main() {
		B b;
		b = new B(322,420);
		(b.m1());
	}
}
class A {
	private int a;
	A() {}
	dynamic void setA(int x){a=x;}
	dynamic int getA(){return a;}
}
class B extends A {
	private int b;
	B(int x, int y) {
		(setA(x));
		b = y;
	}
	dynamic void m1() {
		(System.printIln(getA()));
		(System.printIln(b));
	}
}