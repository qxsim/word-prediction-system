import static org.junit.Assert.*;
import org.junit.Test;

/**
 * @author Qasim Nawaz
 */
public class DictionaryTreeTests {

    @Test
    public void heightOfRootShouldBeZero() {
        DictionaryTree unit = new DictionaryTree();
        assertEquals(0, unit.height());
    }

    @Test
    public void heightOfWordShouldBeWordLength() {
        DictionaryTree unit = new DictionaryTree();
        unit.insert("word", 0);
        assertEquals("word".length(), unit.height());
    }
    
    @Test
    public void heightOfLongestWordShouldBeTreeHeight() {
        DictionaryTree unit = new DictionaryTree();
        unit.insert("thisisaverylongword");
        assertEquals(unit.longestWord().length(), unit.height());
    }
    
    @Test
    public void wordShouldNotBeInTreeAfterRemove() {
        DictionaryTree unit = new DictionaryTree();
        unit.insert("thisisaverylongword");
        unit.remove("thisisaverylongword");
        assertEquals(unit.contains("thisisaverylongword"), false);
    }
    
    @Test
    public void wordShouldBeInTreeAfterInsert() {
        DictionaryTree unit = new DictionaryTree();
        unit.insert("thisisaverylongword");
        assertEquals(unit.contains("thisisaverylongword"), true);
    }
    
    @Test
    public void numLeavesShouldBe6() {
        DictionaryTree unit = new DictionaryTree();
        unit.insert("the");
        unit.insert("their");
        unit.insert("theres");
        unit.insert("they");
        unit.insert("alpha");
        unit.insert("beta");
        unit.insert("omega");
        assertEquals(unit.numLeaves(), 6);
    }
    
    @Test
    public void maxBranchingShouldBe4() {
        DictionaryTree unit = new DictionaryTree();
        unit.insert("the");
        unit.insert("their");
        unit.insert("theres");
        unit.insert("they");
        unit.insert("alpha");
        unit.insert("beta");
        unit.insert("omega");
        assertEquals(unit.maximumBranching(), 4);
    }
    
    @Test
    public void sizeShouldBe13DueToRootNode() {
        DictionaryTree unit = new DictionaryTree();
        unit.insert("abcd");
        unit.insert("efgh");
        unit.insert("ijkl");
        assertEquals(unit.size(), 13);
    }
    
    @Test
    public void wordShouldNotBeInAllWordsAfterRemove() {
        DictionaryTree unit = new DictionaryTree();
        unit.insert("thisisaverylongword");
        unit.remove("thisisaverylongword");
        assertEquals(unit.allWords().contains("thisisaverylongword"), false);
    }
    
    @Test
    public void sizeShouldReduceBy1AfterThisRemoval() {
    	 DictionaryTree unit = new DictionaryTree();
         unit.insert("test");
         unit.insert("testt");
         int sizeBefore = unit.size();
         unit.remove("testt");
         int sizeAfter = unit.size();
         assertEquals((sizeBefore - sizeAfter), 1);
    }
}