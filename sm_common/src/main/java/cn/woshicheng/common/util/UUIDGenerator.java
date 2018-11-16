package cn.woshicheng.common.util;




import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

public class UUIDGenerator {

    /** 
     * 获取32位GUID 
     * @return 
     */ 
    public static String getGUID() { 
        try { 
            MessageDigest md = MessageDigest.getInstance("MD5"); 
            UUID uuid = UUID.randomUUID(); 
            String guidStr = uuid.toString(); 
            md.update(guidStr.getBytes(), 0, guidStr.length()); 
            return new BigInteger(1, md.digest()).toString(16); 
        } catch (NoSuchAlgorithmException e) { 
            return null; 
        } 
    }

    /**
     * 
     *
     * 例子 6090614452266 日期 + 100以内随机数
     */
    public static String getRandomFor13(){
    	String nowString = TimeUtil.currentStr2();
    	nowString = nowString.substring(3,nowString.length());
    	int randNum = (int)(1+Math.random()*(100));
    	return nowString + randNum;
    }
    
  public static void main(String[] args) {
	  System.out.println(getRandomFor13());
}
}  
