// expresion de lado izquierdo no valida
class Main{
	static void main(){}
}
class A{
	private int x;
	dynamic int getX(){
		return x;
	}
	dynamic int[] arX(){
		return new int[10];
	}
	dynamic void m1(){
		this.getX()= 230;
		this.arX()[0] = 1;
		this.arX()[1] = 2;
	}
}