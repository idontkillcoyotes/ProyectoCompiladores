// uso de arreglos
class Main {
	static void main() {
		(System.printSln("main1"));
		int[] ar=new int[10];
		ar[0]=9;
		ar[2]=8;
		ar[4]=23;
		ar[6]=77;
		ar[8]=420;
		int i=0;
		while(i<10){
			(System.printIln(ar[i]));
			i=i+1;
		}
		A a1=new A(ar);
		(a1.m1());
	}
}

class A {
	public int[] ar;
	A(int[] x) {
		ar=x;
		(System.printSln("Aconst"));
	}
	dynamic void m1() {		
		(System.printSln("m1A"));		
	}

}