// This class invokes the methods to create a tree,
// insert, delete and serach for  elements on it
public class BT {

    public int Start(){
	Tree root ;
	boolean ntb ;
	int nti ;

	root = new Tree();
	ntb = root.Init(16);
	ntb = root.Print();
	System.out.println(100000000);
	ntb = root.Insert(8) ;
	ntb = root.Print();
	ntb = root.Insert(24) ;
	ntb = root.Insert(4) ;
	ntb = root.Insert(12) ;
	ntb = root.Insert(20) ;
	ntb = root.Insert(28) ;
	ntb = root.Insert(14) ;
	ntb = root.Print();
	System.out.println(root.Search(24));
	System.out.println(root.Search(12));
	System.out.println(root.Search(16));
	System.out.println(root.Search(50));
	System.out.println(root.Search(12));
	ntb = root.Delete(12);
	ntb = root.Print();
	System.out.println(root.Search(12));

	return 0 ;
    }

}

public class Tree{
    Tree left ;
    Tree right;
    int key ;
    boolean has_left ;
    boolean has_right ;
    Tree my_null ;

    // Initialize a node with a key value and no children
    public boolean Init(int v_key){
		key = v_key ;
		has_left = false ;
		has_right = false ;
		return true ;
    }
}