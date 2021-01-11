package com.example.demo.login.domain.repository;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.example.demo.login.domain.model.User;

public interface UserDao {
								//springでは、データベース操作で例外が発生した場合、springが提供しているdataaccessExceptionを投げます。この例外クラスは、SpringJDBCだけでなくspring+mybatisを使ったときにも投げられます
	public int count() throws DataAccessException;
	
	public int insertOne(User user)throws DataAccessException;
	
	public User selectOne(String userId) throws DataAccessException;
	
	public List<User> selectMany() throws DataAccessException;
	
	public int updateOne(User user) throws DataAccessException;
	
	public int deleteOne(String userId) throws DataAccessException;
	
	public void userCsvOut() throws DataAccessException;

}

//repository.jdbcのuserdaojdbcimplファイルで実装している