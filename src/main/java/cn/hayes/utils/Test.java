package cn.hayes.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Test {

	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	public static void main(String[] args) {
		Test test = new Test();
		test.printHello();
	}

	private void printHello() {
		logger.info("Hello!");
	}
}
