package com.example.se11;

public class Exercise01 {

	public static void main(String[] args) {
		Fun fun1 = (u,v) -> u + v;
		Fun fun2 = (double u,double v) -> u * v;
		Fun fun3 = (var u,var v) -> u * v;
		Fun fun4 = (@positive double u,final @negative double v) -> u - v;
		Fun fun5 = (@positive var u,final @negative var v) -> u - v;

	}

}

@FunctionalInterface
interface Fun {
	double math(double x, double y);
}

@interface positive {}
@interface negative {}