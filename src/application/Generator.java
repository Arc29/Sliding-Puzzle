package application;
import java.util.*;
public class Generator {
   public static Integer[][] generate(int n){
	   Integer[] arr=new Integer[n*n];
	   ArrayList<Integer> a=new ArrayList<Integer>();
	   for(int i=0;i<n*n;i++)
		   a.add(i);
	   while(true) {
		   Collections.shuffle(a);
		   arr=a.toArray(new Integer[n*n]);
		   int in=0;
		   for(int i=0;i<n*n-1;i++) {
			   for(int j=i+1;j<n*n;j++)
				   if(arr[j]<arr[i]&&arr[j]!=0)
					   in++;
		   }
		   if(n%2==1&&in%2==0)
			   break;
		   else if(n%2==0) {
			   int zero=0;
			   for(int i=0;i<n*n;i++) {
				   if(arr[i]==0) {
					   zero=i;
					   break;
				   }
			   }
			   
			   if((zero/n-n+1)%2==1&&in%2==1)
				   break;
			   else if((zero/n-n+1)%2==0&&in%2==0)
				   break;
		   }
	   }
	   int i=0;
	   Integer puz[][]=new Integer[n][n];
	   for(int p=0;p<n;p++)
		   for(int q=0;q<n;q++)
			   puz[p][q]=arr[i++];
	   return puz;
		   
	   
	   
   }
   
}