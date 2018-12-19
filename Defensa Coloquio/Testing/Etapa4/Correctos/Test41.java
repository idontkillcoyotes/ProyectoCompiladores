class A{
	private int x;
	private int y;
	
	A(int a,int b){
		this.x=a;
		this.y=b;
	}
	
	dynamic int getX(){
		return x;
	}
	
	dynamic int getY(){
		return y;
	}
	
	static A nuevo(int x){
		return new A(x,0);
	}
	dynamic A[] arreglos(int x){
		return new A[x];
	}
	static void main(){
		B b=new B();
		b.x=1000;		
	}
}
class B{
	private A a;
	private boolean b;
	public int x;
	
	B(){}
	
	B(A arg1,boolean arg2, int arg3){
		this.a=arg1;
		this.b=arg2;
		this.x=arg3;
	}
	
	dynamic void setA(int a,int b){
		this.a=new A(a,b);
	}
	
	dynamic A getA(){
		return a;
	}
	
	dynamic boolean esB(){
		return b;
	}
	
}
