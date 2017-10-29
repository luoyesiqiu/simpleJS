function fib(n)
{
	if(n<2)
		return n;
	else
		return fib(n-2)+fib(n-1);
}
var N=40;
var start=new Date().getTime();
print('计算结果：'+fib(N)+'\n');
var end =new Date().getTime();
print('计算'+N+'个斐波那契数列耗时：'+(end-start)/1000.0+'秒');