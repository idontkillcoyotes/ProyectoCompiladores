class A{
	private int var1;
	public int var2;
	
	A(int i){
		//error
		this.var1=i;
		this.var2=i;
	}
	
	dynamic void met1(int arg1,int arg2){
		int[] x;
		int num;
		{
			num=arg1;
		}
		(this.met1(0,0)); 
		B[] asd;		
		int		k;
		asd[1].var1(57,true)[2].var2 = 2;
		k=new B[25].var1(0,true)[23].var2;
	}
	
	static void main(){
	}
}
class B{
	private int num;
	B(){}
	dynamic A[] var1(int a,boolean b){
		return new A[100];
	}
}