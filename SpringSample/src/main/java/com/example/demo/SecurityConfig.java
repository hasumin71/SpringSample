package com.example.demo;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

//セキュリティ設定用クラス
@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	//データソース
	@Autowired
	private DataSource dataSource;
	
	//ユーザーDとパスワードを取得するSQL文
	private static final String USER_SQL ="SELECT"
			+ " user_id,"
			+ " password,"
			+ " true"
			+ " FROM"
			+ " m_user"
			+ " WHERE"
			+ " user_id = ?";
	
	//ユーザーのロールを取得するSQL文
	private static final String ROLE_SQL = "SELECT"
			+ " user_id,"
			+ " role"
			+ " FROM"
			+ " m_user"
			+ " WHERE"
			+ " user_id = ?";
	@Override
	public void configure(WebSecurity web) throws Exception {
		//性的リソースを除外
		//性的リソースへのアクセスには、セキュリティーを適用しない
		web.ignoring().antMatchers("/webjars/**","/css/**");
	}
	
	@Override
	protected void configure(HttpSecurity http)throws Exception{
		//直リンク禁止
		//ログイン不要ページの設定
		http
			.authorizeRequests() //antMatchersとは？　ant形式で指定したパスパターンに一致するリソースを適用対象にする　ant 正規表現とは別のパターンを用いる
				.antMatchers("/webjars/**").permitAll()//webjarsへアクセス許可
				.antMatchers("/css/**").permitAll()
				.antMatchers("/login").permitAll()
				.antMatchers("/signup").permitAll()
				.antMatchers("/admin").hasAuthority("ROLE_ADMIN")
				.anyRequest().authenticated();//上記以外は直リンク禁止
				
		//ログイン処理
		http
			.formLogin()
				.loginProcessingUrl("/login")//ログイン処理のパス //ログイン画面のhtmlにあるフォームタグのaction="/login"の部分と一致させます。
				.loginPage("/login")//ログインページの指定
				.failureUrl("/login")//ログイン失敗時の遷移先
				.usernameParameter("userId")//ログインページのパスワード
				.passwordParameter("password")//ログインページのパスワード
				.defaultSuccessUrl("/home",true);//ログイン成功後の遷移先
		
		//ログアウト処理
		http
			.logout()
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
				.logoutUrl("/logout")
				.logoutSuccessUrl("/login");
		
		//CSRF対策を無効に設定（一時的）
		//http.csrf().disable();
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth)throws Exception {
		//ユーザーデータの取得
		//ログイン処理時のユーザー情報を、DBから取得する
		auth.jdbcAuthentication()
			.dataSource(dataSource)
			.usersByUsernameQuery(USER_SQL)
			.authoritiesByUsernameQuery(ROLE_SQL)
			.passwordEncoder(passwordEncoder());
	}

}
