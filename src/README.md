# My Word Prediction System

## Insert

My insert method is a recursive procedure that adds the given word, conviniently called word, one letter at a time. It works by taking the first character of the word and storing this as a value. It then inserts the word letter by letter into the tree, recursively, by taking a substring(1) of the word. This essentially makes the next letter the head of the word, and the next letter to be inserted, It also checks if the letter is already inserted, in which case, it doesn't add it, and continues traversing through the tree, until the word is completely inserted. When the last letter of the word is added, it is 'tagged' with a boolean value isEnd. This value is set to true, and it denotes that this specific character is the end of a word, and will be used later in my allWords() method.

	private Boolean isEnd = false;

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

The relevant snippets of code for this method are displayed above.

## Remove

My remove method is, as before, also a recursive procedure, and it takes a word, also called word. It works by traversing through the tree, until the final value is reached. When it gets to this final value, if the value does not have any children, then the value is removed, and the method returns true. If there, however, are children coming off of the value, then the value is not removed and the methd returns false. It also checks if the DictionaryTree contains the word in question, and only if this returns true is the method actually run. My reasoning for only removing the final character and not the whole word, is that, in some instances, more than one word may share the exact same Tree path. For instance, the words 'there' and 'theres', share identical Tree paths, besides the additional 's' that 'theres' contains. If I wanted to remove 'theres', It would only remove 's' from the tree, leaving 'there' intact as it should.

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

The relevant snippets of code for this method are displayed above.

## Contains

My contains method is a simple recursive procedure that checks if the word is present in the tree, character by character, and returns true if every word is present in the tree, and false if not.

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

## Predict

My predict method is split into 2 parts. The first part takes the prefix and traverses the tree until it reaches the last letter of the prefix. The second part works by then traversing through the tree, by taking the first elements of each linkedHashMap, until it reaches the end of the word, and it returns the concatentaion of the prefix, and all of these characters. The predicted word that is returned is not of any specific popularity, and is returned, as it is represented in the tree.

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

## AllWords

My allWords procedure works by using nested for loops, the inner for loop iterates through the tree, adding the letters to the current word, until the End of the word is reached, which is marked by a boolean value, isEnd. The outer for loop traverses the whole tree so that every word is processed and added to the arrayList. When the outer for loop terminates, after the whole tree hasn been processed, the arrayList of all the words in the tree is returned.

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

## Size

My size method works adding 1 to the total size, for every loop of the for loop, and a helper method called sizeRecurs() is used to help with the recursive aspect of the method.

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

## Height

For my height method, I traverssed down the tree with a for loop, and for each traversal, it goes down the branches recursively and gets the maximum height, by comparing it the current maximum height. The maximum height is then returned as an integer.

    int height() { 
        int height = -1;
        for (Map.Entry<Character, DictionaryTree> child  : children.entrySet()) {
            height = Math.max(height, child.getValue().height());
        }
        return 1+height;
    }

## MaximumBranching

For my maximum branching method, it gets the size of the current branch, and then traverses the tree, continiuing to get the size of the branches, and the maximum branch size is returned by, comparing with all of the other branch sizes, and taking the maximum value.

    int maximumBranching() {
        int maxBranch = children.size();
        
        for (Map.Entry<Character, DictionaryTree> child  : children.entrySet()) {
            maxBranch = Math.max(maxBranch, child.getValue().children.size());
            maxBranch = Math.max(maxBranch, child.getValue().maximumBranching());
        }
        return maxBranch;
    }

## LongestWord

This works by, taking allWords(), and making comparisons, until the word with the maximum length is returned.

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

## NumLeaves

This works by traversing through every branch and checking if children is empty, and if it is, this indicates the presence of a leaf, and so it is added to the tally. The number of leaves are summed up and returned as an integer.

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

## Conclusion

The above has detailed how my Word Prediction system works.