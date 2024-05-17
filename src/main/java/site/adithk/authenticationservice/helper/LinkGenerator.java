package site.adithk.authenticationservice.helper;

import lombok.extern.slf4j.Slf4j;

import java.util.Random;

@Slf4j
public class LinkGenerator {

	private static final String CHAR_LIST = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
	public static StringBuilder getRandomString(int length) {
		StringBuilder randomString = new StringBuilder();
		Random rnd = new Random();
		while (randomString.length() < length) {
			int index = (int) (rnd.nextFloat() * CHAR_LIST.length());
			randomString.append(CHAR_LIST.charAt(index));
		}
		log.info("Generated Random String:{}",randomString);

		return randomString;
	}

}
