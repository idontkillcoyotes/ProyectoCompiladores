// Referencia a un atributo publico encadenado a un metodo
class Main {
	static void main() {
		(System.printSln("main1:"));
		A a;
		a = new A(77);		
		(System.printIln(a.getB().a));
	}
}
class A {
	private B b;
	A(int num) {
		(System.printSln("const de A"));
		b = new B(num);
	}
	dynamic B getB() {
		(System.printSln("getB de A"));
		return b;
	}
}
class B {
	public int a;	
	B(int num) {
		(System.printSln("const de B"));
		a = num;
	}
}