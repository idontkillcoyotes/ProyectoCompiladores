// uso de arreglos
class Main {
	static void main() {
		//(System.printSln("main1"));
		A a1=new A();		
		a1.ar[6]=77;
		a1.ar[7]=311;
		a1.ar[8]=99;
		a1.ar[9]=1337;		
		(a1.m1());
	}
}
class A {
	public int[] ar;
	A() {
		ar=new int[10];
		ar[0]=999;
		ar[9]=-1;
		(System.printSln("Aconst"));
	}
	dynamic void m1() {		
		(System.printSln("m1A"));		
		int i=0;
		while(i<10){
			(System.printIln(ar[i]));
			i=i+1;
		}
	}

}
