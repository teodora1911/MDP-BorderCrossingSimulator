package tesi.database.redis;

import java.io.FileInputStream;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.MissingFormatArgumentException;
import java.util.Properties;
import java.util.Set;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import tesi.model.User;
import tesi.util.Utility;

public class DBService {
	
	private static String JedisAddress;
	private static String HashingAlgorithm;
	private static MessageDigest digest;
	
	static {
		try {
			loadConfigFile();
			digest = MessageDigest.getInstance(HashingAlgorithm);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private JedisPool pool;
	private Jedis jedis;
	
	public DBService() {
		pool = new JedisPool(JedisAddress);
		jedis = pool.getResource();
	}
	
	public Set<String> getUsers(){
		return jedis.keys("*");
	}
	
	/*
	 *   Pozitivan broj - ako je uspješno dodavanje
	 *   0 - nije uspješno dodavanje
	 *   Negativan broj - ako korisnièko ime veæ postoji
	 */
	public int add(User user) {
		if(user != null && user.getPassword() != null && user.getUsername() != null && unique(user.getUsername())) {
//			jedis.set(user.getUsername(), user.getPassword());
			jedis.set(user.getUsername(), getHash(user.getPassword(), user.getUsername()));
			return (!unique(user.getUsername())) ? 1 : 0;
		}
		return -1;
	}
	
	public boolean validate(User user) {
		if(user == null || user.getUsername() == null || user.getPassword() == null)
			return false;
		String dbPassword = jedis.get(user.getUsername());
		return (dbPassword != null && dbPassword.equals(getHash(user.getPassword(), user.getUsername())));
	}
	
	public String getPasswordFrom(String user) {
		return jedis.get(user);
	}
	
	
	public boolean update(User user) {
		if(validate(user)) {
			if(user.getNewPassword() != null) {
				jedis.set(user.getUsername(), getHash(user.getNewPassword(), user.getUsername()));
				return true;
			}
		}
		return false;
	}
	
	public boolean delete(User user) {
		if(user == null || user.getUsername() == null)
			return false;
		if(!unique(user.getUsername())) {
			return jedis.del(user.getUsername()) > 0;
		}
		return false;
	}
	
	private boolean unique(String username) {
		Set<String> matches = jedis.keys(username);
		return matches == null || matches.isEmpty();
	}
	
	private static String getHash(String password, String salt) {
		digest.reset();
		digest.update(salt.getBytes());
		byte[] hashBytes = digest.digest(password.getBytes());
		return new String(Base64.getEncoder().encode(hashBytes));
	}
	
	private static void loadConfigFile() throws Exception {
		try(FileInputStream input = new FileInputStream(Utility.CONFIG_PATH)){
			Properties properties = new Properties();
			properties.load(input);
			
			JedisAddress = properties.getProperty("jedis-address");
			if(JedisAddress == null)
				throw new MissingFormatArgumentException("Jedis address is not defined.");
			HashingAlgorithm = properties.getProperty("hashing-algorithm");
			if(HashingAlgorithm == null)
				throw new MissingFormatArgumentException("Hashing algorithm is not defined.");
		} catch (Exception e) {
			throw new Exception(e);
		}
	}
}
