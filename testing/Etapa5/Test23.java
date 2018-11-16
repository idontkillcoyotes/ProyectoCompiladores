// Referencia a this

class Main {

	static public void main() {
		A a;
		a = new A();
		(System.printI(a.a));
	}

}

class A {

	public int a;

	A() {
		a = (this).m1();
	}

	dynamic public int m1() {
		return 1;
	}


}