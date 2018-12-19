class A {
	static void main() {
		A a1;
		a1=new A();
		int n=System.readI();
		(a1.m1(n));
	}

	dynamic void m1(int a){
		int i=0;
		(System.printS("en m1: "));
		(System.printIln(a));		
		while (i<=a){
			(System.printIln(i));
			i=i+1;
		}
	}
}