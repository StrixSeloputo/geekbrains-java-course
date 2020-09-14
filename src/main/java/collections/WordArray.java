package collections;

import java.util.*;

public class WordArray {
    // Создать массив с набором слов (10-20 слов, должны встречаться повторяющиеся).
    private final String[] words;

    public WordArray(String... words) {
        this.words = words;
    }

    public static void main(String[] args) {
        WordArray wordArray = new WordArray("minimize", "jet", "aware", "empire", "understand", "transmission",
                "dull", "arrangement", "empire", "moral", "jet", "dull", "mainstream", "minimize", "dull");
        System.out.println(wordArray.getUniques());
        System.out.println(wordArray.getWordCounts());
    }

    // Найти и вывести список уникальных слов, из которых состоит массив (дубликаты не считаем).
    public Set<String> getUniques() {
        Set<String> uniques = new HashSet<>(words.length / 4);
        Collections.addAll(uniques, words);
        return uniques;
    }

    // Посчитать, сколько раз встречается каждое слово.
    public Map<String, Integer> getWordCounts() {
        Map<String, Integer> wordCounts = new HashMap<>(words.length / 4);
        for (String word : words) {
            wordCounts.put(word, wordCounts.getOrDefault(word, 0) + 1);
        }
        return wordCounts;
    }
}
