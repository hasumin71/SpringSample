package com.example.demo.login.domain.repository.jdbc;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import com.example.demo.login.domain.model.User;
import com.example.demo.login.domain.repository.UserDao;

@Repository("UserDaoJdbcImpl")
public class UserDaoJdbcImpl implements UserDao {

	@Autowired
	JdbcTemplate jdbc;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Override
	public int count() throws DataAccessException{
		
		//全件取得してカウント　//queryForObjectの第二引数はMemberRowMapper()メソッドで生成したRowMapperオブジェクト　//Integer.classとは　
		int count = jdbc.queryForObject("SELECT COUNT(*) FROM m_user", Integer.class);
		
		return count;
	}
	
	@Override
	public int insertOne(User user) throws DataAccessException{
		
		//パスワード暗号化
		String password = passwordEncoder.encode(user.getPassword());
		
		//ユーザーテーブルに一見登録するSQL
		String sql ="INSERT INTO m_user("
				+ " user_id,"
				+ " password,"
				+ " user_name,"
				+ " birthday,"
				+ " age,"
				+ " marriage,"
				+ " role)"
				+ " VALUES(?,?,?,?,?,?,?)";
		
		//一件挿入
		int rowNumber = jdbc.update(sql,
			
				  user.getUserId(),
				  password,
				  user.getUserName(),
				  user.getBirthday(),
				  user.getAge(),
				  user.isMarriage(),
				  user.getRole());
		
		return rowNumber;
		
	}
	
	@Override
	public User selectOne(String userId) throws DataAccessException{
		
		//一件取得
		Map<String, Object>map=jdbc.queryForMap("SELECT * FROM m_user"
				+ " WHERE user_id = ?"
				,userId);
		
		//検索返却用の変数
		User user = new User();
		
		//取得したデータを結果返却用の変数にセットしていく
		user.setUserId((String)map.get("user_id"));
		user.setPassword((String)map.get("password"));
		user.setUserName((String)map.get("user_name"));
		user.setBirthday((Date)map.get("birthday"));
		user.setAge((Integer)map.get("age"));
		user.setMarriage((Boolean)map.get("marriage"));
		user.setRole((String)map.get("role"));
		
		return user;
	}
	
	@Override
	public List<User>selectMany()  throws DataAccessException{
		
		//m_userテーブルのデータを全件獲得
		List<Map<String, Object>>getList = jdbc.queryForList("SELECT * FROM m_user");
		
		//結果返却用の変数
		List<User>userList = new ArrayList<>();
		
		//取得したデータを結果返却用のListに格納していく
		for(Map<String, Object>map: getList){
			
			//Userインスタンスの生成
			User user = new User();
			
			//Userインスタンスに取得したデータをセットする
			user.setUserId((String)map.get("user_id"));
			user.setPassword((String)map.get("password"));
			user.setUserName((String)map.get("user_name"));
			user.setBirthday((Date)map.get("birthday"));
			user.setRole((String)map.get("role"));
			
			//結果返却用のListに追加
			userList.add(user);
		}
		return userList;
	}
	
	//Userテーブルを一件更新
	@Override
	public int updateOne(User user) throws DataAccessException{
		
		//パスワード暗号化
		String password = passwordEncoder.encode(user.getPassword());
		
		//一件更新
		String sql = "UPDATE m_user SET"
				+" password = ?,"
				+" user_name = ?,"
				+" birthday = ?,"
				+" age = ?,"
				+" marriage = ?"
				+" WHERE"
				+" user_id = ?";
		//一件更新
		int rowNumber = jdbc.update(sql,
				password,
				user.getUserName(),
				user.getBirthday(),
				user.getAge(),
				user.isMarriage(),
				user.getUserId());

		return rowNumber;
	}
	
	@Override
	public int deleteOne(String userId) throws DataAccessException{
		
		//一件削除
		int rowNumber = jdbc.update("DELETE FROM m_user WHERE user_id = ?", userId);
		
		return rowNumber;
	}
	
	@Override
	public void userCsvOut() throws DataAccessException{
		
		//M_USERテーブルのデータを全件取得するSQL
		String sql="SELECT * FROM m_user";
		
		//ResultSetExtractorの生成
		UserRowCallbackHandler handler = new UserRowCallbackHandler();
		
		//SQL実行＆CSV出力
		jdbc.query(sql,handler);
		
	}
	
	
}
