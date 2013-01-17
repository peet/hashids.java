package HashidsJava;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class HashidsTest {
  String salt_ = "this is my salt";
  Hashids hashids_;

  @Before
  public void setUp() throws Exception {
    hashids_ = new Hashids(salt_);
  }

  @Test
  public void itHasDefaultSalt() {
    assertEquals(new Hashids().encrypt(1, 2, 3), "katKSA");
  }

  @Test
  public void itHasTheCorrectSalt() {
    assertEquals(hashids_.getSalt(), "this is my salt");
  }

  @Test
  public void itDefaultsToTheMinimumLength0() {
    assertEquals(hashids_.getMinHashLength(), 0);
  }

  @Test
  public void itEncryptsASingleNumber() {
    assertEquals(hashids_.encrypt(12345), "ryBo");
    assertEquals(hashids_.encrypt(1), "LX");
    assertEquals(hashids_.encrypt(22), "5B");
    assertEquals(hashids_.encrypt(333), "o49");
    assertEquals(hashids_.encrypt(9999), "GKnB");
  }

  @Test
  public void itEncryptsAListOfNumbers() {
    assertEquals(hashids_.encrypt(683, 94108, 123, 5), "zBphL54nuMyu5");
    assertEquals(hashids_.encrypt(1, 2, 3), "eGtrS8");
    assertEquals(hashids_.encrypt(2, 4, 6), "9Kh7fz");
    assertEquals(hashids_.encrypt(99, 25), "dAECX");
  }

  @Test
  public void itReturnsAnEmptyStringIfNoNumbers() {
    assertEquals(hashids_.encrypt(), "");
  }

  @Test
  public void itCanEncryptToAMinimumLength() {
    Hashids h = new Hashids(salt_, 8);
    assertEquals(h.encrypt(1), "b9iLXiAa");
  }

  @Test
  public void itDoesNotProduceRepeatingPatternsForIdenticalNumbers() {
    assertEquals(hashids_.encrypt(5, 5, 5, 5), "GLh5SMs9");
  }

  @Test
  public void itDoesNotProduceRepeatingPatternsForIncrementedNumbers() {
    assertEquals(hashids_.encrypt(1, 2, 3, 4, 5, 6, 7, 8, 9, 10), "zEUzfySGIpuyhpF6HaC7");
  }

  @Test
  public void itDoesNotProduceSimilaritiesBetweenIncrementingNumberHashes() {
    assertEquals(hashids_.encrypt(1), "LX");
    assertEquals(hashids_.encrypt(2), "ed");
    assertEquals(hashids_.encrypt(3), "o9");
    assertEquals(hashids_.encrypt(4), "4n");
    assertEquals(hashids_.encrypt(5), "a5");
  }

  @Test
  public void itDecryptsAnEncryptedNumber() {
    assertArrayEquals(hashids_.decrypt("ryBo"), new long[]{12345});
    assertArrayEquals(hashids_.decrypt("qkpA"), new long[]{1337});
    assertArrayEquals(hashids_.decrypt("6aX"), new long[]{808});
    assertArrayEquals(hashids_.decrypt("gz9"), new long[]{303});
  }

  @Test
  public void itDecryptsAListOfEncryptedNumbers() {
    assertArrayEquals(hashids_.decrypt("zBphL54nuMyu5"), new long[]{683, 94108, 123, 5});
    assertArrayEquals(hashids_.decrypt("kEFy"), new long[]{1, 2});
    assertArrayEquals(hashids_.decrypt("Aztn"), new long[]{6, 5});
  }

  @Test
  public void itDoesNotDecryptWithADifferentSalt() {
    Hashids peppers = new Hashids("this is my pepper");
    assertArrayEquals(hashids_.decrypt("ryBo"), new long[]{12345});
    assertArrayEquals(peppers.decrypt("ryBo"), new long[0]);
  }

  @Test
  public void itCanDecryptFromAHashWithAMinimumLength() {
    Hashids h = new Hashids(salt_, 8);
    assertArrayEquals(h.decrypt("b9iLXiAa"), new long[]{1});
  }

  @Test(expected = IllegalArgumentException.class)
  public void itRaisesAnArgumentNullExceptionWhenAlphabetIsNull() {
    new Hashids("", 0, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void itRaisesAnArgumentNullExceptionIfAlphabetContainsLessThan4UniqueCharacters() {
    new Hashids("", 0, "aadsss");
  }

  @Test
  public void itCanEncryptWithASwappedCustom() {
    Hashids hashIds = new Hashids("this is my salt", 0, "abcd");
    assertEquals(hashIds.encrypt(1, 2, 3, 4, 5), "adcdacddcdaacdad");
  }
}
