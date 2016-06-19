package ltf.namerank.dao.fs;

import com.alibaba.fastjson.JSON;
import ltf.namerank.dao.DictItemDao;
import ltf.namerank.entity.DictItem;
import ltf.namerank.utils.PathUtils;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static ltf.namerank.utils.FileUtils.file2Str;
import static ltf.namerank.utils.FileUtils.str2File;

/**
 * @author ltf
 * @since 6/11/16, 5:17 PM
 */
@Component
public class DictItemDaoImpl implements DictItemDao {

    @Override
    public void saveDictItem(DictItem dictItem) {
        File f = new File(PathUtils.getJsonHome() + "/dict", dictItem.getZi());

        Map<String, DictItem> items = new HashMap<>();

        if (f.exists()) {
            try {
                items = JSON.parseObject(file2Str(f), items.getClass());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        items.put(dictItem.getItemType(), dictItem);

        try {
            str2File(JSON.toJSONString(items, true), f);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Collection<DictItem> loadItemsByZi(String zi) {
        return null;
    }
}
