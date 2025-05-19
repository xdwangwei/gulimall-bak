import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.vivi.common.utils.PageUtils;
import com.vivi.common.utils.Query;
import com.vivi.gulimall.product.dao.SkuImagesDao;
import com.vivi.gulimall.product.entity.SkuImagesEntity;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SkuImagesServiceImplTest {

    @Mock
    private SkuImagesDao skuImagesDao;

    @InjectMocks
    private SkuImagesServiceImpl skuImagesService;

    private SkuImagesEntity skuImage;
    private List<SkuImagesEntity> skuImageList;

    @Before
    public void setUp() {
        skuImage = new SkuImagesEntity();
        skuImage.setId(1L);
        skuImage.setSkuId(100L);
        skuImage.setImgUrl("test.jpg");
        skuImage.setImgSort(1);
        skuImage.setDefaultImg(1);

        skuImageList = new ArrayList<>();
        skuImageList.add(skuImage);
    }

    @Test
    public void testQueryPage() {
        Map<String, Object> params = new HashMap<>();
        params.put("page", "1");
        params.put("limit", "10");

        IPage<SkuImagesEntity> page = new Query<SkuImagesEntity>().getPage(params);
        page.setRecords(skuImageList);
        page.setTotal(1);

        when(skuImagesDao.selectPage(any(), any())).thenReturn(page);

        PageUtils result = skuImagesService.queryPage(params);

        assertEquals(1, result.getTotalCount());
        assertEquals(skuImageList, result.getList());
    }

    @Test
    public void testListBySkuId() {
        when(skuImagesDao.selectList(any(QueryWrapper.class))).thenReturn(skuImageList);

        List<SkuImagesEntity> result = skuImagesService.listBySkuId(100L);

        assertEquals(skuImageList, result);
    }
}
