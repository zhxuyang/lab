package ltf.namerank.service;

import ltf.namerank.lab.*;
import ltf.namerank.dao.DictItemDao;
import ltf.namerank.entity.DictItem;
import ltf.namerank.entity.Hanzi;
import ltf.namerank.lab.nametest3rd.NameTestName321;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

/**
 * @author ltf
 * @since 16/6/8, 上午11:03
 */
@Service
public class SpringAutoRunService {

    @Autowired
    private DictItemDao dictItemDao;

    @PostConstruct
    public void autoRun() {
        new NameTestName321().go();

//        new HanziWuxing().run();
//

//        Map<String, List<Hanzi>> data = HanziWuxing.testLoadDict();
//
//        data.forEach((k, l) -> {
//                    if (l.size() > 1) {
//
//                        System.out.println(k);
//                        System.out.println(l.size());
//
//                        for (byte b : k.getBytes()) {
//                            System.out.print("=" + b + "=");
//                        }
//                        System.out.println("------------------------");
//                        l.forEach(z -> {
//                            System.out.println(z.getHtmid());
//                            //File f = new File("/Users/f/flab/jlab/namerank/build/libs/wuxhtm/namerank.jar"+z.getHtmid()+".html");
//                            //f.delete();
//                        });
//                    } else {
//                        DictItem item = l.get(0).toDictItem();
//                        //System.out.println(JSON.toJSONString(item, true));
//                        dictItemDao.saveDictItem(item);
//                    }
//
//                }
//        );
    }
}
