package convert;

import org.junit.Test;

public class MainTest {
  @Test
  public void testDest() {
    String destName = Main.getDestName("6. 5.tif");
    assert (destName.equals("6. 5.jpg"));
  }
}
