package dev.kxxcn.app_with;

import org.junit.Test;

import dev.kxxcn.app_with.util.Utils;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
	@Test
	public void testing_current_time() {
		System.out.println(Utils.Companion.getCurrentTime());
	}
}