package ltf.namerank.rank.dictrank.support;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import static ltf.namerank.utils.FileUtils.file2Lines;
import static ltf.namerank.utils.PathUtils.getWordsHome;

/**
 * @author ltf
 * @since 16/7/7, 下午4:55
 */
public class Words {
    //    public final static Set<String> negativeSet = new HashSet<>();
//    public final static Set<String> positiveSet = new HashSet<>();
//    public final static Set<String> butySet = new HashSet<>();
    public final static Set<String> goodSet = new HashSet<>();
    public final static Set<String> badSet = new HashSet<>();


    static {
        try {
//            file2Lines(getWordsHome() + "/positive.txt", positiveSet);
//            file2Lines(getWordsHome() + "/negative.txt", negativeSet);
//            file2Lines(getWordsHome() + "/buty.txt", butySet);

            file2Lines(getWordsHome() + "/goodwords.txt", goodSet);
            file2Lines(getWordsHome() + "/badwords.txt", badSet);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
    public static boolean isNegative(String word) {
        return Holder.getInstance().negative.contains(word);
    }

    private static double rank(String content, Collection<String> words){
        double rk = 0;
        for (String word : words) {
            int count = existsCount(content, word);
            rk += Math.sqrt(count);
        }
        return rk;
    }

    public static double negativeRank(String content) {
        return rank(content, Holder.getInstance().negative);
    }

    public static boolean isPositive(String word) {
        return Holder.getInstance().positive.contains(word);
    }

    public static double positiveRank(String content) {
        return rank(content, Holder.getInstance().positive);
    }

    public static boolean isButy(String word) {
        return Holder.getInstance().buty.contains(word);
    }

    public static double butyRank(String content) {
        return rank(content, Holder.getInstance().buty);
    }

    private static class Holder {
        private static Words words = new Words();

        public static Words getInstance() {
            return words;
        }
    }
     */
}
