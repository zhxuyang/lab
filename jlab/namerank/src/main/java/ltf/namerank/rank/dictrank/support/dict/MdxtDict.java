package ltf.namerank.rank.dictrank.support.dict;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.sun.istack.internal.NotNull;
import ltf.namerank.rank.RankItem;
import ltf.namerank.rank.Ranker;
import ltf.namerank.rank.dictrank.support.PinyinMap;
import ltf.namerank.service.EvenManager;
import ltf.namerank.service.TeardownListener;
import ltf.namerank.utils.LinesInFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.alibaba.fastjson.serializer.SerializerFeature.PrettyFormat;
import static com.alibaba.fastjson.serializer.SerializerFeature.WriteClassName;
import static ltf.namerank.rank.RankItemHelper.addInfo;
import static ltf.namerank.rank.RankItemHelper.flushResult;
import static ltf.namerank.utils.FileUtils.file2Str;
import static ltf.namerank.utils.FileUtils.str2File;

/**
 * @author ltf
 * @since 16/6/21, 下午4:40
 */
abstract public class MdxtDict implements Ranker, TeardownListener {

    private final Logger logger = LoggerFactory.getLogger(MdxtDict.class);
    private static final String ITEM_END_LINE = "</>";

    abstract protected String getFileName();

    private int count = 0;

    protected Map<String, List<MdxtItem>> itemsMap;

    public MdxtDict() {
        EvenManager.add(this);
    }

    protected void initItems() {
        long start = System.currentTimeMillis();
        if (loadCache()) return;
        if (itemsMap == null) {
            itemsMap = new HashMap<>();
            try {
                loadItems();
            } catch (IOException e) {
                logger.warn("load dictionary failed: " + getFileName(), e);
            }
        }
        System.out.println(System.currentTimeMillis() - start);
    }

    private void loadItems() throws IOException {
        new LinesInFile(getFileName()).each(this::parseLine);
    }

    protected MdxtItem newItem(String key) {
        return new MdxtItem(key);
    }

    private MdxtItem item = null;

    private void parseLine(String line) {
        if (item == null) {
            item = newItem(line);
        } else if (ITEM_END_LINE.equals(line)) {
            item.finishAdd();
            if (item.isValid()) {
                List<MdxtItem> items = itemsMap.get(item.getKey());
                if (items == null) {
                    items = new ArrayList<>(5);
                    itemsMap.put(item.getKey(), items);

                    // add into pinyin manager for reverse search
                    PinyinMap.add(item.getKey());
                }
                items.add(item);
            }
            item = null;
        } else {
            item.addValue(line);
        }
        count++;
    }

    @Override
    public double rank(@NotNull RankItem target) {
        initItems();
        double rk = 0;
        List<MdxtItem> items = itemsMap.get(target.getKey());
        if (items != null) {
            int i = 1;
            for (MdxtItem item : items) {
                double childRk = item.rank(target.newChild(target.getKey()));
                rk += childRk;
                addInfo(String.format("%d: %f; ", i++, childRk));
            }
        }
        flushResult(target, rk);
        return rk;
    }

    public void listKeys() {
        initItems();
//        for (String itemKey : itemsMap.keySet())
//            System.out.println(itemKey);

        System.out.println(itemsMap.size());
    }

    public Map<String, List<MdxtItem>> getItemsMap() {
        return itemsMap;
    }

    @Override
    public void onTeardown() {
        if (itemsMap != null && itemsMap.size() > 0 && !new File(getCacheFilename()).exists())
            saveCache();
    }

    private String getCacheFilename() {
        return getFileName() + ".cache";
    }

    private void saveCache() {
        try {
            FileWriter fileWriter = new FileWriter(getCacheFilename());
            SerializeWriter out = new SerializeWriter(fileWriter, WriteClassName, PrettyFormat);

            try {
                JSONSerializer serializer = new JSONSerializer(out);
                serializer.write(itemsMap);
            } finally {
                out.close();
            }
        } catch (Exception e) {
            logger.error("save cache failed", e);
        }
    }

    private boolean loadCache() {
        try {
            if (new File(getCacheFilename()).exists()) {
                itemsMap = (Map<String, List<MdxtItem>>) JSON.parseObject(file2Str(getCacheFilename()), HashMap.class);
                return true;
            }
        } catch (Exception e) {
            logger.error("load cache failed", e);
        }
        return false;
    }
}


