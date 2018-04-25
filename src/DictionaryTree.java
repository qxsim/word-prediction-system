import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Map.Entry;
import java.util.function.BiFunction;
import java.util.stream.Stream;

/**
 * @author Qasim Nawaz
 */
public class DictionaryTree {
        	
	private Map<Character, DictionaryTree> children = new LinkedHashMap<>();
	private Map<Integer, String> pop = new HashMap<>();
	private Map<String, Integer> popTracker = new HashMap<>();
	private Boolean isEnd = false;
	
    /**
     * Inserts the given word into this dictionary.
     * If the word already exists, nothing will change.
     *
     * @param word the word to insert
     */
    void insert(String word) {
        Character charAt0;
        int length = word.length();
        if(length >= 1) {
        	charAt0 = word.charAt(0);
        	if(!(children.containsKey(charAt0))) {
        		children.put(charAt0, new DictionaryTree());	
        	}
        	if(length == 1) {
        		children.get(charAt0).isEnd = true;
        	}
        	DictionaryTree recur = children.get(charAt0);
        	recur.insert(word.substring(1));
        }
    }

    /**
     * Inserts the given word into this dictionary with the given popularity.
     * If the word already exists, the popularity will be overridden by the given value.
     *
     * @param word       the word to insert
     * @param popularity the popularity of the inserted word
     */
    void insert(String word, int popularity) {
        insert(word);  
        if ((pop.containsValue(word))) {
        	pop.remove(popTracker.get(word));
        	popTracker.remove(word);
        }
        
        pop.put(popularity, word);
        popTracker.put(word, popularity); 
    }
    
    /**
     * Removes the specified word from this dictionary.
     * Returns true if the caller can delete this node without losing
     * part of the dictionary, i.e. if this node has no children after
     * deleting the specified word.
     *
     * @param word the word to delete from this dictionary
     * @return whether or not the parent can delete this node from its children
     */
    boolean remove(String word) {
    	Boolean lose = false;
    	Character charToRemove;
    	int length = word.length();
           
    	if(length >= 1 && contains(word)) {
    	   	charToRemove = word.charAt(length-1);
           	if((children.containsKey(charToRemove)) && length == 1) {
           		if (children.get(charToRemove).children.isEmpty()) {
           			children.remove(charToRemove);
           			lose = true;
           		}
           			
           		else {
           			lose = false;
           		}
           	}
           	
           	else {
           		DictionaryTree recur = children.get(word.charAt(0));
           		return recur.remove(word.substring(1));
           	}
    	}
		return lose;
    }
    
    /**
     * Determines whether or not the specified word is in this dictionary.
     *
     * @param word the word whose presence will be checked
     * @return true if the specified word is stored in this tree; false otherwise
     */
    boolean contains(String word) {
    	Character charAt0;
    	
    	if(word.length() >= 1) {
    		charAt0 = word.charAt(0);
    		DictionaryTree recur = children.get(charAt0);
		    if (children.containsKey(charAt0)) {
		    	if (word.length() == 1) {
		    		return true;
		    	}
		    	
		    	else {
		    		return recur.contains(word.substring(1));
		    	}	
		    }
		    	
		   	else {
		   		return false;
		   	}
    	}
    	
    	else {
    		return false;
    	}
    }

    /**
     * @param prefix the prefix of the word returned
     * @return a word that starts with the given prefix, or an empty optional
     * if no such word is found.
     */
    
    Optional<String> predict(String prefix) {
    	
    	Optional<String> prediction = Optional.empty();
    	Optional<DictionaryTree> currentTree = Optional.of(this);
    	int i=0;
    	char[] chara = prefix.toCharArray();
    	
    	if (!prefix.isEmpty() && contains(prefix)){
    		prediction = Optional.of(prefix);
    		
    		//loops to prefix
        	while(currentTree.isPresent() && currentTree.get().children.containsKey(chara[i])){
        		currentTree = Optional.of(currentTree.get().children.get(chara[i]));
        		if (i == prefix.length()-1){
        			break;
        		}
        		i++;
        	}
        	
        	//loops to end of predicted word
        	while(currentTree.isPresent() && !currentTree.get().children.isEmpty()){
        		Stream<Entry<Character, DictionaryTree>> entry = currentTree.get().children.entrySet().stream();
        		Optional<Entry<Character, DictionaryTree>> first = entry.findFirst();
        		prediction = Optional.of(prediction.get() + first.get().getKey());
        		currentTree = Optional.of(first.get().getValue());
        	}	
    	}
    	return prediction;
    }

    /**
     * Predicts the (at most) n most popular full English words based on the specified prefix.
     * If no word with the specified prefix is found, an empty Optional is returned.
     *
     * @param prefix the prefix of the words found
     * @return the (at most) n most popular words with the specified prefix
     */
    List<String> predict(String prefix, int n) {
        throw new RuntimeException("DictionaryTree.predict not implemented yet");
    }

    /**
     * @return the number of leaves in this tree, i.e. the number of words which are
     * not prefixes of any other word.
     */
    int numLeaves() {
    	int count = 0;
    	
    	for (Map.Entry<Character, DictionaryTree> child  : children.entrySet()) {
    		if(child.getValue().children.isEmpty()) {
    			count = count + 1;
    		}
    		
    		else {
    			count = count + child.getValue().numLeaves();
    		}
    	}
		return count;
    }

    /**
     * @return the maximum number of children held by any node in this tree
     */
    
    int maximumBranching() {
    	int maxBranch = children.size();
    	
    	for (Map.Entry<Character, DictionaryTree> child  : children.entrySet()) {
    		maxBranch = Math.max(maxBranch, child.getValue().children.size());
    		maxBranch = Math.max(maxBranch, child.getValue().maximumBranching());
    	}
    	return maxBranch;
    }
    
    /**
     * @return the height of this tree, i.e. the length of the longest branch
     */
    int height() { 
        int height = -1;

        for (Map.Entry<Character, DictionaryTree> child  : children.entrySet()) {
        	height = Math.max(height, child.getValue().height());
        }
        return 1+height;
    }

    /**
     * @return the number of nodes in this tree
     */
    int size() {
    	return sizeRecurs();
    }
    
    int sizeRecurs() {
    	int size = 1;

        for (Map.Entry<Character, DictionaryTree> child : children.entrySet()) {
        	size = size + child.getValue().sizeRecurs();
        }
        return size;
    }

    /**
     * @return the longest word in this tree
     */
    String longestWord() {
    	String longestWord = "";
    	List<String> allWord = new ArrayList<String>();
    	allWord = allWords();
    	  for(int i = 0; i < allWord.size(); i++) {
             if(allWord.get(i).length() > longestWord.length()) {
                longestWord = allWord.get(i);
             }
    	  }
    	  return longestWord;
    }

    /**
     * @return all words stored in this tree as a list
     */
    List<String> allWords() {
    	List<String> wordList = new ArrayList<String>();
    	String word = "";
    	
    	if (isEnd) {
    		wordList.add(word);
    	}
    	
        for (Map.Entry<Character, DictionaryTree> child  : children.entrySet()) {
        	List<String> wordHelper = child.getValue().allWords();
        	for (String letterIterate : wordHelper) {
        		wordList.add(child.getKey() + letterIterate);
        	}
        }
        return wordList;
    }
   
    /**
     * Folds the tree using the given function. Each of this node's
     * children is folded with the same function, and these results
     * are stored in a collection, cResults, say, then the final
     * result is calculated using f.apply(this, cResults).
     *
     * @param f   the summarising function, which is passed the result of invoking the given function
     * @param <A> the type of the folded value
     * @return the result of folding the tree using f
     */
    <A> A fold(BiFunction<DictionaryTree, Collection<A>, A> f) {
        throw new RuntimeException("DictionaryTree.fold not implemented yet");
    }
}
