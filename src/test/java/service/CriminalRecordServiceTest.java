package service;

import static org.assertj.core.api.Java6Assertions.*;

import org.junit.Test;

public class CriminalRecordServiceTest {

	@Test
	public void test() {
		CriminalRecordService dut = new CriminalRecordService();
		dut.getAllPoliticians().forEach(politician -> {
			assertThat(politician.getRecordList().size()).isEqualTo(politician.getRecordCount());
			System.out.println(politician);
		});
	}
}