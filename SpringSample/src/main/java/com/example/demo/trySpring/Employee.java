package com.example.demo.trySpring;

import lombok.Data;

//リポジトリークラスやサービスクラスなどの間で渡すクラスのことを、Springではdomainクラスと呼びます。
@Data //アノテーションを付けると、getter,setterなどを自動で作成してくれます（Lombokの機能）
public class Employee {
	
	private int employeeId; //従業員ID
	private String employeeName; //従業員名
	private int age; //年齢
	
	public int getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
	}
	public String getEmployeeName() {
		return employeeName;
	}
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}

}
