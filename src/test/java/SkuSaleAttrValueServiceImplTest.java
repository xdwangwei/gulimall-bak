import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.vivi.common.utils.PageUtils;
import com.vivi.common.utils.Query;
import com.vivi.gulimall.product.dao.SkuSaleAttrValueDao;
import com.vivi.gulimall.product.entity.SkuSaleAttrValueEntity;
import com.vivi.gulimall.product.vo.ItemSaleAttrVO;
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
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SkuSaleAttrValueServiceImplTest {

    @Mock
    private SkuSaleAttrValueDao skuSaleAttrValueDao;

    @InjectMocks
    private SkuSaleAttrValueServiceImpl skuSaleAttrValueService;

    private SkuSaleAttrValueEntity skuSaleAttrValueEntity;
    private List<SkuSaleAttrValueEntity> skuSaleAttrValueList;
    private List<ItemSaleAttrVO> itemSaleAttrVOList;
    private List<String> stringList;

    @Before
    public void setUp() {
        skuSaleAttrValueEntity = new SkuSaleAttrValueEntity();
        skuSaleAttrValueEntity.setId(1L);
        skuSaleAttrValueEntity.setSkuId(1L);
        skuSaleAttrValueEntity.setAttrId(1L);
        skuSaleAttrValueEntity.setAttrName("Color");
        skuSaleAttrValueEntity.setAttrValue("Red");
        skuSaleAttrValueEntity.setAttrSort(1);

        skuSaleAttrValueList = new ArrayList<>();
        skuSaleAttrValueList.add(skuSaleAttrValueEntity);

        itemSaleAttrVOList = new ArrayList<>();
        ItemSaleAttrVO itemSaleAttrVO = new ItemSaleAttrVO();
        itemSaleAttrVO.setAttrId(1L);
        itemSaleAttrVO.setAttrName("Color");
        itemSaleAttrVOList.add(itemSaleAttrVO);

        stringList = new ArrayList<>();
        stringList.add("Color:Red");
    }

    @Test
    public void testQueryPage() {
        Map<String, Object> params = new HashMap<>();
        params.put("page", "1");
        params.put("limit", "10");

        IPage<SkuSaleAttrValueEntity> page = new Query<SkuSaleAttrValueEntity>().getPage(params);
        page.setRecords(skuSaleAttrValueList);
        page.setTotal(1);

        when(skuSaleAttrValueDao.selectPage(any(), any())).thenReturn(page);

        PageUtils result = skuSaleAttrValueService.queryPage(params);

        assertNotNull(result);
        assertEquals(1, result.getTotalCount());
        assertEquals(1, result.getList().size());
    }

    @Test
    public void testListBySkuId() {
        when(skuSaleAttrValueDao.selectList(any(QueryWrapper.class))).thenReturn(skuSaleAttrValueList);

        List<SkuSaleAttrValueEntity> result = skuSaleAttrValueService.listBySkuId(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(Long.valueOf(1L), result.get(0).getSkuId());
    }

    @Test
    public void testAllAttrValueWithSkuBySpuId() {
        when(skuSaleAttrValueDao.allAttrValueWithSkuBySpuId(1L)).thenReturn(itemSaleAttrVOList);

        List<ItemSaleAttrVO> result = skuSaleAttrValueService.allAttrValueWithSkuBySpuId(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(Long.valueOf(1L), result.get(0).getAttrId());
    }

    @Test
    public void testStringListBySkuId() {
        when(skuSaleAttrValueDao.stringListBySkuId(1L)).thenReturn(stringList);

        List<String> result = skuSaleAttrValueService.stringListBySkuId(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Color:Red", result.get(0));
    }
}
